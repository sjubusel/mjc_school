package com.epam.esm.aserver.endpoint;

import com.epam.esm.aserver.service.SecurityUserDetailsService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@SuppressWarnings("deprecation")
@FrameworkEndpoint
@RequiredArgsConstructor
public class CustomFrameworkEndpoint {

    private final KeyPair keyPair;
    private final SecurityUserDetailsService userDetailsService;

    @GetMapping("/.well-known/jwks.json")
    @ResponseBody
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

    @GetMapping("/oauth/userinfo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> userInfo(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return ResponseEntity.ok(userDetailsService.receiveUserInfoClaims(header));
    }
}
