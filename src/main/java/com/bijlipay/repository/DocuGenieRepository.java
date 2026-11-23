package com.bijlipay.repository;

import com.bijlipay.model.DocuGenieResponse;
import com.bijlipay.model.LeadInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocuGenieRepository extends JpaRepository<DocuGenieResponse,Long> {

    @Query("select m from DocuGenieResponse m where m.leadInformation =?1")
    DocuGenieResponse getByIds(LeadInformation lead);
}
