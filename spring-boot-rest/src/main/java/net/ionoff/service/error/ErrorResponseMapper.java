package net.ionoff.service.error;

import lombok.AllArgsConstructor;
import net.ionoff.service.api.dto.ErrorMessageDto;
import net.ionoff.service.api.dto.ErrorResponseDto;
import net.ionoff.service.exception.ResourceForbiddenException;
import net.ionoff.service.exception.ResourceNotFoundException;
import net.ionoff.service.exception.ServiceRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static net.ionoff.service.error.ErrorMessage.*;

@Component
@AllArgsConstructor
public class ErrorResponseMapper {
    
    private ErrorMessageMapper errorMessageMapper;

    public ErrorResponseDto fromResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return ErrorResponseDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(e.getClass().getSimpleName())
                .message(e.getMessage())
                .path(getRequestPath(request))
                .status(HttpStatus.NOT_FOUND.value())
                .errors(singletonList(notFound(e.getMessage())))
                .cause(getCauseMessage(e))
                .traceId(getRequestTraceId(request))
                .build();
    }
    
    public ErrorResponseDto fromResourceForbiddenException(ResourceForbiddenException e, WebRequest request) {
        return ErrorResponseDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(e.getClass().getSimpleName())
                .message(e.getMessage())
                .path(getRequestPath(request))
                .status(HttpStatus.FORBIDDEN.value())
                .errors(singletonList(accessDenied(e.getMessage())))
                .cause(getCauseMessage(e))
                .traceId(getRequestTraceId(request))
                .build();
    }

    public ErrorResponseDto fromSpringExceptionInternal(Exception e, HttpStatus status, WebRequest request) {
        return ErrorResponseDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(e.getClass().getSimpleName())
                .message(e.getMessage())
                .path(getRequestPath(request))
                .status(status.value())
                .errors(singletonList(springExceptionInternal(e)))
                .cause(getCauseMessage(e))
                .build();
    }

    public ErrorResponseDto fromServiceRuntimeException(ServiceRuntimeException e, WebRequest request) {
        return ErrorResponseDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(e.getClass().getSimpleName())
                .message("Internal server error")
                .path(getRequestPath(request))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errors(singletonList(internalServerError(e.getMessage())))
                .cause(getCauseMessage(e))
                .traceId(getRequestTraceId(request))
                .build();
    }

    public ErrorResponseDto fromGeneralException(Throwable e, WebRequest request) {
        return ErrorResponseDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(e.getClass().getSimpleName())
                .message("Internal server error")
                .path(getRequestPath(request))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errors(singletonList(internalServerError(e.getMessage())))
                .cause(getCauseMessage(e))
                .traceId(getRequestTraceId(request))
                .build();
    }

    public ErrorResponseDto fromMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<ErrorMessageDto> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(x -> errorMessageMapper.fromObjectError(x))
                .collect(Collectors.toList());
        return ErrorResponseDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(e.getClass().getSimpleName())
                .message("Validation failed")
                .path(getRequestPath(request))
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errorMessages)
                .cause(null)
                .traceId(getRequestTraceId(request))
                .build();
    }

    public ErrorResponseDto fromConstraintViolationException(ConstraintViolationException e,
                                                                    WebRequest request) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<ErrorMessageDto> errorMessages =
                constraintViolations.stream()
                        .map(constraintViolation -> errorMessageMapper.fromConstraintViolation(constraintViolation))
                        .collect(Collectors.toList());
        return ErrorResponseDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(e.getClass().getSimpleName())
                .message("Validation failed")
                .path(getRequestPath(request))
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errorMessages)
                .cause(null)
                .traceId(getRequestTraceId(request))
                .build();
    }

    public ErrorResponseDto fromMultipartException(MultipartException e, WebRequest request) {
        return ErrorResponseDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(e.getClass().getSimpleName())
                .message("Validation failed")
                .path(getRequestPath(request))
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(singletonList(invalidFileSize("file", BigDecimal.TEN)))
                .cause(null)
                .traceId(getRequestTraceId(request))
                .build();
    }

    public String getRequestPath(WebRequest webRequest) {
        if (webRequest instanceof ServletWebRequest) {
            return ((ServletWebRequest) webRequest).getRequest().getRequestURI();
        }
        return webRequest.getContextPath();
    }

    public String getRequestUri(WebRequest webRequest) {
        if (webRequest instanceof ServletWebRequest) {
            return getRequestUri(((ServletWebRequest) webRequest).getRequest());
        }
        Optional<String> pathAttribute = getPathAttribute(webRequest);
        String url = pathAttribute.map(val -> String.valueOf(webRequest.getAttribute(val, 0))).orElse("unknown");
        Map<String, String[]> paramMap = webRequest.getParameterMap();
        if (CollectionUtils.isEmpty(paramMap)) {
            return url;
        }
        String queryString = paramMap.entrySet()
                .stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), Arrays.toString(entry.getValue())))
                .collect(joining("&"));
        return String.format("%s?%s", url, queryString);
    }

    private Optional<String> getPathAttribute(WebRequest webRequest) {
        return Arrays.stream(webRequest.getAttributeNames(0))
                .filter(att -> String.valueOf(att).endsWith(".PATH"))
                .findFirst();
    }

    public static String getRequestUri(HttpServletRequest servletWebRequest) {
        String queryString = servletWebRequest.getQueryString();
        return StringUtils.hasText(queryString)
                ? servletWebRequest.getRequestURI() + '?'+ queryString
                : servletWebRequest.getRequestURI();
    }

    public String getCauseMessage(Throwable e) {
        Throwable cause = e.getCause();
        return cause == null ? null :
                (cause.getClass().getSimpleName() + ": " + cause.getMessage());
    }

    public String getRequestTraceId(WebRequest request) {
        String traceId = request.getHeader("X-Amzn-Trace-Id");
        return traceId == null ? UUID.randomUUID().toString() : traceId;
    }

}
