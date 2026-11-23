package com.bijlipay.repository;

import com.bijlipay.model.LeadDocumentHistory;
import com.bijlipay.model.LeadDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadDocumentHistoryRepository extends JpaRepository<LeadDocumentHistory, Long > {
    @Query("SELECT l FROM LeadDocumentHistory l WHERE l.leadDocuments IN (?1)")
    List<LeadDocumentHistory> findAllByLeadDocIds(List<LeadDocuments> leadDocuments);
}
