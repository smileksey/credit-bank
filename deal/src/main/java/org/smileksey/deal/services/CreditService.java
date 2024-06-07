package org.smileksey.deal.services;

import org.smileksey.deal.dto.FinishRegistrationRequestDto;

import java.util.UUID;

public interface CreditService {

    void calculateCreditAndFinishRegistration(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto);
}
