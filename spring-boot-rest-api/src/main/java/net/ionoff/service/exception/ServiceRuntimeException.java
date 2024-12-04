package net.ionoff.service.exception;

public class ServiceRuntimeException extends RuntimeException {
    public ServiceRuntimeException(String message) {
        super(message);
    }

    public ServiceRuntimeException(String message, Exception cause) {
        super(message, cause);
    }
}
