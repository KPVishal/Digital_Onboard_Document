package com.bijlipay.model.dto.signzy.extraction;

import java.util.List;

public class VoterIdResponseDto {
    private VoterIdResultDto result;
    private List<String> url;
    private String callbackUrl;
    private String instaneId;

    public VoterIdResultDto getResult() {
        return result;
    }

    public void setResult(VoterIdResultDto result) {
        this.result = result;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getInstaneId() {
        return instaneId;
    }

    public void setInstaneId(String instaneId) {
        this.instaneId = instaneId;
    }
}
