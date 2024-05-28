package org.smileksey.calculator.calculators;

import org.junit.jupiter.api.Test;
import org.smileksey.calculator.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditParamsCalculatorImplTest {

    private final CreditParamsCalculatorImpl creditParamsCalculator = new CreditParamsCalculatorImpl();

    @Test
    void calculateMonthlyPaymen() {
        BigDecimal monthlyPayment = creditParamsCalculator.calculateMonthlyPayment(
                new BigDecimal("1000000"), new BigDecimal("10"), 12, new BigDecimal("5000"));

        assertEquals(new BigDecimal("88332.56"), monthlyPayment);
    }

    @Test
    void calculateTotalAmount() {
        BigDecimal totalAmount = creditParamsCalculator.calculateTotalAmount(new BigDecimal("10000"), 12);

        assertEquals(new BigDecimal("120000"), totalAmount);
    }

    @Test
    void calculateRate() {
        BigDecimal initialRate = new BigDecimal("21");

        BigDecimal rateInsFalseSalFalse = creditParamsCalculator.calculateRate(initialRate, false, false);
        BigDecimal rateInsTrueSalFalse = creditParamsCalculator.calculateRate(initialRate, true, false);
        BigDecimal rateInsFalseSalTrue = creditParamsCalculator.calculateRate(initialRate, false, true);
        BigDecimal rateInsTrueSalTrue = creditParamsCalculator.calculateRate(initialRate, true, true);

        assertEquals(new BigDecimal("22.00"), rateInsFalseSalFalse);
        assertEquals(new BigDecimal("18.00"), rateInsTrueSalFalse);
        assertEquals(new BigDecimal("21.00"), rateInsFalseSalTrue);
        assertEquals(new BigDecimal("17.00"), rateInsTrueSalTrue);
    }

    @Test
    void calculateAmount() {
        BigDecimal amountInsTrue = creditParamsCalculator.calculateAmount(new BigDecimal("1000000"), true);
        BigDecimal amountInsFalse = creditParamsCalculator.calculateAmount(new BigDecimal("1000000"), false);

        assertEquals(new BigDecimal("1100000.00"), amountInsTrue);
        assertEquals(new BigDecimal("1000000.00"), amountInsFalse);
    }

    @Test
    void calculateInsurancePrice() {
        BigDecimal insurancePriceInsFalseSalFalse = creditParamsCalculator.calculateInsurancePrice(new BigDecimal("1000000"), false, false);
        BigDecimal insurancePriceInsTrueSalFalse = creditParamsCalculator.calculateInsurancePrice(new BigDecimal("1000000"), true, false);
        BigDecimal insurancePriceInsFalseSalTrue = creditParamsCalculator.calculateInsurancePrice(new BigDecimal("1000000"), false, true);
        BigDecimal insurancePriceInsTrueSalTrue = creditParamsCalculator.calculateInsurancePrice(new BigDecimal("1000000"), true, true);

        assertEquals(new BigDecimal("0.00"), insurancePriceInsFalseSalFalse);
        assertEquals(new BigDecimal("50000.00"), insurancePriceInsTrueSalFalse);
        assertEquals(new BigDecimal("0.00"), insurancePriceInsFalseSalTrue);
        assertEquals(new BigDecimal("0.00"), insurancePriceInsTrueSalTrue);
    }

    @Test
    void calculatePSK() {
        BigDecimal psk = creditParamsCalculator.calculatePSK(new BigDecimal("1138770.29"), new BigDecimal("1000000"), 12);

        assertEquals(new BigDecimal("13.88"), psk);
    }

    @Test
    void getPaymentSchedule() {

        int term = 12;

        List<PaymentScheduleElementDto> paymentSchedule = creditParamsCalculator.getPaymentSchedule(
                new BigDecimal("87915.89"), new BigDecimal("1000000"), new BigDecimal("10"), term);

        assertEquals(term, paymentSchedule.size());

        PaymentScheduleElementDto lastPayment = paymentSchedule.get(paymentSchedule.size() - 1);

        assertEquals(term, lastPayment.getNumber());
        assertEquals(paymentSchedule.get(0).getDate().plusMonths(term - 1), lastPayment.getDate());
        assertEquals(new BigDecimal("0"), lastPayment.getRemainingDebt());
        assertEquals(lastPayment.getTotalPayment(), lastPayment.getDebtPayment().add(lastPayment.getInterestPayment()));
    }
}