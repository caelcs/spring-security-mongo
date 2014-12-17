package uk.co.caeldev.springsecuritymongo.repositories;

import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails;

@Component
public class MongoClientDetailsRepositoryImpl implements MongoClientDetailsRepositoryBase {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoClientDetailsRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean deleteByClientId(String clientId) {
        final Query query = Query.query(Criteria.where("clientId").is(clientId));
        final WriteResult writeResult = mongoTemplate.remove(query, MongoClientDetails.class);
        return writeResult.getN() == 1;
    }

    @Override
    public boolean update(final MongoClientDetails mongoClientDetails) {
        final Query query = Query.query(Criteria.where("clientId").is(mongoClientDetails.getClientId()));

        final Update update = Update.update("scope", mongoClientDetails.getScope())
                .addToSet("resourceIds", mongoClientDetails.getResourceIds())
                .addToSet("authorizedGrantTypes", mongoClientDetails.getAuthorizedGrantTypes())
                .addToSet("authorities", mongoClientDetails.getAuthorities())
                .addToSet("accessTokenValiditySeconds", mongoClientDetails.getAccessTokenValiditySeconds())
                .addToSet("refreshTokenValiditySeconds", mongoClientDetails.getRefreshTokenValiditySeconds())
                .addToSet("additionalInformation", mongoClientDetails.getAdditionalInformation())
                .addToSet("autoApproveScopes", mongoClientDetails.getAutoApproveScopes())
                .addToSet("registeredRedirectUris", mongoClientDetails.getRegisteredRedirectUri());

        final WriteResult writeResult = mongoTemplate.updateFirst(query, update, MongoClientDetails.class);

        return writeResult.getN() == 1;
    }

    @Override
    public boolean updateClientSecret(final String clientId,
                                      final String newSecret) {
        final Query query = Query.query(Criteria.where("clientId").is(clientId));

        final Update update = Update.update("clientSecret", newSecret);

        final WriteResult writeResult = mongoTemplate.updateFirst(query, update, MongoClientDetails.class);

        return writeResult.getN() == 1;
    }

    @Override
    public MongoClientDetails findByClientId(final String clientId) throws IllegalArgumentException {
        final Query query = Query.query(Criteria.where("clientId").is(clientId));
        final MongoClientDetails mongoClientDetails = mongoTemplate.findOne(query, MongoClientDetails.class);
        if (mongoClientDetails == null) {
            throw new IllegalArgumentException("No valid client id");
        }
        return mongoClientDetails;
    }


}
