package org.smileksey.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PaymentScheduleElementDto {
    @Schema(description = "Порядковый номер платежа")
    private Integer number;

    @Schema(description = "Дата платежа")
    private LocalDate date;

    @Schema(description = "Общая сумма платежа")
    private BigDecimal totalPayment;

    @Schema(description = "Часть платежа на погашение процентов")
    private BigDecimal interestPayment;

    @Schema(description = "Часть платежа на погашение основного долга")
    private BigDecimal debtPayment;

    @Schema(description = "Сумма оставшегося основного долга")
    private BigDecimal remainingDebt;
}
