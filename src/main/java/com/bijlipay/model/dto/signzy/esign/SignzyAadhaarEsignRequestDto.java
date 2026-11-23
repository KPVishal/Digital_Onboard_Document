package com.bijlipay.model.dto.signzy.esign;

import java.util.List;

public class SignzyAadhaarEsignRequestDto {
    private String task;
    private String inputFile;
    private String name;
    private String uid;
    private String signatureType;
    private String signaturePosition;
    private int height;
    private int width;
    private String callbackUrl;
    private String redirectUrl;
    private String eventCallbackUrl;
    private String multiPages;
    private List<SignaturesDto> signatures;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getSignaturePosition() {
        return signaturePosition;
    }

    public void setSignaturePosition(String signaturePosition) {
        this.signaturePosition = signaturePosition;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<SignaturesDto> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignaturesDto> signatures) {
        this.signatures = signatures;
    }
}
