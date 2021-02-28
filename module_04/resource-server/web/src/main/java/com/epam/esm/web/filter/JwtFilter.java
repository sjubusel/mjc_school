package com.epam.esm.web.filter;

import com.epam.esm.service.security.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String BEARER_HEADER_VALUE = "Bearer";

    private final SecurityUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String headerValue = ((HttpServletRequest) request).getHeader("Authorization");
        receiveBearerJwt(headerValue).flatMap(userDetailsService::loadUserByJwt).ifPresent(userDetails ->
                SecurityContextHolder.getContext().setAuthentication(
                        new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities())
        ));

        chain.doFilter(request, response);
    }

    private Optional<String> receiveBearerJwt(String headerValue) {
        return (headerValue != null && headerValue.startsWith(BEARER_HEADER_VALUE))
                ? Optional.of(headerValue.replace(BEARER_HEADER_VALUE, "").trim())
                : Optional.empty();
    }
}
