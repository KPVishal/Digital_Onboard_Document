package com.bijlipay.repository;

import com.bijlipay.model.LeadVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadVerificationRepository extends JpaRepository<LeadVerificationStatus,Long> {


}
