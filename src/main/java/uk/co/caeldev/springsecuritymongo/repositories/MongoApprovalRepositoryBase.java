package uk.co.caeldev.springsecuritymongo.repositories;

import org.joda.time.LocalDate;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;

import java.util.Collection;
import java.util.List;

public interface MongoApprovalRepositoryBase {
    boolean updateOrCreate(Collection<MongoApproval> mongoApprovals);

    boolean updateExpiresAt(LocalDate now, MongoApproval mongoApproval);

    boolean deleteByUserIdAndClientIdAndScope(MongoApproval mongoApproval);

    List<MongoApproval> findByUserIdAndClientId(String userId, String clientId);
}
