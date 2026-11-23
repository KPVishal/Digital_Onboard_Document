package com.bijlipay.service.implement;

import com.bijlipay.model.LeadDocumentHistory;
import com.bijlipay.model.LeadDocuments;
import com.bijlipay.repository.LeadDocumentHistoryRepository;
import com.bijlipay.service.LeadDocumentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LeadDocumentHistoryImpl implements LeadDocumentHistoryService {


    @Autowired
    private LeadDocumentHistoryRepository leadDocumentHistoryRepository;

    @Override
    public LeadDocumentHistory createLeadDocumentHistory(LeadDocuments leadDocuments, LeadDocumentHistory leadDocumentHistory) {
        leadDocumentHistory.setLeadDocuments(leadDocuments);
        leadDocumentHistory.setDateOfActivity(new Date());
        return leadDocumentHistoryRepository.save(leadDocumentHistory);
    }
}
