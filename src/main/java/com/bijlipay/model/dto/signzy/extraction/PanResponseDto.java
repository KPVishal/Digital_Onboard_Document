package com.bijlipay.model.dto.signzy.extraction;

import java.util.List;

public class PanResponseDto {
    private List<String> files;
    private String id;
    private String type;
    private SignzyInstanceDto instance;
    private PanResultDto result;

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

    public PanResultDto getResult() {
        return result;
    }

    public void setResult(PanResultDto result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
