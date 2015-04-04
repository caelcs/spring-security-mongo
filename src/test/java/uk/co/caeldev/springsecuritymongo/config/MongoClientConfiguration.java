package uk.co.caeldev.springsecuritymongo.config;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(MongoSettings.class)
public class MongoClientConfiguration {

    @Autowired
    private MongoSettings mongoSettings;

    @Bean
    public MongoClient mongoClient() throws IOException {
        Fongo fongo = new Fongo(mongoSettings.getDatabase());
        return fongo.getMongo();
    }

}
