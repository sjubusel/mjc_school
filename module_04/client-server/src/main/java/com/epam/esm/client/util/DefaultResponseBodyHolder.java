package com.epam.esm.client.util;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Component;

@Component
public class DefaultResponseBodyHolder {

    private final String defaultLoginPage;

    {
        defaultLoginPage = formDefaultLoginPage();
    }

    public String receiveLoginPageBody() {
        return defaultLoginPage;
    }

    private String formDefaultLoginPage() {
        final String defaultLoginPage;
        StringBuilder stringBuilder = new StringBuilder("Login, please, using the following providers:\n");
        stringBuilder
                .append(OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
                .append("gcs_implicit")
                .append("\n")
                .append(OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
                .append("gcs_implicit");
        defaultLoginPage = new String(stringBuilder);
        return defaultLoginPage;
    }
}
