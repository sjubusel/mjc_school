package com.epam.esm.aserver.util;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken enhanced = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("iss", "http://localhost:9999");
        extraInfo.put("aud", "module_04");
        enhanced.setAdditionalInformation(extraInfo);
        return enhanced;
    }
}
