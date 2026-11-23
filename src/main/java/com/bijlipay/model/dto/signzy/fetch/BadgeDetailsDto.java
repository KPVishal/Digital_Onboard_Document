package com.bijlipay.model.dto.signzy.fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BadgeDetailsDto {

    private String badgeIssueDate;
    private String badgeNo;
    private List<ClassOfVehicleDto> classOfVehicle;

    public String getBadgeIssueDate() {
        return badgeIssueDate;
    }

    public void setBadgeIssueDate(String badgeIssueDate) {
        this.badgeIssueDate = badgeIssueDate;
    }

    public String getBadgeNo() {
        return badgeNo;
    }

    public void setBadgeNo(String badgeNo) {
        this.badgeNo = badgeNo;
    }

    public List<ClassOfVehicleDto> getClassOfVehicle() {
        return classOfVehicle;
    }

    public void setClassOfVehicle(List<ClassOfVehicleDto> classOfVehicle) {
        this.classOfVehicle = classOfVehicle;
    }
}
