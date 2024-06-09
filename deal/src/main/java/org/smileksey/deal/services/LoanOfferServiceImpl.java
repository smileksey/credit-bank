package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.exceptions.InvalidMSResponseException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.utils.HttpEntityConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanOfferServiceImpl implements LoanOfferService {

    private final ClientService clientService;
    private final StatementService statementService;

    /** URL of the 'calculator' endpoint */
    private final String CC_OFFERS_URL = "http://localhost:8080/calculator/offers";


    /**
     * Method creates a list of 4 preliminary loan offers by requesting 'calculator' microservice
     * @return list of 4 preliminary loan offers
     */
    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(CC_OFFERS_URL, HttpMethod.POST, HttpEntityConstructor.createHttpEntity(loanStatementRequestDto), new ParameterizedTypeReference<List<LoanOfferDto>>() {});
        List<LoanOfferDto> loanOffers = response.getBody();

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
