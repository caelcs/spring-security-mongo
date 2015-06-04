package uk.co.caeldev.springsecuritymongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.DefaultClientKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import uk.co.caeldev.springsecuritymongo.services.SecurityContextService;

@Configuration
public class SpringSecurityMongoConfiguration {

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
