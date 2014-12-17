package uk.co.caeldev.springsecuritymongo.builders;

import com.google.common.base.Joiner;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.list;
import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.string;

public class ClientDetailsBuilder {

    private String clientId = string().next();
    private String resourceIds = Joiner.on(",").join(list(string()).next());
    private String scopes = Joiner.on(",").join(list(string()).next());
    private String grantTypes = Joiner.on(",").join(list(string()).next());
    private String authorities = Joiner.on(",").join(list(string()).next());

    private ClientDetailsBuilder() {
    }

    public static ClientDetailsBuilder clientDetailsBuilder() {
        return new ClientDetailsBuilder();
    }

    public ClientDetails build() {
        return new BaseClientDetails(clientId, resourceIds, scopes, grantTypes, authorities);
    }
}
