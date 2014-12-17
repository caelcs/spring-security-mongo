package uk.co.caeldev.springsecuritymongo;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2AccessToken;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2RefreshToken;
import uk.co.caeldev.springsecuritymongo.repositories.MongoOAuth2AccessTokenRepository;
import uk.co.caeldev.springsecuritymongo.repositories.MongoOAuth2RefreshTokenRepository;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;

public class MongoTokenStore implements TokenStore {

    private final MongoOAuth2AccessTokenRepository mongoOAuth2AccessTokenRepository;

    private final MongoOAuth2RefreshTokenRepository mongoOAuth2RefreshTokenRepository;

    private final AuthenticationKeyGenerator authenticationKeyGenerator;

    public MongoTokenStore(final MongoOAuth2AccessTokenRepository mongoOAuth2AccessTokenRepository,
                           final MongoOAuth2RefreshTokenRepository mongoOAuth2RefreshTokenRepository,
                           final AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.mongoOAuth2AccessTokenRepository = mongoOAuth2AccessTokenRepository;
        this.mongoOAuth2RefreshTokenRepository = mongoOAuth2RefreshTokenRepository;
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    @Override
    public OAuth2Authentication readAuthentication(final OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(final String token) {
        final String tokenId = extractTokenKey(token);

        final MongoOAuth2AccessToken mongoOAuth2AccessToken = mongoOAuth2AccessTokenRepository.findByTokenId(tokenId);
        return deserializeAuthentication(mongoOAuth2AccessToken.getAuthentication());
    }

    @Override
    public void storeAccessToken(final OAuth2AccessToken token,
                                 final OAuth2Authentication authentication) {
        String refreshToken = null;
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
        }

        if (readAccessToken(token.getValue())!=null) {
            removeAccessToken(token.getValue());
        }

        final String tokenKey = extractTokenKey(token.getValue());

        final MongoOAuth2AccessToken oAuth2AccessToken = new MongoOAuth2AccessToken(tokenKey,
                serializeAccessToken(token),
                authenticationKeyGenerator.extractKey(authentication),
                authentication.isClientOnly() ? null : authentication.getName(),
                authentication.getOAuth2Request().getClientId(),
                serializeAuthentication(authentication),
                extractTokenKey(refreshToken));

        mongoOAuth2AccessTokenRepository.save(oAuth2AccessToken);
    }

    public void removeAccessToken(final String tokenValue) {
        final String tokenKey = extractTokenKey(tokenValue);
        mongoOAuth2AccessTokenRepository.deleteByTokenId(tokenKey);
    }

    @Override
    public OAuth2AccessToken readAccessToken(final String tokenValue) {
        final String tokenKey = extractTokenKey(tokenValue);
        final MongoOAuth2AccessToken mongoOAuth2AccessToken = mongoOAuth2AccessTokenRepository.findByTokenId(tokenKey);
        return deserializeAccessToken(mongoOAuth2AccessToken.getToken());
    }

    @Override
    public void removeAccessToken(final OAuth2AccessToken token) {
        removeAccessToken(token.getValue());
    }

    @Override
    public void storeRefreshToken(final OAuth2RefreshToken refreshToken,
                                  final OAuth2Authentication oAuth2Authentication) {
        final String tokenKey = extractTokenKey(refreshToken.getValue());
        final byte[] token = serializeRefreshToken(refreshToken);
        final byte[] authentication = serializeAuthentication(oAuth2Authentication);

        final MongoOAuth2RefreshToken oAuth2RefreshToken = new MongoOAuth2RefreshToken(tokenKey, token, authentication);

        mongoOAuth2RefreshTokenRepository.save(oAuth2RefreshToken);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(final String tokenValue) {
        final String tokenKey = extractTokenKey(tokenValue);
        final MongoOAuth2RefreshToken mongoOAuth2RefreshToken = mongoOAuth2RefreshTokenRepository.findByTokenId(tokenKey);
        return deserializeRefreshToken(mongoOAuth2RefreshToken.getToken());
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(final OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    @Override
    public void removeRefreshToken(final OAuth2RefreshToken token) {
        removeRefreshToken(token.getValue());
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(final OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    @Override
    public OAuth2AccessToken getAccessToken(final OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken;

        String key = authenticationKeyGenerator.extractKey(authentication);

        final MongoOAuth2AccessToken oAuth2AccessToken = mongoOAuth2AccessTokenRepository.findByAuthenticationId(key);

        accessToken = deserializeAccessToken(oAuth2AccessToken.getToken());

        if (accessToken != null
                && !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
            removeAccessToken(accessToken.getValue());
            // Keep the store consistent (maybe the same user is represented by this authentication but the details have
            // changed)
            storeAccessToken(accessToken, authentication);
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        final List<MongoOAuth2AccessToken> oAuth2AccessTokens = mongoOAuth2AccessTokenRepository.findByUsernameAndClientId(userName, clientId);
        final Collection<MongoOAuth2AccessToken> noNullsTokens = filter(oAuth2AccessTokens, byNotNulls());
        return transform(noNullsTokens, toOAuth2AccessToken());
    }

    private Predicate<MongoOAuth2AccessToken> byNotNulls() {
        return new Predicate<MongoOAuth2AccessToken>() {
            @Override
            public boolean apply(final MongoOAuth2AccessToken token) {
                return token != null;
            }
        };
    }

    private Function<MongoOAuth2AccessToken, OAuth2AccessToken> toOAuth2AccessToken() {
        return new Function<MongoOAuth2AccessToken, OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken apply(MongoOAuth2AccessToken token) {
                return SerializationUtils.deserialize(token.getToken());
            }
        };
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(final String clientId) {
        final List<MongoOAuth2AccessToken> oAuth2AccessTokens = mongoOAuth2AccessTokenRepository.findByClientId(clientId);
        final Collection<MongoOAuth2AccessToken> noNullTokens = filter(oAuth2AccessTokens, byNotNulls());
        return transform(noNullTokens, toOAuth2AccessToken());
    }

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

    protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return SerializationUtils.deserialize(authentication);
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String value) {
        final String tokenId = extractTokenKey(value);

        final MongoOAuth2RefreshToken mongoOAuth2RefreshToken = mongoOAuth2RefreshTokenRepository.findByTokenId(tokenId);

        return deserializeAuthentication(mongoOAuth2RefreshToken.getAuthentication());
    }

    public void removeRefreshToken(String token) {
        final String tokenId = extractTokenKey(token);
        mongoOAuth2RefreshTokenRepository.deleteByTokenId(tokenId);
    }

    public void removeAccessTokenUsingRefreshToken(final String refreshToken) {
        final String tokenId = extractTokenKey(refreshToken);
        mongoOAuth2AccessTokenRepository.deleteByRefreshTokenId(tokenId);

    }
}
