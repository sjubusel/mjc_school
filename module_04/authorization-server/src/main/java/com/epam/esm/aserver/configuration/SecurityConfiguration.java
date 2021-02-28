package com.epam.esm.aserver.configuration;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.repository.util.impl.UserPredicateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;

import javax.persistence.EntityManager;

@Order(1)
@Configuration
public class SecurityConfiguration extends AuthorizationServerSecurityConfiguration {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .requestMatchers()
                .mvcMatchers("/.well-known/jwks.json")
                .and()
                .authorizeRequests()
                .mvcMatchers("/.well-known/jwks.json")
                .permitAll();
    }

    @Bean
    public UserRepository userRepositoryBean(EntityManager entityManager) {
        return new UserRepositoryImpl(entityManager, new UserPredicateBuilder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
