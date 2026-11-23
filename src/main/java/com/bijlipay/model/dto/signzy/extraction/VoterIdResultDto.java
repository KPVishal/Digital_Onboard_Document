package com.bijlipay.model.dto.signzy.extraction;

public class VoterIdResultDto {
    private String name;
    private String fatherName;
    private String address;
    private String state;
    private String ageAsOn;
    private String dob;
    private String gender;
    private String yob;
    private String epicNumber;
    private SignzySplitAddress splitAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAgeAsOn() {
        return ageAsOn;
    }

    public void setAgeAsOn(String ageAsOn) {
        this.ageAsOn = ageAsOn;
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

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }

    public String getEpicNumber() {
        return epicNumber;
    }

    public void setEpicNumber(String epicNumber) {
        this.epicNumber = epicNumber;
    }

    public SignzySplitAddress getSplitAddress() {
        return splitAddress;
    }

    public void setSplitAddress(SignzySplitAddress splitAddress) {
        this.splitAddress = splitAddress;
    }
}
