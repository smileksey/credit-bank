package org.smileksey.gateway.services;

import org.smileksey.gateway.dto.FinishRegistrationRequestDto;
import org.smileksey.gateway.dto.LoanOfferDto;
import org.smileksey.gateway.dto.LoanStatementRequestDto;
import org.smileksey.gateway.dto.SESCodeDto;

import java.util.List;
import java.util.UUID;

public interface GatewayService {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);
    void selectOffer(LoanOfferDto loanOfferDto);
    void calculateCredit(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto);
    void sendDocuments(UUID statementId);
    void signDocuments(UUID statementId);
    void verifySesCode(UUID statementId, SESCodeDto sesCodeDto);

}
