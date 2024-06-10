package org.smileksey.deal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.deal.dto.EmploymentDto;
import org.smileksey.deal.dto.FinishRegistrationRequestDto;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.enums.EmploymentPosition;
import org.smileksey.deal.dto.enums.EmploymentStatus;
import org.smileksey.deal.dto.enums.Gender;
import org.smileksey.deal.dto.enums.MaritalStatus;
import org.smileksey.deal.models.Credit;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.services.CreditService;
import org.smileksey.deal.services.LoanOfferServiceImpl;
import org.smileksey.deal.services.StatementService;
import org.smileksey.deal.utils.validation.LoanStatementRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DealController.class)
@Import({LoanStatementRequestValidator.class})
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanOfferServiceImpl loanOfferServiceImpl;

    @MockBean
    private StatementService statementService;

    @MockBean
    private CreditService creditService;

    @MockBean
    private RestTemplate restTemplate;


    @Test
    void calculateOffersShouldReturn200() throws Exception {

        LoanStatementRequestDto validLoanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(new BigDecimal("100000"))
                .term(12)
                .firstName("Ivan")
                .lastName("Ivanov")
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("ivanov@gmail.com")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        String body = objectMapper.writeValueAsString(validLoanStatementRequestDto);

        when(loanOfferServiceImpl.getLoanOffers(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(loanOfferServiceImpl, times(1)).getLoanOffers(any());
    }


    @Test
    void calculateOffersShouldReturn400() throws Exception {

        LoanStatementRequestDto loanStatementRequestDto = new LoanStatementRequestDto();

        String body = objectMapper.writeValueAsString(loanStatementRequestDto);

        when(loanOfferServiceImpl.getLoanOffers(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void selectOfferShouldReturn200() throws Exception {

        LoanOfferDto validLoanOfferDto = LoanOfferDto.builder()
                .requestedAmount(new BigDecimal("100000"))
                .totalAmount(new BigDecimal("100000"))
                .term(12)
                .monthlyPayment(new BigDecimal("8000"))
                .rate(new BigDecimal("12"))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        String body = objectMapper.writeValueAsString(validLoanOfferDto);

        when(statementService.updateStatementWithSelectedOffer(any())).thenReturn(new Statement());

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }


    @Test
    void selectOfferShouldReturn400() throws Exception {

        String body = objectMapper.writeValueAsString(new LoanOfferDto());

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }


    @Test
    void calculateCreditDetailsShouldReturn200() throws Exception {

        FinishRegistrationRequestDto validFinishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
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

        String body = objectMapper.writeValueAsString(validFinishRegistrationRequestDto);

        when(creditService.calculateCreditAndFinishRegistration(any(), any())).thenReturn(new Credit());

        mockMvc.perform(post("/deal/calculate/04cd382d-d411-4f40-8efb-712f568acf32")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }


    @Test
    void calculateCreditDetailsShouldReturn400() throws Exception {

        String body = objectMapper.writeValueAsString(new FinishRegistrationRequestDto());

        mockMvc.perform(post("/deal/calculate/04cd382d-d411-4f40-8efb-712f568acf32")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }


}