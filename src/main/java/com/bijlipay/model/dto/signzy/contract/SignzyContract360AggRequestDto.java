package com.bijlipay.model.dto.signzy.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignzyContract360AggRequestDto {

    private String pdf;
    private String contractName;
    private String contractExecuterName;
    private String callbackUrl;
    private boolean isParallel;
    private String signerCallbackUrl;
    private boolean workflow;

    private List<SignerDetails360> signerdetail;
    private List<String> customerMailList;
    private String initiationEmailSubject;

    private String eSignProvider;
//    private boolean showAadhaarNameOnSignature;

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
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

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public boolean isParallel() {
        return isParallel;
    }

    public void setParallel(boolean parallel) {
        isParallel = parallel;
    }

    public String getSignerCallbackUrl() {
        return signerCallbackUrl;
    }

    public void setSignerCallbackUrl(String signerCallbackUrl) {
        this.signerCallbackUrl = signerCallbackUrl;
    }

    public boolean isWorkflow() {
        return workflow;
    }

    public void setWorkflow(boolean workflow) {
        this.workflow = workflow;
    }

    public List<SignerDetails360> getSignerdetail() {
        return signerdetail;
    }

    public void setSignerdetail(List<SignerDetails360> signerdetail) {
        this.signerdetail = signerdetail;
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

    public String geteSignProvider() {
        return eSignProvider;
    }

    public void seteSignProvider(String eSignProvider) {
        this.eSignProvider = eSignProvider;
    }

//    public boolean isShowAadhaarNameOnSignature() {
//        return showAadhaarNameOnSignature;
//    }
//
//    public void setShowAadhaarNameOnSignature(boolean showAadhaarNameOnSignature) {
//        this.showAadhaarNameOnSignature = showAadhaarNameOnSignature;
//    }
}
