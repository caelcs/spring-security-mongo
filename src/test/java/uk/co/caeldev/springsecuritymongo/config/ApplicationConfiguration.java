package uk.co.caeldev.springsecuritymongo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableSecurityMongo
public class ApplicationConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class, args);
    }
}
