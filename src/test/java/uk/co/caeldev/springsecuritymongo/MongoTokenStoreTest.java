package uk.co.caeldev.springsecuritymongo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import uk.co.caeldev.springsecuritymongo.builders.*;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2AccessToken;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2RefreshToken;
import uk.co.caeldev.springsecuritymongo.repositories.MongoOAuth2AccessTokenRepository;
import uk.co.caeldev.springsecuritymongo.repositories.MongoOAuth2RefreshTokenRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.*;

@RunWith(MockitoJUnitRunner.class)
public class MongoTokenStoreTest {

    @Mock
    private MongoOAuth2AccessTokenRepository mongoOAuth2AccessTokenRepository;

    @Mock
    private MongoOAuth2RefreshTokenRepository mongoOAuth2RefreshTokenRepository;

    @Mock
    private AuthenticationKeyGenerator authenticationKeyGenerator;

    private TokenStore tokenStore;

    @Before
    public void setup() {
        tokenStore = new MongoTokenStore(mongoOAuth2AccessTokenRepository, mongoOAuth2RefreshTokenRepository, authenticationKeyGenerator);
    }


    @Test
    public void shouldStoreAccessToken() {
        //Given
        final OAuth2AccessToken auth2AccessToken = OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build();
        final byte[] token = SerializationUtils.serialize(auth2AccessToken);

        //And
        final OAuth2Authentication oAuth2Authentication = OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

        //And
        given(mongoOAuth2AccessTokenRepository.findByTokenId(any(String.class))).willReturn(MongoOAuth2AccessTokenBuilder.mongoOAuth2AccessTokenBuilder().token(token).build());

        //When
        tokenStore.storeAccessToken(auth2AccessToken, oAuth2Authentication);

        //Then
        verify(mongoOAuth2AccessTokenRepository).deleteByTokenId(any(String.class));
        verify(mongoOAuth2AccessTokenRepository).save(any(MongoOAuth2AccessToken.class));
    }

    @Test
    public void shouldReadAccessToken() {
        //Given
        final String tokenValue = string().next();

        //And
        final OAuth2AccessToken auth2AccessToken = OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().token(tokenValue).build();
        final byte[] token = SerializationUtils.serialize(auth2AccessToken);

        //And
        given(mongoOAuth2AccessTokenRepository.findByTokenId(any(String.class)))
                .willReturn(MongoOAuth2AccessTokenBuilder.mongoOAuth2AccessTokenBuilder()
                        .token(token)
                        .build());

        //When
        final OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(tokenValue);

        //Then
        assertThat(oAuth2AccessToken.getValue()).isEqualTo(tokenValue);
    }

    @Test
    public void shouldReturnNullWhenNoReadAccessToken() {
        //Given
        final String tokenValue = string().next();

        //And
        given(mongoOAuth2AccessTokenRepository.findByTokenId(any(String.class)))
                .willReturn(null);

        //When
        final OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(tokenValue);

        //Then
        assertThat(oAuth2AccessToken).isNull();
    }

    @Test
    public void shouldRemoveAccessToken() {
        //Given
        final OAuth2AccessToken oAuth2AccessToken = OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build();

        //When
        tokenStore.removeAccessToken(oAuth2AccessToken);

        //Then
        verify(mongoOAuth2AccessTokenRepository).deleteByTokenId(any(String.class));
    }

    @Test
    public void shouldStoreRefreshToken() {
        //Given
        final OAuth2RefreshToken oAuth2RefreshToken = OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

        //And
        final OAuth2Authentication oAuth2Authentication = OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

        //And
        final ArgumentCaptor<MongoOAuth2RefreshToken> argumentCaptor = ArgumentCaptor.forClass(MongoOAuth2RefreshToken.class);

        //When
        tokenStore.storeRefreshToken(oAuth2RefreshToken, oAuth2Authentication);

        //Then
        verify(mongoOAuth2RefreshTokenRepository).save(argumentCaptor.capture());
        final MongoOAuth2RefreshToken refreshToken = argumentCaptor.getValue();
        final byte[] expectedResult = SerializationUtils.serialize(oAuth2RefreshToken);
        assertThat(refreshToken.getToken()).isEqualTo(expectedResult);

    }

    @Test
    public void shouldReadRefreshToken() {
        //Given
        final String tokenValue = string().next();
        final OAuth2RefreshToken oAuth2RefreshToken = OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();
        final byte[] oAuth2RefreshTokenSer = SerializationUtils.serialize(oAuth2RefreshToken);

        //And
        given(mongoOAuth2RefreshTokenRepository.findByTokenId(any(String.class)))
                .willReturn(MongoOAuth2RefreshTokenBuilder.mongoOAuth2RefreshTokenBuilder().token(oAuth2RefreshTokenSer).build());

        //When
        final OAuth2RefreshToken result = tokenStore.readRefreshToken(tokenValue);

        //Then
        assertThat(result.getValue()).isEqualTo(oAuth2RefreshToken.getValue());
    }

    @Test
    public void shouldReadNullWhenNoRefreshToken() {
        //Given
        final String tokenValue = string().next();

        //And
        given(mongoOAuth2RefreshTokenRepository.findByTokenId(any(String.class)))
                .willReturn(null);

        //When
        final OAuth2RefreshToken result = tokenStore.readRefreshToken(tokenValue);

        //Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldReadAuthenticationForRefreshToken() {
        //Given
        final OAuth2RefreshToken oAuth2RefreshToken = OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

        //And
        final OAuth2Authentication authentication = OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();
        final byte[] authenticationSer = SerializationUtils.serialize(authentication);

        //And
        given(mongoOAuth2RefreshTokenRepository.findByTokenId(any(String.class)))
                .willReturn(MongoOAuth2RefreshTokenBuilder.mongoOAuth2RefreshTokenBuilder()
                        .authentication(authenticationSer)
                        .build());
        //When
        final OAuth2Authentication oAuth2Authentication = tokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken);

