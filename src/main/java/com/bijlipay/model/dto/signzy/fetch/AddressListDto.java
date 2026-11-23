package com.bijlipay.model.dto.signzy.fetch;

import com.bijlipay.model.dto.signzy.extraction.SignzySplitAddress;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressListDto {

    private String completeAddress;
    private String type;
    private SignzySplitAddress splitAddress;

    public String getCompleteAddress() {
        return completeAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SignzySplitAddress getSplitAddress() {
        return splitAddress;
    }

    public void setSplitAddress(SignzySplitAddress splitAddress) {
        this.splitAddress = splitAddress;
    }
}
