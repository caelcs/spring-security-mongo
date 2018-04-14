package uk.co.caeldev.springsecuritymongo.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.springsecuritymongo.builders.MongoOAuth2RefreshTokenBuilder;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2RefreshToken;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MongoOAuth2RefreshTokenRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private MongoOAuth2RefreshTokenRepository mongoOAuth2RefreshTokenRepository;

    @Test
    public void shouldSaveRefreshToken() {
        //Given
        final MongoOAuth2RefreshToken mongoOAuth2RefreshToken = MongoOAuth2RefreshTokenBuilder.mongoOAuth2RefreshTokenBuilder().build();

        //When
        final MongoOAuth2RefreshToken saved = mongoOAuth2RefreshTokenRepository.save(mongoOAuth2RefreshToken);

        //Then
        assertThat(saved).isNotNull();
        assertThat(saved).isEqualTo(mongoOAuth2RefreshToken);
    }

    @Test
    public void shouldDeleteRefreshTokenByTokenId() {
        //Given
        final MongoOAuth2RefreshToken mongoOAuth2RefreshToken = MongoOAuth2RefreshTokenBuilder.mongoOAuth2RefreshTokenBuilder().build();
        final MongoOAuth2RefreshToken saved = mongoOAuth2RefreshTokenRepository.save(mongoOAuth2RefreshToken);

        //When
        final boolean result = mongoOAuth2RefreshTokenRepository.deleteByTokenId(saved.getTokenId());

        //Then
        assertThat(result).isTrue();
        final List<MongoOAuth2RefreshToken> all = mongoOAuth2RefreshTokenRepository.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    public void shouldFindRefreshTokenByTokenId() {
        //Given
        final MongoOAuth2RefreshToken mongoOAuth2RefreshToken = MongoOAuth2RefreshTokenBuilder.mongoOAuth2RefreshTokenBuilder().build();
        final MongoOAuth2RefreshToken saved = mongoOAuth2RefreshTokenRepository.save(mongoOAuth2RefreshToken);

        //When
        final MongoOAuth2RefreshToken refreshedTokenFound = mongoOAuth2RefreshTokenRepository.findByTokenId(saved.getTokenId());

        //Then
        assertThat(refreshedTokenFound).isNotNull();
        assertThat(refreshedTokenFound).isEqualTo(saved);
    }
}