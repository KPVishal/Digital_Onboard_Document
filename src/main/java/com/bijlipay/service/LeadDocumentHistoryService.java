package com.bijlipay.service;

import com.bijlipay.model.LeadDocumentHistory;
import com.bijlipay.model.LeadDocuments;

public interface LeadDocumentHistoryService {

    LeadDocumentHistory createLeadDocumentHistory(LeadDocuments leadDocuments, LeadDocumentHistory leadDocumentHistory);
}
