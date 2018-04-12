package uk.co.caeldev.springsecuritymongo.repositories;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class MongoApprovalRepositoryImpl implements MongoApprovalRepositoryBase {

    private final MongoTemplate mongoTemplate;

    public MongoApprovalRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean updateOrCreate(final Collection<MongoApproval> mongoApprovals) {
        boolean result = true;
        for (MongoApproval mongoApproval : mongoApprovals) {
            final Update update = Update.update("expiresAt", mongoApproval.getExpiresAt())
                    .set("status", mongoApproval.getStatus())
                    .set("lastUpdatedAt", mongoApproval.getLastUpdatedAt());

            final UpdateResult upsert = mongoTemplate.upsert(byUserIdAndClientIdAndScope(mongoApproval), update, MongoApproval.class);

            if (!upsert.wasAcknowledged()) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean updateExpiresAt(final LocalDateTime expiresAt,
                                   final MongoApproval mongoApproval) {
        final Update update = Update.update("expiresAt", expiresAt);

        final UpdateResult updateResult = mongoTemplate.updateFirst(byUserIdAndClientIdAndScope(mongoApproval),
                update,
                MongoApproval.class);

        return updateResult.wasAcknowledged();
    }

    @Override
    public boolean deleteByUserIdAndClientIdAndScope(final MongoApproval mongoApproval) {
        final DeleteResult deleteResult = mongoTemplate.remove(byUserIdAndClientIdAndScope(mongoApproval),
                MongoApproval.class);

        return deleteResult.wasAcknowledged();
    }

    @Override
    public List<MongoApproval> findByUserIdAndClientId(final String userId,
                                                       final String clientId) {
        final Query query = Query.query(where("userId").is(userId)
                .andOperator(where("clientId").is(clientId)));
        return mongoTemplate.find(query, MongoApproval.class);
    }

    private Query byUserIdAndClientIdAndScope(final MongoApproval mongoApproval) {
        return Query.query(where("userId").is(mongoApproval.getUserId())
                    .andOperator(where("clientId").is(mongoApproval.getClientId())
                            .andOperator(where("scope").is(mongoApproval.getScope()))));
    }
}
