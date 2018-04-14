package uk.co.caeldev.springsecuritymongo.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.springsecuritymongo.builders.MongoOAuth2ClientTokenBuilder;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2ClientToken;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MongoOAuth2ClientTokenRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private MongoOAuth2ClientTokenRepository mongoOAuth2ClientTokenRepository;

    @Test
    public void shouldSaveMongoOAuth2ClientToken() {
        //Given
        final MongoOAuth2ClientToken mongoOAuth2ClientToken = MongoOAuth2ClientTokenBuilder
                .mongoOAuth2ClientTokenBuilder().build();

        //When
        final MongoOAuth2ClientToken saved = mongoOAuth2ClientTokenRepository.save(mongoOAuth2ClientToken);

        //Then
        assertThat(saved).isEqualTo(mongoOAuth2ClientToken);
    }

    @Test
    public void shouldDeleteMongoOAuth2ClientTokenByAuthenticationId() {
        //Given
        final MongoOAuth2ClientToken mongoOAuth2ClientToken = MongoOAuth2ClientTokenBuilder
                .mongoOAuth2ClientTokenBuilder().build();
        final MongoOAuth2ClientToken saved = mongoOAuth2ClientTokenRepository.save(mongoOAuth2ClientToken);

        //When
        final boolean result = mongoOAuth2ClientTokenRepository.deleteByAuthenticationId(saved.getAuthenticationId());

        //Then
        assertThat(result).isTrue();
        final List<MongoOAuth2ClientToken> all = mongoOAuth2ClientTokenRepository.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    public void shouldFindMongoOAuth2ClientTokenByAuthenticationId() {
        //Given
        final MongoOAuth2ClientToken mongoOAuth2ClientToken = MongoOAuth2ClientTokenBuilder
                .mongoOAuth2ClientTokenBuilder().build();
        final MongoOAuth2ClientToken saved = mongoOAuth2ClientTokenRepository.save(mongoOAuth2ClientToken);

        //When
        final MongoOAuth2ClientToken found = mongoOAuth2ClientTokenRepository
                .findByAuthenticationId(saved.getAuthenticationId());

        //Then
        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(saved);
    }
}