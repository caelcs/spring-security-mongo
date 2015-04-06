package uk.co.caeldev.springsecuritymongo.builders;

import org.joda.time.LocalDate;
import org.springframework.security.oauth2.provider.approval.Approval;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;

import java.util.UUID;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.*;
import static uk.org.fyodor.jodatime.generators.RDG.localDate;

public class MongoApprovalBuilder {

    private String id = UUID.randomUUID().toString();
    private String userId = string().next();
    private String clientId = string().next();
    private String scope = string().next();
    private Approval.ApprovalStatus status = value(Approval.ApprovalStatus.class).next();
    private LocalDate expiresAt = localDate().next();
    private LocalDate lastUpdatedAt = localDate().next();

    private MongoApprovalBuilder() {
    }

    public static MongoApprovalBuilder mongoApprovalBuilder() {
        return new MongoApprovalBuilder();
    }

    public MongoApproval build() {
        return new MongoApproval(id, userId, clientId, scope, status, expiresAt, lastUpdatedAt);
    }

    public MongoApprovalBuilder clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public MongoApprovalBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }
}
