package com.epam.esm.aserver.configuration;

import com.epam.esm.aserver.service.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;

@SuppressWarnings("deprecation")
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends AuthorizationServerSecurityConfiguration {

    private final SecurityUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable()
                .requestMatchers()
                .mvcMatchers("/.well-known/jwks.json", "/oauth/authorize", "/login", "/signUp")
                .and()
                .authorizeRequests()
                .mvcMatchers("/.well-known/jwks.json", "/oauth/authorize", "/login", "/signUp").permitAll()
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
