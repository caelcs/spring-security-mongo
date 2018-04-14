package uk.co.caeldev.springsecuritymongo.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MongoClientDetails)) return false;

        MongoClientDetails that = (MongoClientDetails) o;

        return new EqualsBuilder()
                .append(clientId, that.clientId)
                .append(scope, that.scope)
                .append(resourceIds, that.resourceIds)
                .append(authorizedGrantTypes, that.authorizedGrantTypes)
                .append(registeredRedirectUris, that.registeredRedirectUris)
                .append(authorities, that.authorities)
                .append(accessTokenValiditySeconds, that.accessTokenValiditySeconds)
                .append(refreshTokenValiditySeconds, that.refreshTokenValiditySeconds)
                .append(additionalInformation, that.additionalInformation)
                .append(autoApproveScopes, that.autoApproveScopes)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clientId)
                .append(clientSecret)
                .append(scope)
                .append(resourceIds)
                .append(authorizedGrantTypes)
                .append(registeredRedirectUris)
                .append(authorities)
                .append(accessTokenValiditySeconds)
                .append(refreshTokenValiditySeconds)
                .append(additionalInformation)
                .append(autoApproveScopes)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientId", clientId)
                .append("clientSecret", clientSecret)
                .append("scope", scope)
                .append("resourceIds", resourceIds)
                .append("authorizedGrantTypes", authorizedGrantTypes)
                .append("registeredRedirectUris", registeredRedirectUris)
                .append("authorities", authorities)
                .append("accessTokenValiditySeconds", accessTokenValiditySeconds)
                .append("refreshTokenValiditySeconds", refreshTokenValiditySeconds)
                .append("additionalInformation", additionalInformation)
                .append("autoApproveScopes", autoApproveScopes)
                .toString();
    }
}
