package org.smileksey.statement.exceptions;

/** Exception for querying an entity not existing in database */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
