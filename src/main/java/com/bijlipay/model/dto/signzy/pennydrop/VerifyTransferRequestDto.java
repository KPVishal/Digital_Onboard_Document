package com.bijlipay.model.dto.signzy.pennydrop;

public class VerifyTransferRequestDto {
    private String task;
    private VerifyTransferEssentialDto essentials;

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
}
