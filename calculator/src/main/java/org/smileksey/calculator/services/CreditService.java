package org.smileksey.calculator.services;

import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.ScoringDataDto;

import java.util.Optional;

public interface CreditService {

    Optional<CreditDto> getCreditDto(ScoringDataDto scoringDataDto);
}
