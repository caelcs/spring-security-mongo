package uk.co.caeldev.springsecuritymongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;

@Repository
public interface MongoClientDetailsRepository extends MongoRepository<MongoClientDetails, String>, MongoClientDetailsRepositoryBase {
}
