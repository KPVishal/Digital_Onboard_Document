package com.bijlipay.model.dto.signzy.extraction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AadhaarResponseDto {
    private List<String> files;
    private String  id;
    private SignzyInstanceDto instance;
    private AadhaarResultDto result;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SignzyInstanceDto getInstance() {
        return instance;
    }

    public void setInstance(SignzyInstanceDto instance) {
        this.instance = instance;
    }

    public AadhaarResultDto getResult() {
        return result;
    }

    public void setResult(AadhaarResultDto result) {
        this.result = result;
    }
}
