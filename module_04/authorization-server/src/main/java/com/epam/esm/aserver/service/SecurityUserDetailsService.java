package com.epam.esm.aserver.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface SecurityUserDetailsService extends UserDetailsService {

    Map<String, Object> receiveUserInfoClaims(String authorizationHeader);
}
