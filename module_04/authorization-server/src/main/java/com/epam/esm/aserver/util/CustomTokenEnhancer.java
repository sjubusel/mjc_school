package com.epam.esm.aserver.util;

import com.epam.esm.model.domain.User;
import com.epam.esm.repository.impl.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@Component
@RequiredArgsConstructor
public class CustomTokenEnhancer implements TokenEnhancer {

    private final UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName()).orElseThrow(() ->
                new UsernameNotFoundException(authentication.getName()));
        DefaultOAuth2AccessToken enhanced = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("user_id", user.getId());
        extraInfo.put("iss", "http://localhost:9999");
        extraInfo.put("aud", "module_04");
        enhanced.setAdditionalInformation(extraInfo);
        return enhanced;
    }
}
