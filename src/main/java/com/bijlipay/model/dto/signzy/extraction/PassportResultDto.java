package com.bijlipay.model.dto.signzy.extraction;

import java.util.List;

public class PassportResultDto {
    private String parentsGuardianName;
    private String issueDate;
    private String expiryDate;
    private String birthDate;
    private String name;
    private List<String> country;
    private String nationality;
    private String sex;
    private String address;
    private String pincode;
    private String passportNumber;
    private String fileNumber;
    private String placeOfBirth;
    private String placeOfIssue;
    private SignzySplitAddress splitAddress;
    private PassportSummaryDto summary;

    public String getParentsGuardianName() {
        return parentsGuardianName;
    }

    public void setParentsGuardianName(String parentsGuardianName) {
        this.parentsGuardianName = parentsGuardianName;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public SignzySplitAddress getSplitAddress() {
        return splitAddress;
    }

    public void setSplitAddress(SignzySplitAddress splitAddress) {
        this.splitAddress = splitAddress;
    }

    public PassportSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(PassportSummaryDto summary) {
        this.summary = summary;
    }
}
