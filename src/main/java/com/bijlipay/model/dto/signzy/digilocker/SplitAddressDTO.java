package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SplitAddressDTO {
    @JsonProperty("district")
    private List<String> district;

    @JsonProperty("state")
    private List<List<String>> state;

    @JsonProperty("city")
    private List<String> city;

    @JsonProperty("pincode")
    private String pincode;

    @JsonProperty("country")
    private List<String> country;

    @JsonProperty("addressLine")
    private String addressLine;

    @JsonProperty("landMark")
    private String landMark;


    public List<String> getDistrict() {
        return district;
    }

    public void setDistrict(List<String> district) {
        this.district = district;
    }

    public List<List<String>> getState() {
        return state;
    }

    public void setState(List<List<String>> state) {
        this.state = state;
    }

    public List<String> getCity() {
        return city;
    }

    public void setCity(List<String> city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    @Override
    public String toString() {
        return "SplitAddressDTO{" +
                "district=" + district +
                ", state=" + state +
                ", city=" + city +
                ", pincode='" + pincode + '\'' +
                ", country=" + country +
                ", addressLine='" + addressLine + '\'' +
                ", landMark='" + landMark + '\'' +
                '}';
    }
}
