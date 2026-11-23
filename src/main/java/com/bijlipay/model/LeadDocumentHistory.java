package com.bijlipay.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lead_document_history")
public class LeadDocumentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_document_history_id", nullable = false)
    private long leadDocumentHistoryId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_document_id", referencedColumnName = "lead_document_id")
    private LeadDocuments leadDocuments;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_of_activity")
    private Date dateOfActivity;

    @Column(name = "remarks")
    private String remarks;

    public LeadDocuments getLeadDocuments() {
        return leadDocuments;
    }

    public void setLeadDocuments(LeadDocuments leadDocuments) {
        this.leadDocuments = leadDocuments;
    }

    public long getLeadDocumentHistoryId() {
        return leadDocumentHistoryId;
    }

    public void setLeadDocumentHistoryId(long leadDocumentHistoryId) {
        this.leadDocumentHistoryId = leadDocumentHistoryId;
    }

    public Date getDateOfActivity() {
        return dateOfActivity;
    }

    public void setDateOfActivity(Date dateOfActivity) {
        this.dateOfActivity = dateOfActivity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "LeadDocumentHistory{" +
                "leadDocumentHistoryId=" + leadDocumentHistoryId +
                ", leadDocuments=" + leadDocuments +
                ", dateOfActivity=" + dateOfActivity +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
