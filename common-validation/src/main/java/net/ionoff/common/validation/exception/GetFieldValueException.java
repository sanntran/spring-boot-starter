package net.ionoff.common.validation.exception;

public class GetFieldValueException extends RuntimeException {

    public GetFieldValueException(String message) {
        super(message);
    }

    public GetFieldValueException(String message, Exception cause) {
        super(message, cause);
    }
}
