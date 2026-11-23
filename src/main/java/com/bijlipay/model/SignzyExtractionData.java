package com.bijlipay.model;

import javax.persistence.*;

@Entity
@Table(name = "signzy_extraction_data")
public class SignzyExtractionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name",columnDefinition = "TEXT")
    private String name;
    @Column(name = "number",columnDefinition = "TEXT")
    private String number;
    @Column(name = "dob",columnDefinition = "TEXT")
    private String dob;
    @Column(name = "dba_name",columnDefinition = "TEXT")
    private String dbaName;
    @Column(name = "father_name",columnDefinition = "TEXT")
    private String fatherName;
    @Column(name = "address",columnDefinition = "TEXT")
    private String address;
    @Column(name = "lead_id")
    private long leadId;
    @Column(name = "sub_document")
    private long subDocument;

    @Column(name = "date_of_issue",columnDefinition = "TEXT")
    private String dateOfIssue;
    @Column(name = "date_of_expiry",columnDefinition = "TEXT")
    private String dateOfExpiry;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public long getLeadId() {
        return leadId;
    }

    public void setLeadId(long leadId) {
        this.leadId = leadId;
    }

    public long getSubDocument() {
        return subDocument;
    }

    public void setSubDocument(long subDocument) {
        this.subDocument = subDocument;
    }

    public String getDbaName() {
        return dbaName;
    }

    public void setDbaName(String dbaName) {
        this.dbaName = dbaName;
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
}
