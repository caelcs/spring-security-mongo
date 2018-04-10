package uk.co.caeldev.springsecuritymongo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import uk.co.caeldev.springsecuritymongo.builders.ClientDetailsBuilder;
import uk.co.caeldev.springsecuritymongo.builders.MongoClientDetailsBuilder;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;
import uk.co.caeldev.springsecuritymongo.repositories.MongoClientDetailsRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.string;

@RunWith(MockitoJUnitRunner.class)
public class MongoClientDetailsServiceTest {

    @Mock
    private MongoClientDetailsRepository mongoClientDetailsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MongoClientDetailsService mongoClientDetailsService;

    @Before
    public void setup() {
        mongoClientDetailsService = new MongoClientDetailsService(mongoClientDetailsRepository, passwordEncoder);
    }

    @Test
    public void shouldAddClientDetails() {
        //Given
        final ClientDetails clientDetails = ClientDetailsBuilder.clientDetailsBuilder().build();

        //When
        mongoClientDetailsService.addClientDetails(clientDetails);

        //Then
        verify(mongoClientDetailsRepository).save(any(MongoClientDetails.class));
        verify(passwordEncoder).encode(clientDetails.getClientSecret());
    }

    @Test
    public void shouldRemoveClientDetailsWithValidClientId() throws Exception {
        //Given
        final String clientId = string().next();

        //And
        given(mongoClientDetailsRepository.deleteByClientId(clientId)).willReturn(true);

        //When
        mongoClientDetailsService.removeClientDetails(clientId);
    }

    @Test(expected = NoSuchClientException.class)
    public void shouldThrowsExceptionWhenTryToRemoveClientDetailsWithInvalidClientId() throws Exception {
        //Given
        final String clientId = string().next();

        //And
        given(mongoClientDetailsRepository.deleteByClientId(clientId)).willReturn(false);

        //When
        mongoClientDetailsService.removeClientDetails(clientId);
    }

    @Test
    public void shouldUpdateClientDetails() throws NoSuchClientException {
        //Given
        final ClientDetails clientDetails = ClientDetailsBuilder.clientDetailsBuilder().build();

        //And
        given(mongoClientDetailsRepository.update(any(MongoClientDetails.class))).willReturn(true);

        //When
        mongoClientDetailsService.updateClientDetails(clientDetails);
    }

    @Test(expected = NoSuchClientException.class)
    public void shouldNotUpdateClientDetailsWhenClientIdIsNotValid() throws NoSuchClientException {
        //Given
        final ClientDetails clientDetails = ClientDetailsBuilder.clientDetailsBuilder().build();

        //And
        given(mongoClientDetailsRepository.update(any(MongoClientDetails.class))).willReturn(false);

        //When
        mongoClientDetailsService.updateClientDetails(clientDetails);
    }

    @Test
    public void shouldUpdateClientSecret() throws NoSuchClientException {
        //Given
        final String clientId = string().next();
        final String secret = string().next();

        //And
        final String expectedNewSecret = string().next();
        given(passwordEncoder.encode(secret)).willReturn(expectedNewSecret);

        //And
        given(mongoClientDetailsRepository.updateClientSecret(clientId, expectedNewSecret)).willReturn(true);

        //When
        mongoClientDetailsService.updateClientSecret(clientId, secret);
    }

    @Test(expected = NoSuchClientException.class)
    public void shouldNotUpdateClientSecretWhenClientIdIsInvalid() throws NoSuchClientException {
        //Given
        final String clientId = string().next();
        final String secret = string().next();

        //And
        final String expectedNewSecret = string().next();
        given(passwordEncoder.encode(secret)).willReturn(expectedNewSecret);

        //And
        given(mongoClientDetailsRepository.updateClientSecret(clientId, expectedNewSecret)).willReturn(false);

        //When
        mongoClientDetailsService.updateClientSecret(clientId, secret);
    }

    @Test
    public void shouldLoadClientByClientId() throws NoSuchClientException {
        //Given
        final String clientId = string().next();

        final MongoClientDetails expectedClientDetails = MongoClientDetailsBuilder.mongoClientDetailsBuilder().build();
        given(mongoClientDetailsRepository.findByClientId(clientId)).willReturn(expectedClientDetails);

        //When
        final ClientDetails clientDetails = mongoClientDetailsService.loadClientByClientId(clientId);

        //Then
        assertThat(clientDetails.getClientId()).isEqualTo(expectedClientDetails.getClientId());
    }

}
