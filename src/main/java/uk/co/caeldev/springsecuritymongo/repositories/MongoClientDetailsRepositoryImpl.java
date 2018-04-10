package uk.co.caeldev.springsecuritymongo.repositories;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;

@Component
public class MongoClientDetailsRepositoryImpl implements MongoClientDetailsRepositoryBase {

    public static final String ID = "_id";
    public static final String CLIENT_SECRET = "clientSecret";
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoClientDetailsRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean deleteByClientId(String clientId) {
        final Query query = Query.query(Criteria.where(ID).is(clientId));
        final DeleteResult deleteResult = mongoTemplate.remove(query, MongoClientDetails.class);
        return deleteResult.wasAcknowledged();
    }

    @Override
    public boolean update(final MongoClientDetails mongoClientDetails) {
        final Query query = Query.query(Criteria.where(ID).is(mongoClientDetails.getClientId()));

        final Update update = Update.update("scope", mongoClientDetails.getScope())
                .set("resourceIds", mongoClientDetails.getResourceIds())
                .set("authorizedGrantTypes", mongoClientDetails.getAuthorizedGrantTypes())
                .set("authorities", mongoClientDetails.getAuthorities())
                .set("accessTokenValiditySeconds", mongoClientDetails.getAccessTokenValiditySeconds())
                .set("refreshTokenValiditySeconds", mongoClientDetails.getRefreshTokenValiditySeconds())
                .set("additionalInformation", mongoClientDetails.getAdditionalInformation())
                .set("autoApproveScopes", mongoClientDetails.getAutoApproveScopes())
                .set("registeredRedirectUris", mongoClientDetails.getRegisteredRedirectUri());

        final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MongoClientDetails.class);

        return updateResult.wasAcknowledged();
    }

    @Override
    public boolean updateClientSecret(final String clientId,
                                      final String newSecret) {
        final Query query = Query.query(Criteria.where(ID).is(clientId));

        final Update update = Update.update(CLIENT_SECRET, newSecret);

        final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MongoClientDetails.class);

        return updateResult.wasAcknowledged();
    }

    @Override
    public MongoClientDetails findByClientId(final String clientId) throws IllegalArgumentException {
        final Query query = Query.query(Criteria.where(ID).is(clientId));
        final MongoClientDetails mongoClientDetails = mongoTemplate.findOne(query, MongoClientDetails.class);
        if (mongoClientDetails == null) {
            throw new IllegalArgumentException("No valid client id");
        }
        return mongoClientDetails;
    }


}
