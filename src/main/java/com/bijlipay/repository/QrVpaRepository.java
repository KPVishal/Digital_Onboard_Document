package com.bijlipay.repository;

import com.bijlipay.model.QrVpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface QrVpaRepository extends JpaRepository<QrVpa, Integer> {
    @Query("select r.qrVpa from QrVpa r where r.qr_Lead_id.leadId=?1 and is_Active=0")
    List<String> getVpa(long leadId);

    @Query("select count(r.qrVpa) from QrVpa r where r.qr_Lead_id.leadId=?1 and is_Active=0")
    int getCount(long leadId);

    @Query(" select r from QrVpa r where r.qr_Lead_id.leadId=?1 and qrVpa=?2")
    QrVpa vpaById(long leadId, String l);

    @Transactional
    @Modifying
    @Query("delete from QrVpa r where r.qr_Lead_id.leadId=?1")
    void deleteByIds(long leadId);
}
