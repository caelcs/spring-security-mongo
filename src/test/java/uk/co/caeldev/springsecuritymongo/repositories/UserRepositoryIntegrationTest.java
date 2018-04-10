package uk.co.caeldev.springsecuritymongo.repositories;

import com.github.fakemongo.junit.FongoRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.springsecuritymongo.builders.UserBuilder;
import uk.co.caeldev.springsecuritymongo.config.ApplicationConfiguration;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { ApplicationConfiguration.class })
@ActiveProfiles("test")
@DirtiesContext
public class UserRepositoryIntegrationTest {

    @Rule
    public FongoRule fongoRule = new FongoRule();

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() {
        //Given
        User user = UserBuilder.userBuilder().build();

        //When
        User result = userRepository.save(user);

        //Then
        Optional<User> expectedUser = userRepository.findByUsername(user.getUsername());
        assertThat(result).isEqualTo(expectedUser.get());
    }

    @Test
    public void shouldChangePasswordUser() {
        //Given
        User user = UserBuilder.userBuilder().build();
        userRepository.save(user);

        //When
        final boolean result = userRepository.changePassword(user.getPassword(), string().next(), user.getUsername());

        //Then
        assertThat(result).isTrue();
    }
}
