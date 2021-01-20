package net.ionoff.common.validation.exception;

public class CommonValueParserException extends RuntimeException {

    public CommonValueParserException(String message) {
        super(message);
    }

    public CommonValueParserException(String message, Exception cause) {
        super(message, cause);
    }
}
