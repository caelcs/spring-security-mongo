package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.oauth2.provider.approval.Approval;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;

import java.time.LocalDateTime;
import java.util.UUID;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.*;

public class MongoApprovalBuilder {

    private String id = UUID.randomUUID().toString();
    private String userId = string().next();
    private String clientId = string().next();
    private String scope = string().next();
    private Approval.ApprovalStatus status = value(Approval.ApprovalStatus.class).next();
    private LocalDateTime expiresAt = localDateTime().next();
    private LocalDateTime lastUpdatedAt = localDateTime().next();

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

    public MongoApprovalBuilder scope(String scope) {
        this.scope = scope;
        return this;
    }
}
