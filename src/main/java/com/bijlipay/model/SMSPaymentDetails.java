package com.bijlipay.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sms_payment_details")
public class SMSPaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sms_pid", nullable = false)
    private int id;

    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    private boolean isActive;

    @JsonBackReference(value = "leadInformationmi")
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_id", referencedColumnName = "lead_id")
    private LeadInformation leadInformation;

    @Column(name = "sms_reference_id", nullable = true)
    private String smsReferenceId;

    @Column(name = "sms_id", nullable = true)
    private String smsId;

    @Column(name = "sms_payment_id", nullable = true)
    private String smsPaymentId;

    @Column(name = "payment_response", columnDefinition =  "TEXT", nullable = true)
    private String paymentResponse;

    @Column(name = "payment_url", nullable = true)
    private String paymentUrl;

    @Column(name = "call_back_response", nullable = true,columnDefinition = "TEXT")
    private String callBackResponse;

    @Column(name = "payment_status", columnDefinition = "int(11) default 0")
    private Integer paymentStatus; // 0-failure 1-paid  2-created

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LeadInformation getLeadInformation() {
        return leadInformation;
    }

    public void setLeadInformation(LeadInformation leadInformation) {
        this.leadInformation = leadInformation;
    }

    public String getSmsReferenceId() {
        return smsReferenceId;
    }

    public void setSmsReferenceId(String smsReferenceId) {
        this.smsReferenceId = smsReferenceId;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getSmsPaymentId() {
        return smsPaymentId;
    }

    public void setSmsPaymentId(String smsPaymentId) {
        this.smsPaymentId = smsPaymentId;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getCallBackResponse() {
        return callBackResponse;
    }

    public void setCallBackResponse(String callBackResponse) {
        this.callBackResponse = callBackResponse;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    @Override
    public String toString() {
        return "SMSPaymentDetails{" +
                "id=" + id +
                ", isActive=" + isActive +
                ", leadInformation=" + leadInformation +
                ", smsReferenceId='" + smsReferenceId + '\'' +
                ", smsId='" + smsId + '\'' +
                ", smsPaymentId='" + smsPaymentId + '\'' +
                ", paymentResponse='" + paymentResponse + '\'' +
                ", paymentUrl='" + paymentUrl + '\'' +
                ", callBackResponse='" + callBackResponse + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
