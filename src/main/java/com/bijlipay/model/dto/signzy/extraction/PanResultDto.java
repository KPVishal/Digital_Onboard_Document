package com.bijlipay.model.dto.signzy.extraction;

public class PanResultDto {
    private String dob;
    private String name;
    private String fatherName;
    private String number;
    private String panType;
    private PanSummaryDto summary;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPanType() {
        return panType;
    }

    public void setPanType(String panType) {
        this.panType = panType;
    }

    public PanSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(PanSummaryDto summary) {
        this.summary = summary;
    }
}
