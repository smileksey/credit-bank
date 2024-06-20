package org.smileksey.deal.exceptions;

/** Exception for validation errors */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
