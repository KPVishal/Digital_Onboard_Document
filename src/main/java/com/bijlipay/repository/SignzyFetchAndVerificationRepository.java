package com.bijlipay.repository;

import com.bijlipay.model.LeadDocuments;
import com.bijlipay.model.SignzyFetchAndVerification;
import com.bijlipay.model.UploadedDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SignzyFetchAndVerificationRepository extends JpaRepository<SignzyFetchAndVerification, Long> {

    @Query("SELECT s FROM SignzyFetchAndVerification s WHERE s.uploadedDocuments = ?1")
    SignzyFetchAndVerification findByUploadedDocumentId(UploadedDocuments uploadedDocuments);

    @Modifying
    @Transactional
    @Query("DELETE FROM SignzyFetchAndVerification WHERE uploadedDocuments = ?1 ")
    void deleteByUploadedDocId(UploadedDocuments uploadedDocuments);

    @Query("select s from SignzyFetchAndVerification s where s.request_id= ?1")
    SignzyFetchAndVerification findVerifiedDetails(String requestId);

    @Query("select s from SignzyFetchAndVerification s where s.uploadedDocuments.leadDocuments = ?1")
    SignzyFetchAndVerification fetchUpdatedDocumentStatus(LeadDocuments leadDocuments);
    @Query("SELECT s FROM SignzyFetchAndVerification s WHERE s.uploadedDocuments IN ?1 AND s.panDetails IS NOT NULL")
    List<SignzyFetchAndVerification> fetchPanDetails(List<UploadedDocuments> uploadedDocuments);
    @Query("SELECT s FROM SignzyFetchAndVerification s WHERE s.uploadedDocuments IN ?1 AND s.gstInDetails IS NOT NULL")
    List<SignzyFetchAndVerification> fetchGSTDetails(List<UploadedDocuments> uploadedDocumentsList);
}
