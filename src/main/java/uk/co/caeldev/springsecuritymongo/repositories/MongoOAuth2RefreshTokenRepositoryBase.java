package uk.co.caeldev.springsecuritymongo.repositories;

import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2RefreshToken;

public interface MongoOAuth2RefreshTokenRepositoryBase {
    MongoOAuth2RefreshToken findByTokenId(String tokenId);

    boolean deleteByTokenId(String tokenId);
}
