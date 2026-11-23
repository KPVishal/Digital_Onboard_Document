package com.bijlipay.model.dto.signzy.fetch;

import com.bijlipay.model.dto.signzy.extraction.SignzySplitAddress;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailsOfDrivingLicenceDto {

    private String dateOfIssue;
    private String dateOfLastTransaction;
    private String status;
    private String lastTransactedAt;
    private String name;
    private String fatherOrHusbandName;
    private String address;
    private List<AddressListDto> addressList;
    private String photo;
    private SignzySplitAddress splitAddress;
    private List<CovDetailsDto> covDetails;

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getDateOfLastTransaction() {
        return dateOfLastTransaction;
    }

    public void setDateOfLastTransaction(String dateOfLastTransaction) {
        this.dateOfLastTransaction = dateOfLastTransaction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastTransactedAt() {
        return lastTransactedAt;
    }

    public void setLastTransactedAt(String lastTransactedAt) {
        this.lastTransactedAt = lastTransactedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherOrHusbandName() {
        return fatherOrHusbandName;
    }

    public void setFatherOrHusbandName(String fatherOrHusbandName) {
        this.fatherOrHusbandName = fatherOrHusbandName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<AddressListDto> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressListDto> addressList) {
        this.addressList = addressList;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public SignzySplitAddress getSplitAddress() {
        return splitAddress;
    }

    public void setSplitAddress(SignzySplitAddress splitAddress) {
        this.splitAddress = splitAddress;
    }

    public List<CovDetailsDto> getCovDetails() {
        return covDetails;
    }

    public void setCovDetails(List<CovDetailsDto> covDetails) {
        this.covDetails = covDetails;
    }
}
