package org.smileksey.dossier.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.dossier.utils.HttpEntityConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealClient {

    private final RestTemplate restTemplate;

    /** URL of the 'deal' microservice */
    @Value("${deal.url}")
    private String dealUrl;

    //FIXME
    /**
     * Method makes a request to the 'deal' microservice and returns its response
     * @param statementId - ID of the Statement entity to be updated
     * @return ResponseEntity with void body
     */
    public ResponseEntity<Void> sendUpdateStatementStatus(Long statementId) {
//        log.info("Sending request to {}{}", dealUrl, OFFER_SELECT_PATH);
        return restTemplate.exchange(dealUrl + "/admin/statement/" + statementId + "/status", HttpMethod.PUT, HttpEntityConstructor.createHttpEntity(null), new ParameterizedTypeReference<Void>() {});
    }
}
