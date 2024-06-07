package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final StatementService statementService;
    private final CreditRepository creditRepository;

    private final String CALC_URL = "http://localhost:8080/calculator/calc";

    @Transactional
    @Override
    public void calculateCreditAndFinishRegistration(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {

        //FIXME разобраться, что делать в случае отказа в кредите, разделить метод

        Statement statement = statementService.getStatementById(statementId);
        Client client = statement.getClient();
        LoanOfferDto appliedOffer = statement.getAppliedOffer();

        log.info("Statement: {}", statement);

        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
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

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<CreditDto> response = restTemplate.exchange(CALC_URL, HttpMethod.POST, createHttpEntity(scoringDataDto), new ParameterizedTypeReference<CreditDto>() {});

        CreditDto creditDto = response.getBody();

        if (creditDto != null) {

            Credit credit = Credit.builder()
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

            log.info("Credit: {}", credit);

            Credit savedCredit = creditRepository.save(credit);

            statement.setStatus(ApplicationStatus.CC_APPROVED);
            statement.setCredit(savedCredit);

            statement.getStatusHistory().add(StatusHistory.builder()
                    .status(ApplicationStatus.CC_APPROVED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build()
            );


        } else {
            throw new InvalidMSResponseException("Failed to get CreditDto from 'calculator'");
        }
    }


    private HttpEntity<ScoringDataDto> createHttpEntity(ScoringDataDto scoringDataDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(scoringDataDto ,httpHeaders);
    }
}
