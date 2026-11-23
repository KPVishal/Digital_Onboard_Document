package com.bijlipay.model.dto;

import com.bijlipay.model.MerchantDocumentType;

public class DocumentRejectDto {
    private String status;
    private String message;
    private int responseCode;
    private int merchantRefNumber;
    private String applicationNumber;
    private String institutionCode;
    private String data;
    private String remarks;
    private String document_type;
    private String document_Name;

    private long documentId;

    private int StatusCode;
    private String Remark;



    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getMerchantRefNumber() {
        return merchantRefNumber;
    }

    public void setMerchantRefNumber(int merchantRefNumber) {
        this.merchantRefNumber = merchantRefNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDocument_Name() {
        return document_Name;
    }

    public void setDocument_Name(String document_Name) {
        this.document_Name = document_Name;
    }


    public long getDocumentId() {
        return documentId;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "DocumentRejectDto{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", responseCode=" + responseCode +
                ", merchantRefNumber=" + merchantRefNumber +
                ", applicationNumber='" + applicationNumber + '\'' +
                ", institutionCode='" + institutionCode + '\'' +
                ", data='" + data + '\'' +
                ", remarks='" + remarks + '\'' +
                ", document_type=" + document_type +
                ", document_Name='" + document_Name + '\'' +

                ", documentId=" + documentId +
                ", StatusCode=" + StatusCode +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
