package uk.co.caeldev.springsecuritymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;
import java.util.Objects;

@Document
public class MongoOAuth2ProtectedResourceDetails {

    @Id
    private String id;
    private String grantType = "unsupported";
    private String clientId;
    private String accessTokenUri;
    private List<String> scope;
    private String clientSecret;
    private AuthenticationScheme clientAuthenticationScheme = AuthenticationScheme.header;
    private AuthenticationScheme authorizationScheme = AuthenticationScheme.header;
    private String tokenName = OAuth2AccessToken.ACCESS_TOKEN;


    public MongoOAuth2ProtectedResourceDetails(final String id,
                                               final String grantType,
                                               final String clientId,
                                               final String accessTokenUri,
                                               final List<String> scope,
                                               final String clientSecret,
                                               final AuthenticationScheme clientAuthenticationScheme,
                                               final AuthenticationScheme authorizationScheme,
                                               final String tokenName) {
        this.id = id;
        this.grantType = grantType;
        this.clientId = clientId;
        this.accessTokenUri = accessTokenUri;
        this.scope = scope;
        this.clientSecret = clientSecret;
        this.clientAuthenticationScheme = clientAuthenticationScheme;
        this.authorizationScheme = authorizationScheme;
        this.tokenName = tokenName;
    }

    public String getId() {
        return id;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public List<String> getScope() {
        return scope;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public AuthenticationScheme getClientAuthenticationScheme() {
        return clientAuthenticationScheme;
    }

    public AuthenticationScheme getAuthorizationScheme() {
        return authorizationScheme;
    }

    public String getTokenName() {
        return tokenName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(grantType,
                clientId,
                accessTokenUri,
                scope,
                clientSecret,
                clientAuthenticationScheme,
                authorizationScheme,
                tokenName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MongoOAuth2ProtectedResourceDetails other = (MongoOAuth2ProtectedResourceDetails) obj;
        return Objects.equals(this.grantType, other.grantType)
                && Objects.equals(this.clientId, other.clientId)
                && Objects.equals(this.accessTokenUri, other.accessTokenUri)
                && Objects.equals(this.scope, other.scope)
                && Objects.equals(this.clientSecret, other.clientSecret)
                && Objects.equals(this.clientAuthenticationScheme, other.clientAuthenticationScheme)
                && Objects.equals(this.authorizationScheme, other.authorizationScheme)
                && Objects.equals(this.tokenName, other.tokenName);
    }

    @Override
    public String toString() {
        return "MongoOAuth2ProtectedResourceDetails{" +
                "id='" + id + '\'' +
                ", grantType='" + grantType + '\'' +
                ", clientId='" + clientId + '\'' +
                ", accessTokenUri='" + accessTokenUri + '\'' +
                ", scope=" + scope +
                ", clientSecret='" + clientSecret + '\'' +
                ", clientAuthenticationScheme=" + clientAuthenticationScheme +
                ", authorizationScheme=" + authorizationScheme +
                ", tokenName='" + tokenName + '\'' +
                '}';
    }
}
