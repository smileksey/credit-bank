package org.smileksey.deal.exceptions;

/** Exception for receiving invalid SES code from client */
public class InvalidSesCodeException extends RuntimeException {
    public InvalidSesCodeException(String message) {
        super(message);
    }
}
