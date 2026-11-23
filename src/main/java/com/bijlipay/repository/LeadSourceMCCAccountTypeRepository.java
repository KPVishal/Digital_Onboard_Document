package com.bijlipay.repository;

import com.bijlipay.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeadSourceMCCAccountTypeRepository extends JpaRepository<LeadSourceMCCAccountTypeMapping, Long> {
    @Query("SELECT l FROM LeadSourceMCCAccountTypeMapping l WHERE l.leadSource=?1 and l.merchantCategoryCode =?2")
    LeadSourceMCCAccountTypeMapping findAccountType(LeadSource leadSource, MerchantCategoryCode mcc);
}
