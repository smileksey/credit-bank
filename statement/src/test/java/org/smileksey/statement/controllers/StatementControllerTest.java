package org.smileksey.statement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.smileksey.statement.dto.LoanOfferDto;
import org.smileksey.statement.dto.LoanStatementRequestDto;
import org.smileksey.statement.services.LoanOfferService;
import org.smileksey.statement.utils.validation.LoanStatementRequestValidator;
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

@WebMvcTest(StatementController.class)
@Import({LoanStatementRequestValidator.class})
class StatementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanOfferService loanOfferService;

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

        when(loanOfferService.getLoanOffers(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(loanOfferService, times(1)).getLoanOffers(any());
    }


    @Test
    void calculateOffersShouldReturn400() throws Exception {

        LoanStatementRequestDto loanStatementRequestDto = new LoanStatementRequestDto();

        String body = objectMapper.writeValueAsString(loanStatementRequestDto);

        when(loanOfferService.getLoanOffers(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/statement")
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

        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(loanOfferService, times(1)).selectOffer(any());
    }


    @Test
    void selectOfferShouldReturn400() throws Exception {

        String body = objectMapper.writeValueAsString(new LoanOfferDto());

        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}