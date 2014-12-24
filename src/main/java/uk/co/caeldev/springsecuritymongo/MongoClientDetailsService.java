package uk.co.caeldev.springsecuritymongo;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;
import uk.co.caeldev.springsecuritymongo.repositories.MongoClientDetailsRepository;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newHashSet;

@Component
public class MongoClientDetailsService implements ClientDetailsService, ClientRegistrationService {

    private final MongoClientDetailsRepository mongoClientDetailsRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MongoClientDetailsService(final MongoClientDetailsRepository mongoClientDetailsRepository,
                                     final PasswordEncoder passwordEncoder) {
        this.mongoClientDetailsRepository = mongoClientDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        try {
            final MongoClientDetails mongoClientDetails = mongoClientDetailsRepository.findByClientId(clientId);

            return new BaseClientDetails(mongoClientDetails.getClientId(),
                    Joiner.on(",").join(mongoClientDetails.getResourceIds()),
                    Joiner.on(",").join(mongoClientDetails.getScope()),
                    Joiner.on(",").join(mongoClientDetails.getAuthorizedGrantTypes()),
                    Joiner.on(",").join(mongoClientDetails.getAuthorities()),
                    Joiner.on(",").join(mongoClientDetails.getRegisteredRedirectUri()));
        } catch (IllegalArgumentException e) {
            throw new ClientRegistrationException("No Client Details for client id", e);
        }
    }

    @Override
    public void addClientDetails(final ClientDetails clientDetails) throws ClientAlreadyExistsException {
        final MongoClientDetails mongoClientDetails = new MongoClientDetails(clientDetails.getClientId(),
                passwordEncoder.encode(clientDetails.getClientSecret()),
                clientDetails.getScope(),
                clientDetails.getResourceIds(),
                clientDetails.getAuthorizedGrantTypes(),
                clientDetails.getRegisteredRedirectUri(),
                newArrayList(clientDetails.getAuthorities()),
                clientDetails.getAccessTokenValiditySeconds(),
                clientDetails.getRefreshTokenValiditySeconds(),
                clientDetails.getAdditionalInformation(),
                null);

        mongoClientDetailsRepository.save(mongoClientDetails);
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        final MongoClientDetails mongoClientDetails = new MongoClientDetails(clientDetails.getClientId(),
                clientDetails.getClientSecret(),
                clientDetails.getScope(),
                clientDetails.getResourceIds(),
                clientDetails.getAuthorizedGrantTypes(),
                clientDetails.getRegisteredRedirectUri(),
                newArrayList(clientDetails.getAuthorities()),
                clientDetails.getAccessTokenValiditySeconds(),
                clientDetails.getRefreshTokenValiditySeconds(),
                clientDetails.getAdditionalInformation(),
                getAutoApproveScopes(clientDetails));
        final boolean result = mongoClientDetailsRepository.update(mongoClientDetails);

        if (!result) {
            throw new NoSuchClientException("No such Client Id");
        }
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        final boolean result = mongoClientDetailsRepository.updateClientSecret(clientId, passwordEncoder.encode(secret));
        if (!result) {
            throw new NoSuchClientException("No such client id");
        }
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        final boolean result = mongoClientDetailsRepository.deleteByClientId(clientId);
        if (!result) {
            throw new NoSuchClientException("No such client id");
        }
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return null;
    }

    private Set<String> getAutoApproveScopes(final ClientDetails clientDetails) {
        if (clientDetails.isAutoApprove("true")) {
            return newHashSet("true"); // all scopes autoapproved
        }
        return filter(clientDetails.getScope(), ByAutoApproveOfScope(clientDetails));
    }

    private Predicate<String> ByAutoApproveOfScope(final ClientDetails clientDetails) {
        return new Predicate<String>() {
            @Override
            public boolean apply(final String scope) {
                return clientDetails.isAutoApprove(scope);
            }
        };
    }
}
