package com.bijlipay.repository;

import com.bijlipay.model.LeadInformation;
import com.bijlipay.model.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import com.bijlipay.config.Constants;

@Repository
public interface LeadInformationRepository extends JpaRepository<LeadInformation, Long> {

    @Query("select i from LeadInformation i where i.leadId=?1")
    LeadInformation findByLeadId(long leadInformation);

    @Query("SELECT l FROM LeadInformation l WHERE l.leadNumber =?1")
    LeadInformation findByLeadNumber(String leadNumber);

    @Transactional
    @Modifying
    @Query("update LeadInformation l set l.leadStatus=?2,isActive=?3 where l.leadId=?1")
    int update(long leadId, LeadStatus fetchLeadStatusById,boolean active);

    @Transactional
    @Modifying
    @Query("update LeadInformation l set l.leadStatus=?2,isActive=?3 where l.leadId=?1")
    int updateMarsDetails(long leadId, LeadStatus fetchLeadStatusById,boolean active);


}
