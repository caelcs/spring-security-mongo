package uk.co.caeldev.springsecuritymongo.repositories;

import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;

public interface MongoClientDetailsRepositoryBase {
    boolean deleteByClientId(String clientId);

    boolean update(MongoClientDetails mongoClientDetails);

    boolean updateClientSecret(String clientId, String newSecret);

    MongoClientDetails findByClientId(String clientId) throws IllegalArgumentException;
}
