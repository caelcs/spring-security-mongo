package uk.co.caeldev.springsecuritymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Document
public class User implements UserDetails, CredentialsContainer {

    @Id
    private final String id;
    private final String uuid;
    private String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public User(final String id,
                final String uuid,
                final String password,
                final String username,
                final Set<GrantedAuthority> authorities,
                final boolean accountNonExpired,
                final boolean accountNonLocked,
                final boolean credentialsNonExpired,
                final boolean enabled) {
        this.id = id;
        this.uuid = uuid;
        this.password = password;
        this.username = username;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, password, username, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.password, other.password) && Objects.equals(this.username, other.username) && Objects.equals(this.authorities, other.authorities) && Objects.equals(this.accountNonExpired, other.accountNonExpired) && Objects.equals(this.accountNonLocked, other.accountNonLocked) && Objects.equals(this.credentialsNonExpired, other.credentialsNonExpired) && Objects.equals(this.enabled, other.enabled);
    }

    @Override
    public String toString() {
        return "User{" +
                "enabled=" + enabled +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", accountNonExpired=" + accountNonExpired +
                ", authorities=" + authorities +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uuid='" + uuid + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
