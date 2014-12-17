package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

public class OAuth2ProtectedResourceDetailsBuilder {

    private OAuth2ProtectedResourceDetailsBuilder() {
    }

    public static OAuth2ProtectedResourceDetailsBuilder oAuth2ProtectedResourceDetailsBuilder() {
        return new OAuth2ProtectedResourceDetailsBuilder();
    }

    public OAuth2ProtectedResourceDetails build() {
        return new BaseOAuth2ProtectedResourceDetails();
    }
}
