package uk.co.caeldev.springsecuritymongo.repositories;

import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2ClientToken;

public interface MongoOAuth2ClientTokenRepositoryBase {
    boolean deleteByAuthenticationId(String authenticationId);

    MongoOAuth2ClientToken findByAuthenticationId(String authenticationId);
}
