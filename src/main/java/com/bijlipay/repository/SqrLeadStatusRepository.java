package com.bijlipay.repository;

import com.bijlipay.model.LeadStatus;
import com.bijlipay.model.SqrLeadStatus;
import org.apache.tomcat.util.net.SendfileState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SqrLeadStatusRepository extends JpaRepository<SqrLeadStatus, Long> {

    @Query("select r from SqrLeadStatus r where r.sqrStatusName =?1")
    SqrLeadStatus findSqrStatus(String vpaStatus);
    @Query("select r from SqrLeadStatus r where r.sqrStatusName =?1")
    LeadStatus getStatus(long qrAgreementPending);
}
