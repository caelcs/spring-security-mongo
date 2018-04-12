package uk.co.caeldev.springsecuritymongo;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.stereotype.Component;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;
import uk.co.caeldev.springsecuritymongo.repositories.MongoClientDetailsRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

@Component
public class MongoClientDetailsService implements ClientDetailsService, ClientRegistrationService {

    private final MongoClientDetailsRepository mongoClientDetailsRepository;

    private final PasswordEncoder passwordEncoder;

    public MongoClientDetailsService(final MongoClientDetailsRepository mongoClientDetailsRepository,
                                     final PasswordEncoder passwordEncoder) {
        this.mongoClientDetailsRepository = mongoClientDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ClientDetails loadClientByClientId(final String clientId) {
        try {
            return mongoClientDetailsRepository.findByClientId(clientId);
        } catch (IllegalArgumentException e) {
            throw new ClientRegistrationException("No Client Details for client id", e);
        }
    }

    @Override
    public void addClientDetails(final ClientDetails clientDetails) {
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
                getAutoApproveScopes(clientDetails));

        mongoClientDetailsRepository.save(mongoClientDetails);
    }

    @Override
    public void updateClientDetails(final ClientDetails clientDetails) {
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
    public void updateClientSecret(final String clientId,
                                   final String secret) {
        final boolean result = mongoClientDetailsRepository.updateClientSecret(clientId, passwordEncoder.encode(secret));
        if (!result) {
            throw new NoSuchClientException("No such client id");
        }
    }

    @Override
    public void removeClientDetails(String clientId) {
        final boolean result = mongoClientDetailsRepository.deleteByClientId(clientId);
        if (!result) {
            throw new NoSuchClientException("No such client id");
        }
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        final List<MongoClientDetails> allClientDetails = mongoClientDetailsRepository.findAll();
        return newArrayList(allClientDetails);
    }

    private Set<String> getAutoApproveScopes(final ClientDetails clientDetails) {
        if (clientDetails.isAutoApprove("true")) {
            return newHashSet("true"); // all scopes autoapproved
        }

        return clientDetails.getScope().stream()
                .filter(clientDetails::isAutoApprove)
                .collect(Collectors.toSet());
    }
}
