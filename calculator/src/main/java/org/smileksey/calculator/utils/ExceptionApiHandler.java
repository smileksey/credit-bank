package org.smileksey.calculator.utils;

import lombok.extern.slf4j.Slf4j;
import org.smileksey.calculator.exceptions.LoanRefusedException;
import org.smileksey.calculator.exceptions.PrescoringException;
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

    /** This method intercepts PrescoringException and returns an error response to a client  */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlePrescoringException(PrescoringException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        log.error("Validation error: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /** This method intercepts DateTimeParseException in case of invalid date input and returns an error response to a client */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeParseException e) {

        String message = "Birthdate must be in yyyy-mm-dd format";
        ErrorResponse response = new ErrorResponse(message);

        log.error("Prescoring error: {}", message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /** This method intercepts LoanRefusedException in case of loan refusal and returns an error response to a client */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleLoanRefusedException(LoanRefusedException e) {

        String message = "Loan refused";
        ErrorResponse response = new ErrorResponse(message);

        log.error(message);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    /** This method intercepts HttpMessageNotReadableException in case of invalid ENUM input and returns an error response to a client */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        String message = "Enums may be invalid";
        ErrorResponse response = new ErrorResponse(message);

        log.error(message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
