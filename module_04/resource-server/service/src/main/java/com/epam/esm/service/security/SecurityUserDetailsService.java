package com.epam.esm.service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface SecurityUserDetailsService extends UserDetailsService {

    Optional<UserDetails> loadUserByJwt(String jwt);
}
