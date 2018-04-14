package uk.co.caeldev.springsecuritymongo.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MongoOAuth2ClientToken {

    @Id
    private String id;
    private String tokenId;
    private byte[] token;
    private String authenticationId;
    private String username;
    private String clientId;

    public MongoOAuth2ClientToken() {
    }

    @PersistenceConstructor
    public MongoOAuth2ClientToken(final String id,
                                  final String tokenId,
                                  final byte[] token,
                                  final String authenticationId,
                                  final String username,
                                  final String clientId) {
        this.id = id;
        this.tokenId = tokenId;
        this.token = token;
        this.authenticationId = authenticationId;
        this.username = username;
        this.clientId = clientId;
    }

    public String getId() {
        return id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public byte[] getToken() {
        return token;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public String getUsername() {
        return username;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MongoOAuth2ClientToken)) return false;

        MongoOAuth2ClientToken that = (MongoOAuth2ClientToken) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(tokenId, that.tokenId)
                .append(token, that.token)
                .append(authenticationId, that.authenticationId)
                .append(username, that.username)
                .append(clientId, that.clientId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(tokenId)
                .append(token)
                .append(authenticationId)
                .append(username)
                .append(clientId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("tokenId", tokenId)
                .append("token", token)
                .append("authenticationId", authenticationId)
                .append("username", username)
                .append("clientId", clientId)
                .toString();
    }
}
