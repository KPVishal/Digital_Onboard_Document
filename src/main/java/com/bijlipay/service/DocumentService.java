package com.bijlipay.service;

import com.bijlipay.model.*;
import com.bijlipay.model.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.parser.ParseException;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DocumentService {

    UploadedDocuments storeLeadDocuments(LeadInformation lead, FileDto fileDtoList, SubDocumentType subDocumentType, String typeOfDocument, Optional<String> latlng);

    List<UploadedDocuments> findByLeadDocumentId(LeadDocuments leadDocuments);

    Pair<Integer, Object> verifyDocuments(SubDocumentType subDocumentType, DocumentVerifyDto documentVerifyDto) throws ParseException, IOException;

    DocumentType getDocumentDetails(long documentType);

    LeadInformation checkLead(long leadInformation);

    void delete(LeadDocuments leadDocId);

    SubDocumentType getSubDocumentDetails(long documentType);

    void deleteAttachment(String fileName);

    List<DocumentType> getCategoryBasedDocumentType(DocumentCategory documentCategory);

    SignzyFetchAndVerification getSignzyDetails(String requestId);

    SignzyFetchAndVerification saveCallBackDetails(SignzyFetchAndVerification fetchAndVerification);

    Pair<Integer, Object> getDataFromSignzy(SubDocumentType subDocumentType, DocumentVerifyDto documentVerifyDto, SignzyFetchAndVerification verification) throws ParseException, JsonProcessingException;

    MultipartFile convertByteArrayOutputStreamToMultiPartFile(ByteArrayOutputStream byteArrayOutputStream, String fileName, String contentType);

    List<UploadedDocuments> findByLeadId(LeadInformation leadInfo);

    AgreementDetails findByToken(String token);

    AgreementDetails storePdfFile(MultipartFile multipartFile,LeadInformation leadInformation,boolean qrFlag);

    LeadInformation findLeadByLeadId(Long leadId);

    List<SignzyFetchAndVerification> getPanDetails(List<UploadedDocuments> uploadedDocumentsList);

    List<SignzyFetchAndVerification> getGSTDetails(List<UploadedDocuments> uploadedDocumentsList);

    AgreementDetails findAgreementDetailsByLeadId(LeadInformation leadInformation);
    List<AgreementDetails> findAgreementDetailsByPosAndQr(LeadInformation leadInformation);

    LeadInformation updateLeadStatus(long leadId,long value);

    String storePdfFileForContract(MultipartFile multipartFile, LeadInformation leadInformation);

    List<LeadDocuments> documentRejectOrApproval(long leadId, DocumentType documentType, int action, DocumentUpdateDto documentUpdateDto, User curuser);

    boolean checkDocumentVerifiedBySignzy(LeadInformation leadInformation);

    List<LeadDocuments> reUploadOfDocument(long leadId, int action, DocumentUpdateDto documentUpdateDto);

    List<SubDocumentType> getSubDocumentTypeList(DocumentType documentType);

    List<UploadedDocuments> storeOnboardingLeads(DocumentDto documentDto);

    // store aggregator Documents
//    UploadedDocuments storeLeadAndUploadedDocuments(LeadInformation lead, FileDto file, SubDocumentType subDocumentType, String typeOfDocument);

    // store aggregator Documents
    UploadedDocuments storeLeadAndUploadedDocuments(LeadInformation lead, FileDto file, String subDocumentType, String typeOfDocument, String merchantDocumentTypeName, int isQr,SubDocumentType subDocumentTypes);

    List<LeadDocuments> aggDocumentApprovalOrReject(long leadId, Document documentType, int action, DocumentUpdateDto documentUpdateDto) throws MessagingException;

    LeadInformation findAggLeadNumber(String leadNumber);

    void aggLeadDelete(LeadInformation leadInformation);

    void deleteAllAttachment(List<String> fileNameList);

    UploadedDocuments updateAggregatorReuploadedDoc(String leadNumber, LeadDocumentDto leadDocumentDto);

    AgreementDetails findByContractId(String contractId);

    AgreementDetails findBySignerId(String signerId);

    boolean checkAllAggregatorDocumentsVerifiedByOps(LeadInformation leadInfo);

    Pair<Integer, Object> aggregatorLeadApproval(long leadId);

    boolean checkUploadedDocuments(LeadInformation leadInfo);

    AgreementDetails storeUploadedPdfFile(MultipartFile file, LeadInformation leadInformation) throws JsonProcessingException;

    AgreementDetails storeAggPdfFile(MultipartFile multipartFile, LeadInformation leadInformation);

    AgreementDetails manualUpload(LeadInformation leadInformation, AgreementDetailsDto detailsDto);

    AgreementDetails rejectManualAgreement(LeadInformation leadInformation,DocumentUpdateDto documentUpdateDto);

    List<LeadDocuments> findLeadDocument(LeadInformation leadInformation);

    LeadInformation updateResubmittedLead(String leadNumber, LeadDto leadDto);

    LeadInformation approveAgreementManually(long leadId);

    List<QrVpa> storeVpaByLead(VpaShareDto vpaShareDto) throws ParseException, JsonProcessingException;

    List<String> findsLeadByLeadId(long leadId);

    MarsDeviceDetails getMarsDeviceDetails(long leadId);

    int getCount(long leadId);

    QrVpa getQrVpa(long leadId, String l);

    LeadStatus getLeadStatus(long qrAgreementPending);

    AgreementDetails getAggrement(long leadId);


    List<LeadDocuments> aggDocumentApprovalOrRejectMcc(long leadId, long subDocumentType, int action, DocumentUpdateDto documentUpdateDto, User curuser) throws MessagingException;

    List<RiskMccNpci> getMccNpci();

    MerchantCategoryCode findMccCode1(String mcc);

    RiskMccNpci saveLead(RiskMccNpci riskMccNpci);

    LeadInformation saveStatus(long leadId);

    List<LeadDocuments> aggDocumentApprovalOrRejectWowPe(long leadId, long subDocType, int action, DocumentUpdateDto documentUpdateDto) throws MessagingException;
    DocumentMapping getDocument(long subDocumentType);

    UploadedDocuments storeLeadDocuments1(LeadInformation lead, FileDto fileDto, DocumentMapping subDocument, String typeOfDocument);

    LeadDocuments aggApprovalDocumentReject(long leadId, LeadDocuments documentType, DocumentUpdateDto documentUpdateDto);

    List<DirectorDTO> getAuthorizerDocuments(LeadInformation leadInformation);

    GstDocumentDTO getGst(LeadInformation leadInformation);

    DocumentCategory getDocumentCategory();

    List<SubDocumentType> getCategoryBasedDocument(DocumentCategory documentCategory);

    boolean verifyAdditionalDocuments(DocumentCategory documentCategory, long leadId);

    DocumentCategory getDocumentCategoryForOPS();

    DocumentCategory getDocumentCategoryForOfflinePayment();

    void deleteUnMaskedImage(String unMaskedFileName);

    int sms(String phn,String messageTemplate);
    UploadedDocuments storeDigitalLeadDocuments(LeadInformation lead, FileDto file, SubDocumentType subDocumentType, String typeOfDocument) ;

    List<MarsDeviceDetails> getMarsDeviceDetailsList(long leadId);

    }
