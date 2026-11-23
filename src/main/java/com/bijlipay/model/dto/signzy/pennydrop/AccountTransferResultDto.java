package com.bijlipay.model.dto.signzy.pennydrop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTransferResultDto {
    private String active;
    private String reason;
    private String nameMatch;
    private String nameMatchScore;
    private String mobileMatch;
    private String signzyReferenceId;
    private AuditTrailDto auditTrail;
    private BankTransferDto bankTransfer;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNameMatch() {
        return nameMatch;
    }

    public void setNameMatch(String nameMatch) {
        this.nameMatch = nameMatch;
    }

    public String getNameMatchScore() {
        return nameMatchScore;
    }

    public void setNameMatchScore(String nameMatchScore) {
        this.nameMatchScore = nameMatchScore;
    }

    public String getMobileMatch() {
        return mobileMatch;
    }

    public void setMobileMatch(String mobileMatch) {
        this.mobileMatch = mobileMatch;
    }

    public String getSignzyReferenceId() {
        return signzyReferenceId;
    }

    public void setSignzyReferenceId(String signzyReferenceId) {
        this.signzyReferenceId = signzyReferenceId;
    }

    public AuditTrailDto getAuditTrail() {
        return auditTrail;
    }

    public void setAuditTrail(AuditTrailDto auditTrail) {
        this.auditTrail = auditTrail;
    }

    public BankTransferDto getBankTransfer() {
        return bankTransfer;
    }

    public void setBankTransfer(BankTransferDto bankTransfer) {
        this.bankTransfer = bankTransfer;
    }
}
