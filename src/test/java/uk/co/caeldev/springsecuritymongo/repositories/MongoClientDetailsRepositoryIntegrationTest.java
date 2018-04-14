package uk.co.caeldev.springsecuritymongo.repositories;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.springsecuritymongo.builders.MongoClientDetailsBuilder;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

public class MongoClientDetailsRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private MongoClientDetailsRepository mongoClientDetailsRepository;

    @Test
    public void shouldSaveMongoClientDetails() {
        //Given
        final MongoClientDetails mongoClientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();

        //When
        final MongoClientDetails saved = mongoClientDetailsRepository.save(mongoClientDetails);

        //Then
        assertThat(saved).isEqualTo(mongoClientDetails);
    }

    @Test
    public void shouldDeleteMongoClientDetailsByClientId() {
        //Given
        final MongoClientDetails mongoClientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();
        final MongoClientDetails saved = mongoClientDetailsRepository.save(mongoClientDetails);

        //When
        final boolean result = mongoClientDetailsRepository.deleteByClientId(saved.getClientId());

        //Then
        assertThat(result).isTrue();
        final List<MongoClientDetails> all = mongoClientDetailsRepository.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    public void shouldUpdateClientSecret() {
        //Given
        final MongoClientDetails mongoClientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();
        final MongoClientDetails saved = mongoClientDetailsRepository.save(mongoClientDetails);
        final String newSecret = string().next();

        //When
        final boolean result = mongoClientDetailsRepository.updateClientSecret(saved.getClientId(), newSecret);

        //Then
        assertThat(result).isTrue();
        final List<MongoClientDetails> all = mongoClientDetailsRepository.findAll();
        assertThat(all).hasSize(1);
        final MongoClientDetails found = all.get(0);
        assertThat(found.getClientSecret()).isEqualTo(newSecret);
    }

    @Test
    public void shouldFindMongoClientDetailsByClientId() {
        //Given
        final MongoClientDetails mongoClientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();
        final MongoClientDetails saved = mongoClientDetailsRepository.save(mongoClientDetails);

        //When
        final MongoClientDetails found = mongoClientDetailsRepository.findByClientId(saved.getClientId());

        //Then
        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(saved);
    }

    @Test
    public void shouldUpdateFindMongoClientDetailsByClientId() {
        //Given
        final MongoClientDetails mongoClientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();
        final MongoClientDetails saved = mongoClientDetailsRepository.save(mongoClientDetails);
        saved.setAutoApproveScopes(Sets.newHashSet("ADMIN"));

        //When
        final boolean result = mongoClientDetailsRepository.update(saved);

        //Then
        assertThat(result).isTrue();
    }
}