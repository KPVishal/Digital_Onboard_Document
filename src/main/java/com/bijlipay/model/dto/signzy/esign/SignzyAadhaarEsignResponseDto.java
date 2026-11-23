package com.bijlipay.model.dto.signzy.esign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignzyAadhaarEsignResponseDto {
    private String task;
    private String id;
    private String uid;
    private String multiPages;
    private String customerId;
    private String inputFile;
    private String name;
    private String signatureType;
    private String callbackUrl;
    private String redirectUrl;
    private String eventCallbackUrl;
    private EsignResultDto result;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMultiPages() {
        return multiPages;
    }

    public void setMultiPages(String multiPages) {
        this.multiPages = multiPages;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getEventCallbackUrl() {
        return eventCallbackUrl;
    }

    public void setEventCallbackUrl(String eventCallbackUrl) {
        this.eventCallbackUrl = eventCallbackUrl;
    }

    public EsignResultDto getResult() {
        return result;
    }

    public void setResult(EsignResultDto result) {
        this.result = result;
    }
}
