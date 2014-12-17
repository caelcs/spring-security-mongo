package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.string;
import static uk.co.caeldev.springsecuritymongo.builders.OAuth2RequestBuilder.oAuth2RequestBuilder;

public class OAuth2AuthenticationBuilder {

    private OAuth2AuthenticationBuilder() {
    }

    public static OAuth2AuthenticationBuilder oAuth2AuthenticationBuilder() {
        return new OAuth2AuthenticationBuilder();
    }

    public OAuth2Authentication build() {
        return new OAuth2Authentication(oAuth2RequestBuilder().build(), new TestingAuthenticationToken(UserBuilder.userBuilder().build(), string().next()));
    }
}
