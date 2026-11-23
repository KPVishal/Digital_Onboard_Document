package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyInfoDTO {
    @JsonProperty("X509Data")
    private X509Data2DTO x509Data;

    public X509Data2DTO getX509Data() {
        return x509Data;
    }

    public void setX509Data(X509Data2DTO x509Data) {
        this.x509Data = x509Data;
    }

    @Override
    public String toString() {
        return "KeyInfoDTO{" +
                "x509Data=" + x509Data +
                '}';
    }
}
