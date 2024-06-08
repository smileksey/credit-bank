package org.smileksey.deal.services;

import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.*;
import org.smileksey.deal.dto.enums.ApplicationStatus;
import org.smileksey.deal.dto.enums.ChangeType;
import org.smileksey.deal.dto.enums.CreditStatus;
import org.smileksey.deal.exceptions.InvalidMSResponseException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Credit;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.models.StatusHistory;
import org.smileksey.deal.repositories.CreditRepository;
import org.smileksey.deal.utils.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final StatementService statementService;
    private final CreditRepository creditRepository;

    private final RestTemplate restTemplate;

    private final String CC_CALC_URL = "http://localhost:8080/calculator/calc";

    @Autowired
    public CreditServiceImpl(RestTemplateBuilder restTemplateBuilder, StatementService statementService, CreditRepository creditRepository) {
        this.statementService = statementService;
        this.creditRepository = creditRepository;

        this.restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Transactional
    @Override
    public void calculateCreditAndFinishRegistration(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {

        Statement statement = statementService.getStatementById(statementId);
        ScoringDataDto scoringDataDto = buildScoringDataDto(finishRegistrationRequestDto, statement.getAppliedOffer(), statement.getClient());

        log.info("Statement: {}", statement);
        log.info("Sending ScoringDataDto to 'calculator': {}", scoringDataDto);

        ResponseEntity<CreditDto> creditDtoFromCC = restTemplate.exchange(CC_CALC_URL, HttpMethod.POST, createHttpRequest(scoringDataDto), new ParameterizedTypeReference<CreditDto>() {});

        if(creditDtoFromCC.getStatusCode() == HttpStatus.OK) {

            CreditDto creditDto = creditDtoFromCC.getBody();

            log.info("CreditDto received from 'calculator': {}", creditDto);

            if (creditDto != null) {

                Credit credit = buildCredit(creditDto);
                Credit savedCredit = creditRepository.save(credit);

                statement.setCredit(savedCredit);
                updateStatement(statement, true);

                log.info("Credit: {}", savedCredit);

            } else throw new InvalidMSResponseException("ERROR: CreditDto from 'calculator' == null");

        } else if (creditDtoFromCC.getStatusCode() == HttpStatus.NOT_FOUND) {

            updateStatement(statement, false);

        } else throw new InvalidMSResponseException("ERROR: Failed to get CreditDto from 'calculator'");

        log.info("Updated statement: {}", statement);
    }


    private ScoringDataDto buildScoringDataDto(FinishRegistrationRequestDto finishRegistrationRequestDto, LoanOfferDto appliedOffer, Client client) {

        return ScoringDataDto.builder()
                .amount(appliedOffer.getRequestedAmount())
                .term(appliedOffer.getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(finishRegistrationRequestDto.getGender())
                .birthdate(client.getBirthDate())
                .passportSeries(client.getPassport().getSeries())
                .passportNumber(client.getPassport().getNumber())
                .passportIssueDate(finishRegistrationRequestDto.getPassportIssueDate())
                .passportIssueBranch(finishRegistrationRequestDto.getPassportIssueBranch())
                .maritalStatus(finishRegistrationRequestDto.getMaritalStatus())
                .dependentAmount(finishRegistrationRequestDto.getDependentAmount())
                .employment(EmploymentDto.builder()
                        .employmentStatus(finishRegistrationRequestDto.getEmployment().getEmploymentStatus())
                        .employerINN(finishRegistrationRequestDto.getEmployment().getEmployerINN())
                        .salary(finishRegistrationRequestDto.getEmployment().getSalary())
                        .position(finishRegistrationRequestDto.getEmployment().getPosition())
                        .workExperienceTotal(finishRegistrationRequestDto.getEmployment().getWorkExperienceTotal())
                        .workExperienceCurrent(finishRegistrationRequestDto.getEmployment().getWorkExperienceCurrent())
                        .build())
                .accountNumber(finishRegistrationRequestDto.getAccountNumber())
                .isInsuranceEnabled(appliedOffer.getIsInsuranceEnabled())
                .isSalaryClient(appliedOffer.getIsSalaryClient())
                .build();
    }


    private Credit buildCredit(CreditDto creditDto) {

        return Credit.builder()
                .creditId(UUID.randomUUID())
                .amount(creditDto.getAmount())
                .term(creditDto.getTerm())
                .monthlyPayment(creditDto.getMonthlyPayment())
                .rate(creditDto.getRate())
                .psk(creditDto.getPsk())
                .paymentSchedule(creditDto.getPaymentSchedule())
                .isInsuranceEnabled(creditDto.getIsInsuranceEnabled())
                .isSalaryClient(creditDto.getIsSalaryClient())
                .creditStatus(CreditStatus.CALCULATED)
                .build();
    }


    private void updateStatement(Statement statement, boolean isApproved) {

        if (isApproved) {
            statement.setStatus(ApplicationStatus.CC_APPROVED);
            statement.getStatusHistory().add(StatusHistory.builder()
                    .status(ApplicationStatus.CC_APPROVED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build()
            );

        } else {
            statement.setStatus(ApplicationStatus.CC_DENIED);
            statement.getStatusHistory().add(StatusHistory.builder()
                    .status(ApplicationStatus.CC_DENIED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build()
            );
        }
    }


    private HttpEntity<ScoringDataDto> createHttpRequest(ScoringDataDto scoringDataDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(scoringDataDto ,httpHeaders);
    }
}
