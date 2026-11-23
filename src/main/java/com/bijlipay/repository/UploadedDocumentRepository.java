package com.bijlipay.repository;

import com.bijlipay.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UploadedDocumentRepository extends JpaRepository<UploadedDocuments, Long> {

    @Transactional
    @Modifying
    @Query("delete from UploadedDocuments u where u.leadDocuments=?1")
    void deleteLeadDocumentsRejected(LeadDocuments documents);

    @Query("SELECT u FROM UploadedDocuments u WHERE u.leadDocuments = ?1")
    List<UploadedDocuments> findByDocumentId(LeadDocuments leadDocuments);
    @Transactional
    @Modifying
    @Query("DELETE FROM UploadedDocuments u WHERE u.leadDocuments = ?1")
    void deleteByLeadDocId(LeadDocuments leadDocId);

    @Query("SELECT u FROM UploadedDocuments u WHERE u.uploadedDocumentId = ?1")
    UploadedDocuments findByUpdloadedDocumentId(long uploadedDocumentId);
    @Query("SELECT u FROM UploadedDocuments u WHERE u.leadDocuments.leadInformation =?1")
    List<UploadedDocuments> findByLeadId(LeadInformation leadInfo);
    @Query("SELECT u FROM UploadedDocuments u WHERE u.leadDocuments IN (?1)")
    List<UploadedDocuments> findByLeadDocumentId(List<LeadDocuments> leadDocuments);

    @Query("select d from UploadedDocuments d where d.leadDocuments.leadInformation =?1 and d.leadDocuments.subDocumentType = ?2 order by d.leadDocuments.updatedAt desc")
    List<UploadedDocuments> findDocumentByLeadAndDocumentType(LeadInformation leadInformation, SubDocumentType document);

    @Query("select d from UploadedDocuments d where d.leadDocuments.leadInformation =?1 and d.leadDocuments.subDocumentType IN (?2) order by d.leadDocuments.updatedAt desc")
    List<UploadedDocuments> findDocumentByLeadAndDocumentTypeList(LeadInformation leadInformation, List<SubDocumentType> document);

    @Query("select d from UploadedDocuments d where d.leadDocuments.leadInformation =?1 and d.leadDocuments.subDocumentType.documentCategory in (?2) order by d.leadDocuments.updatedAt desc")
    List<UploadedDocuments> findAuthorizerDocument(LeadInformation leadInformation, DocumentCategory document);

}
