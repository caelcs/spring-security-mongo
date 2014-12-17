package uk.co.caeldev.springsecuritymongo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import uk.co.caeldev.springsecuritymongo.commons.SecurityRDG;
import uk.co.caeldev.springsecuritymongo.builders.UserBuilder;
import uk.co.caeldev.springsecuritymongo.domain.User;
import uk.co.caeldev.springsecuritymongo.repositories.UserRepository;
import uk.co.caeldev.springsecuritymongo.services.SecurityContextService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.string;

@RunWith(MockitoJUnitRunner.class)
public class MongoUserDetailsManagerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SecurityContextService securityContextService;

    private UserDetailsManager mongoUserDetailsManager;

    @Before
    public void setup() {
        mongoUserDetailsManager = new MongoUserDetailsManager(userRepository, authenticationManager, securityContextService);
    }

    @Test
    public void shouldCreateUser() throws Exception {
        // Given
        final User user = UserBuilder.userBuilder().build();

        // When
        mongoUserDetailsManager.createUser(user);

        // Then
        verify(userRepository).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateUserWhenUsernameIsEmpty() throws Exception {
        // Given
        final User user = UserBuilder.userBuilder().username("").build();

        // When
        mongoUserDetailsManager.createUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateUserWhenUsernameIsValidAndNoAuthorities() throws Exception {
        // Given
        final User user = UserBuilder.userBuilder().authorities(null).build();

        // When
        mongoUserDetailsManager.createUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateUserWhenUsernameIsValidAndAuthoritiesNotValid() throws Exception {
        // Given
        final User user = UserBuilder.userBuilder().authorities(SecurityRDG.set(SecurityRDG.ofInvalidAuthority()).next()).build();

        // When
        mongoUserDetailsManager.createUser(user);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        // Given
        final String username = string().next();
        final User user = UserBuilder.userBuilder().username(username).build();

        // And
        given(userRepository.findByUsername(username)).willReturn(user);

        // When
        mongoUserDetailsManager.deleteUser(username);

        // Then
        verify(userRepository).delete(user);
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        // Given
        final User user = UserBuilder.userBuilder().build();

        // When
        mongoUserDetailsManager.updateUser(user);

        // Then
        verify(userRepository).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotUpdateUserWhenUsernameIsInvalid() throws Exception {
        // Given
        final User user = UserBuilder.userBuilder().username("").build();

        // When
        mongoUserDetailsManager.updateUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotUpdateUserWhenUsernameIsValidAndNoAuthorities() throws Exception {
        // Given
        final User user = UserBuilder.userBuilder().authorities(null).build();

        // When
        mongoUserDetailsManager.updateUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotUpdateUserWhenUsernameIsValidAndAuthoritiesNotValid() throws Exception {
        // Given
        final User user = UserBuilder.userBuilder().authorities(SecurityRDG.set(SecurityRDG.ofInvalidAuthority()).next()).build();

        // When
        mongoUserDetailsManager.updateUser(user);
    }

    @Test
    public void shouldReturnTrueWhenUserExists() throws Exception {
        // Given
        final String username = string().next();
        final User user = UserBuilder.userBuilder().username(username).build();

        // And
        given(userRepository.findByUsername(username)).willReturn(user);

        // When
        final boolean userExists = mongoUserDetailsManager.userExists(username);

        // Then
        assertThat(userExists).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenUserDoesNotExists() throws Exception {
        // Given
        final String username = string().next();

        // And
        given(userRepository.findByUsername(username)).willReturn(null);

        // When
        final boolean userExists = mongoUserDetailsManager.userExists(username);

        // Then
        assertThat(userExists).isFalse();
    }

    @Test
    public void shouldLoadUserByUsernameWhenUserExists() throws Exception {
        // Given
        final String username = string().next();

        // And
        given(userRepository.findByUsername(username)).willReturn(UserBuilder.userBuilder().username(username).build());

        // When
        final UserDetails user = mongoUserDetailsManager.loadUserByUsername(username);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    public void shouldChangePasswordForExitingUser() throws Exception {
        // Given
        final String username = string().next();
        final String currentPassword = string().next();
        final User user = UserBuilder.userBuilder().username(username).password(currentPassword).build();

        // And
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, currentPassword);
        given(securityContextService.getAuthentication()).willReturn(authenticationToken);

        // And
        given(userRepository.findByUsername(username)).willReturn(user);

        // When
        final String newPassword = string().next();
        mongoUserDetailsManager.changePassword(currentPassword, newPassword);

        // Then
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(securityContextService).setAuthentication(any(Authentication.class));
        verify(userRepository).changePassword(currentPassword, newPassword, username);
    }

}