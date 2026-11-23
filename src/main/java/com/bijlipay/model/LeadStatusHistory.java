package com.bijlipay.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lead_status_history")
public class LeadStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_status_history_id")
    private Long id;
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_id", referencedColumnName = "lead_id")
    private LeadInformation leadInformation;
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_status_id", referencedColumnName = "lead_status_id")
    private LeadStatus leadStatus;
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_sub_status_id", referencedColumnName = "lead_sub_status_id",nullable = true)
    private LeadSubStatus leadSubStatus;
    @Column(name = "date_of_activity", nullable = true)
    private Date dateOfActivity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeadInformation getLeadInformation() {
        return leadInformation;
    }

    public void setLeadInformation(LeadInformation leadInformation) {
        this.leadInformation = leadInformation;
    }

    public LeadStatus getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(LeadStatus leadStatus) {
        this.leadStatus = leadStatus;
    }

    public LeadSubStatus getLeadSubStatus() {
        return leadSubStatus;
    }

    public void setLeadSubStatus(LeadSubStatus leadSubStatus) {
        this.leadSubStatus = leadSubStatus;
    }

    public Date getDateOfActivity() {
        return dateOfActivity;
    }

    public void setDateOfActivity(Date dateOfActivity) {
        this.dateOfActivity = dateOfActivity;
    }

    @Override
    public String toString() {
        return "LeadStatusHistory{" +
                "id=" + id +
                ", leadInformation=" + leadInformation +
                ", leadStatus=" + leadStatus +
                ", leadSubStatus=" + leadSubStatus +
                ", dateOfActivity=" + dateOfActivity +
                '}';
    }
}
