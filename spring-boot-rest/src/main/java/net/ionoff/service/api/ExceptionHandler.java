package net.ionoff.service.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.ionoff.service.api.dto.ErrorResponseDto;
import net.ionoff.service.error.ErrorResponseMapper;
import net.ionoff.service.exception.ResourceForbiddenException;
import net.ionoff.service.exception.ResourceNotFoundException;
import net.ionoff.service.exception.ServiceRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    private ErrorResponseMapper errorResponseMapper;

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception e, WebRequest request) {
        log.error(getErrorMessage(e, request), e);
        ErrorResponseDto error = errorResponseMapper.fromGeneralException(e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ServiceRuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleServiceRuntimeException(ServiceRuntimeException e, WebRequest request) {
        log.error(getErrorMessage(e, request), e);
        ErrorResponseDto error = errorResponseMapper.fromServiceRuntimeException(e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(getErrorMessage(e, request));
        if (e instanceof MethodArgumentNotValidException) {
            ErrorResponseDto errorResponse = errorResponseMapper.fromMethodArgumentNotValidException((MethodArgumentNotValidException) e, request);
            return ResponseEntity.status(status).headers(headers).body(errorResponse);
        }
        ErrorResponseDto errorResponse = errorResponseMapper.fromSpringExceptionInternal(e, status, request);
        return ResponseEntity.status(status).headers(headers).body(errorResponse);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        log.warn(getErrorMessage(e, request));
        ErrorResponseDto errorResponse = errorResponseMapper.fromResourceNotFoundException(e, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ResourceForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceForbiddenException(ResourceForbiddenException e, WebRequest request) {
        log.warn(getErrorMessage(e, request));
        ErrorResponseDto errorResponse = errorResponseMapper.fromResourceForbiddenException(e, request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponseDto> handleMultipartException(MultipartException e, WebRequest request) {
        log.warn(getErrorMessage(e, request));
        ErrorResponseDto errorResponse = errorResponseMapper.fromMultipartException(e, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @Override // error handle for @Valid
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        try {
            return ResponseEntity.status(status)
                    .body(errorResponseMapper.fromMethodArgumentNotValidException(ex, request));
        } catch (Exception e) {
            ResponseEntity<ErrorResponseDto> response = handleGlobalException(e, request);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex,
                                                                               WebRequest request) {
        try {
            ErrorResponseDto error = errorResponseMapper.fromConstraintViolationException(ex, request);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            return handleGlobalException(e, request);
        }
    }

    protected String getErrorMessage(Exception e, WebRequest request) {
        return String.format("Error when handling request with url %s: %s",
                errorResponseMapper.getRequestUri(request), e.getMessage());
    }
}