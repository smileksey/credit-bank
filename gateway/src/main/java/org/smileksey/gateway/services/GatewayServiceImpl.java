package org.smileksey.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.gateway.dto.FinishRegistrationRequestDto;
import org.smileksey.gateway.dto.LoanOfferDto;
import org.smileksey.gateway.dto.LoanStatementRequestDto;
import org.smileksey.gateway.dto.SESCodeDto;
import org.smileksey.gateway.utils.HttpEntityConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayServiceImpl {

    private final RestTemplate restTemplate;

    /** URL of the 'statement' microservice */
    @Value("${statement.url}")
    private String statementUrl;

    /** URL of the 'deal' microservice */
    @Value("${deal.url}")
    private String dealUrl;

    /** Path to the '/statement' endpoint */
    private static final String STATEMENT_PATH = "/statement";

    /** Path to the '/statement/offer' endpoint */
    private static final String OFFER_SELECT_PATH = "/statement/offer";

    /** Path to the '/deal/calculate/{statementId}' endpoint */
    private static final String CALCULATE_CREDIT_PATH = "/deal/calculate/";

    /** Path to the '/deal/document/{statementId}/send' endpoint */
    private static final String SEND_DOCUMENTS_PATH = "/deal/document/{statementId}/send";

    /** Path to the '/deal/document/{statementId}/sign' endpoint */
    private static final String SIGN_DOCUMENTS_PATH = "/deal/document/{statementId}/sign";

    /** Path to the '/deal/document/{statementId}/code' endpoint */
    private static final String VERIFY_SES_CODE_PATH = "/deal/document/{statementId}/code";


    /**
     * Method makes a request to statement's '/statement' endpoint and returns its response with a list of LoanOfferDtos
     * @param loanStatementRequestDto - request body
     * @return ResponseEntity containing a list of LoanOfferDto entities
     */
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        String url = statementUrl + STATEMENT_PATH;
        log.info("Sending request to {}", url);

        ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntityConstructor.createHttpEntity(loanStatementRequestDto),
                new ParameterizedTypeReference<List<LoanOfferDto>>() {}
        );

        log.info("Response status: {}", response.getStatusCode());

        return response.getBody();
    }


    /**
     * Method makes a request to statement's '/statement/offer' endpoint
     * @param loanOfferDto - request body
     */
    public void selectOffer(LoanOfferDto loanOfferDto) {
        String url = statementUrl + OFFER_SELECT_PATH;
        log.info("Sending request to {}", url);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntityConstructor.createHttpEntity(loanOfferDto),
                Void.class
        );

        log.info("Response status: {}", response.getStatusCode());
    }


    /**
     * Method makes a request to deal's '/deal/calculate/{statementId}' endpoint
     * @param statementId - path variable representing Statement's ID
     * @param finishRegistrationRequestDto - input data from client
     */
    public void calculateCredit(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        String url = dealUrl + CALCULATE_CREDIT_PATH + statementId;
        log.info("Sending request to {}", url);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntityConstructor.createHttpEntity(finishRegistrationRequestDto),
                Void.class
        );

        log.info("Response status: {}", response.getStatusCode());
    }

    /**
     * Method makes a request to deal's '/deal/document/{statementId}/send' endpoint
     * @param statementId - path variable representing Statement's ID
     */
    public void sendDocuments(UUID statementId) {
        String url = dealUrl + SEND_DOCUMENTS_PATH.replace("{statementId}", statementId.toString());
        log.info("Sending request to {}", url);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntityConstructor.createHttpEntity(null),
                Void.class
        );

        log.info("Response status: {}", response.getStatusCode());
    }


    /**
     * Method makes a request to deal's '/deal/document/{statementId}/sign' endpoint
     * @param statementId - path variable representing Statement's ID
     */
    public void signDocuments(UUID statementId) {
        String url = dealUrl + SIGN_DOCUMENTS_PATH.replace("{statementId}", statementId.toString());
        log.info("Sending request to {}", url);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntityConstructor.createHttpEntity(null),
                Void.class
        );

        log.info("Response status: {}", response.getStatusCode());
    }


    /**
     * Method makes a request to deal's '/deal/document/{statementId}/code' endpoint
     * @param statementId - path variable representing Statement's ID
     * @param sesCodeDto - request body containing SES code
     */
    public void verifySesCode(UUID statementId, SESCodeDto sesCodeDto) {
        String url = dealUrl + VERIFY_SES_CODE_PATH.replace("{statementId}", statementId.toString());
        log.info("Sending request to {}", url);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntityConstructor.createHttpEntity(sesCodeDto),
                Void.class
        );

        log.info("Response status: {}", response.getStatusCode());
    }
}
