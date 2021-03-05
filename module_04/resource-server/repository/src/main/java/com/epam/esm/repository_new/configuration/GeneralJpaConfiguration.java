package com.epam.esm.repository_new.configuration;

import com.epam.esm.repository_new.GeneralCrudRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = GeneralCrudRepository.class)
public class GeneralJpaConfiguration {

}
