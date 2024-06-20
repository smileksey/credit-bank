package org.smileksey.statement.services;

import org.smileksey.statement.dto.LoanOfferDto;
import org.smileksey.statement.dto.LoanStatementRequestDto;

import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    public void selectOffer(LoanOfferDto loanOfferDto);
}
