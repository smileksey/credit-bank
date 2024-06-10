package org.smileksey.deal.services;

import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;

import java.util.UUID;

public interface StatementService {

    Statement createAndSaveStatement(Client client);

    Statement updateStatementWithSelectedOffer(LoanOfferDto loanOfferDto);

    Statement getStatementById(UUID statementId);
}
