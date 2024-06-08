package org.smileksey.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanOfferDto {
    @Schema(description = "Unique identificator")
    private UUID statementId;

    @NotNull(message = "'amount' is not specified")
    @Schema(description = "Offered loan amount",
            example = "500000")
    private BigDecimal requestedAmount;

    @NotNull(message = "'totalAmount' is not specified")
    @Schema(description = "Total amount of all loan payments",
            example = "1265175.84")
    private BigDecimal totalAmount;

    @NotNull(message = "'term' is not specified")
    @Schema(description = "Loan term, months",
            example = "12")
    private Integer term;

    @NotNull(message = "'monthlyPayment' is not specified")
    @Schema(description = "Monthly payment amount",
            example = "105431.32")
    private BigDecimal monthlyPayment;

    @NotNull(message = "'rate' is not specified")
    @Schema(description = "Loan rate, %",
            example = "18.00")
    private BigDecimal rate;

    @NotNull(message = "'isInsuranceEnabled' is not specified")
    @Schema(description = "Having insurance",
            example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull(message = "'isSalaryClient' is not specified")
    @Schema(description = "Is a salary client",
            example = "false")
    private Boolean isSalaryClient;

}
