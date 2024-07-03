package org.smileksey.deal.exceptions;

/** Exception for actions that cannot be done with a current Statement status */
public class StatementStatusException extends RuntimeException{
    public StatementStatusException(String message) {
        super(message);
    }
}
