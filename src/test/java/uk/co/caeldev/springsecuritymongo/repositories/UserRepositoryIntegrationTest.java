package uk.co.caeldev.springsecuritymongo.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.springsecuritymongo.builders.UserBuilder;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

public class UserRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() {
        //Given
        final User user = UserBuilder.userBuilder().build();

        //When
        final User result = userRepository.save(user);

        //Then
        Optional<User> expectedUser = userRepository.findByUsername(user.getUsername());
        assertThat(result).isEqualTo(expectedUser.get());
    }

    @Test
    public void shouldChangePasswordUser() {
        //Given
        final User user = UserBuilder.userBuilder().build();
        userRepository.save(user);

        //When
        final boolean result = userRepository.changePassword(user.getPassword(), string().next(), user.getUsername());

        //Then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldFindUserByUsername() {
        //Given
        final User user = UserBuilder.userBuilder().build();
        final User savedUser = userRepository.save(user);

        //When
        final Optional<User> userFound = userRepository.findByUsername(user.getUsername());

        //Then
        assertThat(userFound.isPresent()).isTrue();
        assertThat(userFound.get()).isEqualTo(savedUser);
    }

    @Test
    public void shouldDeleteUserByUsername() {
        //Given
        final User user = UserBuilder.userBuilder().build();
        final User savedUser = userRepository.save(user);

        //When
        userRepository.deleteByUsername(savedUser.getUsername());

        //Then
        final List<User> all = userRepository.findAll();
        assertThat(all).isEmpty();
    }
}
