package org.natlex.geo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.helper.ResponseGenerator;
import org.natlex.geo.util.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/22/24
 * Time: 2:43 PM
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseGenerator responseGenerator;
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        GenericResponse genericResponse = responseGenerator
                .generateErrorExceptionResponse(
                        ResponseMessages.REQUEST_PROC_FAILED,
                        authException.getLocalizedMessage(),
                        HttpStatus.UNAUTHORIZED);
        // Custom logic for handling authentication failure
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(genericResponse));

    }
}
