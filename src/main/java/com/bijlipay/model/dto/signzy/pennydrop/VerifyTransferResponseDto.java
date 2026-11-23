package com.bijlipay.model.dto.signzy.pennydrop;

public class VerifyTransferResponseDto {
    private String task;
    private VerifyTransferEssentialDto essentials;
    private String id;
    private String patronId;
    private VerifyTransferResultDto result;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public VerifyTransferEssentialDto getEssentials() {
        return essentials;
    }

    public void setEssentials(VerifyTransferEssentialDto essentials) {
        this.essentials = essentials;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public VerifyTransferResultDto getResult() {
        return result;
    }

    public void setResult(VerifyTransferResultDto result) {
        this.result = result;
    }
}
