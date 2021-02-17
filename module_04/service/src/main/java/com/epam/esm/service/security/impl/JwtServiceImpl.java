package com.epam.esm.service.security.impl;

import com.epam.esm.service.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public boolean isTokenValid(String jwt) {
        return false;
    }

    @Override
    public String receiveUsername(String jwt) {
        return null;
    }

    @Override
    public String receiveAuthorities(String jwt) {
        return null;
    }
}
