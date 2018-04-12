package uk.co.caeldev.springsecuritymongo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import uk.co.caeldev.springsecuritymongo.builders.UserBuilder;
import uk.co.caeldev.springsecuritymongo.commons.SecurityRDG;
import uk.co.caeldev.springsecuritymongo.domain.User;
import uk.co.caeldev.springsecuritymongo.repositories.UserRepository;
import uk.co.caeldev.springsecuritymongo.services.SecurityContextService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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

    private MongoUserDetailsManager mongoUserDetailsManager;

    @Before
    public void setup() {
        mongoUserDetailsManager = new MongoUserDetailsManager(userRepository, securityContextService, authenticationManager);
    }

    @Test
    public void shouldCreateUser() {
        // Given
        final User user = UserBuilder.userBuilder().build();

        // When
        mongoUserDetailsManager.createUser(user);

        // Then
        verify(userRepository).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateUserWhenUsernameIsEmpty() {
        // Given
        final User user = UserBuilder.userBuilder().username("").build();

        // When
        mongoUserDetailsManager.createUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateUserWhenUsernameIsValidAndNoAuthorities() {
        // Given
        final User user = UserBuilder.userBuilder().authorities(null).build();

        // When
        mongoUserDetailsManager.createUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateUserWhenUsernameIsValidAndAuthoritiesNotValid() {
        // Given
        final User user = UserBuilder.userBuilder().authorities(SecurityRDG.set(SecurityRDG.ofInvalidAuthority()).next()).build();

        // When
        mongoUserDetailsManager.createUser(user);
    }

    @Test
    public void shouldDeleteUser() {
        // Given
        final String username = string().next();

        // When
        mongoUserDetailsManager.deleteUser(username);

        //Then
        verify(userRepository).deleteByUsername(username);
    }

    @Test
    public void shouldUpdateUser() {
        // Given
        final User user = UserBuilder.userBuilder().build();

        // When
        mongoUserDetailsManager.updateUser(user);

        // Then
        verify(userRepository).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotUpdateUserWhenUsernameIsInvalid() {
        // Given
        final User user = UserBuilder.userBuilder().username("").build();

        // When
        mongoUserDetailsManager.updateUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotUpdateUserWhenUsernameIsValidAndNoAuthorities() {
        // Given
        final User user = UserBuilder.userBuilder().authorities(null).build();

        // When
        mongoUserDetailsManager.updateUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotUpdateUserWhenUsernameIsValidAndAuthoritiesNotValid() {
        // Given
        final User user = UserBuilder.userBuilder().authorities(SecurityRDG.set(SecurityRDG.ofInvalidAuthority()).next()).build();

        // When
        mongoUserDetailsManager.updateUser(user);
    }

    @Test
    public void shouldReturnTrueWhenUserExists() {
        // Given
        final String username = string().next();
        final User user = UserBuilder.userBuilder().username(username).build();

        // And
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        // When
        final boolean userExists = mongoUserDetailsManager.userExists(username);

        // Then
        assertThat(userExists).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenUserDoesNotExists() {
        // Given
        final String username = string().next();

        // And
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        // When
        final boolean userExists = mongoUserDetailsManager.userExists(username);

        // Then
        assertThat(userExists).isFalse();
    }

    @Test
    public void shouldLoadUserByUsernameWhenUserExists() {
        // Given
        final String username = string().next();

        // And
        given(userRepository.findByUsername(username)).willReturn(Optional.of(UserBuilder.userBuilder().username(username).build()));

        // When
        final UserDetails user = mongoUserDetailsManager.loadUserByUsername(username);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    public void shouldChangePasswordForExitingUser() {
        // Given
        final String username = string().next();
        final String currentPassword = string().next();
        final User user = UserBuilder.userBuilder().username(username).password(currentPassword).build();

        // And
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, currentPassword);
        given(securityContextService.getAuthentication()).willReturn(authenticationToken);

        // And
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        // When
        final String newPassword = string().next();
        mongoUserDetailsManager.changePassword(currentPassword, newPassword);

        // Then
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(securityContextService).setAuthentication(any(Authentication.class));
        verify(userRepository).changePassword(currentPassword, newPassword, username);
    }

}