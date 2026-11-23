package com.bijlipay.model.dto.signzy.pennydrop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTransferEssentialDto {
    private String beneficiaryAccount;
    private String beneficiaryIFSC;
    private String beneficiaryMobile;
    private String beneficiaryName;
    private String remarks;
    private String nameMatchScore;
    private String nameFuzzy;
    private String email;

    public String getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public void setBeneficiaryAccount(String beneficiaryAccount) {
        this.beneficiaryAccount = beneficiaryAccount;
    }

    public String getBeneficiaryIFSC() {
        return beneficiaryIFSC;
    }

    public void setBeneficiaryIFSC(String beneficiaryIFSC) {
        this.beneficiaryIFSC = beneficiaryIFSC;
    }

    public String getBeneficiaryMobile() {
        return beneficiaryMobile;
    }

    public void setBeneficiaryMobile(String beneficiaryMobile) {
        this.beneficiaryMobile = beneficiaryMobile;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNameMatchScore() {
        return nameMatchScore;
    }

    public void setNameMatchScore(String nameMatchScore) {
        this.nameMatchScore = nameMatchScore;
    }

    public String getNameFuzzy() {
        return nameFuzzy;
    }

    public void setNameFuzzy(String nameFuzzy) {
        this.nameFuzzy = nameFuzzy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
