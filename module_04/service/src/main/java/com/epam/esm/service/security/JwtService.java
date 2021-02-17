package com.epam.esm.service.security;

import com.epam.esm.model.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {

    boolean isTokenValid(String jwt);

    String receiveUsername(String jwt);

    Collection<? extends GrantedAuthority> receiveAuthorities(String jwt);

    String createJwt(UserDto user);
}
