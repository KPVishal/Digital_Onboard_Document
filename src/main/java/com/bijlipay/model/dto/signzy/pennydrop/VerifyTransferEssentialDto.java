package com.bijlipay.model.dto.signzy.pennydrop;

public class VerifyTransferEssentialDto {
    private String amount;
    private String signzyId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSignzyId() {
        return signzyId;
    }

    public void setSignzyId(String signzyId) {
        this.signzyId = signzyId;
    }
}
