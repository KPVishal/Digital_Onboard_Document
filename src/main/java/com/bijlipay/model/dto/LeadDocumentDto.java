package com.bijlipay.model.dto;

import com.bijlipay.model.DocumentMapping;
import com.bijlipay.model.SubDocumentType;

public class LeadDocumentDto {
    private SubDocumentType subDocumentType;
    private String fileOriginalName;
    private String fileName;
    private String mimeType;
    private String typeOfDocument;
    private String imageUrl;
    private String contentType;
    private int documentVerifiedStatus;

    private String cancellationReason;
    private String documentRemarks;
    private String documentMappingName;

    private String merchantDocumentTypeName;

    private DocumentMapping documentMapping;

    private int isQR;

    public SubDocumentType getSubDocumentType() {
        return subDocumentType;
    }

    public void setSubDocumentType(SubDocumentType subDocumentType) {
        this.subDocumentType = subDocumentType;
    }

    public String getFileOriginalName() {
        return fileOriginalName;
    }

    public void setFileOriginalName(String fileOriginalName) {
        this.fileOriginalName = fileOriginalName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getTypeOfDocument() {
        return typeOfDocument;
    }

    public void setTypeOfDocument(String typeOfDocument) {
        this.typeOfDocument = typeOfDocument;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getDocumentVerifiedStatus() {
        return documentVerifiedStatus;
    }

    public void setDocumentVerifiedStatus(int documentVerifiedStatus) {
        this.documentVerifiedStatus = documentVerifiedStatus;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getDocumentRemarks() {
        return documentRemarks;
    }

    public void setDocumentRemarks(String documentRemarks) {
        this.documentRemarks = documentRemarks;
    }

    public String getDocumentMappingName() {
        return documentMappingName;
    }

    public void setDocumentMappingName(String documentMappingName) {
        this.documentMappingName = documentMappingName;
    }

    public DocumentMapping getDocumentMapping() {
        return documentMapping;
    }

    public void setDocumentMapping(DocumentMapping documentMapping) {
        this.documentMapping = documentMapping;
    }

    public String getMerchantDocumentTypeName() {
        return merchantDocumentTypeName;
    }

    public void setMerchantDocumentTypeName(String merchantDocumentTypeName) {
        this.merchantDocumentTypeName = merchantDocumentTypeName;
    }

    public int getIsQR() {
        return isQR;
    }

    public void setIsQR(int isQR) {
        this.isQR = isQR;
    }
}
