package com.bijlipay.repository;

import com.bijlipay.model.OfflinePaymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OfflinePaymentRepo extends JpaRepository<OfflinePaymentData,Long> {
    @Modifying
    @Transactional
    @Query("delete from OfflinePaymentData m where m.id =?1")
    void deleteByIds(Long id);

    @Query("select m from OfflinePaymentData m where m.leadId =?1 order by createdAt desc")
    List<OfflinePaymentData> getByLeadId(long leadId);
}
