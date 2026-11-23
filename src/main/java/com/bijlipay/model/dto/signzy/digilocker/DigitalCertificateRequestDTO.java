package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalCertificateRequestDTO {
    private String requestId;
    private boolean extraDigitalCertificateParams;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isExtraDigitalCertificateParams() {
        return extraDigitalCertificateParams;
    }

    public void setExtraDigitalCertificateParams(boolean extraDigitalCertificateParams) {
        this.extraDigitalCertificateParams = extraDigitalCertificateParams;
    }

    @Override
    public String toString() {
        return "DigitalCertificateRequestDTO{" +
                "requestId='" + requestId + '\'' +
                ", extraDigitalCertificateParams=" + extraDigitalCertificateParams +
                '}';
    }
}
