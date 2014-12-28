package uk.co.caeldev.springsecuritymongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Component;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2ClientToken;
import uk.co.caeldev.springsecuritymongo.repositories.MongoOAuth2ClientTokenRepository;

import java.util.UUID;

@Component
public class MongoClientTokenServices implements ClientTokenServices {

    private final MongoOAuth2ClientTokenRepository mongoOAuth2ClientTokenRepository;

    private final ClientKeyGenerator clientKeyGenerator;

    @Autowired
    public MongoClientTokenServices(final MongoOAuth2ClientTokenRepository mongoOAuth2ClientTokenRepository,
                                    final ClientKeyGenerator clientKeyGenerator) {
        this.mongoOAuth2ClientTokenRepository = mongoOAuth2ClientTokenRepository;
        this.clientKeyGenerator = clientKeyGenerator;
    }

    @Override
    public OAuth2AccessToken getAccessToken(final OAuth2ProtectedResourceDetails resource,
                                            final Authentication authentication) {
        final MongoOAuth2ClientToken mongoOAuth2ClientToken = mongoOAuth2ClientTokenRepository.findByAuthenticationId(clientKeyGenerator.extractKey(resource, authentication));
        return SerializationUtils.deserialize(mongoOAuth2ClientToken.getToken());
    }

    @Override
    public void saveAccessToken(final OAuth2ProtectedResourceDetails resource,
                                final Authentication authentication,
                                final OAuth2AccessToken accessToken) {
        removeAccessToken(resource, authentication);
        final MongoOAuth2ClientToken mongoOAuth2ClientToken = new MongoOAuth2ClientToken(UUID.randomUUID().toString(),
                accessToken.getValue(),
                SerializationUtils.serialize(accessToken),
                clientKeyGenerator.extractKey(resource, authentication),
                authentication.getName(),
                resource.getClientId());

        mongoOAuth2ClientTokenRepository.save(mongoOAuth2ClientToken);
    }

    @Override
    public void removeAccessToken(final OAuth2ProtectedResourceDetails resource,
                                  final Authentication authentication) {
        mongoOAuth2ClientTokenRepository.deleteByAuthenticationId(clientKeyGenerator.extractKey(resource, authentication));
    }
}
