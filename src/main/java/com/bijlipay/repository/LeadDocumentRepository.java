package com.bijlipay.repository;

import com.bijlipay.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
public interface LeadDocumentRepository extends JpaRepository<LeadDocuments, Integer> {

//    @Query("select l from LeadDocuments l where l.leadInformation= ?1 and l.documentType =?2")
//    LeadDocuments isDocumentTypeExists(LeadInformation leadInformation, DocumentType documentType);
    @Query("SELECT l FROM LeadDocuments l WHERE l.leadInformation=?1 AND l.subDocumentType =?2")
    LeadDocuments isSubDocumentTypeExists(LeadInformation lead, SubDocumentType subDocumentType);

    @Transactional
    @Modifying
    @Query("update LeadDocuments set documentVerifiedStatus = ?2 where leadDocumentId =?1")
    void updateLeadDocumentStatus(int leadDocuments, int value);

    @Query("select l from LeadDocuments l where l.leadDocumentId = ?1")
    LeadDocuments fetchLeadDocument(int leadDocumentId);
    @Query("SELECT l FROM LeadDocuments l WHERE l.leadInformation = ?1")
    List<LeadDocuments> findByLeadId(LeadInformation leadInfo);

    @Query("select l from LeadDocuments l where l.leadInformation =?1 and l.subDocumentType.documentType =?2")
    List<LeadDocuments> fetchLeadDocumentByLeadAndDocumentType(LeadInformation leadId, DocumentType documentType);

    @Query("SELECT new map(l.documentVerifiedStatus as documentVerifiedStatus) FROM LeadDocuments l WHERE l.leadInformation=?1 AND l.subDocumentType IN (?2) ")
    List<HashMap<String, Integer>> getLeadDocumentListByLeadId(LeadInformation leadInformation,List<SubDocumentType> subDocumentType);

    @Query("select l from LeadDocuments l where l.subDocumentType in (?1) and l.leadInformation =?2")
    List<LeadDocuments> findDocumentsByLeadAndSubDocument(List<SubDocumentType> subDocumentTypes, LeadInformation leadInformation);
    @Query("select l from LeadDocuments l where l.leadInformation =?1 AND l.subDocumentType = ?2 order by l.updatedAt desc")
    List<LeadDocuments> getLeadDocumentsByLeadAndSubDocument(LeadInformation leadInformation, SubDocumentType l);
    @Query("select l from LeadDocuments l where l.leadInformation =?1 AND l.subDocumentType IN (?2) order by l.updatedAt desc")
    List<LeadDocuments> getLeadDocumentsByLeadAndSubDocumentList(LeadInformation leadInformation,List<SubDocumentType> subDocumentType);

    @Query("select l from LeadDocuments l where l.leadInformation =?1 and l.subDocumentType.documentType =?2 and l.documentVerifiedStatus not in (?3)")
    LeadDocuments fetchDocumentByLeadWithDocumentTypeAndStatus(LeadInformation leadInformation, DocumentType documentType, int value);

    @Query("SELECT l.subDocumentType FROM LeadDocuments l WHERE l.leadInformation =?1 GROUP BY l.subDocumentType")
    List<SubDocumentType> fetchSubDocumentsByLeadId(LeadInformation leadInfo);

    @Query("SELECT l FROM LeadDocuments l WHERE l.leadInformation =?1 AND l.subDocumentType =?2 ORDER BY l.createdAt DESC")
    List<LeadDocuments> getLeadDocumentsBySubDocType(LeadInformation leadInformation ,SubDocumentType subDocumentType);

    @Query("select l from LeadDocuments l where l.leadInformation =?1 and l.documentMapping in (?2)")
    List<LeadDocuments> findLeadDocumentsByLeadAndDocumentList(LeadInformation leadInfo, List<DocumentMapping> mappingList);

    @Query("select l from LeadDocuments l where l.leadInformation =?1 and l.documentMapping.document =?2")
    List<LeadDocuments> fetchLeadDocumentByLeadAndDocument(LeadInformation leadId, Document type);

    @Query("SELECT l.documentMapping FROM LeadDocuments l WHERE l.leadInformation =?1 GROUP BY l.documentMapping")
    List<DocumentMapping> fetchDocumentMappingByLeadId(LeadInformation leadInfo);

    @Query("SELECT l FROM LeadDocuments l WHERE l.leadInformation =?1 AND l.documentMapping =?2 ORDER BY l.createdAt DESC")
    List<LeadDocuments> getLeadDocumentsByDocumentMapping(LeadInformation leadInformation ,DocumentMapping documentMapping);

    @Query("SELECT l FROM LeadDocuments l WHERE l.leadInformation =?1 AND l.documentVerifiedStatus =?2")
    List<LeadDocuments> fetchLeadDocumentsByLead(LeadInformation leadInformation, int status);
    @Query("select l from LeadDocuments l where l.leadInformation =?1 and l.subDocumentType.subDocumentTypeId=?2")
    List<LeadDocuments> fetchLeadDocumentByLeadAndDocument1(LeadInformation leadInformation, long subDocumentType1);

    @Query("select l from LeadDocuments l where l.leadInformation = ?1 AND l.documentMapping =?2 ORDER BY l.createdAt DESC")
    List<LeadDocuments> fetchLeadDocumentByLeadAndDocumentMapping(LeadInformation lead, DocumentMapping subDocument);

    @Query("SELECT l.subDocumentType FROM LeadDocuments l WHERE l.leadInformation =?1 GROUP BY l.subDocumentType")
    Set<SubDocumentType> fetchSubDocuments(LeadInformation leadInfo);
    @Query("SELECT l FROM LeadDocuments l WHERE l.leadInformation.leadId = ?1 AND l.subDocumentType.documentCategory.id in (?2) ORDER BY l.createdAt DESC")
    List<LeadDocuments>  findLeadDocByCatagory(Long leadId, long doccatagory);

    @Query("SELECT l FROM LeadDocuments l WHERE l.leadInformation.leadId = ?1 AND l.subDocumentType.documentCategory.id in (?2) ORDER BY l.createdAt DESC")
    List<LeadDocuments>  findLeadDocByCatagoryList(Long leadId, List<Long> doccatagory);
}
