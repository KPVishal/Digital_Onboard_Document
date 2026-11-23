package com.bijlipay.model.dto.signzy.fetch;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CovDetailsDto {

    private String covCategory;
    private String classOfVehicle;
    private String covIssuedate;

    public String getCovCategory() {
        return covCategory;
    }

    public void setCovCategory(String covCategory) {
        this.covCategory = covCategory;
    }

    public String getClassOfVehicle() {
        return classOfVehicle;
    }

    public void setClassOfVehicle(String classOfVehicle) {
        this.classOfVehicle = classOfVehicle;
    }

    public String getCovIssuedate() {
        return covIssuedate;
    }

    public void setCovIssuedate(String covIssuedate) {
        this.covIssuedate = covIssuedate;
    }
}
