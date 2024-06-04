package org.smileksey.calculator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.smileksey.calculator.dto.*;
import org.smileksey.calculator.dto.enums.EmploymentStatus;
import org.smileksey.calculator.dto.enums.Gender;
import org.smileksey.calculator.dto.enums.MaritalStatus;
import org.smileksey.calculator.dto.enums.Position;
import org.smileksey.calculator.services.CreditService;
import org.smileksey.calculator.services.LoanOfferService;
import org.smileksey.calculator.utils.validation.LoanStatementRequestValidator;
import org.smileksey.calculator.utils.validation.ScoringDataDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
@Import({LoanStatementRequestValidator.class, ScoringDataDtoValidator.class})
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanOfferService loanOfferService;

    @MockBean
    private CreditService creditService;


    @Test
    void calculateOffersShouldReturn200() throws Exception {

        LoanStatementRequestDto loanStatementRequestDto = createLoanStatementRequestDto();

        String loanStatementRequestDtoJson = objectMapper.writeValueAsString(loanStatementRequestDto);

        when(loanOfferService.getLoanOffers(any())).thenReturn(new ArrayList<LoanOfferDto>());


        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanStatementRequestDtoJson))
                .andExpect(status().isOk());

        verify(loanOfferService, times(1)).getLoanOffers(any());
    }


    @Test
    void calculateOffersShouldReturn400() throws Exception {
        LoanStatementRequestDto loanStatementRequestDto = new LoanStatementRequestDto();

        String loanStatementRequestDtoJson = objectMapper.writeValueAsString(loanStatementRequestDto);

        when(loanOfferService.getLoanOffers(any())).thenReturn(new ArrayList<LoanOfferDto>());

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanStatementRequestDtoJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void calculateCreditDetailsShouldReturn200() throws Exception {

        ScoringDataDto scoringDataDto = createScoringDataDto();

        String scoringDataDtoJson = objectMapper.writeValueAsString(scoringDataDto);

        when(creditService.getCreditDto(any())).thenReturn(Optional.of(new CreditDto()));

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scoringDataDtoJson))
                .andExpect(status().isOk());

        verify(creditService, times(1)).getCreditDto(any());
    }


    @Test
    void calculateCreditDetailsShouldReturn400() throws Exception {

        ScoringDataDto scoringDataDto = new ScoringDataDto();

        String scoringDataDtoJson = objectMapper.writeValueAsString(scoringDataDto);

        when(creditService.getCreditDto(any())).thenReturn(Optional.of(new CreditDto()));

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scoringDataDtoJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void calculateCreditDetailsShouldReturn404() throws Exception {

        ScoringDataDto scoringDataDto = createScoringDataDto();

        String scoringDataDtoJson = objectMapper.writeValueAsString(scoringDataDto);

        when(creditService.getCreditDto(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scoringDataDtoJson))
                .andExpect(status().isNotFound());

        verify(creditService, times(1)).getCreditDto(any());
    }


    private LoanStatementRequestDto createLoanStatementRequestDto() {

        LoanStatementRequestDto loanStatementRequestDto = new LoanStatementRequestDto();

        loanStatementRequestDto.setAmount(new BigDecimal("100000"));
        loanStatementRequestDto.setTerm(12);
        loanStatementRequestDto.setFirstName("Ivan");
        loanStatementRequestDto.setLastName("Ivanov");
        loanStatementRequestDto.setBirthdate(LocalDate.of(1990, 1, 1));
        loanStatementRequestDto.setEmail("ivanov@gmail.com");
        loanStatementRequestDto.setPassportSeries("1234");
        loanStatementRequestDto.setPassportNumber("123456");

        return loanStatementRequestDto;
    }


    private ScoringDataDto createScoringDataDto() {

        ScoringDataDto scoringDataDto = new ScoringDataDto();

        EmploymentDto employmentDto = new EmploymentDto();
        employmentDto.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employmentDto.setSalary(new BigDecimal("100000.00"));
        employmentDto.setPosition(Position.EMPLOYEE);
        employmentDto.setWorkExperienceTotal(50);
        employmentDto.setWorkExperienceCurrent(12);
        employmentDto.setEmployerINN("1234567890");

        scoringDataDto.setAmount(new BigDecimal("1000000"));
        scoringDataDto.setTerm(12);
        scoringDataDto.setFirstName("Ivan");
        scoringDataDto.setLastName("Ivanov");
        scoringDataDto.setPassportSeries("1234");
        scoringDataDto.setPassportNumber("123456");
        scoringDataDto.setAccountNumber("123456789");
        scoringDataDto.setPassportIssueDate(LocalDate.of(2020, 1, 1));
        scoringDataDto.setPassportIssueBranch("issue branch");
        scoringDataDto.setDependentAmount(0);
        scoringDataDto.setGender(Gender.MALE);
        scoringDataDto.setBirthdate(LocalDate.of(2004, 1, 1));
        scoringDataDto.setMaritalStatus(MaritalStatus.NOT_MARRIED);
        scoringDataDto.setIsInsuranceEnabled(false);
        scoringDataDto.setIsSalaryClient(false);
        scoringDataDto.setEmployment(employmentDto);

        return scoringDataDto;
    }
}