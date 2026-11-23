package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssuerDTO {
    @JsonProperty("countryName")
    private String countryName;

    @JsonProperty("organizationName")
    private String organizationName;

    @JsonProperty("organizationalUnitName")
    private String organizationalUnitName;

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

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @Override
    public String toString() {
        return "IssuerDTO{" +
                "countryName='" + countryName + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationalUnitName='" + organizationalUnitName + '\'' +
                ", commonName='" + commonName + '\'' +
                '}';
    }
}
