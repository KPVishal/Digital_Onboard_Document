package com.bijlipay.model.dto.signzy.fetch;

public class UserDetailsDto {

    private String digilockerid;
    private String name;
    private String dob;
    private String gender;
    private String eaadhaar;
    private String mobile;

    public String getDigilockerid() {
        return digilockerid;
    }

    public void setDigilockerid(String digilockerid) {
        this.digilockerid = digilockerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEaadhaar() {
        return eaadhaar;
    }

    public void setEaadhaar(String eaadhaar) {
        this.eaadhaar = eaadhaar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
