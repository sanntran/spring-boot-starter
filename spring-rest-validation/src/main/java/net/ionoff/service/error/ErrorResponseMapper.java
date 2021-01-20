package net.ionoff.service.error;

import com.mkyong.api.model.ErrorMessageDto;
import com.mkyong.api.model.ErrorResponseDto;
import net.ionoff.common.validation.message.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ErrorResponseMapper {

    @Autowired
    private MessageSource messageSource;

    public ErrorResponseDto fromInternalError(Exception ex, WebRequest request) {
        return new ErrorResponseDto().message(ex.getMessage())
                        .cause(ex.getCause() != null ? ex.getCause().getClass().getSimpleName() : null)
                        .timestamp(System.currentTimeMillis())
                        .path(getRequestPath(request));
    }

    public ErrorResponseDto fromMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ErrorMessageDto> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(x -> toErrorMessage(x))
                .collect(Collectors.toList());
        return new ErrorResponseDto()
                .message("Validation failed")
                .cause(ex.getCause() != null ? ex.getCause().getClass().getSimpleName() : null)
                .status(status.value())
                .timestamp(System.currentTimeMillis())
                .errors(errorMessages)
                .path(getRequestPath(request));

    }

    public ErrorMessageDto toErrorMessage(ObjectError objectError) {
        ValidationMessage message = ValidationMessage.fromObjectError(objectError, messageSource);
        return new ErrorMessageDto()
                .code(message.getCode())
                .fields(message.getFields())
                .message(message.getMessage());
    }

    private String getRequestPath(WebRequest webRequest) {
        if (webRequest instanceof ServletWebRequest) {
            return ((ServletWebRequest) webRequest).getRequest().getRequestURI();
        }
        return webRequest.getContextPath();
    }

}
