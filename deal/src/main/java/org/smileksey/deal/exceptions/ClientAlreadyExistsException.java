package org.smileksey.deal.exceptions;

/** Exception for registration already existing client */
public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
