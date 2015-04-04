package uk.co.caeldev.springsecuritymongo.domain;

import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;

import java.util.Objects;

@Document
public class MongoApproval {

    @Id
    private String id;
    private String userId;
    private String clientId;
    private String scope;
    private ApprovalStatus status;
    private LocalDate expiresAt;
    private LocalDate lastUpdatedAt;

    @PersistenceConstructor
    public MongoApproval(final String id,
                         final String userId,
                         final String clientId,
                         final String scope,
                         final ApprovalStatus status,
                         final LocalDate expiresAt,
                         final LocalDate lastUpdatedAt) {
        this.id = id;
        this.userId = userId;
        this.clientId = clientId;
        this.scope = scope;
        this.status = status;
        this.expiresAt = expiresAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getScope() {
        return scope;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public LocalDate getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clientId, scope, status, expiresAt, lastUpdatedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MongoApproval other = (MongoApproval) obj;
        return Objects.equals(this.userId, other.userId)
                && Objects.equals(this.clientId, other.clientId)
                && Objects.equals(this.scope, other.scope)
                && Objects.equals(this.status, other.status)
                && Objects.equals(this.expiresAt, other.expiresAt)
                && Objects.equals(this.lastUpdatedAt, other.lastUpdatedAt);
    }

    @Override
    public String toString() {
        return "MongoApproval{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", scope='" + scope + '\'' +
                ", status=" + status +
                ", expiresAt=" + expiresAt +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}
