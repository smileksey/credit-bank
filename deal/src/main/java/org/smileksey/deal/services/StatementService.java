package org.smileksey.deal.services;

import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;

public interface StatementService {

    Statement createAndSaveStatement(Client client);
}
