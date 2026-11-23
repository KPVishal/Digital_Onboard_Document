package com.bijlipay.model.dto.signzy.identity;

import com.bijlipay.model.dto.SignzyErrorDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignzyIdentityResponseDto {
    private String type;
    private String email;
    private String callBackUrl;
    private String[] images;
    private String[] autoRecognition;
    private String[] verification;
    private String[] forgeryCheck;
    private String accessToken;
    private String id;
    private String patronId;
    private SignzyErrorDto error;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getAutoRecognition() {
        return autoRecognition;
    }

    public void setAutoRecognition(String[] autoRecognition) {
        this.autoRecognition = autoRecognition;
    }

    public String[] getVerification() {
        return verification;
    }

    public void setVerification(String[] verification) {
        this.verification = verification;
    }

    public String[] getForgeryCheck() {
        return forgeryCheck;
    }

    public void setForgeryCheck(String[] forgeryCheck) {
        this.forgeryCheck = forgeryCheck;
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

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public SignzyErrorDto getError() {
        return error;
    }

    public void setError(SignzyErrorDto error) {
        this.error = error;
    }
}
