package org.smileksey.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
public class LoanOfferDto {
    @Schema(description = "Unique identificator")
    private UUID statementId;

    @Schema(description = "Offered loan amount")
    private BigDecimal requestedAmount;

    @Schema(description = "Total amount of all loan payments")
    private BigDecimal totalAmount;

    @Schema(description = "Loan term, months")
    private Integer term;

    @Schema(description = "Monthly payment amount")
    private BigDecimal monthlyPayment;

    @Schema(description = "Loan rate, %")
    private BigDecimal rate;

    @Schema(description = "Having insurance")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Is a salary client")
    private Boolean isSalaryClient;

}
