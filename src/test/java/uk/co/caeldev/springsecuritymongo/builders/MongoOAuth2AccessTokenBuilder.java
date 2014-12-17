package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.oauth2.common.util.SerializationUtils;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2AccessToken;

import static uk.co.caeldev.springsecuritymongo.builders.OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder;
import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.string;

public class MongoOAuth2AccessTokenBuilder {

    private String tokenId = string().next();
    private byte[] token = SerializationUtils.serialize(oAuth2AccessTokenBuilder().build());
    private String authenticationId = string().next();
    private String username = string().next();
    private String clientId = string().next();
    private byte[] authentication = SerializationUtils.serialize(OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build());
    private String refreshToken = string().next();

    private MongoOAuth2AccessTokenBuilder() {
    }

    public static MongoOAuth2AccessTokenBuilder mongoOAuth2AccessTokenBuilder() {
        return new MongoOAuth2AccessTokenBuilder();
    }


    public MongoOAuth2AccessToken build() {
        return new MongoOAuth2AccessToken(tokenId, token, authenticationId, username, clientId, authentication, refreshToken);
    }

    public MongoOAuth2AccessTokenBuilder token(final byte[] token) {
        this.token = token;
        return this;
    }
}
