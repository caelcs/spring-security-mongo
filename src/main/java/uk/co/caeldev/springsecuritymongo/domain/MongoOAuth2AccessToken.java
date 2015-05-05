package uk.co.caeldev.springsecuritymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;

@Document
public class MongoOAuth2AccessToken {

    @Id
    private String tokenId;
    private byte[] token;
    private String authenticationId;
    private String username;
    private String clientId;
    private byte[] authentication;
    private String refreshToken;

    public MongoOAuth2AccessToken() {
    }

    @PersistenceConstructor
    public MongoOAuth2AccessToken(final String tokenId,
                                  final byte[] token,
                                  final String authenticationId,
                                  final String username,
                                  final String clientId,
                                  final byte[] authentication,
                                  final String refreshToken) {
        this.tokenId = tokenId;
        this.token = token;
        this.authenticationId = authenticationId;
        this.username = username;
        this.clientId = clientId;
        this.authentication = authentication;
        this.refreshToken = refreshToken;
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

    public byte[] getAuthentication() {
        return authentication;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, authenticationId, username, clientId, authentication, refreshToken);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MongoOAuth2AccessToken other = (MongoOAuth2AccessToken) obj;
        return Objects.equals(this.token, other.token) && Objects.equals(this.authenticationId, other.authenticationId) && Objects.equals(this.username, other.username) && Objects.equals(this.clientId, other.clientId) && Objects.equals(this.authentication, other.authentication) && Objects.equals(this.refreshToken, other.refreshToken);
    }

    @Override
    public String toString() {
        return "MongoOAuth2AccessToken{" +
                "tokenId='" + tokenId + '\'' +
                ", token=" + Arrays.toString(token) +
                ", authenticationId='" + authenticationId + '\'' +
                ", username='" + username + '\'' +
                ", clientId='" + clientId + '\'' +
                ", authentication=" + Arrays.toString(authentication) +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
