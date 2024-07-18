package org.smileksey.deal.exceptions;

/** Exception for loan refusal */
public class LoanRefusedException extends RuntimeException {
    public LoanRefusedException(String message) {
        super(message);
    }

}
