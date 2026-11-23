package com.bijlipay.model.dto.signzy.esign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsignResultDto {
    private String token;
    private String isUsed;
    private String url;
    private String inputFile;
    private String redirectUrl;
    private String email;
    private List<String> createSignatureOptions;
    private String redirectTime;
    private String callbackUrl;
    private String signatureType;
    private String name;
    private String showSignatureConsent;
    private String scaleSignature;;
    private String selectPage;
    private String signaturePosition;
    private String pageNo;
    private String expiryTimestamp;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getCreateSignatureOptions() {
        return createSignatureOptions;
    }

    public void setCreateSignatureOptions(List<String> createSignatureOptions) {
        this.createSignatureOptions = createSignatureOptions;
    }

    public String getRedirectTime() {
        return redirectTime;
    }

    public void setRedirectTime(String redirectTime) {
        this.redirectTime = redirectTime;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShowSignatureConsent() {
        return showSignatureConsent;
    }

    public void setShowSignatureConsent(String showSignatureConsent) {
        this.showSignatureConsent = showSignatureConsent;
    }

    public String getScaleSignature() {
        return scaleSignature;
    }

    public void setScaleSignature(String scaleSignature) {
        this.scaleSignature = scaleSignature;
    }

    public String getSelectPage() {
        return selectPage;
    }

    public void setSelectPage(String selectPage) {
        this.selectPage = selectPage;
    }

    public String getSignaturePosition() {
        return signaturePosition;
    }

    public void setSignaturePosition(String signaturePosition) {
        this.signaturePosition = signaturePosition;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(String expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }
}
