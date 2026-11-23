package com.bijlipay.model.dto.signzy.pennydrop;

public class VerifyTransferResultDto {
    private String amountMatch;
    private String owerName;
    private String mobile;
    private String mmid;

    public String getAmountMatch() {
        return amountMatch;
    }

    public void setAmountMatch(String amountMatch) {
        this.amountMatch = amountMatch;
    }

    public String getOwerName() {
        return owerName;
    }

    public void setOwerName(String owerName) {
        this.owerName = owerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMmid() {
        return mmid;
    }

    public void setMmid(String mmid) {
        this.mmid = mmid;
    }
}
