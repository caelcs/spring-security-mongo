package uk.co.caeldev.springsecuritymongo.repositories;

import com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.co.caeldev.springsecuritymongo.builders.UserBuilder;
import uk.co.caeldev.springsecuritymongo.config.ApplicationConfiguration;
import uk.co.caeldev.springsecuritymongo.domain.User;

import static com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb.InMemoryMongoRuleBuilder.newInMemoryMongoDbRule;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
public class UserRepositoryIntegrationTest {

    @ClassRule
    public static InMemoryMongoDb inMemoryMongoDb = newInMemoryMongoDbRule().build();

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() {
        //Given
        User user = UserBuilder.userBuilder().build();

        //When
        User result = userRepository.save(user);

        //Then
        User expectedUser = userRepository.findByUsername(user.getUsername());
        assertThat(result).isEqualTo(expectedUser);
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
