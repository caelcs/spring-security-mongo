package uk.co.caeldev.springsecuritymongo.builders;

import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2RefreshToken;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.string;

public class MongoOAuth2RefreshTokenBuilder {

    private String tokenId = string().next();
    private byte[] token = string().next().getBytes();
    private byte[] authentication = string().next().getBytes();

    private MongoOAuth2RefreshTokenBuilder() {
    }

    public static MongoOAuth2RefreshTokenBuilder mongoOAuth2RefreshTokenBuilder() {
        return new MongoOAuth2RefreshTokenBuilder();
    }

    public MongoOAuth2RefreshToken build() {
        return new MongoOAuth2RefreshToken(tokenId, token, authentication);
    }

    public MongoOAuth2RefreshTokenBuilder token(final byte[] oAuth2RefreshTokenSer) {
        this.token = oAuth2RefreshTokenSer;
        return this;
    }

    public MongoOAuth2RefreshTokenBuilder authentication(final byte[] authenticationSer) {
        this.authentication = authenticationSer;
        return this;
    }
}
