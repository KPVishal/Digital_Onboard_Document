package com.bijlipay.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "penny_drop_verification")
public class PennyDropVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "beneficiary_account_no")
    private String accountNumber;
    @Column(name = "beneficiary_ifsc")
    private String ifsc;
    @Column(name = "beneficiary_name")
    private String name;
    @Column(name = "is_active")
    private String active;
    @Column(name = "reason")
    private String reason;
    @Column(name = "signzy_referenced_id")
    private String signzyReferencedId;
    @Column(name = "account_transfer_details",columnDefinition = "TEXT")
    private String accountTransferDetails;
    @Column(name = "response",nullable = true)
    private String response;
    @Column(name = "amount_match")
    private String amountMatch;
    @Column(name = "verify_transfer_details",columnDefinition = "TEXT")
    private String verifyTransferDetails;
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_id",referencedColumnName = "lead_id")
    private LeadInformation leadInformation;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LeadInformation getLeadInformation() {
        return leadInformation;
    }

    public void setLeadInformation(LeadInformation leadInformation) {
        this.leadInformation = leadInformation;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getSignzyReferencedId() {
        return signzyReferencedId;
    }

    public void setSignzyReferencedId(String signzyReferencedId) {
        this.signzyReferencedId = signzyReferencedId;
    }

    public String getAccountTransferDetails() {
        return accountTransferDetails;
    }

    public void setAccountTransferDetails(String accountTransferDetails) {
        this.accountTransferDetails = accountTransferDetails;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getAmountMatch() {
        return amountMatch;
    }

    public void setAmountMatch(String amountMatch) {
        this.amountMatch = amountMatch;
    }

    public String getVerifyTransferDetails() {
        return verifyTransferDetails;
    }

    public void setVerifyTransferDetails(String verifyTransferDetails) {
        this.verifyTransferDetails = verifyTransferDetails;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
