package uk.co.caeldev.springsecuritymongo.services;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

public class SecurityContextServiceTest {

    private SecurityContextService securityContextService;

    @Before
    public void setUp() {
        securityContextService = new SecurityContextService();
    }

    @Test
    public void shouldSetAuthentication() {
        //Given
        final TestingAuthenticationToken authentication = new TestingAuthenticationToken(string().next(), new Object());

        //When
        securityContextService.setAuthentication(authentication);

        //Then
        assertThat(securityContextService.getAuthentication()).isEqualTo(authentication);
    }

}