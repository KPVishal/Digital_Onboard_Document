package com.bijlipay.model.dto;

import java.util.List;

public class DirectorDTO {

    private int authorizerNumber;
    private String name;
    private Documents documents;

    public static class Documents {
        private SubDocument pan;
        private List<SubDocument> uploadAnyOne;
        private SubDocument sign;

        // Getters and setters
        public SubDocument getPan() {
            return pan;
        }

        public void setPan(SubDocument pan) {
            this.pan = pan;
        }

        public List<SubDocument> getUploadAnyOne() {
            return uploadAnyOne;
        }

        public void setUploadAnyOne(List<SubDocument> uploadAnyOne) {
            this.uploadAnyOne = uploadAnyOne;
        }

        public SubDocument getSign() {
            return sign;
        }

        public void setSign(SubDocument sign) {
            this.sign = sign;
        }
    }

    public static class SubDocument {
        private long subDocumentTypeId;
        private String name;
        private String uploadedFileName;
        private boolean isUploaded;
        private long leadDocumentId;
        private int status;

        // Getters and setters
        public long getSubDocumentTypeId() {
            return subDocumentTypeId;
        }

        public void setSubDocumentTypeId(long subDocumentTypeId) {
            this.subDocumentTypeId = subDocumentTypeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUploadedFileName() {
            return uploadedFileName;
        }

        public void setUploadedFileName(String uploadedFileName) {
            this.uploadedFileName = uploadedFileName;
        }

        public boolean isUploaded() {
            return isUploaded;
        }

        public void setUploaded(boolean uploaded) {
            isUploaded = uploaded;
        }

        public long getLeadDocumentId() {
            return leadDocumentId;
        }

        public void setLeadDocumentId(long leadDocumentId) {
            this.leadDocumentId = leadDocumentId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    // Getters and setters for outer class
    public int getAuthorizerNumber() {
        return authorizerNumber;
    }

    public void setAuthorizerNumber(int authorizerNumber) {
        this.authorizerNumber = authorizerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents documents) {
        this.documents = documents;
    }


}

