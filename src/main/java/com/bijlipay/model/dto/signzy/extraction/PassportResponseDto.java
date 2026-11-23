package com.bijlipay.model.dto.signzy.extraction;

import java.util.List;

public class PassportResponseDto {
    private List<String> files;
    private String id;
    private SignzyInstanceDto instance;
    private PassportResultDto result;

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

    public PassportResultDto getResult() {
        return result;
    }

    public void setResult(PassportResultDto result) {
        this.result = result;
    }
}
