package org.smileksey.gateway.exceptions;

/** Exception for invalid responses from other microservices due to inner microservice error */
public class InvalidMSResponseException extends RuntimeException {
    public InvalidMSResponseException(String message) {
        super(message);
    }
}
