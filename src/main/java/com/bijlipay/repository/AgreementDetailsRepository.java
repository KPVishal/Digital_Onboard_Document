package com.bijlipay.repository;

import com.bijlipay.model.AgreementDetails;
import com.bijlipay.model.LeadInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementDetailsRepository extends JpaRepository<AgreementDetails,Long> {
    @Query("SELECT a FROM AgreementDetails a WHERE a.token = ?1")
    AgreementDetails findByToken(String token);
    @Query("SELECT a FROM AgreementDetails a WHERE a.leadInformation = ?1 and a.isQrAgreement=0")
    AgreementDetails findByLeadId(LeadInformation leadInformation);
    @Query("SELECT a FROM AgreementDetails a WHERE a.leadInformation = ?1")
    List<AgreementDetails> findByLeadPosQr(LeadInformation leadInformation);

    @Query("SELECT a FROM AgreementDetails a WHERE a.contractId = ?1")
    AgreementDetails findByContractId(String contractId);

    @Query("SELECT a FROM AgreementDetails a WHERE a.signerId = ?1")
    AgreementDetails findBySignerId(String signerId);

    @Query("select a from AgreementDetails a where a.leadInformation.leadId =?1 and a.leadInformation.posQr=0 and a.isQrAgreement=0")
    AgreementDetails findLead(long leadId);

    @Query("SELECT a FROM AgreementDetails a WHERE a.leadInformation = ?1 and a.isQrAgreement=?2")
    AgreementDetails findByLeadId1(LeadInformation leadInformation, boolean qrFlag);

    @Query("select a from AgreementDetails a where a.leadInformation.leadId =?1 and a.leadInformation.posQr=0 and a.isQrAgreement=0")
    AgreementDetails findLead1(long leadId);
}
