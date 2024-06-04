package org.smileksey.deal.models;

import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.deal.models.enums.ApplicationStatus;

import java.time.LocalDate;
import java.util.UUID;

public class Statement {

    private UUID statementId;
    private Client client;
    private Credit credit;
    private ApplicationStatus status;
    private LocalDate creationDate;
    private LoanOfferDto appliedOffer;
    private LocalDate signDate;
    private String sesCode;
    private StatusHistory statusHistory;
}
