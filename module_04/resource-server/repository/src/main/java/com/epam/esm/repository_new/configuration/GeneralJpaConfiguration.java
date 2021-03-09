package com.epam.esm.repository_new.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.epam.esm.repository_new"})
public class GeneralJpaConfiguration {

}
