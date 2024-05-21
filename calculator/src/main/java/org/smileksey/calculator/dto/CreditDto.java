package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreditDto {
    private BigDecimal amount;
    private int term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private boolean isInsuranceEnabled;
    private boolean isSalaryClient;
    private List<PaymentScheduleElementDto> paymentSchedule;
}
