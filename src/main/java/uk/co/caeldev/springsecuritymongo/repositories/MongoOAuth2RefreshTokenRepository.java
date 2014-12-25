package uk.co.caeldev.springsecuritymongo.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2RefreshToken;

public interface MongoOAuth2RefreshTokenRepository extends MongoRepository<MongoOAuth2RefreshToken, String>, MongoOAuth2RefreshTokenRepositoryBase {
}
