package com.epam.esm.service.configuration;

import com.epam.esm.repository.configuration.RepositoryConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.epam.esm.service")
@Import(RepositoryConfiguration.class)
public class ServiceConfiguration {

}

