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
import uk.co.caeldev.springsecuritymongo.builders.MongoApprovalBuilder;
import uk.co.caeldev.springsecuritymongo.config.ApplicationConfiguration;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { ApplicationConfiguration.class })
@ActiveProfiles("test")
@DirtiesContext
public class MongoApprovalRepositoryIntegrationTest {

    @Rule
    public FongoRule fongoRule = new FongoRule();

    @Autowired
    private MongoApprovalRepository mongoApprovalRepository;

    @Test
    public void shouldSaveMongoApproval() {
        //Given
        final String clientId = string().next();
        final String userId = string().next();
        final MongoApproval mongoApproval = MongoApprovalBuilder.mongoApprovalBuilder()
                .clientId(clientId).userId(userId).build();

        //When
        mongoApprovalRepository.save(mongoApproval);

        //Then
        final List<MongoApproval> results = mongoApprovalRepository.findByUserIdAndClientId(userId, clientId);
        assertThat(results).isNotEmpty();
        final MongoApproval approval = results.get(0);
        assertThat(approval).isEqualToComparingFieldByField(approval);
    }
}
