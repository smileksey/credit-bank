package org.smileksey.calculator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.calculator.calculators.CreditParamsCalculator;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.utils.LoanOfferDtoComparator;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanOfferServiceImplTest {

    @Mock
    private CreditParamsCalculator creditParamsCalculator;

    @InjectMocks
    private LoanOfferServiceImpl loanOfferService;

    @Test
    void getLoanOffers() {

        ReflectionTestUtils.setField(loanOfferService, "stringBaseRate", "21.00");

        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        BigDecimal amount = new BigDecimal("100000");

        requestDto.setAmount(amount);

        BigDecimal rateInsFalseSalFalse = new BigDecimal("23");
        BigDecimal rateInsFalseSalTrue = new BigDecimal("22");
        BigDecimal rateInsTrueSalFalse = new BigDecimal("21");
        BigDecimal rateInsTrueSalTrue = new BigDecimal("20");

        BigDecimal monthlyPayment1 = new BigDecimal("5000");
        BigDecimal monthlyPayment2 = new BigDecimal("6000");
        BigDecimal monthlyPayment3 = new BigDecimal("7000");
        BigDecimal monthlyPayment4 = new BigDecimal("8000");

        BigDecimal totalAmountInsFalseSalFalse = new BigDecimal("110000");
        BigDecimal totalAmountInsFalseSalTrue = new BigDecimal("107000");
        BigDecimal totalAmountInsTrueSalFalse = new BigDecimal("105000");
        BigDecimal totalAmountInsTrueSalTrue = new BigDecimal("103000");

        BigDecimal amountInsTrue = new BigDecimal("150000");

        when(creditParamsCalculator.calculateAmount(any(), any())).thenReturn(
                amount,
                amount,
                amountInsTrue,
                amountInsTrue
        );

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(
                rateInsFalseSalFalse,
                rateInsFalseSalTrue,
                rateInsTrueSalFalse,
                rateInsTrueSalTrue
        );

        when(creditParamsCalculator.calculateMonthlyPayment(any(), any(), any(), any())).thenReturn(
                monthlyPayment1,
                monthlyPayment2,
                monthlyPayment3,
                monthlyPayment4
        );

        when(creditParamsCalculator.calculateTotalAmount(any(), any())).thenReturn(
                totalAmountInsFalseSalFalse,
                totalAmountInsFalseSalTrue,
                totalAmountInsTrueSalFalse,
                totalAmountInsTrueSalTrue
                );


        List<LoanOfferDto> loanOffers = loanOfferService.getLoanOffers(requestDto);

        assertEquals(4, loanOffers.size());

        assertEquals(amount, loanOffers.get(0).getRequestedAmount());
        assertEquals(amount, loanOffers.get(1).getRequestedAmount());
        assertEquals(amountInsTrue, loanOffers.get(2).getRequestedAmount());
        assertEquals(amountInsTrue, loanOffers.get(3).getRequestedAmount());

        assertEquals(rateInsFalseSalFalse, loanOffers.get(0).getRate());
        assertEquals(rateInsFalseSalTrue, loanOffers.get(1).getRate());
        assertEquals(rateInsTrueSalFalse, loanOffers.get(2).getRate());
        assertEquals(rateInsTrueSalTrue, loanOffers.get(3).getRate());

        assertEquals(monthlyPayment1, loanOffers.get(0).getMonthlyPayment());
        assertEquals(monthlyPayment2, loanOffers.get(1).getMonthlyPayment());
        assertEquals(monthlyPayment3, loanOffers.get(2).getMonthlyPayment());
        assertEquals(monthlyPayment4, loanOffers.get(3).getMonthlyPayment());

        assertEquals(totalAmountInsFalseSalFalse, loanOffers.get(0).getTotalAmount());
        assertEquals(totalAmountInsFalseSalTrue, loanOffers.get(1).getTotalAmount());
        assertEquals(totalAmountInsTrueSalFalse, loanOffers.get(2).getTotalAmount());
        assertEquals(totalAmountInsTrueSalTrue, loanOffers.get(3).getTotalAmount());

        verify(creditParamsCalculator, times(4)).calculateAmount(any(), any());
        verify(creditParamsCalculator, times(4)).calculateRate(any(), any(), any());
        verify(creditParamsCalculator, times(4)).calculateInsurancePrice(any(), any(), any());
        verify(creditParamsCalculator, times(4)).calculateMonthlyPayment(any(), any(), any(), any());
        verify(creditParamsCalculator, times(4)).calculateTotalAmount(any(), any());
    }
}