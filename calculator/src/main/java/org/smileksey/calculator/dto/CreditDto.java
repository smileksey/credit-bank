package org.smileksey.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class CreditDto {
    @Schema(description = "Сумма кредита")
    private BigDecimal amount;

    @Schema(description = "Срок кредита, мес.")
    private Integer term;

    @Schema(description = "Размер ежемесячного платежа")
    private BigDecimal monthlyPayment;

    @Schema(description = "Ставка, %")
    private BigDecimal rate;

    @Schema(description = "Полная стоимость кредита (ПСК), %")
    private BigDecimal psk;

    @Schema(description = "Наличие страховки")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является ли зарплатным клиентом")
    private Boolean isSalaryClient;

    @Schema(description = "График платежей")
    private List<PaymentScheduleElementDto> paymentSchedule;
}
