package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class LoanOfferDto {
    private UUID statementId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private int term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private boolean isInsuranceEnabled;
    private boolean isSalaryClient;

}
