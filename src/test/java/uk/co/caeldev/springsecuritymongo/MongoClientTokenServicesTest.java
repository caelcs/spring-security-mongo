package uk.co.caeldev.springsecuritymongo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import static uk.co.caeldev.springsecuritymongo.builders.OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder;
import static uk.co.caeldev.springsecuritymongo.builders.OAuth2ProtectedResourceDetailsBuilder.oAuth2ProtectedResourceDetailsBuilder;
import static uk.co.caeldev.springsecuritymongo.builders.UserBuilder.userBuilder;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class MongoClientTokenServicesTest {

    private MongoClientTokenServices mongoClientTokenServices;

    @Before
    public void setup() {
        mongoClientTokenServices = new MongoClientTokenServices();
    }

    @Test
    public void shouldSaveAccessToken() {
        //Given
        final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails = oAuth2ProtectedResourceDetailsBuilder().build();
        final TestingAuthenticationToken authentication = new TestingAuthenticationToken(userBuilder().build(), string().next());
        final OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokenBuilder().build();

        //When
        mongoClientTokenServices.saveAccessToken(oAuth2ProtectedResourceDetails, authentication, oAuth2AccessToken);

        //Then


    }
}
