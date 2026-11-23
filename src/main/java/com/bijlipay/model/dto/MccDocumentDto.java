package com.bijlipay.model.dto;

import com.bijlipay.model.UploadedDocuments;

import java.util.List;

public class MccDocumentDto {

    private List<UploadedDocuments> mccAllDocuments;
    private List<UploadedDocuments> mccRejectDocuments;

    public List<UploadedDocuments> getMccAllDocuments() {
        return mccAllDocuments;
    }

    public void setMccAllDocuments(List<UploadedDocuments> mccAllDocuments) {
        this.mccAllDocuments = mccAllDocuments;
    }

    public List<UploadedDocuments> getMccRejectDocuments() {
        return mccRejectDocuments;
    }

    public void setMccRejectDocuments(List<UploadedDocuments> mccRejectDocuments) {
        this.mccRejectDocuments = mccRejectDocuments;
    }
}
