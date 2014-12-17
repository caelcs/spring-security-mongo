package uk.co.caeldev.springsecuritymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MongoOAuth2RefreshToken {

    @Id
    private final String tokenId;
    private final byte[] token;
    private final byte[] authentication;

    public MongoOAuth2RefreshToken(final String tokenId,
                                   final byte[] token,
                                   final byte[] authentication) {
        this.tokenId = tokenId;
        this.token = token;
        this.authentication = authentication;
    }

    public String getTokenId() {
        return tokenId;
    }

    public byte[] getToken() {
        return token;
    }

    public byte[] getAuthentication() {
        return authentication;
    }
}
