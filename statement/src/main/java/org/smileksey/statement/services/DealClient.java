package org.smileksey.statement.services;

import lombok.RequiredArgsConstructor;
import org.smileksey.statement.dto.LoanOfferDto;
import org.smileksey.statement.dto.LoanStatementRequestDto;
import org.smileksey.statement.utils.HttpEntityConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealClient {

    private final RestTemplate restTemplate;

    /** URL of the 'deal' microservice */
    @Value("${deal.url}")
    private String dealUrl;

    /** Path to the '/deal/statement' endpoint */
    private static final String STATEMENT_PATH = "/deal/statement";

    /** Path to the '/deal/offer/select' endpoint */
    private static final String OFFER_SELECT_PATH = "/deal/offer/select";


    /**
     * Method makes a request to the 'deal' microservice and returns its response with a list of LoanOfferDtos
     * @param loanStatementRequestDto - request body
     * @return ResponseEntity containing a list of LoanOfferDto entities
     */
    public ResponseEntity<List<LoanOfferDto>> getLoanOffersResponse(LoanStatementRequestDto loanStatementRequestDto) {
        return restTemplate.exchange(dealUrl + STATEMENT_PATH, HttpMethod.POST, HttpEntityConstructor.createHttpEntity(loanStatementRequestDto), new ParameterizedTypeReference<List<LoanOfferDto>>() {});
    }


    //    /**
//     * Method makes a request to the 'calculator' microservice and returns its response with CreditDto entity
//     * @param scoringDataDto - request body
//     * @return ResponseEntity containing a CreditDto entity
//     */
//    public ResponseEntity<CreditDto> getCreditDtoResponse(ScoringDataDto scoringDataDto) {
//        return restTemplate.exchange(dealUrl + STATEMENT_PATH, HttpMethod.POST, HttpEntityConstructor.createHttpEntity(scoringDataDto), new ParameterizedTypeReference<CreditDto>() {});
//    }
}
