package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignedInfoDTO {
    @JsonProperty("CanonicalizationMethod")
    private AlgorithmDTO canonicalizationMethod;

    @JsonProperty("SignatureMethod")
    private AlgorithmDTO signatureMethod;

    @JsonProperty("Reference")
    private ReferenceDTO reference;

    public AlgorithmDTO getCanonicalizationMethod() {
        return canonicalizationMethod;
    }

    public void setCanonicalizationMethod(AlgorithmDTO canonicalizationMethod) {
        this.canonicalizationMethod = canonicalizationMethod;
    }

    public AlgorithmDTO getSignatureMethod() {
        return signatureMethod;
    }

    public void setSignatureMethod(AlgorithmDTO signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public ReferenceDTO getReference() {
        return reference;
    }

    public void setReference(ReferenceDTO reference) {
        this.reference = reference;
    }


    @Override
    public String toString() {
        return "SignedInfoDTO{" +
                "canonicalizationMethod=" + canonicalizationMethod +
                ", signatureMethod=" + signatureMethod +
                ", reference=" + reference +
                '}';
    }
}
