package uk.co.caeldev.springsecuritymongo.repositories;

import com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.co.caeldev.springsecuritymongo.builders.MongoApprovalBuilder;
import uk.co.caeldev.springsecuritymongo.config.ApplicationConfiguration;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;

import java.util.List;

import static com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb.InMemoryMongoRuleBuilder.newInMemoryMongoDbRule;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
public class MongoApprovalRepositoryIntegrationTest {

    @ClassRule
    public static InMemoryMongoDb inMemoryMongoDb = newInMemoryMongoDbRule().build();

    @Autowired
    private MongoApprovalRepository mongoApprovalRepository;

    @Test
    public void shouldSaveMongoApproval() {
        //Given
        final String clientId = string().next();
        final String userId = string().next();
        final MongoApproval mongoApproval = MongoApprovalBuilder.mongoApprovalBuilder().clientId(clientId).userId(userId).build();

        //When
        mongoApprovalRepository.save(mongoApproval);

        //Then
        final List<MongoApproval> byUserIdAndClientId = mongoApprovalRepository.findByUserIdAndClientId(userId, clientId);
        assertThat(byUserIdAndClientId).isNotEmpty();
    }
}
