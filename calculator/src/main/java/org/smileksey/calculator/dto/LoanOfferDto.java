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
    @Schema(description = "Уникальный идентификатор")
    private UUID statementId;

    @Schema(description = "Запрошенная сумма кредита")
    private BigDecimal requestedAmount;

    @Schema(description = "Предлагаемая сумма кредита")
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита, мес.")
    private Integer term;

    @Schema(description = "Размер ежемесячного платежа")
    private BigDecimal monthlyPayment;

    @Schema(description = "Ставка, %")
    private BigDecimal rate;

    @Schema(description = "Наличие страховки")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является ли зарплатным клиентом")
    private Boolean isSalaryClient;

}
