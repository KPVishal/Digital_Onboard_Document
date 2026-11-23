package com.bijlipay.model.dto.signzy.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StampDetailsDto {
    private String stateCode;
    private String articleCode;
    private String stampDutyValue;
    private String purposeOfStampDuty;
    private String count;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getStampDutyValue() {
        return stampDutyValue;
    }

    public void setStampDutyValue(String stampDutyValue) {
        this.stampDutyValue = stampDutyValue;
    }

    public String getPurposeOfStampDuty() {
        return purposeOfStampDuty;
    }

    public void setPurposeOfStampDuty(String purposeOfStampDuty) {
        this.purposeOfStampDuty = purposeOfStampDuty;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
