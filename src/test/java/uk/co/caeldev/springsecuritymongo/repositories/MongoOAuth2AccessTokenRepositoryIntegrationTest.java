package uk.co.caeldev.springsecuritymongo.repositories;

import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.springsecuritymongo.builders.MongoOAuth2AccessTokenBuilder;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2AccessToken;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MongoOAuth2AccessTokenRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private MongoOAuth2AccessTokenRepository mongoOAuth2AccessTokenRepository;

    @Autowired
    private MongoClient mongoClient;

    @Before
    public void dropDatabase() {
        mongoClient.dropDatabase("copyshare");
    }

    @Test
    public void shouldSaveToken() {
        //Given
        final MongoOAuth2AccessToken mongoOAuth2AccessToken = MongoOAuth2AccessTokenBuilder
                .mongoOAuth2AccessTokenBuilder().build();

        //When
        final MongoOAuth2AccessToken saved = mongoOAuth2AccessTokenRepository.save(mongoOAuth2AccessToken);

        //Then
        assertThat(saved).isNotNull();
        final List<MongoOAuth2AccessToken> all = mongoOAuth2AccessTokenRepository.findAll();
        assertThat(all).hasSize(1);
    }

    @Test
    public void shouldDeleteTokenByRefreshToken() {
        //Given
        final MongoOAuth2AccessToken mongoOAuth2AccessToken = MongoOAuth2AccessTokenBuilder
                .mongoOAuth2AccessTokenBuilder().build();
        final MongoOAuth2AccessToken saved = mongoOAuth2AccessTokenRepository.save(mongoOAuth2AccessToken);

        //When
        final boolean result = mongoOAuth2AccessTokenRepository.deleteByRefreshTokenId(saved.getRefreshToken());

        //Then
        assertThat(result).isTrue();
        final List<MongoOAuth2AccessToken> all = mongoOAuth2AccessTokenRepository.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    public void shouldDeleteTokenById() {
        //Given
        final MongoOAuth2AccessToken mongoOAuth2AccessToken = MongoOAuth2AccessTokenBuilder
                .mongoOAuth2AccessTokenBuilder().build();
        final MongoOAuth2AccessToken saved = mongoOAuth2AccessTokenRepository.save(mongoOAuth2AccessToken);

        //When
        final boolean result = mongoOAuth2AccessTokenRepository.deleteByTokenId(saved.getTokenId());

        //Then
        assertThat(result).isTrue();
        final List<MongoOAuth2AccessToken> all = mongoOAuth2AccessTokenRepository.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    public void shouldFindTokenById() {
        //Given
        final MongoOAuth2AccessToken mongoOAuth2AccessToken = MongoOAuth2AccessTokenBuilder
                .mongoOAuth2AccessTokenBuilder().build();
        final MongoOAuth2AccessToken saved = mongoOAuth2AccessTokenRepository.save(mongoOAuth2AccessToken);

        //When
        final MongoOAuth2AccessToken result = mongoOAuth2AccessTokenRepository
                .findByTokenId(saved.getTokenId());

        //Then
        assertThat(result).isNotNull();
    }

    @Test
    public void shouldFindTokenByAuthenticationId() {
        //Given
        final MongoOAuth2AccessToken mongoOAuth2AccessToken = MongoOAuth2AccessTokenBuilder
                .mongoOAuth2AccessTokenBuilder().build();
        final MongoOAuth2AccessToken saved = mongoOAuth2AccessTokenRepository.save(mongoOAuth2AccessToken);

        //When
        final MongoOAuth2AccessToken result = mongoOAuth2AccessTokenRepository
                .findByAuthenticationId(saved.getAuthenticationId());

        //Then
        assertThat(result).isNotNull();
    }

    @Test
    public void shouldFindAllTokensByClientId() {
        //Given
        final MongoOAuth2AccessToken mongoOAuth2AccessToken = MongoOAuth2AccessTokenBuilder
                .mongoOAuth2AccessTokenBuilder().build();
        final MongoOAuth2AccessToken saved = mongoOAuth2AccessTokenRepository.save(mongoOAuth2AccessToken);

        //When
        final List<MongoOAuth2AccessToken> tokens = mongoOAuth2AccessTokenRepository
                .findByClientId(saved.getClientId());

        //Then
        assertThat(tokens).isNotNull();
        assertThat(tokens).hasSize(1);
    }

    @Test
    public void shouldFindAllTokensByUsernameAndClientId() {
        //Given
        final MongoOAuth2AccessToken mongoOAuth2AccessToken = MongoOAuth2AccessTokenBuilder
                .mongoOAuth2AccessTokenBuilder().build();
        final MongoOAuth2AccessToken saved = mongoOAuth2AccessTokenRepository.save(mongoOAuth2AccessToken);

        //When
        final List<MongoOAuth2AccessToken> tokens = mongoOAuth2AccessTokenRepository
                .findByUsernameAndClientId(saved.getUsername(), saved.getClientId());

        //Then
        assertThat(tokens).isNotNull();
        assertThat(tokens).hasSize(1);
    }

}