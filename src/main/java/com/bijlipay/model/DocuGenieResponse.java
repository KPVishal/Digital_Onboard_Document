package com.bijlipay.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "docugenie_response")
public class DocuGenieResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "aadhaar_card_front",columnDefinition = "TEXT")
    private String aadhaarCardFront;

    @Column(name = "aadhaar_card_back",columnDefinition = "TEXT")
    private String aadhaarCardBack;

    @Column(name = "pan_card",columnDefinition = "TEXT")
    private String panCard;

    @Column(name = "gst",columnDefinition = "TEXT")
    private String gst;

    @Column(name = "all_document_response",columnDefinition = "TEXT")
    private String allDocumentResponse;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_id", referencedColumnName = "lead_id")
    private LeadInformation leadInformation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAadhaarCardFront() {
        return aadhaarCardFront;
    }

    public void setAadhaarCardFront(String aadhaarCardFront) {
        this.aadhaarCardFront = aadhaarCardFront;
    }

    public String getAadhaarCardBack() {
        return aadhaarCardBack;
    }

    public void setAadhaarCardBack(String aadhaarCardBack) {
        this.aadhaarCardBack = aadhaarCardBack;
    }

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public LeadInformation getLeadInformation() {
        return leadInformation;
    }

    public void setLeadInformation(LeadInformation leadInformation) {
        this.leadInformation = leadInformation;
    }

    public String getAllDocumentResponse() {
        return allDocumentResponse;
    }

    public void setAllDocumentResponse(String allDocumentResponse) {
        this.allDocumentResponse = allDocumentResponse;
    }
}
