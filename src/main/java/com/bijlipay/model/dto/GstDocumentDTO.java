package com.bijlipay.model.dto;

import com.bijlipay.model.SubDocumentType;
import com.bijlipay.model.UploadedDocuments;

import java.util.List;

public class GstDocumentDTO {

    private List<SubDocumentType> subDocumentTypeList;
    private List<UploadedDocuments> uploadedDocuments;
    private List<UploadedDocuments> rejectedDocument;


    public List<SubDocumentType> getSubDocumentTypeList() {
        return subDocumentTypeList;
    }

    public void setSubDocumentTypeList(List<SubDocumentType> subDocumentTypeList) {
        this.subDocumentTypeList = subDocumentTypeList;
    }

    public List<UploadedDocuments> getUploadedDocuments() {
        return uploadedDocuments;
    }

    public void setUploadedDocuments(List<UploadedDocuments> uploadedDocuments) {
        this.uploadedDocuments = uploadedDocuments;
    }

    public List<UploadedDocuments> getRejectedDocument() {
        return rejectedDocument;
    }

    public void setRejectedDocument(List<UploadedDocuments> rejectedDocument) {
        this.rejectedDocument = rejectedDocument;
    }
}
