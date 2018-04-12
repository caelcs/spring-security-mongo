package uk.co.caeldev.springsecuritymongo.repositories;

import com.github.fakemongo.junit.FongoRule;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.springsecuritymongo.config.ApplicationConfiguration;

import static com.lordofthejars.nosqlunit.core.LoadStrategyEnum.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = { ApplicationConfiguration.class })
@ActiveProfiles("test")
@DirtiesContext
public class MongoOAuth2AccessTokenRepositoryIntegrationTest {

    @Rule
    public FongoRule fongoRule = new FongoRule();

    @Autowired
    private MongoOAuth2AccessTokenRepository mongoOAuth2AccessTokenRepository;

    @Test
    @UsingDataSet(locations = {"/samples/mongoAccessTokens.json"}, loadStrategy = CLEAN_INSERT)
    public void shouldDeleteTokenByRefreshToken() {
        //Given
        String tokenId = "49d855f31931b6063aa9e315e092f17f";

        //When
        final boolean result = mongoOAuth2AccessTokenRepository.deleteByRefreshTokenId(tokenId);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    @UsingDataSet(locations = {"/samples/mongoAccessTokens.json"}, loadStrategy = CLEAN_INSERT)
    public void shouldDeleteTokenById() {
        //Given
        String tokenId = "49d855f31931b6063aa9e315e092f17f";

        //When
        final boolean result = mongoOAuth2AccessTokenRepository.deleteByTokenId(tokenId);

        //Then
        assertThat(result).isTrue();
    }

}