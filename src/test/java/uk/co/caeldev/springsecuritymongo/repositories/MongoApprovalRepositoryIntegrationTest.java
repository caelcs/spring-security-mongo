package uk.co.caeldev.springsecuritymongo.repositories;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.springsecuritymongo.builders.MongoApprovalBuilder;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

public class MongoApprovalRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private MongoApprovalRepository mongoApprovalRepository;

    @Test
    public void shouldSaveMongoApproval() {
        //Given
        final String clientId = string().next();
        final String userId = string().next();
        final MongoApproval mongoApproval = MongoApprovalBuilder.mongoApprovalBuilder()
                .clientId(clientId).userId(userId).build();

        //When
        mongoApprovalRepository.save(mongoApproval);

        //Then
        final List<MongoApproval> results = mongoApprovalRepository.findByUserIdAndClientId(userId, clientId);
        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        final MongoApproval approval = results.get(0);
        assertThat(approval).isEqualToComparingFieldByField(approval);
    }

    @Test
    public void shouldDeleteMongoApprovalByUserIdAndClientIdAndScope() {
        //Given
        final String clientId = "167181125test-approval-1237710666";
        final String userId = "12d865f31941b6063aa9e315e092f113";
        final String scope = "ADMIN";

        //And
        final MongoApproval mongoApproval = MongoApprovalBuilder.mongoApprovalBuilder()
                .clientId(clientId).userId(userId).scope(scope).build();
        mongoApprovalRepository.save(mongoApproval);

        //When
        mongoApprovalRepository.deleteByUserIdAndClientIdAndScope(mongoApproval);

        //Then
        final List<MongoApproval> results = mongoApprovalRepository.findByUserIdAndClientId(userId, clientId);
        assertThat(results).isEmpty();
    }

    @Test
    public void shouldDeleteMongoApproval() {
        //Given
        final MongoApproval mongoApproval = MongoApprovalBuilder.mongoApprovalBuilder().build();
        final MongoApproval mongoApprovalSaved = mongoApprovalRepository.save(mongoApproval);

        //When
        mongoApprovalRepository.delete(mongoApprovalSaved);

        //Then
        final List<MongoApproval> mongoApprovals = mongoApprovalRepository.findAll();
        assertThat(mongoApprovals).isEmpty();
    }

    @Test
    public void shouldUpdateExpiresAt() {
        //Given
        final LocalDateTime expiresAt = LocalDateTime.now();
        final MongoApproval mongoApproval = MongoApprovalBuilder.mongoApprovalBuilder().build();
        final MongoApproval mongoApprovalSaved = mongoApprovalRepository.save(mongoApproval);

        //When
        mongoApprovalRepository.updateExpiresAt(expiresAt, mongoApprovalSaved);

        //Then
        final List<MongoApproval> mongoApprovals = mongoApprovalRepository.findAll();
        assertThat(mongoApprovals).hasSize(1);
        assertThat(mongoApprovals.get(0).getExpiresAt()).isEqualTo(expiresAt);
    }

    @Test
    public void shouldCreateOrUpdateACollectionOfMongoApprovals() {
        //Given
        final MongoApproval first = MongoApprovalBuilder.mongoApprovalBuilder().build();
        final MongoApproval second = MongoApprovalBuilder.mongoApprovalBuilder().build();
        final ArrayList<MongoApproval> approvals = Lists.newArrayList(first, second);

        //When
        final boolean result = mongoApprovalRepository.updateOrCreate(approvals);

        //Then
        assertThat(result).isTrue();
        final List<MongoApproval> mongoApprovals = mongoApprovalRepository.findAll();
        assertThat(mongoApprovals).hasSameSizeAs(approvals);
    }
}
