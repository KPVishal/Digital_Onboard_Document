package com.bijlipay.model.dto.signzy.verification;

import com.bijlipay.model.dto.signzy.extraction.SignzyInstanceDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignzyResponseDto {
    private SignzyResultDto result;
    private SignzyInstanceDto instance;
    private String number;
    private String id;
    private String dob;
    private String name;
    private String fuzzy;

    //AADHAAR
    private Object url;
    private String uid;

    // Driving Licence
    private String issueDate;

    // PAN
    private String panStatus;

    //Passport
    private String fileNumber;

    //VoterId
    private String epicNumber;
    private String state;

    public SignzyResultDto getResult() {
        return result;
    }

    public void setResult(SignzyResultDto result) {
        this.result = result;
    }

    public SignzyInstanceDto getInstance() {
        return instance;
    }

    public void setInstance(SignzyInstanceDto instance) {
        this.instance = instance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getFuzzy() {
        return fuzzy;
    }

    public void setFuzzy(String fuzzy) {
        this.fuzzy = fuzzy;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getPanStatus() {
        return panStatus;
    }

    public void setPanStatus(String panStatus) {
        this.panStatus = panStatus;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
