package com.epam.esm.repository.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.epam.esm.repository"})
public class GeneralJpaConfiguration {

}
