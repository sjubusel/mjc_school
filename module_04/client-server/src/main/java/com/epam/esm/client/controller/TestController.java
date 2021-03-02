package com.epam.esm.client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final WebClient webClient;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/test")
    public String test(Authentication authentication){
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient("gcs_code",
                authentication.getName());
        String tokenValue = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        return webClient
                .get()
                .uri("http://localhost:8888/module_04/test")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenValue))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
