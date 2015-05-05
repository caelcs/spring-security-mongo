package uk.co.caeldev.springsecuritymongo;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.co.caeldev.springsecuritymongo.domain.User;
import uk.co.caeldev.springsecuritymongo.repositories.UserRepository;
import uk.co.caeldev.springsecuritymongo.services.SecurityContextService;

import java.util.Collection;

@Component
public class MongoUserDetailsManager implements UserDetailsManager {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private SecurityContextService securityContextService;
    
    @Autowired
    public MongoUserDetailsManager(final UserRepository userRepository,
                                   final SecurityContextService securityContextService,
                                   final AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.securityContextService = securityContextService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void createUser(final UserDetails user) {
        validateUserDetails(user);
        userRepository.save(getUser(user));
    }

    private User getUser(UserDetails userDetails) {
        final User user = (User) userDetails;
        return new User(user.getPassword(), user.getUsername(), user.getUserUUID(), Sets.newConcurrentHashSet(user.getAuthorities()), user.isAccountNonExpired(), user.isAccountNonLocked(),user.isCredentialsNonExpired(), user.isEnabled());
    }

    @Override
    public void updateUser(final UserDetails user) {
        validateUserDetails(user);
        userRepository.save(getUser(user));
    }

    @Override
    public void deleteUser(final String username) {
        final User user = userRepository.findOne(username);
        userRepository.delete(user);
    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {
        final Authentication currentUser = securityContextService.getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " +
                    "for current user.");
        }

        final String username = currentUser.getName();

        // If an authentication manager has been set, re-authenticate the user with the supplied password.
        if (authenticationManager != null) {
            logger.debug("Reauthenticating user '"+ username + "' for password change request.");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        logger.debug("Changing password for user '"+ username + "'");

        userRepository.changePassword(oldPassword, newPassword, username);

        securityContextService.setAuthentication(createNewAuthentication(currentUser));
    }

    @Override
    public boolean userExists(final String username) {
        final User user = userRepository.findOne(username);
        return user != null;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findOne(username);
    }

    protected Authentication createNewAuthentication(final Authentication currentAuth) {
        final UserDetails user = loadUserByUsername(currentAuth.getName());

        final UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());

        return newAuthentication;
    }

    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
        validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");

        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }
}
