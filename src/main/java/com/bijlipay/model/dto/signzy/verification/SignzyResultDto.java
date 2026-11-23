package com.bijlipay.model.dto.signzy.verification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignzyResultDto {

    private String verified;
    private String message;
    private String upstreamName;

    //AADHAAR
    private String ageBand;
    private String state;
    private String mobileNumber;
    private String gender;

    // Driving Licence
    private DLMoreInfo moreInfo;

    //PAN
    private String panStatus;
    private String panStatusCode;

    //Voter
    private boolean verification;

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUpstreamName() {
        return upstreamName;
    }

    public void setUpstreamName(String upstreamName) {
        this.upstreamName = upstreamName;
    }

    public String getAgeBand() {
        return ageBand;
    }

    public void setAgeBand(String ageBand) {
        this.ageBand = ageBand;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public DLMoreInfo getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(DLMoreInfo moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getPanStatus() {
        return panStatus;
    }

    public void setPanStatus(String panStatus) {
        this.panStatus = panStatus;
    }

    public String getPanStatusCode() {
        return panStatusCode;
    }

    public void setPanStatusCode(String panStatusCode) {
        this.panStatusCode = panStatusCode;
    }

    public boolean isVerification() {
        return verification;
    }

    public void setVerification(boolean verification) {
        this.verification = verification;
    }
}
