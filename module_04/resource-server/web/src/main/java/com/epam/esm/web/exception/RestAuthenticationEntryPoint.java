package com.epam.esm.web.exception;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    /**
     * "Qualifier" is used due to default Spring beans
     * (in org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration,
     * org.springframework.boot.actuate.autoconfigure.web.servlet.WebMvcEndpointChildContextConfiguration,
     * which are autowired by default
     *
     * @param resolver used in order to override default Spring Security exception handling mechanism
     */
    public RestAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        resolver.resolveException(request, response, null, authException);
    }
}
