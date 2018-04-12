package uk.co.caeldev.springsecuritymongo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.provider.approval.Approval;
import uk.co.caeldev.springsecuritymongo.domain.MongoApproval;
import uk.co.caeldev.springsecuritymongo.repositories.MongoApprovalRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.*;

@RunWith(MockitoJUnitRunner.class)
public class MongoApprovalStoreTest {

    @Mock
    private MongoApprovalRepository mongoApprovalRepository;

    private MongoApprovalStore mongoApprovalStore;

    @Before
    public void setup() {
        mongoApprovalStore = new MongoApprovalStore(mongoApprovalRepository);
    }

    @Test
    public void shouldAddApprovals() {
        //Given
        final List<Approval> approvals = list(ofApproval()).next();

        //And
        given(mongoApprovalRepository.updateOrCreate(anyCollection())).willReturn(true);

        //When
        final boolean result = mongoApprovalStore.addApprovals(approvals);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenSomeApprovalsFailedToUpdateOrInsert() {
        //Given
        final List<Approval> approvals = list(ofApproval()).next();

        //And
        given(mongoApprovalRepository.updateOrCreate(anyCollection())).willReturn(false);

        //When
        final boolean result = mongoApprovalStore.addApprovals(approvals);

        //Then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldRevokeApprovalsByRemoveWhenHandleRevocationsAsExpiryIsFalse() {
        //Given
        final List<Approval> approvals = list(ofApproval()).next();

        //And
        mongoApprovalStore.setHandleRevocationsAsExpiry(false);

        //And
        given(mongoApprovalRepository.deleteByUserIdAndClientIdAndScope(any(MongoApproval.class))).willReturn(true);

        //When
        final boolean result = mongoApprovalStore.revokeApprovals(approvals);

        //Then
        assertThat(result).isTrue();
        verify(mongoApprovalRepository, never()).updateExpiresAt(any(LocalDateTime.class), any(MongoApproval.class));
    }

    @Test
    public void shouldRevokeApprovalsByUpdateWhenHandleRevocationsAsExpiryIsTrue() {
        //Given
        final List<Approval> approvals = list(ofApproval()).next();

        //And
        mongoApprovalStore.setHandleRevocationsAsExpiry(true);

        //And
        given(mongoApprovalRepository.updateExpiresAt(any(LocalDateTime.class), any(MongoApproval.class))).willReturn(true);

        //When
        final boolean result = mongoApprovalStore.revokeApprovals(approvals);

        //Then
        assertThat(result).isTrue();
        verify(mongoApprovalRepository, never()).deleteByUserIdAndClientIdAndScope(any(MongoApproval.class));
    }

    @Test
    public void shouldGetApprovals() {
        //Given
        final String userId = string().next();
        final String clientId = string().next();

        //And
        final List<MongoApproval> expectedMongoApprovals = list(ofMongoApproval()).next();
        given(mongoApprovalRepository.findByUserIdAndClientId(userId, clientId)).willReturn(expectedMongoApprovals);

        //When
        final Collection<Approval> approvals = mongoApprovalStore.getApprovals(userId, clientId);

        //Then
        assertThat(approvals).hasSameSizeAs(expectedMongoApprovals);
    }


}
