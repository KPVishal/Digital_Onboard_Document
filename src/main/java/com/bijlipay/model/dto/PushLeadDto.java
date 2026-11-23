package com.bijlipay.model.dto;

public class PushLeadDto {
    private String redirectTo;
    private String leadNumber;
    private Long leadId;
    private Long leadStatus;
    private Long notificationId;

    public PushLeadDto() {
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public Long getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Long leadStatus) {
        this.leadStatus = leadStatus;
    }
}
