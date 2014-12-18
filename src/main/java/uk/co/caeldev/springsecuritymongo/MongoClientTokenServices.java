package uk.co.caeldev.springsecuritymongo;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2ClientToken;
import uk.co.caeldev.springsecuritymongo.repositories.MongoOAuth2ClientTokenRepository;

import java.util.UUID;

public class MongoClientTokenServices implements ClientTokenServices {

    private final MongoOAuth2ClientTokenRepository mongoOAuth2ClientTokenRepository;

    private final ClientKeyGenerator keyGenerator;

    public MongoClientTokenServices(final MongoOAuth2ClientTokenRepository mongoOAuth2ClientTokenRepository,
                                    final ClientKeyGenerator keyGenerator) {
        this.mongoOAuth2ClientTokenRepository = mongoOAuth2ClientTokenRepository;
        this.keyGenerator = keyGenerator;
    }

    @Override
    public OAuth2AccessToken getAccessToken(final OAuth2ProtectedResourceDetails resource,
                                            final Authentication authentication) {
        final MongoOAuth2ClientToken mongoOAuth2ClientToken = mongoOAuth2ClientTokenRepository.findByAuthenticationId(keyGenerator.extractKey(resource, authentication));
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
                keyGenerator.extractKey(resource, authentication),
                authentication.getName(),
                resource.getClientId());

        mongoOAuth2ClientTokenRepository.save(mongoOAuth2ClientToken);
    }

    @Override
    public void removeAccessToken(final OAuth2ProtectedResourceDetails resource,
                                  final Authentication authentication) {
        mongoOAuth2ClientTokenRepository.deleteByAuthenticationId(keyGenerator.extractKey(resource, authentication));
    }
}
