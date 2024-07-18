package org.smileksey.statement.utils;

import lombok.extern.slf4j.Slf4j;
import org.smileksey.statement.exceptions.BadRequestException;
import org.smileksey.statement.exceptions.InvalidMSResponseException;
import org.smileksey.statement.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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

        String responseBody = new BufferedReader(new InputStreamReader(response.getBody()))
                .lines().collect(Collectors.joining("\n"));

        if (response.getStatusCode().is5xxServerError()) {
            //Handle SERVER_ERROR
            throw new InvalidMSResponseException(responseBody);

        } else if (response.getStatusCode().is4xxClientError()) {
            //Handle CLIENT_ERROR
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BadRequestException(responseBody);
            }

            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EntityNotFoundException(responseBody);
            }
        }
    }
}
