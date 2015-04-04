package uk.co.caeldev.springsecuritymongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import uk.co.caeldev.springsecuritymongo.domain.User;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryBase {

    @Query(value = "{ 'username' : ?0 }")
    User findByUsername(String username);
}
