package uk.co.caeldev.springsecuritymongo.builders;

import org.springframework.security.oauth2.provider.approval.Approval;

import static uk.co.caeldev.springsecuritymongo.commons.SecurityRDG.*;

public class ApprovalBuilder {

    private String userId = string().next();
    private String clientId = string().next();
    private String scope = string().next();
    private Integer expiresIn = integer().next();
    private Approval.ApprovalStatus status = value(Approval.ApprovalStatus.class).next();

    private ApprovalBuilder() {
    }

    public static ApprovalBuilder approvalBuilder() {
        return new ApprovalBuilder();
    }

    public Approval build() {
        return new Approval(userId, clientId, scope, expiresIn, status);
    }
}
