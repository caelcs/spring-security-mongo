package uk.co.caeldev.springsecuritymongo;

import com.google.common.base.Function;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Component;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;
import uk.co.caeldev.springsecuritymongo.repositories.MongoApprovalRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Collections2.transform;
import static uk.co.caeldev.springsecuritymongo.util.LocalDateTimeUtil.convertTolocalDateTimeFrom;

@Component
public class MongoApprovalStore implements ApprovalStore {

    private final MongoApprovalRepository mongoApprovalRepository;

    private boolean handleRevocationsAsExpiry = false;

    public MongoApprovalStore(final MongoApprovalRepository mongoApprovalRepository) {
        this.mongoApprovalRepository = mongoApprovalRepository;
    }

    @Override
    public boolean addApprovals(final Collection<Approval> approvals) {
        final Collection<MongoApproval> mongoApprovals = transform(approvals, toMongoApproval());

        return mongoApprovalRepository.updateOrCreate(mongoApprovals);
    }

    @Override
    public boolean revokeApprovals(final Collection<Approval> approvals) {
        boolean success = true;

        final Collection<MongoApproval> mongoApprovals = transform(approvals, toMongoApproval());

        for (final MongoApproval mongoApproval : mongoApprovals) {
            if (handleRevocationsAsExpiry) {
                final boolean updateResult = mongoApprovalRepository.updateExpiresAt(LocalDateTime.now(), mongoApproval);
                if (!updateResult) {
                    success = false;
                }

            }
            else {
                final boolean deleteResult = mongoApprovalRepository.deleteByUserIdAndClientIdAndScope(mongoApproval);

                if (!deleteResult) {
                    success = false;
                }
            }
        }
        return success;
    }

    @Override
    public Collection<Approval> getApprovals(final String userId,
                                             final String clientId) {
        final List<MongoApproval> mongoApprovals = mongoApprovalRepository.findByUserIdAndClientId(userId, clientId);
        return transform(mongoApprovals, toApproval());
    }

    private Function<Approval, MongoApproval> toMongoApproval() {
        return approval -> new MongoApproval(UUID.randomUUID().toString(),
                approval.getUserId(),
                approval.getClientId(),
                approval.getScope(),
                approval.getStatus() == null ? Approval.ApprovalStatus.APPROVED: approval.getStatus(),
                convertTolocalDateTimeFrom(approval.getExpiresAt()),
                convertTolocalDateTimeFrom(approval.getLastUpdatedAt()));
    }

    private Function<MongoApproval, Approval> toApproval() {
        return mongoApproval -> new Approval(mongoApproval.getUserId(),
                mongoApproval.getClientId(),
                mongoApproval.getScope(),
                Date.from(mongoApproval.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()),
                mongoApproval.getStatus(),
                Date.from(mongoApproval.getLastUpdatedAt().atZone(ZoneId.systemDefault()).toInstant()));
    }

    public void setHandleRevocationsAsExpiry(boolean handleRevocationsAsExpiry) {
        this.handleRevocationsAsExpiry = handleRevocationsAsExpiry;
    }
}
