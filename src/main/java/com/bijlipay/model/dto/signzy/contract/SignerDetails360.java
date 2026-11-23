package com.bijlipay.model.dto.signzy.contract;

import com.bijlipay.model.dto.signzy.esign.SignaturesDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignerDetails360 {

    private String signerName;
    private String signerEmail;
    private String signatureType;
    private List<SignaturesDto> signatures;



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

    public List<SignaturesDto> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignaturesDto> signatures) {
        this.signatures = signatures;
    }


}
