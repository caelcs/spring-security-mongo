package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.core.GrantedAuthority;
import uk.co.caeldev.springsecuritymongo.commons.SecurityRDG;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.*;

public class MongoClientDetailsBuilder {

    private String clientId = string().next();
    private String clientSecret = string().next();
    private Set<String> scope = set(string()).next();
    private Set<String> resourceIds = set(string()).next();
    private Set<String> authorizedGrantTypes = set(string()).next();
    private Set<String> registeredRedirectUris = set(string()).next();
    private List<GrantedAuthority> authorities = list(SecurityRDG.ofGrantedAuthority()).next();
    private Integer accessTokenValiditySeconds = integer().next();
    private Integer refreshTokenValiditySeconds = integer().next();
    private Map<String, Object> additionalInformation = map(string(), SecurityRDG.objectOf(string())).next();
    private Set<String> autoApproveScopes = set(string()).next();

    private MongoClientDetailsBuilder() {
    }

    public static MongoClientDetailsBuilder mongoClientDetailsBuilder() {
        return new MongoClientDetailsBuilder();
    }

    public MongoClientDetails build() {
        return new MongoClientDetails(clientId,
                clientSecret,
                scope,
                resourceIds,
                authorizedGrantTypes,
                registeredRedirectUris,
                authorities,
                accessTokenValiditySeconds,
                refreshTokenValiditySeconds,
                additionalInformation,
                autoApproveScopes);
    }
}
