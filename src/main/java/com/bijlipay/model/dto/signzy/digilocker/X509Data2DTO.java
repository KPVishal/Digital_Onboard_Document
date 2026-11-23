package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class X509Data2DTO {
    @JsonProperty("X509SubjectName")
    private String[] X509SubjectName;

    @JsonProperty("X509Certificate")
    private String[] X509Certificate;

    public String[] getX509SubjectName() {
        return X509SubjectName;
    }

    public void setX509SubjectName(String[] x509SubjectName) {
        X509SubjectName = x509SubjectName;
    }

    public String[] getX509Certificate() {
        return X509Certificate;
    }

    public void setX509Certificate(String[] x509Certificate) {
        X509Certificate = x509Certificate;
    }

    @Override
    public String toString() {
        return "X509Data2DTO{" +
                "X509SubjectName=" + Arrays.toString(X509SubjectName) +
                ", X509Certificate=" + Arrays.toString(X509Certificate) +
                '}';
    }
}
