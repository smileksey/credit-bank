package org.smileksey.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditDto {
    @Schema(description = "Loan amount")
    private BigDecimal amount;

    @Schema(description = "Loan term, months")
    private Integer term;

    @Schema(description = "Monthly payment amount")
    private BigDecimal monthlyPayment;

    @Schema(description = "Loan rate, %")
    private BigDecimal rate;

    @Schema(description = "Total loan cost, %")
    private BigDecimal psk;

    @Schema(description = "Having insurance")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Is a salary client")
    private Boolean isSalaryClient;

    @Schema(description = "Payment schedule")
    private List<PaymentScheduleElementDto> paymentSchedule;
}
