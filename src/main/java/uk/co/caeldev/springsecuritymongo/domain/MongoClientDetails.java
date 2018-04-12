package uk.co.caeldev.springsecuritymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

import static java.util.Objects.isNull;

@Document
public class MongoClientDetails implements ClientDetails {

    @Id
    private String clientId;
    private String clientSecret;
    private Set<String> scope = Collections.emptySet();
    private Set<String> resourceIds = Collections.emptySet();
    private Set<String> authorizedGrantTypes = Collections.emptySet();
    private Set<String> registeredRedirectUris;
    private List<GrantedAuthority> authorities = Collections.emptyList();
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Map<String, Object> additionalInformation = new LinkedHashMap<>();
    private Set<String> autoApproveScopes;

    public MongoClientDetails() {
    }

    @PersistenceConstructor
    public MongoClientDetails(final String clientId,
                              final String clientSecret,
                              final Set<String> scope,
                              final Set<String> resourceIds,
                              final Set<String> authorizedGrantTypes,
                              final Set<String> registeredRedirectUris,
                              final List<GrantedAuthority> authorities,
                              final Integer accessTokenValiditySeconds,
                              final Integer refreshTokenValiditySeconds,
                              final Map<String, Object> additionalInformation,
                              final Set<String> autoApproveScopes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
        this.resourceIds = resourceIds;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.registeredRedirectUris = registeredRedirectUris;
        this.authorities = authorities;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.additionalInformation = additionalInformation;
        this.autoApproveScopes = autoApproveScopes;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Set<String> getScope() {
        return scope;
    }

    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAutoApproveScopes(final Set<String> autoApproveScopes) {
        this.autoApproveScopes = autoApproveScopes;
    }

    public Set<String> getAutoApproveScopes() {
        return autoApproveScopes;
    }

    @Override
    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUris;
    }

    @Override
    public boolean isAutoApprove(final String scope) {
        if (isNull(autoApproveScopes)) {
            return false;
        }
        for (String auto : autoApproveScopes) {
            if ("true".equals(auto) || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientSecret, scope, resourceIds, authorizedGrantTypes, registeredRedirectUris, authorities, accessTokenValiditySeconds, refreshTokenValiditySeconds, additionalInformation, autoApproveScopes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MongoClientDetails other = (MongoClientDetails) obj;
        return Objects.equals(this.clientId, other.clientId)
                && Objects.equals(this.scope, other.scope)
                && Objects.equals(this.resourceIds, other.resourceIds)
                && Objects.equals(this.authorizedGrantTypes, other.authorizedGrantTypes)
                && Objects.equals(this.registeredRedirectUris, other.registeredRedirectUris)
                && Objects.equals(this.authorities, other.authorities)
                && Objects.equals(this.accessTokenValiditySeconds, other.accessTokenValiditySeconds)
                && Objects.equals(this.refreshTokenValiditySeconds, other.refreshTokenValiditySeconds)
                && Objects.equals(this.additionalInformation, other.additionalInformation)
                && Objects.equals(this.autoApproveScopes, other.autoApproveScopes);
    }

    @Override
    public String toString() {
        return "MongoClientDetails{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", scope=" + scope +
                ", resourceIds=" + resourceIds +
                ", authorizedGrantTypes=" + authorizedGrantTypes +
                ", registeredRedirectUris=" + registeredRedirectUris +
                ", authorities=" + authorities +
                ", accessTokenValiditySeconds=" + accessTokenValiditySeconds +
                ", refreshTokenValiditySeconds=" + refreshTokenValiditySeconds +
                ", additionalInformation=" + additionalInformation +
                ", autoApproveScopes=" + autoApproveScopes +
                '}';
    }
}
