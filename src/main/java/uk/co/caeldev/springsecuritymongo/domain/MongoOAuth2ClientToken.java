package uk.co.caeldev.springsecuritymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;

@Document
public class MongoOAuth2ClientToken {

    @Id
    private String id;
    private String tokenId;
    private byte[] token;
    private String authenticationId;
    private String username;
    private String clientId;

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
    public int hashCode() {
        return Objects.hash(tokenId, token, authenticationId, username, clientId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MongoOAuth2ClientToken other = (MongoOAuth2ClientToken) obj;
        return Objects.equals(this.tokenId, other.tokenId)
                && Objects.equals(this.token, other.token)
                && Objects.equals(this.authenticationId, other.authenticationId)
                && Objects.equals(this.username, other.username)
                && Objects.equals(this.clientId, other.clientId);
    }

    @Override
    public String toString() {
        return "MongoOAuth2ClientToken{" +
                "id='" + id + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", token=" + Arrays.toString(token) +
                ", authenticationId='" + authenticationId + '\'' +
                ", username='" + username + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
