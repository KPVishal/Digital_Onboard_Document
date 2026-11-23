package com.bijlipay.model.dto.signzy.verification;

public class SignzyVerificationRequestDto {
    private String type;
    private String service;
    private String itemId;
    private String accessToken;
    private String task;
    private EssentialDto essentials;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public EssentialDto getEssentials() {
        return essentials;
    }

    public void setEssentials(EssentialDto essentials) {
        this.essentials = essentials;
    }
}
