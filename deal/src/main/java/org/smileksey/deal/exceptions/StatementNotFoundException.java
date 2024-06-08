package org.smileksey.deal.exceptions;

/** Exception for querying a statement not existing in database */
public class StatementNotFoundException extends RuntimeException {

    public StatementNotFoundException(String message) {
        super(message);
    }
}
