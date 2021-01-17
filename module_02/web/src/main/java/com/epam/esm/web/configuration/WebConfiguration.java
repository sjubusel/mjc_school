package com.epam.esm.web.configuration;

import com.epam.esm.repository.configuration.RepositoryConfiguration;
import com.epam.esm.service.configuration.ServiceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan({"com.epam.esm.web"})
@EnableWebMvc
@Import({RepositoryConfiguration.class, ServiceConfiguration.class})
public class WebConfiguration {

    /**
     * a bean which allows to apply @org.springframework.validation.annotation.Validated
     *
     * @return an object type of @org.springframework.validation.annotation.Validated which is
     * a convenient BeanPostProcessor implementation that performs method-le.vel validation on the annotated methods
     * @see Validated
     * @see MethodValidationPostProcessor
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
