package uk.co.caeldev.springsecuritymongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryBase {

    void deleteByUsername(String username);

    Optional<User> findByUsername(String username);

}
