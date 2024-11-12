package net.ionoff.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ionoff.service.api.dto.ErrorResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static net.ionoff.service.error.ErrorResponseMapper.getRequestUri;

@Slf4j
@AllArgsConstructor
public class RequestFilterExceptionHandler {

    private ObjectMapper objectMapper;

    protected ErrorResponseDto buildErrorResponse(HttpServletRequest request, Exception e) {
        ErrorResponseDto response = new ErrorResponseDto();
        response.setTimestamp(System.currentTimeMillis());
        response.setErrors(new ArrayList<>());
        response.setType(e.getClass().getSimpleName());
        if (e.getCause() != null) {
            response.setCause(e.getCause().getMessage());
        }
        response.setPath(request.getRequestURI());
        response.setMessage(e.getMessage());
        return response;
    }

    public void handleException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception e) throws IOException {
        log.error("Error when handling request {}: {}, {}", getRequestUri(httpServletRequest), e.getClass().getSimpleName(), e.getMessage());
        ErrorResponseDto response = buildErrorResponse(httpServletRequest, e);
        String message = objectMapper.writeValueAsString(response);
        httpServletResponse.setStatus(response.getStatus());
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpServletResponse.getOutputStream().print(message);
    }
}
