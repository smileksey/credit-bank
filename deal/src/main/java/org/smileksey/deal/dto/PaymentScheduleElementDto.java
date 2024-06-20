package org.smileksey.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElementDto {
    @Schema(description = "Number of payment")
    private Integer number;

    @Schema(description = "Date of payment")
    private LocalDate date;

    @Schema(description = "Total amount of payment")
    private BigDecimal totalPayment;

    @Schema(description = "Interest part of payment")
    private BigDecimal interestPayment;

    @Schema(description = "Debt part of payment")
    private BigDecimal debtPayment;

    @Schema(description = "Amount of remaining debt")
    private BigDecimal remainingDebt;
}
