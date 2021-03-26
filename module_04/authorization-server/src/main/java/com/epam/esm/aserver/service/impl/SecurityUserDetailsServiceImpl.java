package com.epam.esm.aserver.service.impl;

import com.epam.esm.aserver.service.SecurityUserDetailsService;
import com.epam.esm.model.domain.User;
import com.epam.esm.model.domain.UserAuthority;
import com.epam.esm.repository.impl.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsServiceImpl implements SecurityUserDetailsService {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE);

    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return withUsername(user.getLogin())
                .password(user.getPassword())
                .authorities(receiveAuthorities(user))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private Collection<? extends GrantedAuthority> receiveAuthorities(User user) {
        Set<UserAuthority> authorities = user.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return Collections.emptySet();
        }
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        authorities.forEach(authority ->
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getRole().name()))
        );
        return grantedAuthorities;
    }

    @Override
    public Map<String, Object> receiveUserInfoClaims(String authorizationHeader) {
        String token = null;
        Matcher matcher = authorizationPattern.matcher(authorizationHeader);
        if (matcher.find()) {
            token = matcher.group("token");
        }

        Jwt jwt = jwtDecoder.decode(token);
        Object claim = jwt.getClaim("user_name");

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("sub", claim);
        return userInfo;
    }
}

