package com.bijlipay.model.dto.signzy.pennydrop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankTransferDto {
    private String response;
    private String bankRRN;
    private String beneName;
    private String beneMMID;
    private String beneMobile;
    private String beneIFSC;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getBankRRN() {
        return bankRRN;
    }

    public void setBankRRN(String bankRRN) {
        this.bankRRN = bankRRN;
    }

    public String getBeneName() {
        return beneName;
    }

    public void setBeneName(String beneName) {
        this.beneName = beneName;
    }

    public String getBeneMMID() {
        return beneMMID;
    }

    public void setBeneMMID(String beneMMID) {
        this.beneMMID = beneMMID;
    }

    public String getBeneMobile() {
        return beneMobile;
    }

    public void setBeneMobile(String beneMobile) {
        this.beneMobile = beneMobile;
    }

    public String getBeneIFSC() {
        return beneIFSC;
    }

    public void setBeneIFSC(String beneIFSC) {
        this.beneIFSC = beneIFSC;
    }
}
