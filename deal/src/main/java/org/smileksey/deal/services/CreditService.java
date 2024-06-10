package org.smileksey.deal.services;

import org.smileksey.deal.dto.FinishRegistrationRequestDto;
import org.smileksey.deal.models.Credit;

import java.util.UUID;

public interface CreditService {

    Credit calculateCreditAndFinishRegistration(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto);
}
