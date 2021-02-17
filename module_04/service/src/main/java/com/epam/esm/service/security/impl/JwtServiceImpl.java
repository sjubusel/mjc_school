package com.epam.esm.service.security.impl;

import com.epam.esm.service.security.JwtService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    public static final String AUTHORITIES_CLAIM = "authorities";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expiration}")
    private Long validityInMillis;

    @Override
    public boolean isTokenValid(String jwt) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String receiveUsername(String jwt) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends GrantedAuthority> receiveAuthorities(String jwt) {
        List<String> roles = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody()
                .get(AUTHORITIES_CLAIM, List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
