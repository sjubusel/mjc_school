package com.epam.esm.service.security.impl;

import com.epam.esm.service.dto.impl.UserCredentialsDto;
import com.epam.esm.service.security.AuthenticationService;
import com.epam.esm.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    @Override
    public String singIn(UserCredentialsDto credentials) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword())
        );
        Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities();
        return jwtService.createJwt(credentials.getLogin(), authorities);
    }

}
