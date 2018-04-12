package uk.co.caeldev.springsecuritymongo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.springsecuritymongo.builders.MongoClientDetailsBuilder;
import uk.co.caeldev.springsecuritymongo.config.ApplicationConfiguration;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;
import uk.co.caeldev.springsecuritymongo.repositories.MongoClientDetailsRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { ApplicationConfiguration.class })
@ActiveProfiles("test")
@DirtiesContext
public class MongoClientDetailsServiceIntegrationTest {

    @Autowired
    private MongoClientDetailsService mongoClientDetailsService;

    @Autowired
    private MongoClientDetailsRepository mongoClientDetailsRepository;

    @Test
    public void shouldPersistClientDetailsSuccessfully() {
        //Given
        final MongoClientDetails clientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();

        //When
        mongoClientDetailsService.addClientDetails(clientDetails);

        //Then
        final MongoClientDetails expectedClientDetails = mongoClientDetailsRepository.findByClientId(clientDetails.getClientId());
        assertThat(expectedClientDetails).isNotNull();
        assertThat(expectedClientDetails).isEqualTo(clientDetails);
    }

    @Test
    public void shouldLoadClientDetailsByIdSuccessfully() {
        //Given
        final MongoClientDetails clientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();

        //And
        mongoClientDetailsService.addClientDetails(clientDetails);

        //When
        final ClientDetails expectedClientDetails = mongoClientDetailsService.loadClientByClientId(clientDetails.getClientId());

        //Then
        assertThat(expectedClientDetails).isNotNull();
        assertThat(expectedClientDetails).isEqualTo(clientDetails);
    }

    @Test
    public void shouldGetListOfClientDetailsByIdSuccessfully() {
        //Given
        final MongoClientDetails clientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();

        //And
        mongoClientDetailsService.addClientDetails(clientDetails);

        //When
        final List<ClientDetails> expectedClientDetails = mongoClientDetailsService.listClientDetails();

        //Then
        assertThat(expectedClientDetails).contains(clientDetails);
    }
}
