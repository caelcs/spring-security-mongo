package uk.co.caeldev.springsecuritymongo.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2RefreshToken;

@Repository
public interface MongoOAuth2RefreshTokenRepository extends MongoRepository<MongoOAuth2RefreshToken, String>, MongoOAuth2RefreshTokenRepositoryBase {
}
