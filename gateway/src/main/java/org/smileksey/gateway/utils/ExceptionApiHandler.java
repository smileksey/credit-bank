package org.smileksey.gateway.utils;

import lombok.extern.slf4j.Slf4j;

import org.smileksey.gateway.exceptions.EntityNotFoundException;
import org.smileksey.gateway.exceptions.BadRequestException;
import org.smileksey.gateway.exceptions.InvalidMSResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

/** Handler for API exceptions */
@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {


    /** This method intercepts BadRequestException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<String> handleBadRequestExceptionException(BadRequestException e) {

        log.error("ERROR: {}", e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(e.getMessage());
    }


    /** This method intercepts EntityNotFoundException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {

        log.error("ERROR: {}", e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(headers)
                .body(e.getMessage());
    }


    /** This method intercepts InvalidMSResponseException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<String> handleInvalidMsResponseException(InvalidMSResponseException e) {

        log.error("ERROR: {}", e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(headers)
                .body(e.getMessage());
    }


    /** This method intercepts DateTimeParseException in case of invalid date input and returns an error response to a client */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeParseException e) {

        String message = "Birthdate must be in yyyy-mm-dd format";
        ErrorResponse response = new ErrorResponse(message);

        log.error("Prescoring ERROR: {}", message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /** This method intercepts HttpMessageNotReadableException in case of invalid ENUM input and returns an error response to a client */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        String message = "Some fields are invalid. Check enums and dates formatting";
        ErrorResponse response = new ErrorResponse(message);

        log.error(message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
