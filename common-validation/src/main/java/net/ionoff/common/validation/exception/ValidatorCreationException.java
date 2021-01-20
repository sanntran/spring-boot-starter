package net.ionoff.common.validation.exception;

public class ValidatorCreationException extends RuntimeException {

    public ValidatorCreationException(String message, Exception cause) {
        super(message, cause);
    }
}
