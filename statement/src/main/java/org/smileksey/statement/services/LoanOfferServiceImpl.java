package org.smileksey.statement.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.statement.dto.LoanOfferDto;
import org.smileksey.statement.dto.LoanStatementRequestDto;
import org.smileksey.statement.exceptions.InvalidMSResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanOfferServiceImpl implements LoanOfferService {

    private final DealClient dealClient;


    /**
     * Method creates a list of 4 preliminary loan offers by requesting 'calculator' microservice
     * @return list of 4 preliminary loan offers
     */
    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {

        ResponseEntity<List<LoanOfferDto>> loanOffersResponse = dealClient.getLoanOffersResponse(loanStatementRequestDto);

        List<LoanOfferDto> loanOffers = loanOffersResponse.getBody();

        if (loanOffers != null && !loanOffers.isEmpty()) {

            log.info("**** Final LoanOfferDto list: ****");

            for (LoanOfferDto loanOfferDto : loanOffers) {
                log.info(String.valueOf(loanOfferDto));
            }

        } else {
            throw new InvalidMSResponseException("Failed to get LoanOfferDtos from 'deal'");
        }

        return loanOffers;
    }
}
