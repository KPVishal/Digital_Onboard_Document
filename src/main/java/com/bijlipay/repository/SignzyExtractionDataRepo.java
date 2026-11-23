package com.bijlipay.repository;

import com.bijlipay.model.SignzyExtractionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SignzyExtractionDataRepo extends JpaRepository<SignzyExtractionData,Long> {

    @Query("select m from SignzyExtractionData m where m.leadId=?1 and subDocument=?2")
    SignzyExtractionData findByleadAndSubDocument(long leadId, long subDocumentType);
}
