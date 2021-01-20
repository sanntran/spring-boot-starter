package net.ionoff.common.validation.exception;

public class ValueParserException extends RuntimeException {

    public ValueParserException(String message) {
        super(message);
    }

    public ValueParserException(String message, Exception cause) {
        super(message, cause);
    }
}
