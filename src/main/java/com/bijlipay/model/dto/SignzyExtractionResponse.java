package com.bijlipay.model.dto;

import com.bijlipay.model.UploadedDocuments;

public class SignzyExtractionResponse {

    private long subDocumentId;

    private long leadDocumentId;
    private String name;
    private String number;
    private String dob;
    private String fatherName;
    private String address;

    private String dbaName;
    private UploadedDocuments uploadedDocuments;

    private String dateOfIssue;
    private String dateOfExpiry;


    public long getSubDocumentId() {
        return subDocumentId;
    }

    public void setSubDocumentId(long subDocumentId) {
        this.subDocumentId = subDocumentId;
    }

    public long getLeadDocumentId() {
        return leadDocumentId;
    }

    public void setLeadDocumentId(long leadDocumentId) {
        this.leadDocumentId = leadDocumentId;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getDbaName() {
        return dbaName;
    }

    public void setDbaName(String dbaName) {
        this.dbaName = dbaName;
    }

    public UploadedDocuments getUploadedDocuments() {
        return uploadedDocuments;
    }

    public void setUploadedDocuments(UploadedDocuments uploadedDocuments) {
        this.uploadedDocuments = uploadedDocuments;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    @Override
    public String toString() {
        return "SignzyExtractionResponse{" +
                "subDocumentId=" + subDocumentId +
                ", leadDocumentId=" + leadDocumentId +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", dob='" + dob + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", address='" + address + '\'' +
                ", dbaName='" + dbaName + '\'' +
                ", uploadedDocuments=" + uploadedDocuments +
                ", dateOfIssue='" + dateOfIssue + '\'' +
                ", dateOfExpiry='" + dateOfExpiry + '\'' +
                '}';
    }
}
