package uk.co.caeldev.springsecuritymongo.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MongoOAuth2RefreshToken {

    @Id
    private String tokenId;
    private byte[] token;
    private byte[] authentication;

    public MongoOAuth2RefreshToken() {
    }

    @PersistenceConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MongoOAuth2RefreshToken)) return false;

        MongoOAuth2RefreshToken that = (MongoOAuth2RefreshToken) o;

        return new EqualsBuilder()
                .append(tokenId, that.tokenId)
                .append(token, that.token)
                .append(authentication, that.authentication)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tokenId)
                .append(token)
                .append(authentication)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tokenId", tokenId)
                .append("token", token)
                .append("authentication", authentication)
                .toString();
    }
}
