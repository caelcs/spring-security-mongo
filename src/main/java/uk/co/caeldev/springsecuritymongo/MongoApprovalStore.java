package uk.co.caeldev.springsecuritymongo;

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
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
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
        final Collection<MongoApproval> mongoApprovals = transformToMongoApproval(approvals);

        return mongoApprovalRepository.updateOrCreate(mongoApprovals);
    }

    @Override
    public boolean revokeApprovals(final Collection<Approval> approvals) {
        boolean success = true;

        final Collection<MongoApproval> mongoApprovals = transformToMongoApproval(approvals);

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
        return transformToApprovals(mongoApprovals);
    }

    private List<Approval> transformToApprovals(final List<MongoApproval> mongoApprovals) {
        return mongoApprovals.stream().map(mongoApproval -> new Approval(mongoApproval.getUserId(),
                mongoApproval.getClientId(),
                mongoApproval.getScope(),
                Date.from(mongoApproval.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()),
                mongoApproval.getStatus(),
                Date.from(mongoApproval.getLastUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())))
                .collect(Collectors.toList());
    }

    private List<MongoApproval> transformToMongoApproval(final Collection<Approval> approvals) {
        return approvals.stream().map(approval -> new MongoApproval(UUID.randomUUID().toString(),
                approval.getUserId(),
                approval.getClientId(),
                approval.getScope(),
                isNull(approval.getStatus()) ? Approval.ApprovalStatus.APPROVED: approval.getStatus(),
                convertTolocalDateTimeFrom(approval.getExpiresAt()),
                convertTolocalDateTimeFrom(approval.getLastUpdatedAt()))).collect(Collectors.toList());
    }

    public void setHandleRevocationsAsExpiry(boolean handleRevocationsAsExpiry) {
        this.handleRevocationsAsExpiry = handleRevocationsAsExpiry;
    }
}
