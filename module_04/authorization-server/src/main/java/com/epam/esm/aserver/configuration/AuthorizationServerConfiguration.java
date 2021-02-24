package com.epam.esm.aserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;


/**
 * a stub realization, while completing an optional task #5
 * https://github.com/mjc-school/MJC-School/blob/master/java/module%20%234.%20Authentication%20%26%20Spring%20Security/authentication_and_spring_security_task.md
 */
@Configuration
@EnableResourceServer
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauth2Server) throws Exception {
        oauth2Server.tokenKeyAccess("permitAll()") // /oauth/token_key endpoint
                .checkTokenAccess("isAuthenticated()"); //  /oauth/check_token endpoint
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new ClientDetailsService() {
            @Override
            public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                return null; // TODO write me
            }
        });

        clients.inMemory().withClient("gift_certificates_app")
                .authorizedGrantTypes("authorization_code")
                .secret(passwordEncoder().encode("secret"))
                .scopes("user_info")
                .redirectUris("http://localhost:8888/module_04/login/oauth2/code/gift_certificates")
                .autoApprove(true);
    }
}
