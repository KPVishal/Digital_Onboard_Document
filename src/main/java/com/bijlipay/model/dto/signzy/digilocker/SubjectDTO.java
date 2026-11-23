package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectDTO {
    @JsonProperty("countryName")
    private String countryName;

    @JsonProperty("organizationName")
    private String organizationName;

    @JsonProperty("organizationalUnitName")
    private String organizationalUnitName;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("stateOrProvinceName")
    private String stateOrProvinceName;

    @JsonProperty("commonName")
    private String commonName;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationalUnitName() {
        return organizationalUnitName;
    }

    public void setOrganizationalUnitName(String organizationalUnitName) {
        this.organizationalUnitName = organizationalUnitName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateOrProvinceName() {
        return stateOrProvinceName;
    }

    public void setStateOrProvinceName(String stateOrProvinceName) {
        this.stateOrProvinceName = stateOrProvinceName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @Override
    public String toString() {
        return "SubjectDTO{" +
                "countryName='" + countryName + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationalUnitName='" + organizationalUnitName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", stateOrProvinceName='" + stateOrProvinceName + '\'' +
                ", commonName='" + commonName + '\'' +
                '}';
    }
}
