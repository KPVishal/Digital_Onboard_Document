package com.bijlipay.model.dto.signzy;

public class SignzyUpdateDto {

    private boolean isVerified;
    private String message;
    private String url;
    private String requestId;

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "SignzyUpdateDto{" +
                "isVerified=" + isVerified +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
