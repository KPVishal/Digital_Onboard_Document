package com.bijlipay.model.dto.signzy.extraction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AadhaarResultDto {

    private String uid;
    private String vid;
    private String name;
    private String yob;
    private String dob;
    private String pincode;
    private String  address;
    private String gender;
    private SignzySplitAddress splitAddress;
    private String uidHash;
    private AadhaarSummaryDto summary;
    private boolean validBackAndFront;
    private String dateOfBirth;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public SignzySplitAddress getSplitAddress() {
        return splitAddress;
    }

    public void setSplitAddress(SignzySplitAddress splitAddress) {
        this.splitAddress = splitAddress;
    }

    public String getUidHash() {
        return uidHash;
    }

    public void setUidHash(String uidHash) {
        this.uidHash = uidHash;
    }

    public AadhaarSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(AadhaarSummaryDto summary) {
        this.summary = summary;
    }

    public boolean isValidBackAndFront() {
        return validBackAndFront;
    }

    public void setValidBackAndFront(boolean validBackAndFront) {
        this.validBackAndFront = validBackAndFront;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
