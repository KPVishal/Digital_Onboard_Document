package com.bijlipay.model.dto.signzy.pennydrop;

public class AccountTransferRequestDto {
    private String task;
    private AccountTransferEssentialDto essentials;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public AccountTransferEssentialDto getEssentials() {
        return essentials;
    }

    public void setEssentials(AccountTransferEssentialDto essentials) {
        this.essentials = essentials;
    }
}
