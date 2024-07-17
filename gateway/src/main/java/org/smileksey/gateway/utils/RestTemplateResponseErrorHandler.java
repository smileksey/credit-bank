package org.smileksey.gateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.smileksey.gateway.exceptions.EntityNotFoundException;
import org.smileksey.gateway.exceptions.InvalidMSResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/** Class for handling response errors returned by other microservices */
@Component
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError() ||
                response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is5xxServerError()) {
            //Handle SERVER_ERROR
            throw new InvalidMSResponseException("Error when requesting another microservice: " + response.getStatusCode());

        } else if (response.getStatusCode().is4xxClientError()) {
            //Handle CLIENT_ERROR
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new InvalidMSResponseException("Error when requesting another microservice: " + response.getStatusCode() + ". Check the input data.");
            }

            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EntityNotFoundException("Entity was not found by another microservice: " + response.getStatusCode());
            }
        }
    }
}
