package org.smileksey.calculator.services;

import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;

import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);
}
