package org.smileksey.deal.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.deal.dto.*;
import org.smileksey.deal.dto.enums.*;
import org.smileksey.deal.exceptions.InvalidMSResponseException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Credit;
import org.smileksey.deal.models.Passport;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.repositories.CreditRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    private StatementService statementService;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CreditServiceImpl creditServiceImpl;


    @Test
    void calculateCreditAndFinishRegistrationShouldReturnFilledOptional() {

        FinishRegistrationRequestDto validFinishRegistrationRequestDto = createFinishRegistrationRequestDto();
        CreditDto mockCreditDto = createCreditDto();

        when(statementService.getStatementById(any())).thenReturn(createStatement(createLoanOfferDto()));
        when(creditRepository.save(any())).thenReturn(createCredit(createCreditDto()));

        lenient().when(restTemplate.exchange(
                        anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        (ParameterizedTypeReference<Object>) any()))
                .thenReturn(new ResponseEntity<>(new CreditDto(), HttpStatus.OK));

        Optional<Credit> creditOptional = creditServiceImpl.calculateCreditAndFinishRegistration(UUID.randomUUID(), validFinishRegistrationRequestDto);

        assertNotNull(creditOptional);
        assertTrue(creditOptional.isPresent());
        assertEquals(mockCreditDto.getAmount(), creditOptional.get().getAmount());
        assertEquals(mockCreditDto.getTerm(), creditOptional.get().getTerm());
        assertEquals(CreditStatus.CALCULATED, creditOptional.get().getCreditStatus());

        verify(statementService, times(1)).getStatementById(any());
        verify(creditRepository, times(1)).save(any());
        verify(restTemplate, times(1)).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Object>) any());
    }


    @Test
    void calculateCreditAndFinishRegistrationShouldReturnEmptyOptional() {

        FinishRegistrationRequestDto validFinishRegistrationRequestDto = createFinishRegistrationRequestDto();

        when(statementService.getStatementById(any())).thenReturn(createStatement(createLoanOfferDto()));

        lenient().when(restTemplate.exchange(
                        anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        (ParameterizedTypeReference<Object>) any()))
                .thenReturn(new ResponseEntity<>(new CreditDto(), HttpStatus.NOT_FOUND));

        Optional<Credit> creditOptional = creditServiceImpl.calculateCreditAndFinishRegistration(UUID.randomUUID(), validFinishRegistrationRequestDto);

        assertNotNull(creditOptional);
        assertFalse(creditOptional.isPresent());

        verify(creditRepository, times(0)).save(any());
    }


    @Test
    void calculateCreditAndFinishRegistrationShouldThrowInvalidMSResponseException() {

        FinishRegistrationRequestDto validFinishRegistrationRequestDto = createFinishRegistrationRequestDto();

        when(statementService.getStatementById(any())).thenReturn(createStatement(createLoanOfferDto()));

        lenient().when(restTemplate.exchange(
                        anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        (ParameterizedTypeReference<Object>) any()))
                .thenReturn(new ResponseEntity<>(new CreditDto(), HttpStatus.BAD_REQUEST));

        assertThrows(InvalidMSResponseException.class, () -> creditServiceImpl.calculateCreditAndFinishRegistration(UUID.randomUUID(), validFinishRegistrationRequestDto));
    }


    private FinishRegistrationRequestDto createFinishRegistrationRequestDto() {

        return FinishRegistrationRequestDto.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(0)
                .passportIssueDate(LocalDate.of(2020, 1, 1))
                .passportIssueBranch("Issue branch")
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .employerINN("1234567890")
                        .salary(new BigDecimal("80000"))
                        .position(EmploymentPosition.MID_MANAGER)
                        .workExperienceTotal(60)
                        .workExperienceCurrent(3)
                        .build())
                .accountNumber("1234567890")
                .build();
    }


    private LoanOfferDto createLoanOfferDto() {
        return LoanOfferDto.builder()
                .requestedAmount(new BigDecimal("100000"))
                .totalAmount(new BigDecimal("100000"))
                .term(12)
                .monthlyPayment(new BigDecimal("8000"))
                .rate(new BigDecimal("12"))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }


    private CreditDto createCreditDto() {

        return CreditDto.builder()
                .amount(new BigDecimal("100000"))
                .term(12)
                .build();
    }


    private Statement createStatement(LoanOfferDto loanOfferDto) {

        return Statement.builder()
                .client(Client.builder()
                        .lastName("Ivanov")
                        .firstName("Ivan")
                        .middleName("Ivanovich")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .email("ivanov@mail.com")
                        .passport(Passport.builder()
                                .series("1111")
                                .number("222222")
                                .build())
                        .build())
                .appliedOffer(loanOfferDto)
                .statusHistory(new ArrayList<>())
                .build();
    }


    private Credit createCredit(CreditDto creditDto) {

        return Credit.builder()
                .amount(creditDto.getAmount())
                .term(creditDto.getTerm())
                .creditStatus(CreditStatus.CALCULATED)
                .build();
    }
}