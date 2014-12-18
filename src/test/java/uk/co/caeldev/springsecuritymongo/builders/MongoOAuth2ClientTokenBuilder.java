package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.SerializationUtils;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2ClientToken;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.string;

public class MongoOAuth2ClientTokenBuilder {

    private String id = string().next();
    private String tokenId = string().next();
    private byte[] token = SerializationUtils.serialize(OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build());
    private String authenticationId = string().next();
    private String username = string().next();
    private String clientId = string().next();

    private MongoOAuth2ClientTokenBuilder() {
    }

    public static MongoOAuth2ClientTokenBuilder mongoOAuth2ClientTokenBuilder() {
        return new MongoOAuth2ClientTokenBuilder();
    }

    public MongoOAuth2ClientToken build() {
        return new MongoOAuth2ClientToken(id,
                tokenId,
                token,
                authenticationId,
                username,
                clientId);
    }

    public MongoOAuth2ClientTokenBuilder token(final OAuth2AccessToken token) {
        this.token = SerializationUtils.serialize(token);
        return this;
    }
}
