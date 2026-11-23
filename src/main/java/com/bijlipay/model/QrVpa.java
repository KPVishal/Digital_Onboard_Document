package com.bijlipay.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qr_vpa_table")
public class QrVpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "qr_Lead_id",referencedColumnName = "lead_id")
    private LeadInformation qr_Lead_id ;
    @Column(name = "qr_Vpa", nullable = true)
    private String qrVpa;

    @ManyToOne
    @JoinColumn(name = "vpa_status", referencedColumnName = "sqr_status_id")
    private SqrLeadStatus vpaStatus;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name="is_Active",columnDefinition = "tinyint(1) default 0")
    private boolean is_Active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LeadInformation getQr_Lead_id() {
        return qr_Lead_id;
    }

    public void setQr_Lead_id(LeadInformation qr_Lead_id) {
        this.qr_Lead_id = qr_Lead_id;
    }

    public String getQrVpa() {
        return qrVpa;
    }

    public void setQrVpa(String qrVpa) {
        this.qrVpa = qrVpa;
    }

    public SqrLeadStatus getVpaStatus(SqrLeadStatus sqrStatus1) {
        return vpaStatus;
    }

    public void setVpaStatus(SqrLeadStatus vpaStatus) {
        this.vpaStatus = vpaStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SqrLeadStatus getVpaStatus() {
        return vpaStatus;
    }

    public boolean getIs_Active() {
        return is_Active;
    }

    public void setIs_Active(boolean is_Active) {
        this.is_Active = is_Active;
    }

    @Override
    public String toString() {
        return "QrVpa{" +
                "id=" + id +
                ", qr_Lead_id=" + qr_Lead_id +
                ", qrVpa='" + qrVpa + '\'' +
                ", vpaStatus=" + vpaStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", is_Active=" + is_Active +
                '}';
    }
}
