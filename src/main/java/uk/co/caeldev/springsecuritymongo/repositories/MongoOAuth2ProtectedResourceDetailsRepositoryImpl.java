package uk.co.caeldev.springsecuritymongo.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoOAuth2ProtectedResourceDetailsRepositoryImpl implements MongoOAuth2ProtectedResourceDetailsRepositoryBase {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoOAuth2ProtectedResourceDetailsRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

}
