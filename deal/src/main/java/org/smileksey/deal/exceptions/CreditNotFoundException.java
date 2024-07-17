package org.smileksey.deal.exceptions;

/** Exception for querying a Credit entity not existing in database */
public class CreditNotFoundException extends RuntimeException {

    public CreditNotFoundException(String message) {
        super(message);
    }
}
