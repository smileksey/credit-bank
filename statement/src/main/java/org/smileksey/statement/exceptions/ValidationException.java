package org.smileksey.statement.exceptions;

/** Exception for validation errors */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
