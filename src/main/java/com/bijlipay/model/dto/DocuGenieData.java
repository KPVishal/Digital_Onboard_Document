package com.bijlipay.model.dto;

import org.json.JSONObject;

import java.util.Map;

public class DocuGenieData {

    private Map<String, Object> data;
    private long uploadedDocumentId;
    private long leadDocumentId;


    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public long getUploadedDocumentId() {
        return uploadedDocumentId;
    }

    public void setUploadedDocumentId(long uploadedDocumentId) {
        this.uploadedDocumentId = uploadedDocumentId;
    }

    public long getLeadDocumentId() {
        return leadDocumentId;
    }

    public void setLeadDocumentId(long leadDocumentId) {
        this.leadDocumentId = leadDocumentId;
    }
}
