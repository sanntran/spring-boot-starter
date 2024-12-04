package net.ionoff.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.ionoff.service.api.dto.ErrorResponseDto;
import net.ionoff.service.error.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserSecurityExceptionHandler extends RequestFilterExceptionHandler {

    @Autowired
    public UserSecurityExceptionHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public ErrorResponseDto buildErrorResponse(HttpServletRequest request, Exception e) {
        ErrorResponseDto response = super.buildErrorResponse(request, e);
        if (e instanceof AuthenticationException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getErrors().add(ErrorMessage.unauthorized());
        } else if (e instanceof AccessDeniedException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getErrors().add(ErrorMessage.accessDenied(e.getMessage()));
        } else {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getErrors().add(ErrorMessage.internalServerError(e.getMessage()));
        }
        return response;
    }
}
