package uk.co.caeldev.springsecuritymongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2ProtectedResourceDetails;

@Repository
public interface MongoOAuth2ProtectedResourceDetailsRepository extends MongoRepository<MongoOAuth2ProtectedResourceDetails, String>, MongoOAuth2ProtectedResourceDetailsRepositoryBase {
}
