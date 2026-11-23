package com.bijlipay.model.dto;

import com.bijlipay.model.*;
import com.bijlipay.property.FileConfig;

import java.util.List;

public class PdfEsignData {
    private String pan;
    private String gstin;
    private String natureOfBusiness;
    private List<Vas> vasList;
    private List<String> documentList;

    private List<UploadedDocuments> uploadedDocuments;

    private List<LeadDocuments> leadDocuments;

    private List<MerchantType> merchantTypeList;
    private int bankProof;  // 1.Bank Statement  2.Cancelled Cheque

    private String referenceNumber;

    private String paymentDate;
    private double amount;
    private InternalSubvention internalSubvention;

  private List<UploadedDocuments> allUploadedDocuments;

  private FileConfig fileConfig;

  private double offlineAmount;
  private String OfflineReferenceNo;
  private String offlineDate;
    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public List<Vas> getVasList() {
        return vasList;
    }

    public void setVasList(List<Vas> vasList) {
        this.vasList = vasList;
    }

    public List<MerchantType> getMerchantTypeList() {
        return merchantTypeList;
    }

    public void setMerchantTypeList(List<MerchantType> merchantTypeList) {
        this.merchantTypeList = merchantTypeList;
    }

    public List<String> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<String> documentList) {
        this.documentList = documentList;
    }

    public List<UploadedDocuments> getUploadedDocuments() {
        return uploadedDocuments;
    }

    public void setUploadedDocuments(List<UploadedDocuments> uploadedDocuments) {
        this.uploadedDocuments = uploadedDocuments;
    }

    public List<LeadDocuments> getLeadDocuments() {
        return leadDocuments;
    }

    public void setLeadDocuments(List<LeadDocuments> leadDocuments) {
        this.leadDocuments = leadDocuments;
    }

    public int getBankProof() {
        return bankProof;
    }

    public void setBankProof(int bankProof) {
        this.bankProof = bankProof;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public InternalSubvention getInternalSubvention() {
        return internalSubvention;
    }

    public void setInternalSubvention(InternalSubvention internalSubvention) {
        this.internalSubvention = internalSubvention;
    }

    public List<UploadedDocuments> getAllUploadedDocuments() {
        return allUploadedDocuments;
    }

    public void setAllUploadedDocuments(List<UploadedDocuments> allUploadedDocuments) {
        this.allUploadedDocuments = allUploadedDocuments;
    }

    public FileConfig getFileConfig() {
        return fileConfig;
    }

    public void setFileConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    public double getOfflineAmount() {
        return offlineAmount;
    }

    public void setOfflineAmount(double offlineAmount) {
        this.offlineAmount = offlineAmount;
    }

    public String getOfflineReferenceNo() {
        return OfflineReferenceNo;
    }

    public void setOfflineReferenceNo(String offlineReferenceNo) {
        OfflineReferenceNo = offlineReferenceNo;
    }

    public String getOfflineDate() {
        return offlineDate;
    }

    public void setOfflineDate(String offlineDate) {
        this.offlineDate = offlineDate;
    }


}
