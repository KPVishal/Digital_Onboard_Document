package com.bijlipay.model.dto.signzy.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignzyContract360ResponseDto {

    private String customerId;
    private String username;
    private String contractId;
    private String pdf;
    private String initialContractHash;
    private String contractName;
    private String contractExecuterName;
    private String eSignProvider;
    private String contractTtl;
    private List<SignerDetailsResponseDto> signerdetail;
    private String callbackUrl;
    private String signerCallbackUrl;
    private List<String> callbackExtraParameter;
    private Long fileTtl;
    private String workflow;
    private String isParallel;
    private String isEncryptionOpted;
    private List<String> customerMailList;
    private String initiationEmailSubject;
    private String contractExpiresOn;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getInitialContractHash() {
        return initialContractHash;
    }

    public void setInitialContractHash(String initialContractHash) {
        this.initialContractHash = initialContractHash;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractExecuterName() {
        return contractExecuterName;
    }

    public void setContractExecuterName(String contractExecuterName) {
        this.contractExecuterName = contractExecuterName;
    }

    public String geteSignProvider() {
        return eSignProvider;
    }

    public void seteSignProvider(String eSignProvider) {
        this.eSignProvider = eSignProvider;
    }

    public String getContractTtl() {
        return contractTtl;
    }

    public void setContractTtl(String contractTtl) {
        this.contractTtl = contractTtl;
    }

    public List<SignerDetailsResponseDto> getSignerdetail() {
        return signerdetail;
    }

    public void setSignerdetail(List<SignerDetailsResponseDto> signerdetail) {
        this.signerdetail = signerdetail;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getSignerCallbackUrl() {
        return signerCallbackUrl;
    }

    public void setSignerCallbackUrl(String signerCallbackUrl) {
        this.signerCallbackUrl = signerCallbackUrl;
    }

    public List<String> getCallbackExtraParameter() {
        return callbackExtraParameter;
    }

    public void setCallbackExtraParameter(List<String> callbackExtraParameter) {
        this.callbackExtraParameter = callbackExtraParameter;
    }

    public Long getFileTtl() {
        return fileTtl;
    }

    public void setFileTtl(Long fileTtl) {
        this.fileTtl = fileTtl;
    }


    public String getIsParallel() {
        return isParallel;
    }

    public void setIsParallel(String isParallel) {
        this.isParallel = isParallel;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public String getIsEncryptionOpted() {
        return isEncryptionOpted;
    }

    public void setIsEncryptionOpted(String isEncryptionOpted) {
        this.isEncryptionOpted = isEncryptionOpted;
    }

    public List<String> getCustomerMailList() {
        return customerMailList;
    }

    public void setCustomerMailList(List<String> customerMailList) {
        this.customerMailList = customerMailList;
    }

    public String getInitiationEmailSubject() {
        return initiationEmailSubject;
    }

    public void setInitiationEmailSubject(String initiationEmailSubject) {
        this.initiationEmailSubject = initiationEmailSubject;
    }

    public String getContractExpiresOn() {
        return contractExpiresOn;
    }

    public void setContractExpiresOn(String contractExpiresOn) {
        this.contractExpiresOn = contractExpiresOn;
    }
}
