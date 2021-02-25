package com.epam.esm.client.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

@Configuration
public class OAuth2LoginConfiguration {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(implicitGrantGiftCertificatesSystemRegistration(),
                resourceOwnerPasswordCredentialsGrantGiftCertificatesSystemRegistration()
        );
    }

    private ClientRegistration implicitGrantGiftCertificatesSystemRegistration() {
        return ClientRegistration.withRegistrationId("gcs")
                .clientId("gift-certificates-system-id")
                .clientSecret("gift-certificates-system-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("custom-gift-certificates-system")
                .authorizationUri("http://localhost:9999/oauth2/authorize")
                .tokenUri("http://localhost:9999/oauth2/token")
                .userInfoUri("http://localhost:9999/oauth2/userinfo")
                .userInfoAuthenticationMethod(AuthenticationMethod.QUERY)
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("http://localhost:9999/oauth2/authorize/certs")
                .clientName("Gift-certificates System")
                .build();
    }

    private ClientRegistration resourceOwnerPasswordCredentialsGrantGiftCertificatesSystemRegistration() {
        return ClientRegistration.withRegistrationId("gcs")
                .clientId("gift-certificates-system-id")
                .clientSecret("gift-certificates-system-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("custom-gift-certificates-system")
                .authorizationUri("http://localhost:9999/oauth2/authorize")
                .tokenUri("http://localhost:9999/oauth2/token")
                .userInfoUri("http://localhost:9999/oauth2/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("http://localhost:9999/oauth2/authorize/certs")
                .clientName("Gift-certificates System")
                .build();
    }
}
