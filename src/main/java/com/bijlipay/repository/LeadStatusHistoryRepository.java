package com.bijlipay.repository;

import com.bijlipay.model.LeadInformation;
import com.bijlipay.model.LeadStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadStatusHistoryRepository extends JpaRepository<LeadStatusHistory, Long> {

    @Query("select ls from LeadStatusHistory ls where ls.leadInformation =?1")
    LeadStatusHistory findCompletedDate(LeadInformation leadInformation);

    @Query("select ls from LeadStatusHistory ls where ls.leadInformation =?1")
    List<LeadStatusHistory> getLeadHistoryDetails(LeadInformation leadInfo);
}
