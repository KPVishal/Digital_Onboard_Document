package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceDTO {
    @JsonProperty("URI")
    private String uri;

    @JsonProperty("Transforms")
    private TransformsDTO transforms;

    @JsonProperty("DigestMethod")
    private AlgorithmDTO digestMethod;

    @JsonProperty("DigestValue")
    private String digestValue;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public TransformsDTO getTransforms() {
        return transforms;
    }

    public void setTransforms(TransformsDTO transforms) {
        this.transforms = transforms;
    }

    public AlgorithmDTO getDigestMethod() {
        return digestMethod;
    }

    public void setDigestMethod(AlgorithmDTO digestMethod) {
        this.digestMethod = digestMethod;
    }

    public String getDigestValue() {
        return digestValue;
    }

    public void setDigestValue(String digestValue) {
        this.digestValue = digestValue;
    }

    @Override
    public String toString() {
        return "ReferenceDTO{" +
                "uri='" + uri + '\'' +
                ", transforms=" + transforms +
                ", digestMethod=" + digestMethod +
                ", digestValue='" + digestValue + '\'' +
                '}';
    }
}
