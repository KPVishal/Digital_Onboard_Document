package com.bijlipay.model.dto.signzy.verification;

import java.util.List;

public class DLMoreInfo {
    private String issueDate;
    private String expiryDate;
    private List<String> vehicleClass;

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<String> getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(List<String> vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
}
