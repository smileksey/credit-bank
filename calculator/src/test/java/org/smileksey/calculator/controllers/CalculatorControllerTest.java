package org.smileksey.calculator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.services.LoanOfferServiceImpl;
import org.smileksey.calculator.utils.validation.LoanStatementRequestValidator;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private LoanOfferServiceImpl loanOfferService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private LoanStatementRequestValidator loanStatementRequestValidator;

    @InjectMocks
    private CalculatorController calculatorController;

    //TODO
    @Test
    void getOffers() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

//        LoanStatementRequestDto loanStatementRequestDto = new LoanStatementRequestDto();
//        loanStatementRequestDto.setAmount(new BigDecimal("100000"));
//        loanStatementRequestDto.setTerm(12);
//        loanStatementRequestDto.setFirstName("Ivan");
//        loanStatementRequestDto.setLastName("Ivanov");
//        loanStatementRequestDto.setBirthdate(LocalDate.of(1990, 1, 1));
//        loanStatementRequestDto.setEmail("ivanov@gmail.com");
//        loanStatementRequestDto.setPassportSeries("1234");
//        loanStatementRequestDto.setPassportNumber("123456");

        when(loanOfferService.getLoanOffers(any())).thenReturn(new ArrayList<LoanOfferDto>());
        when(bindingResult.hasErrors()).thenReturn(false);

        List<LoanOfferDto> loanOfferDtos = calculatorController.getOffers(new LoanStatementRequestDto(), bindingResult);


    }

    //TODO
    @Test
    void getCreditDetails() {
    }
}