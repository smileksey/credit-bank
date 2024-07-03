package org.smileksey.deal.utils;

import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

/** Handler for API exceptions */
@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {

    /** This method intercepts ValidationException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        log.error("Validation ERROR: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /** This method intercepts InvalidMSResponseException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleInvalidMsResponseException(InvalidMSResponseException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        log.error("ERROR: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /** This method intercepts DateTimeParseException in case of invalid date input and returns an error response to a client */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeParseException e) {

        String message = "Birthdate must be in yyyy-mm-dd format";
        ErrorResponse response = new ErrorResponse(message);

        log.error("Prescoring ERROR: {}", message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /** This method intercepts StatementNotFoundException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleStatementNotFoundException(StatementNotFoundException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        log.error("ERROR: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    /** This method intercepts ClientAlreadyExistsException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleClientAlreadyExistsException(ClientAlreadyExistsException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        log.error("ERROR: {}", e.getMessage());

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


    /** This method intercepts StatementStatusException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleStatementStatusException(StatementStatusException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        log.error("ERROR: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /** This method intercepts InvalidSesCodeException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleInvalidSesCodeException(InvalidSesCodeException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        log.error("ERROR: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
