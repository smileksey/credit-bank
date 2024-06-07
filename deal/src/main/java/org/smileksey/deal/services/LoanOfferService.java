package org.smileksey.deal.services;

import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;

import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);
}
