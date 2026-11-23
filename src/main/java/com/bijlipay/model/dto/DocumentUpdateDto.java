package com.bijlipay.model.dto;

import com.bijlipay.model.LeadCancellationReason;

public class DocumentUpdateDto {

    private LeadCancellationReason cancellationReason;
    private String documentRemarks;

    public LeadCancellationReason getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(LeadCancellationReason cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getDocumentRemarks() {
        return documentRemarks;
    }

    public void setDocumentRemarks(String documentRemarks) {
        this.documentRemarks = documentRemarks;
    }
}
