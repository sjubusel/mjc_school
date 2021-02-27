package com.epam.esm.aserver.configuration;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.repository.util.impl.UserPredicateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.persistence.EntityManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public UserRepository userRepositoryBean(EntityManager entityManager) {
        return new UserRepositoryImpl(entityManager, new UserPredicateBuilder());
    }
}
