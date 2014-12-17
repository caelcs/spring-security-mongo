package uk.co.caeldev.springsecuritymongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;

@Repository
public interface MongoApprovalRepository extends MongoRepository<MongoApproval, String>, MongoApprovalRepositoryBase {
}
