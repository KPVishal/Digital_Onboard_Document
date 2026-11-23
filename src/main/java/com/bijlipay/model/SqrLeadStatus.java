package com.bijlipay.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sqr_lead_status")
public class SqrLeadStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sqr_status_id")
    private long sqrStatusId;

    @Column(name = "sqr_status_name", nullable = false, unique = true)
    private String sqrStatusName;

    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    private boolean isActive;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public long getSqrStatusId() {
        return sqrStatusId;
    }

    public void setSqrStatusId(long sqrStatusId) {
        this.sqrStatusId = sqrStatusId;
    }

    public String getSqrStatusName() {
        return sqrStatusName;
    }

    public void setSqrStatusName(String sqrStatusName) {
        this.sqrStatusName = sqrStatusName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    @Override
    public String toString() {
        return "SqrLeadStatus{" +
                "sqrStatusId=" + sqrStatusId +
                ", sqrStatusName='" + sqrStatusName + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