        //Then
        assertThat(oAuth2Authentication.getPrincipal()).isEqualTo(authentication.getPrincipal());
        assertThat(oAuth2Authentication.getCredentials()).isEqualTo(authentication.getCredentials());
    }

    @Test
    public void shouldReadNullWhenAuthenticationForNoRefreshToken() {
        //Given
        final OAuth2RefreshToken oAuth2RefreshToken = OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

        //And
        given(mongoOAuth2RefreshTokenRepository.findByTokenId(any(String.class)))
                .willReturn(null);
        //When
        final OAuth2Authentication oAuth2Authentication = tokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken);

        //Then
        assertThat(oAuth2Authentication).isNull();
    }

    @Test
    public void shouldRemoveRefreshToken() {
        //Given
        final OAuth2RefreshToken oAuth2RefreshToken = OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

        //When
        tokenStore.removeRefreshToken(oAuth2RefreshToken);

        //Then
        verify(mongoOAuth2RefreshTokenRepository).deleteByTokenId(any(String.class));
    }

    @Test
    public void shouldRemoveAccessTokenUsingRefreshToken() {
        //Given
        final OAuth2RefreshToken oAuth2RefreshToken = OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

        //When
        tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);

        //Then
        verify(mongoOAuth2AccessTokenRepository).deleteByRefreshTokenId(any(String.class));
    }

    @Test
    public void shouldGetAccessToken() {
        //Given
        final OAuth2Authentication oAuth2Authentication = OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

        //And
        final String value = string().next();
        given(authenticationKeyGenerator.extractKey(oAuth2Authentication)).willReturn(value);

        //And
        final OAuth2AccessToken oAuth2AccessToken = OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build();

        final byte[] oAuth2AccessTokenSer = SerializationUtils.serialize(oAuth2AccessToken);
        given(mongoOAuth2AccessTokenRepository.findByAuthenticationId(value))
                .willReturn(MongoOAuth2AccessTokenBuilder.mongoOAuth2AccessTokenBuilder()
                        .token(oAuth2AccessTokenSer)
                        .build());

        //And
        given(authenticationKeyGenerator.extractKey(any(OAuth2Authentication.class))).willReturn(value);

        //And
        given(mongoOAuth2AccessTokenRepository.findByTokenId(anyString())).willReturn(MongoOAuth2AccessTokenBuilder.mongoOAuth2AccessTokenBuilder().build());

        //When
        tokenStore.getAccessToken(oAuth2Authentication);

        //Then
        verify(mongoOAuth2AccessTokenRepository, never()).deleteByTokenId(any(String.class));
        verify(mongoOAuth2AccessTokenRepository, never()).save(any(MongoOAuth2AccessToken.class));
    }

    @Test
    public void shouldReturnNullWhenNoAccessToken() {
        //Given
        final OAuth2Authentication oAuth2Authentication = OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

        //And
        final String value = string().next();
        given(authenticationKeyGenerator.extractKey(oAuth2Authentication)).willReturn(value);

        //And
        given(mongoOAuth2AccessTokenRepository.findByAuthenticationId(value))
                .willReturn(null);

        //When
        tokenStore.getAccessToken(oAuth2Authentication);

        //Then
        verify(mongoOAuth2AccessTokenRepository, never()).deleteByTokenId(any(String.class));
        verify(mongoOAuth2AccessTokenRepository, never()).save(any(MongoOAuth2AccessToken.class));
        verify(mongoOAuth2AccessTokenRepository, never()).findByTokenId(anyString());
    }

    @Test
    public void shouldGetAccessTokenAndRemoveOldTokenAndPersistNewOne() {
        //Given
        final OAuth2Authentication oAuth2Authentication = OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

        //And
        final String value = string().next();
        given(authenticationKeyGenerator.extractKey(oAuth2Authentication)).willReturn(value);

        //And
        given(mongoOAuth2AccessTokenRepository.findByAuthenticationId(value))
                .willReturn(MongoOAuth2AccessTokenBuilder.mongoOAuth2AccessTokenBuilder()
                        .build());

        //And
        given(mongoOAuth2AccessTokenRepository.findByTokenId(anyString())).willReturn(MongoOAuth2AccessTokenBuilder.mongoOAuth2AccessTokenBuilder().build());

        //When
        tokenStore.getAccessToken(oAuth2Authentication);

        //Then
        verify(mongoOAuth2AccessTokenRepository, atLeastOnce()).deleteByTokenId(any(String.class));
        verify(mongoOAuth2AccessTokenRepository).save(any(MongoOAuth2AccessToken.class));
    }

    @Test
    public void shouldFindTokensByClientIdAndUserName() {
        //Given
        final String username = string().next();
        final String clientId = string().next();

        //And
        final List<MongoOAuth2AccessToken> expectedTokens = list(ofMongoOAuth2AccessToken()).next();
        given(mongoOAuth2AccessTokenRepository.findByUsernameAndClientId(username, clientId)).willReturn(expectedTokens);

        //When
        final Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, username);

        //Then
        assertThat(tokens).hasSize(expectedTokens.size());
    }

    @Test
    public void shouldFindTokensByClientId() {
        //Given
        final String clientId = string().next();

        //And
        final List<MongoOAuth2AccessToken> expectedTokens = list(ofMongoOAuth2AccessToken()).next();
        given(mongoOAuth2AccessTokenRepository.findByClientId(clientId)).willReturn(expectedTokens);

        //When
        final Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);

        //Then
        assertThat(tokens).hasSize(expectedTokens.size());
    }

}
