package com.bijlipay.model.dto.signzy.fetch;

public class SignzyFetchRequestDto {

    private String task;
    private EssentialFetchDto essentials;
    private String accessToken;
    private String userToken;
    private String type;
    private String callbackUrl;
    private String requestId;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public EssentialFetchDto getEssentials() {
        return essentials;
    }

    public void setEssentials(EssentialFetchDto essentials) {
        this.essentials = essentials;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
