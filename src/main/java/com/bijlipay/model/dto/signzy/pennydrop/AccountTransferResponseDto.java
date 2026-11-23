package com.bijlipay.model.dto.signzy.pennydrop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTransferResponseDto {
    private String task;
    private AccountTransferEssentialDto essentials;
    private String id;
    private String patronId;
    private AccountTransferResultDto result;

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

    public AccountTransferResultDto getResult() {
        return result;
    }

    public void setResult(AccountTransferResultDto result) {
        this.result = result;
    }
}
