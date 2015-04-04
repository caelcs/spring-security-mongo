package uk.co.caeldev.springsecuritymongo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@ComponentScan(basePackages = {"uk.co.caeldev"})
@EnableAutoConfiguration
@EnableSpringDataWebSupport
public class ApplicationConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class, args);
    }
}
