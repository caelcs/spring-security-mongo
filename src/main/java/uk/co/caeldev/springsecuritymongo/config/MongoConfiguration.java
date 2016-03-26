package uk.co.caeldev.springsecuritymongo.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.DefaultClientKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import uk.co.caeldev.springsecuritymongo.services.SecurityContextService;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableConfigurationProperties(MongoSettings.class)
@ConditionalOnProperty({
        "mongo.host",
        "mongo.port",
        "mongo.database",
        "mongo.username",
        "mongo.password"})
@ComponentScan(basePackages = {"uk.co.caeldev.springsecuritymongo"})
@EnableMongoRepositories(basePackages = {"uk.co.caeldev.springsecuritymongo.repositories"})
public class MongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(final MongoClient mongoClient,
                                       final MongoSettings mongoSettings) throws Exception {
        return new MongoTemplate(mongoClient, mongoSettings.getDatabase());
    }

    @Configuration
    @EnableConfigurationProperties(MongoSettings.class)
    @Profile("!test")
    static class MongoClientConfiguration {

        @Bean
        public MongoClient mongoClient(final MongoSettings mongoSettings) throws Exception {
            ServerAddress serverAddress = new ServerAddress(
                    mongoSettings.getHost(), mongoSettings.getPort());

            MongoCredential credential = MongoCredential.createCredential(
                    mongoSettings.getUsername(),
                    mongoSettings.getDatabase(),
                    mongoSettings.getPassword().toCharArray());

            return new MongoClient(
                    serverAddress, newArrayList(credential));
        }
    }

    @Configuration
    static class SpringSecurityConfiguration {

        @Bean
        public SecurityContextService securityContextService() {
            return new SecurityContextService();
        }

        @Bean
        public AuthenticationKeyGenerator authenticationKeyGenerator() {
            return new DefaultAuthenticationKeyGenerator();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }

        @Bean
        public ClientKeyGenerator clientKeyGenerator(){
            return new DefaultClientKeyGenerator();
        }

    }
}
