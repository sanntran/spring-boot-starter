package net.ionoff.common.validation.exception;

import net.ionoff.common.validation.message.ValidationMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestValidationException extends RuntimeException {

    private final List<ValidationMessage> messages;

    public RequestValidationException(ValidationMessage message) {
        this(message.getMessage(), message);
    }

    public RequestValidationException(List<ValidationMessage> messages) {
        this(String.join("; ", messages.stream().map(ValidationMessage::getMessage).collect(Collectors.toList())), messages);
    }

    public RequestValidationException(String error, ValidationMessage message) {
        super(error);
        this.messages = Arrays.asList(message);
    }

    public RequestValidationException(String error, List<ValidationMessage> messages) {
        super(error);
        this.messages = messages;
    }

    public List<ValidationMessage> getMessages() {
        return messages;
    }
}
