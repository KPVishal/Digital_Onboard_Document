package com.bijlipay.model.dto.signzy.contract;

import com.bijlipay.model.dto.signzy.esign.SignaturesDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignerDetailsResponseDto {

    private String signerId;
    private String signerName;
    private String signerEmail;
    private String signatureType;
    private List<String> additionalSignatureTypes;
    private List<SignaturesDto> signatures;
    private String workflowUrl;

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getSignerEmail() {
        return signerEmail;
    }

    public void setSignerEmail(String signerEmail) {
        this.signerEmail = signerEmail;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public List<String> getAdditionalSignatureTypes() {
        return additionalSignatureTypes;
    }

    public void setAdditionalSignatureTypes(List<String> additionalSignatureTypes) {
        this.additionalSignatureTypes = additionalSignatureTypes;
    }

    public List<SignaturesDto> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignaturesDto> signatures) {
        this.signatures = signatures;
    }

    public String getWorkflowUrl() {
        return workflowUrl;
    }

    public void setWorkflowUrl(String workflowUrl) {
        this.workflowUrl = workflowUrl;
    }
}
