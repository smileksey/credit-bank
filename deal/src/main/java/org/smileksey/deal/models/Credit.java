package org.smileksey.deal.models;

import org.smileksey.calculator.dto.PaymentScheduleElementDto;
import org.smileksey.deal.models.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Credit {
    private UUID creditId;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private List<PaymentScheduleElementDto> paymentSchedule;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private CreditStatus creditStatus;
}
