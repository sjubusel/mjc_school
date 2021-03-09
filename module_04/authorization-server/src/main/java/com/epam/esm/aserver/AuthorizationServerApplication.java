package com.epam.esm.aserver;

import com.epam.esm.model.domain.User;
import com.epam.esm.service.impl.UserServiceImpl;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

@SpringBootApplication(scanBasePackages = {"com.epam.esm.aserver", "com.epam.esm.service.converter"})
@EntityScan(basePackageClasses = {User.class})
@EnableJpaRepositories(basePackages = {"com.epam.esm.repository"})
@Import({UserServiceImpl.class})
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
    public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException, JOSEException {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair().getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return NimbusJwtDecoder
                .withPublicKey(key.toRSAPublicKey())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
