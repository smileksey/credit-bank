package org.smileksey.calculator.calculators;

import org.smileksey.calculator.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;

public interface CreditParamsCalculator {

    BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term, BigDecimal insurancePrice);

    BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, Integer term);

    BigDecimal calculateRate(BigDecimal initialRate, Boolean isInsuranceEnabled, Boolean isSalaryClient);

    BigDecimal calculateAmount(BigDecimal initialAmount, Boolean isInsuranceEnabled);

    BigDecimal calculateInsurancePrice(BigDecimal amount, Boolean isInsuranceEnabled, Boolean isSalaryClient);

    BigDecimal calculatePSK(BigDecimal totalAmount, BigDecimal amount, Integer term);

    List<PaymentScheduleElementDto> getPaymentSchedule(BigDecimal monthlyPayment, BigDecimal amount, BigDecimal rate, Integer term);
}
