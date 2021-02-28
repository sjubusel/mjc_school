package com.epam.esm.service.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {

    boolean isTokenValid(String jwt);

    String receiveUsername(String jwt);

    Collection<? extends GrantedAuthority> receiveAuthorities(String jwt);

    String createJwt(String username, Collection<? extends GrantedAuthority> authorities);
}
