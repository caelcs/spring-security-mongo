package uk.co.caeldev.springsecuritymongo.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableConfigurationProperties(MongoSettings.class)
@EnableMongoRepositories(basePackages = {"uk.co.caeldev"})
public class MongoConfiguration {

    @Autowired
    private MongoSettings mongoSettings;

    @Autowired
    private MongoClient mongoClient;

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        final UserCredentials userCredentials = new UserCredentials(mongoSettings.getUsername(), mongoSettings.getPassword());

        return new SimpleMongoDbFactory(mongoClient, mongoSettings.getDatabase(), userCredentials);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

}
