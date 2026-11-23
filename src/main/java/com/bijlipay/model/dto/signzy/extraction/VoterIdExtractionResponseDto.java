package com.bijlipay.model.dto.signzy.extraction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoterIdExtractionResponseDto {
    private String service;
    private String itemId;
    private String task;
    private Object essentials;
    private String accessToken;
    private String id;
    private VoterIdResponseDto response;

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

    public Object getEssentials() {
        return essentials;
    }

    public void setEssentials(Object essentials) {
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

    public VoterIdResponseDto getResponse() {
        return response;
    }

    public void setResponse(VoterIdResponseDto response) {
        this.response = response;
    }
}
