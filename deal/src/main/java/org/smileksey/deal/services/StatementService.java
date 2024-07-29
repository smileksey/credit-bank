package org.smileksey.deal.services;

import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.enums.ApplicationStatus;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;

import java.util.List;
import java.util.UUID;

public interface StatementService {

    Statement createAndSaveStatement(Client client);

    Statement updateStatementWithSelectedOffer(LoanOfferDto loanOfferDto);

    Statement getStatementById(UUID statementId);

    List<Statement> getAllStatements();

    void updateToDocumentCreated(UUID statementId);

    void updateStatement(Statement statement);

    void updateStatementStatus(Statement statement, ApplicationStatus newStatus);
}
