package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class X509Data1DTO {
    @JsonProperty("subjectName")
    private String subjectName;

    @JsonProperty("certificate")
    private String certificate;

    @JsonProperty("details")
    private DetailsDTO details;

    @JsonProperty("validAadhaarDSC")
    private String validAadhaarDSC;

    public String getSubjectName() {
        return subjectName;
    }



    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public DetailsDTO getDetails() {
        return details;
    }

    public void setDetails(DetailsDTO details) {
        this.details = details;
    }

    public String getValidAadhaarDSC() {
        return validAadhaarDSC;
    }

    public void setValidAadhaarDSC(String validAadhaarDSC) {
        this.validAadhaarDSC = validAadhaarDSC;
    }

    @Override
    public String toString() {
        return "X509Data1DTO{" +
                "subjectName='" + subjectName + '\'' +
                ", certificate='" + certificate + '\'' +
                ", details=" + details +
                ", validAadhaarDSC='" + validAadhaarDSC + '\'' +
                '}';
    }
}
