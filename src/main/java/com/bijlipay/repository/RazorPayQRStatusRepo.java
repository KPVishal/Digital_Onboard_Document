package com.bijlipay.repository;

import com.bijlipay.model.RazorPayQRStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RazorPayQRStatusRepo extends JpaRepository<RazorPayQRStatus, Long> {
    @Query("select r from RazorPayQRStatus r where r.leadId=?1 and r.qrId=?2")
    RazorPayQRStatus getByLeadId(long leadId, String qr);
    @Query("select r from RazorPayQRStatus r where r.leadId=?1 order by created_at desc")
    List<RazorPayQRStatus> getByLead(long leadId);
}
