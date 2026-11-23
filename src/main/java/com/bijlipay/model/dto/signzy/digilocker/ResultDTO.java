package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("dob")
    private String dob;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("x509Data")
    private X509Data1DTO x509Data;

    @JsonProperty("address")
    private String address;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("splitAddress")
    private SplitAddressDTO splitAddress;

    @JsonProperty("signatureData")
    private SignatureDataDTO signatureData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public X509Data1DTO getX509Data() {
        return x509Data;
    }

    public void setX509Data(X509Data1DTO x509Data) {
        this.x509Data = x509Data;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public SplitAddressDTO getSplitAddress() {
        return splitAddress;
    }

    public void setSplitAddress(SplitAddressDTO splitAddress) {
        this.splitAddress = splitAddress;
    }

    public SignatureDataDTO getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(SignatureDataDTO signatureData) {
        this.signatureData = signatureData;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", x509Data=" + x509Data +
                ", address='" + address + '\'' +
                ", photo='" + photo + '\'' +
                ", splitAddress=" + splitAddress +
                ", signatureData=" + signatureData +
                '}';
    }
}
