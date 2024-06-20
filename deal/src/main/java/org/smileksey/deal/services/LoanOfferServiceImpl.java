package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.exceptions.InvalidMSResponseException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanOfferServiceImpl implements LoanOfferService {

    private final ClientService clientService;
    private final StatementService statementService;
    private final CalculatorClient calculatorClient;


    /**
     * Method creates a list of 4 preliminary loan offers by requesting 'calculator' microservice
     * @return list of 4 preliminary loan offers
     */
    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {

        ResponseEntity<List<LoanOfferDto>> loanOffersResponse = calculatorClient.getLoanOffersResponse(loanStatementRequestDto);

        List<LoanOfferDto> loanOffers = loanOffersResponse.getBody();

        if (loanOffers != null && !loanOffers.isEmpty()) {

            Client client = clientService.createAndSaveClient(loanStatementRequestDto);
            Statement statement = statementService.createAndSaveStatement(client);

            log.info("**** Final LoanOfferDto list: ****");

            for (LoanOfferDto loanOfferDto : loanOffers) {
                loanOfferDto.setStatementId(statement.getStatementId());
                log.info(String.valueOf(loanOfferDto));
            }

        } else {
            throw new InvalidMSResponseException("Failed to get LoanOfferDtos from 'calculator'");
        }

        return loanOffers;
    }

}
