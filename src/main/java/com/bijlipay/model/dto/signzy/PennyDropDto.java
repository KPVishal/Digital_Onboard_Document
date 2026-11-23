package com.bijlipay.model.dto.signzy;

public class PennyDropDto {
    private String active;
    private String reason;
    private String amountMatch;
    private String name;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAmountMatch() {
        return amountMatch;
    }

    public void setAmountMatch(String amountMatch) {
        this.amountMatch = amountMatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
