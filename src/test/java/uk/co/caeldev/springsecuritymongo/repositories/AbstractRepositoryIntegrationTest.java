package uk.co.caeldev.springsecuritymongo.repositories;

import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.springsecuritymongo.config.ApplicationConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { ApplicationConfiguration.class })
@ActiveProfiles("test")
public abstract class AbstractRepositoryIntegrationTest {

    @Autowired
    protected MongoClient mongoClient;

    @Before
    public void dropDatabase() {
        mongoClient.dropDatabase("copyshare");
    }
}
