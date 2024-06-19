package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import org.smileksey.deal.dto.CreditDto;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.dto.ScoringDataDto;
import org.smileksey.deal.utils.HttpEntityConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorClient {

    private final RestTemplate restTemplate;

    /** URL of the 'calculator' microservice */
    @Value("${calculator.url}")
    private String calculatorUrl;

    /** Path to the '/calculator/calc' endpoint */
    private static final String CALC_PATH = "/calculator/calc";

    /** Path to the '/calculator/offers' endpoint */
    private static final String OFFERS_PATH = "/calculator/offers";


    /**
     * Method makes a request to the 'calculator' microservice and returns its response with CreditDto entity
     * @param scoringDataDto - request body
     * @return ResponseEntity containing a CreditDto entity
     */
    public ResponseEntity<CreditDto> getCreditDtoResponse(ScoringDataDto scoringDataDto) {
        return restTemplate.exchange(calculatorUrl + CALC_PATH, HttpMethod.POST, HttpEntityConstructor.createHttpEntity(scoringDataDto), new ParameterizedTypeReference<CreditDto>() {});
    }


    /**
     * Method makes a request to the 'calculator' microservice and returns its response with a list of LoanOfferDtos
     * @param loanStatementRequestDto - request body
     * @return ResponseEntity containing a list of LoanOfferDto entities
     */
    public ResponseEntity<List<LoanOfferDto>> getLoanOffersResponse(LoanStatementRequestDto loanStatementRequestDto) {
        return restTemplate.exchange(calculatorUrl + OFFERS_PATH, HttpMethod.POST, HttpEntityConstructor.createHttpEntity(loanStatementRequestDto), new ParameterizedTypeReference<List<LoanOfferDto>>() {});
    }

}
