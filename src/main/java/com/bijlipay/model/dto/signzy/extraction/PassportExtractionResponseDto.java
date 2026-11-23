package com.bijlipay.model.dto.signzy.extraction;

import com.bijlipay.model.dto.signzy.verification.EssentialDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassportExtractionResponseDto {
    private String service;
    private String itemId;
    private String task;
    private EssentialDto essentials;
    private String accessToken;
    private String id;
    private PassportResponseDto response;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PassportResponseDto getResponse() {
        return response;
    }

    public void setResponse(PassportResponseDto response) {
        this.response = response;
    }
}
