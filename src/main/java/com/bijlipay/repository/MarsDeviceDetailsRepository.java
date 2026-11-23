package com.bijlipay.repository;

import com.bijlipay.model.LeadStatus;
import com.bijlipay.model.MarsDeviceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MarsDeviceDetailsRepository extends JpaRepository<MarsDeviceDetails,Long> {
    @Query("select l from MarsDeviceDetails l where l.leadInformation.leadId=?1 and l.sqrVpa IS NULL")
    MarsDeviceDetails getMarsDeviceDetails(long leadId);

    @Transactional
    @Modifying
    @Query("update MarsDeviceDetails l set l.deviceStatus=?2 where l.leadInformation.leadId=?1 and l.sqrVpa IS NULL")
    int updateMars(long leadId, int status);


    @Query("select l from MarsDeviceDetails l where l.leadInformation.leadId=?1")
    List<MarsDeviceDetails> getMarsDeviceDetailsList(long leadId);

}
