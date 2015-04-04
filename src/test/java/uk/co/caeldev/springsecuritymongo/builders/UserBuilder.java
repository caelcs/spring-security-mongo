package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.core.GrantedAuthority;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.Set;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.*;

public final class UserBuilder {

    private String password = string().next();
    private String username = string().next();
    private Set<GrantedAuthority> authorities = set(ofGrantedAuthority()).next();
    private boolean accountNonExpired = bool().next();
    private boolean accountNonLocked = bool().next();
    private boolean credentialsNonExpired = bool().next();
    private boolean enabled = bool().next();

    private UserBuilder() {
    }

    public static UserBuilder userBuilder() {
        return new UserBuilder();
    }

    public User build() {
        return new User(
                password,
                username,
                authorities,
                accountNonExpired,
                accountNonLocked,
                credentialsNonExpired,
                enabled);
    }

    public UserBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public UserBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public UserBuilder authorities(final Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }
}
