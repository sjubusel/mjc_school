package com.epam.esm.service.security;

public interface JwtService {

    boolean isTokenValid(String jwt);

    String receiveUsername(String jwt);

    String receiveAuthorities(String jwt);
}
