package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransformsDTO {
    @JsonProperty("Transform")
    private AlgorithmDTO transform;

    public AlgorithmDTO getTransform() {
        return transform;
    }

    public void setTransform(AlgorithmDTO transform) {
        this.transform = transform;
    }


    @Override
    public String toString() {
        return "TransformsDTO{" +
                "transform=" + transform +
                '}';
    }
}
