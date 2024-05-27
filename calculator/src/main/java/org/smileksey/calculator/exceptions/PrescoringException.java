package org.smileksey.calculator.exceptions;

/** Исключение для ошибок прескоринга */
public class PrescoringException extends RuntimeException {

    public PrescoringException(String message) {
        super(message);
    }
}
