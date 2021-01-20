package net.ionoff.common.validation.exception;

public class MethodInvocationException extends RuntimeException {

    public MethodInvocationException(String message) {
        super(message);
    }

    public MethodInvocationException(String message, Exception cause) {
        super(message, cause);
    }
}
