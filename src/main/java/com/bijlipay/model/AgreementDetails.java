package com.bijlipay.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "agreement_details")
public class AgreementDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "token")
    private String token;
    @Column(name = "input_file_name",columnDefinition = "TEXT")
    private String inputFileName;
    @Column(name = "input_file_pdf_url",columnDefinition = "TEXT")
    private String inputFilePdfUrl;
    @Column(name = "signature_type")
    private String signatureType;
    @Column(name = "response")
    private boolean response;
    @Column(name = "signed_file_name")
    private String signedFileName;
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_id",referencedColumnName = "lead_id")
    private LeadInformation leadInformation;

    @Column(name = "callback_details",columnDefinition = "TEXT")
    private String callBackDetails;
    @Column(name = "event_callback_details",columnDefinition = "TEXT")
    private String eventCallbackDetails;
    @Column(name = "url",columnDefinition = "TEXT")
    private String url;
    @Column(name = "signed_file_pdf_url",columnDefinition = "TEXT")
    private String signedFilePdfUrl;
    @Column(name = "contract_callback_details",columnDefinition = "TEXT")
    private String contractCallbackDetails;
    @Column(name = "signer_callback_details",columnDefinition = "TEXT")
    private String signerCallbackDetails;
    @Column(name = "contract_id",columnDefinition = "TEXT")
    private String contractId;

    @Column(name = "signerId")
    private String signerId;
    @Column(name = "signer_signed_pdfurl",columnDefinition = "TEXT")
    private String signerSignedPdfUrl;
    @Column(name = "final_signed_contract",columnDefinition = "TEXT")
    private String finalSignedContract;

    @Column(name = "audit_certificate",columnDefinition = "TEXT")
    private String auditCertificate;

    @Column(name = "final_signed_name",columnDefinition = "TEXT")
    private String finalSignedName;

    @Column(name = "audit_certificate_name",columnDefinition = "TEXT")
    private String auditCertificateName;

    @Column(name = "rejected_remarks",nullable = true)
    private String remarks;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "isQr_Agreement",columnDefinition = "tinyint(1) default 0")
    private boolean isQrAgreement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public String getSignedFileName() {
        return signedFileName;
    }

    public void setSignedFileName(String signedFileName) {
        this.signedFileName = signedFileName;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }



    public LeadInformation getLeadInformation() {
        return leadInformation;
    }

    public void setLeadInformation(LeadInformation leadInformation) {
        this.leadInformation = leadInformation;
    }

    public String getCallBackDetails() {
        return callBackDetails;
    }

    public void setCallBackDetails(String callBackDetails) {
        this.callBackDetails = callBackDetails;
    }

    public String getEventCallbackDetails() {
        return eventCallbackDetails;
    }

    public void setEventCallbackDetails(String eventCallbackDetails) {
        this.eventCallbackDetails = eventCallbackDetails;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSignedFilePdfUrl() {
        return signedFilePdfUrl;
    }

    public void setSignedFilePdfUrl(String signedFilePdfUrl) {
        this.signedFilePdfUrl = signedFilePdfUrl;
    }

    public String getContractCallbackDetails() {
        return contractCallbackDetails;
    }

    public void setContractCallbackDetails(String contractCallbackDetails) {
        this.contractCallbackDetails = contractCallbackDetails;
    }

    public String getSignerCallbackDetails() {
        return signerCallbackDetails;
    }

    public void setSignerCallbackDetails(String signerCallbackDetails) {
        this.signerCallbackDetails = signerCallbackDetails;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getSignerSignedPdfUrl() {
        return signerSignedPdfUrl;
    }

    public void setSignerSignedPdfUrl(String signerSignedPdfUrl) {
        this.signerSignedPdfUrl = signerSignedPdfUrl;
    }

    public String getFinalSignedContract() {
        return finalSignedContract;
    }

    public void setFinalSignedContract(String finalSignedContract) {
        this.finalSignedContract = finalSignedContract;
    }

    public String getAuditCertificate() {
        return auditCertificate;
    }

    public void setAuditCertificate(String auditCertificate) {
        this.auditCertificate = auditCertificate;
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

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public String getFinalSignedName() {
        return finalSignedName;
    }

    public void setFinalSignedName(String finalSignedName) {
        this.finalSignedName = finalSignedName;
    }

    public String getAuditCertificateName() {
        return auditCertificateName;
    }

    public void setAuditCertificateName(String auditCertificateName) {
        this.auditCertificateName = auditCertificateName;
    }

    public String getInputFilePdfUrl() {
        return inputFilePdfUrl;
    }

    public void setInputFilePdfUrl(String inputFilePdfUrl) {
        this.inputFilePdfUrl = inputFilePdfUrl;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isQrAgreement() {
        return isQrAgreement;
    }

    public void setQrAgreement(boolean qrAgreement) {
        isQrAgreement = qrAgreement;
    }

    @Override
    public String toString() {
        return "AgreementDetails{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", inputFileName='" + inputFileName + '\'' +
                ", inputFilePdfUrl='" + inputFilePdfUrl + '\'' +
                ", signatureType='" + signatureType + '\'' +
                ", response=" + response +
                ", signedFileName='" + signedFileName + '\'' +
                ", leadInformation=" + leadInformation +
                ", callBackDetails='" + callBackDetails + '\'' +
                ", eventCallbackDetails='" + eventCallbackDetails + '\'' +
                ", url='" + url + '\'' +
                ", signedFilePdfUrl='" + signedFilePdfUrl + '\'' +
                ", contractCallbackDetails='" + contractCallbackDetails + '\'' +
                ", signerCallbackDetails='" + signerCallbackDetails + '\'' +
                ", contractId='" + contractId + '\'' +
                ", signerId='" + signerId + '\'' +
                ", signerSignedPdfUrl='" + signerSignedPdfUrl + '\'' +
                ", finalSignedContract='" + finalSignedContract + '\'' +
                ", auditCertificate='" + auditCertificate + '\'' +
                ", finalSignedName='" + finalSignedName + '\'' +
                ", auditCertificateName='" + auditCertificateName + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isQrAgreement=" + isQrAgreement +
                '}';
    }
}
