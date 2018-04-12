package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.*;

public class OAuth2RequestBuilder {

    private Map<String, String> requestParameters = map(string(), string()).next();
    private String clientId = string().next();
    private Collection<? extends GrantedAuthority> authorities = list(ofGrantedAuthority()).next();
    private boolean approved = bool().next();
    private Set<String> scope = set(string()).next();
    private Set<String> resourceIds = set(string()).next();
    private String redirectUri = string().next();
    private Set<String> responseTypes = set(string()).next();
    private Map<String, Serializable> extensionProperties = map(string(), serializableOf(longVal())).next();

    private OAuth2RequestBuilder() {
    }

    public static OAuth2RequestBuilder oAuth2RequestBuilder() {
        return new OAuth2RequestBuilder();
    }

    public OAuth2Request build() {
        return new OAuth2Request(requestParameters,
                clientId,
                authorities,
                approved,
                scope,
                resourceIds,
                redirectUri,
                responseTypes,
                extensionProperties);
    }
}
