package com.bijlipay.model.dto;

import javax.persistence.Column;

public class AgreementDetailsDto {
    private String inputFile;
    private String inputFileUrl;
    private String inputFilePdfUrl;
    private boolean response;
    private String signedFileName;
    private String signedFilePdfUrl;
    private String leadNumber;

    private String token;
    private String signatureType;
    private String callBackDetails;
    private String eventCallbackDetails;
    private String contractCallbackDetails;
    private String signerCallbackDetails;
    private String contractId;
    private String signerId;
    private String signerSignedPdfUrl;
    private String finalSignedContract;
    private String auditCertificate;
    private String finalSignedName;
    private String auditCertificateName;

    private String finalSignedDownUrl;
    private String auditCertificateDownUrl;

    private String signedFileDownUrl;
    private String remarks;
    private boolean isQrAgreement;

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFilePdfUrl() {
        return inputFilePdfUrl;
    }

    public String getInputFileUrl() {
        return inputFileUrl;
    }

    public void setInputFileUrl(String inputFileUrl) {
        this.inputFileUrl = inputFileUrl;
    }

    public void setInputFilePdfUrl(String inputFilePdfUrl) {
        this.inputFilePdfUrl = inputFilePdfUrl;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getSignedFileName() {
        return signedFileName;
    }

    public void setSignedFileName(String signedFileName) {
        this.signedFileName = signedFileName;
    }

    public String getSignedFilePdfUrl() {
        return signedFilePdfUrl;
    }

    public void setSignedFilePdfUrl(String signedFilePdfUrl) {
        this.signedFilePdfUrl = signedFilePdfUrl;
    }

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
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

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
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

    public String getFinalSignedDownUrl() {
        return finalSignedDownUrl;
    }

    public void setFinalSignedDownUrl(String finalSignedDownUrl) {
        this.finalSignedDownUrl = finalSignedDownUrl;
    }

    public String getAuditCertificateDownUrl() {
        return auditCertificateDownUrl;
    }

    public void setAuditCertificateDownUrl(String auditCertificateDownUrl) {
        this.auditCertificateDownUrl = auditCertificateDownUrl;
    }

    public String getSignedFileDownUrl() {
        return signedFileDownUrl;
    }

    public void setSignedFileDownUrl(String signedFileDownUrl) {
        this.signedFileDownUrl = signedFileDownUrl;
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
}
