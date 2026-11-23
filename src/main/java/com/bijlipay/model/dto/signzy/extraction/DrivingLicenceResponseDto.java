package com.bijlipay.model.dto.signzy.extraction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrivingLicenceResponseDto {
    private List<String> files;
    private String id;
    private SignzyInstanceDto instance;
    private DrivingLicenceResultDto result;

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

    public DrivingLicenceResultDto getResult() {
        return result;
    }

    public void setResult(DrivingLicenceResultDto result) {
        this.result = result;
    }
}
