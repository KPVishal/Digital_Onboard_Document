package com.bijlipay.model.dto.signzy.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EStampDto {
    private String type;
    private String stateCode;
    private String articleCode;
    private String firstPartyName;
    private String secondPartyName;
    private String stampDutyPaidBy;
    private String purposeOfStampDuty;
    private String stampDutyValue;
    private String considerationPrice;
    private String amount;
    private List<Integer> pageNo;
    private String location;
    private String custmonDefacement;
    private String message;
    private List<StampDetailsDto> stampDetails;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getFirstPartyName() {
        return firstPartyName;
    }

    public void setFirstPartyName(String firstPartyName) {
        this.firstPartyName = firstPartyName;
    }

    public String getSecondPartyName() {
        return secondPartyName;
    }

    public void setSecondPartyName(String secondPartyName) {
        this.secondPartyName = secondPartyName;
    }

    public String getStampDutyPaidBy() {
        return stampDutyPaidBy;
    }

    public void setStampDutyPaidBy(String stampDutyPaidBy) {
        this.stampDutyPaidBy = stampDutyPaidBy;
    }

    public String getPurposeOfStampDuty() {
        return purposeOfStampDuty;
    }

    public void setPurposeOfStampDuty(String purposeOfStampDuty) {
        this.purposeOfStampDuty = purposeOfStampDuty;
    }

    public String getStampDutyValue() {
        return stampDutyValue;
    }

    public void setStampDutyValue(String stampDutyValue) {
        this.stampDutyValue = stampDutyValue;
    }

    public String getConsiderationPrice() {
        return considerationPrice;
    }

    public void setConsiderationPrice(String considerationPrice) {
        this.considerationPrice = considerationPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Integer> getPageNo() {
        return pageNo;
    }

    public void setPageNo(List<Integer> pageNo) {
        this.pageNo = pageNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCustmonDefacement() {
        return custmonDefacement;
    }

    public void setCustmonDefacement(String custmonDefacement) {
        this.custmonDefacement = custmonDefacement;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StampDetailsDto> getStampDetails() {
        return stampDetails;
    }

    public void setStampDetails(List<StampDetailsDto> stampDetails) {
        this.stampDetails = stampDetails;
    }
}
