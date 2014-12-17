package uk.co.caeldev.springsecuritymongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2AccessToken;

@Repository
public interface MongoOAuth2AccessTokenRepository extends MongoRepository<MongoOAuth2AccessToken, String>, MongoOAuth2AccessTokenRepositoryBase {

}
