package net.ionoff.common.validation.exception;

import net.ionoff.common.validation.message.ServiceMessage;

import java.util.Arrays;
import java.util.List;

public class ServiceRuntimeException extends RuntimeException {

    private final List<ServiceMessage> messages;

    public ServiceRuntimeException(String error, ServiceMessage message) {
        super(error);
        this.messages = Arrays.asList(message);
    }

    public ServiceRuntimeException(String error, ServiceMessage message, Exception cause) {
        super(error, cause);
        this.messages = Arrays.asList(message);
    }

    public ServiceRuntimeException(String error, List<ServiceMessage> messages) {
        super(error);
        this.messages = messages;
    }

    public List<ServiceMessage> getMessages() {
        return messages;
    }

}
