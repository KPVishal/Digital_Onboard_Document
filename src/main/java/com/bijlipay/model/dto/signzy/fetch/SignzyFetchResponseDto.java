package com.bijlipay.model.dto.signzy.fetch;

import com.bijlipay.model.dto.SignzyErrorDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignzyFetchResponseDto {

    private String id;
    private String patronId;
    private String task;
    private EssentialFetchDto essentials;
    private String accessToken;
    private String userToken;
    private String type;
    private SignzyFetchResultDto result;
    private SignzyErrorDto error;
    private String event;
    private String requestId;
    private String message;
    private String status;
    private String timestamp;
    private String aadharDetail;
    private String details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SignzyFetchResultDto getResult() {
        return result;
    }

    public void setResult(SignzyFetchResultDto result) {
        this.result = result;
    }

    public SignzyErrorDto getError() {
        return error;
    }

    public void setError(SignzyErrorDto error) {
        this.error = error;
    }
}
