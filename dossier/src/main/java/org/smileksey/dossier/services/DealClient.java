package org.smileksey.dossier.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.dossier.utils.HttpEntityConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealClient {

    private final RestTemplate restTemplate;

    /** URL of the 'deal' microservice */
    @Value("${deal.url}")
    private String dealUrl;


    /**
     * Method executes a PUT request to the 'deal' microservice to update a Statement entity
     * @param statementId - ID of the Statement entity to be updated
     */
    public void sendUpdateStatementStatus(UUID statementId) {
        String url = dealUrl + "/deal/admin/statement/" + statementId + "/status";
        restTemplate.exchange(
                url,
                HttpMethod.PUT, HttpEntityConstructor.createHttpEntity(null),
                Void.class);
        log.info("Sending PUT request to {}", url);
    }
}
