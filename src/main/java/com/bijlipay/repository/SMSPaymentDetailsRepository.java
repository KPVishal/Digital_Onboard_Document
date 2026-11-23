package com.bijlipay.repository;

import com.bijlipay.model.LeadInformation;
import com.bijlipay.model.SMSPaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SMSPaymentDetailsRepository extends JpaRepository<SMSPaymentDetails, Integer> {

//    @Query("select s from SMSPaymentDetails s where s.smsReferenceId =?1")
//    Optional<SMSPaymentDetails> findBySMSRefId(String razorpay_payment_link_reference_id);

    @Query("select s from SMSPaymentDetails s where s.smsId =?1")
    Optional<SMSPaymentDetails> findBySMSRefId(String razorpay_payment_link_reference_id);

    @Query("SELECT s FROM SMSPaymentDetails s WHERE s.leadInformation = ?1 and s.paymentStatus = 1")
    List<SMSPaymentDetails> findByLeadId(LeadInformation leadId);

    @Query("select s from SMSPaymentDetails s where s.smsReferenceId =?1")
    SMSPaymentDetails findBySMSPayment(String referenceId);
}

