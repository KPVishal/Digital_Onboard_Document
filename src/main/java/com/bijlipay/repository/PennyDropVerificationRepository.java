package com.bijlipay.repository;

import com.bijlipay.model.LeadInformation;
import com.bijlipay.model.PennyDropVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PennyDropVerificationRepository extends JpaRepository<PennyDropVerification , Long> {
    @Query("SELECT p FROM PennyDropVerification p WHERE p.leadInformation=?1")
    PennyDropVerification findByLeadId(LeadInformation leadInformation);
}
