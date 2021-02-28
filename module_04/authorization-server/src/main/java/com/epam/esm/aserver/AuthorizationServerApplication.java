package com.epam.esm.aserver;

import com.epam.esm.model.domain.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.repository.util.impl.UserPredicateBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EntityScan(basePackageClasses = {User.class})
public class AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

	@Bean
	public KeyPair keyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		return generator.generateKeyPair();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	public UserRepository userRepositoryBean(EntityManager entityManager) {
		return new UserRepositoryImpl(entityManager, new UserPredicateBuilder());
	}
}
