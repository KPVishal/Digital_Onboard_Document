package com.bijlipay.model.dto.signzy.extraction;

public class SignzyResponseDto {
    private String[] files;
    private long id;

    private SignzyInstanceDto instance;


    private SignzyResultDto result;

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SignzyInstanceDto getInstance() {
        return instance;
    }

    public void setInstance(SignzyInstanceDto instance) {
        this.instance = instance;
    }

    public SignzyResultDto getResult() {
        return result;
    }

    public void setResult(SignzyResultDto result) {
        this.result = result;
    }
}
