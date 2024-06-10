package org.smileksey.deal.services;

import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.models.Client;

public interface ClientService {

    Client createAndSaveClient(LoanStatementRequestDto loanStatementRequestDto);

}
