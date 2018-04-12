package uk.co.caeldev.springsecuritymongo.commons;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.approval.Approval;
import uk.co.caeldev.springsecuritymongo.builders.MongoApprovalBuilder;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;
import uk.co.caeldev.springsecuritymongo.domain.MongoOAuth2AccessToken;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

import java.io.Serializable;
import java.time.LocalDateTime;

import static uk.co.caeldev.springsecuritymongo.builders.ApprovalBuilder.approvalBuilder;
import static uk.co.caeldev.springsecuritymongo.builders.MongoOAuth2AccessTokenBuilder.mongoOAuth2AccessTokenBuilder;

public class SecurityRDG extends uk.org.fyodor.generators.RDG {

    public static Generator<String> ofEscapedString() {
        return () -> string(30, CharacterSetFilter.LettersAndDigits).next();
    }

    public static Generator<GrantedAuthority> ofGrantedAuthority() {
        return () -> new SimpleGrantedAuthority(string().next());
    }

    public static Generator<GrantedAuthority> ofInvalidAuthority() {
        return () -> new SimpleGrantedAuthority("");
    }

    public static Generator<MongoOAuth2AccessToken> ofMongoOAuth2AccessToken() {
        return () -> mongoOAuth2AccessTokenBuilder().build();
    }

    public static Generator<Object> objectOf(final Generator generator) {
        return () -> generator.next();
    }

    public static Generator<Serializable> serializableOf(final Generator<? extends Serializable> generator) {
        return () -> generator.next();
    }

    public static Generator<Approval> ofApproval() {
        return () -> approvalBuilder().build();
    }

    public static Generator<MongoApproval> ofMongoApproval() {
        return () -> MongoApprovalBuilder.mongoApprovalBuilder().build();
    }

    public static Generator<LocalDateTime> localDateTime() {
        return () -> LocalDateTime.now().minusDays(longVal(30).next());
    }
}
