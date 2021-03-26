package com.epam.esm.aserver;

import com.epam.esm.model.domain.User;
import com.epam.esm.service.impl.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.epam.esm.aserver", "com.epam.esm.service.converter"})
@EntityScan(basePackageClasses = {User.class})
@EnableJpaRepositories(basePackages = {"com.epam.esm.repository"})
@Import({UserServiceImpl.class})
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
