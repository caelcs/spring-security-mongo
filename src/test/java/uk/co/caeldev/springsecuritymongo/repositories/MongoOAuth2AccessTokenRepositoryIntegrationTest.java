package uk.co.caeldev.springsecuritymongo.repositories;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MongoOAuth2AccessTokenRepositoryIntegrationTest {

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("copyshare");

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MongoOAuth2AccessTokenRepository mongoOAuth2AccessTokenRepository;

    @Test
    @UsingDataSet(locations = {"/samples/mongoAccessTokens.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldDeleteTokenByRefreshToken() throws Exception {
        //Given
        String tokenId = "49d855f31931b6063aa9e315e092f17f";

        //When
        final boolean result = mongoOAuth2AccessTokenRepository.deleteByRefreshTokenId(tokenId);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    @UsingDataSet(locations = {"/samples/mongoAccessTokens.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldDeleteTokenById() throws Exception {
        //Given
        String tokenId = "49d855f31931b6063aa9e315e092f17f";

        //When
        final boolean result = mongoOAuth2AccessTokenRepository.deleteByTokenId(tokenId);

        //Then
        assertThat(result).isTrue();
    }

}