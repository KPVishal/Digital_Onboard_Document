package com.bijlipay.model.dto.signzy.verification;

import com.bijlipay.model.dto.SignzyErrorDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignzyVerificationResponseDto {
    private String service;
    private String itemId;
    private String task;
    private EssentialDto essentials;
    private String accessToken;
    private String id;
    private String type;
    private SignzyResponseDto response;
    private SignzyResultDto result;
    private SignzyErrorDto error;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SignzyResponseDto getResponse() {
        return response;
    }

    public void setResponse(SignzyResponseDto response) {
        this.response = response;
    }

    public SignzyResultDto getResult() {
        return result;
    }

    public void setResult(SignzyResultDto result) {
        this.result = result;
    }

    public SignzyErrorDto getError() {
        return error;
    }

    public void setError(SignzyErrorDto error) {
        this.error = error;
    }
}
