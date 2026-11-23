package com.bijlipay.model.dto;

import com.bijlipay.model.UploadedDocuments;
import java.util.List;

public class DocumentVerifyDto {

    private List<UploadedDocuments> uploadedDocuments;
    private String fileNumber;
    private String dob;
    private String epicNumber;
    private String state;
    private String number;
    private String gstin;
    private String uid;
    private String requestId;

    public List<UploadedDocuments> getUploadedDocuments() {
        return uploadedDocuments;
    }

    public void setUploadedDocuments(List<UploadedDocuments> uploadedDocuments) {
        this.uploadedDocuments = uploadedDocuments;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEpicNumber() {
        return epicNumber;
    }

    public void setEpicNumber(String epicNumber) {
        this.epicNumber = epicNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
