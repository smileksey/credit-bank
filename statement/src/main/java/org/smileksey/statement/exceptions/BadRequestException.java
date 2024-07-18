package org.smileksey.statement.exceptions;

/** Exception for invalid responses from other microservices due to bad request */
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
