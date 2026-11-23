package com.bijlipay.model.dto.signzy.extraction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrivingLicenceResultDto {
    private List<String> dlType;
    private String issueDate;
    private String expiryDate;
    private String dob;
    private String guardianName;
    private String name;
    private String number;
    private String state;
    private String address;
    private SignzySplitAddress splitAddress;
    private DrivingLicenceSummaryDto summary;

    public List<String> getDlType() {
        return dlType;
    }

    public void setDlType(List<String> dlType) {
        this.dlType = dlType;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SignzySplitAddress getSplitAddress() {
        return splitAddress;
    }

    public void setSplitAddress(SignzySplitAddress splitAddress) {
        this.splitAddress = splitAddress;
    }

    public DrivingLicenceSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(DrivingLicenceSummaryDto summary) {
        this.summary = summary;
    }
}
