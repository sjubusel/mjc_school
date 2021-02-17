package com.epam.esm.service.security;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.UserAuthorityDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.converter.impl.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.security.core.userdetails.User.withUsername;

@RequiredArgsConstructor
@Service
public class SecurityUserDetailsServiceImpl implements SecurityUserDetailsService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        UserDto userDto = userConverter.convertToSecurityDto(user);
        return withUsername(userDto.getLogin())
                .password(userDto.getPassword())
                .authorities(receiveAuthorities(userDto))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    @Override
    public Optional<UserDetails> loadUserByJwt(String jwt) {
        if (jwtService.isTokenValid(jwt)) {
            return Optional.of(withUsername(jwtService.receiveUsername(jwt))
                    .password("")
                    .authorities(jwtService.receiveAuthorities(jwt))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build());
        }

        return Optional.empty();
    }

    private Collection<? extends GrantedAuthority> receiveAuthorities(UserDto user) {
        Set<UserAuthorityDto> authorities = user.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return Collections.emptySet();
        }
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        authorities.forEach(authority ->
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.getRole().name()))
        );
        return grantedAuthorities;
    }
}
