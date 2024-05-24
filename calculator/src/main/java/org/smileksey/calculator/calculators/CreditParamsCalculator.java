package org.smileksey.calculator.calculators;

import java.math.BigDecimal;

public interface CreditParamsCalculator {

    BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term, BigDecimal insurancePrice);

    BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, Integer term);

    BigDecimal calculateRate(BigDecimal initialRate, Boolean isInsuranceEnabled, Boolean isSalaryClient);

    BigDecimal calculateAmount(BigDecimal initialAmount, Boolean isInsuranceEnabled);

    BigDecimal calculateInsurancePrice(BigDecimal amount, Boolean isInsuranceEnabled, Boolean isSalaryClient);
}
