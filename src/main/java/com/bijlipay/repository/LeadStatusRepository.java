package com.bijlipay.repository;

import com.bijlipay.model.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadStatusRepository extends JpaRepository<LeadStatus,Long>{
    @Query("SELECT l FROM LeadStatus l WHERE l.leadStatusId =?1")
    LeadStatus findLeadStatusById(long agreementSigningCompleted);
    @Query("SELECT l FROM LeadStatus l WHERE l.leadStatusId =?1")
    LeadStatus getStatus(long qrAgreementPending);

}
