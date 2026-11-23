package com.bijlipay.service.implement;

import com.bijlipay.config.Constants;
import com.bijlipay.controller.BaseController;
import com.bijlipay.model.*;
import com.bijlipay.model.dto.*;
import com.bijlipay.model.dto.signzy.SignzyUpdateDto;
import com.bijlipay.model.dto.signzy.digilocker.AadhaarResponseDTO;
import com.bijlipay.model.dto.signzy.digilocker.DigilockerResponseDTO;
import com.bijlipay.model.dto.signzy.esign.EsignPdfResponseDto;
import com.bijlipay.model.dto.signzy.fetch.EssentialFetchDto;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchRequestDto;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchResponseDto;
import com.bijlipay.property.FileStorageProperties;
import com.bijlipay.repository.*;
import com.bijlipay.service.DocumentService;
import com.bijlipay.service.FileStorageService;
import com.bijlipay.service.SignzyService;
import com.bijlipay.util.*;
//import com.ctc.wstx.shaded.msv_core.util.Uri;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itextpdf.text.pdf.PdfReader;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.bijlipay.config.Constants.*;
import static com.bijlipay.config.Constants.LEAD_PENDING_OSV;

@Service
public class DocumentServiceImpl extends BaseController implements DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    private LeadDocumentRepository leadDocumentRepository;
    @Autowired
    private LeadInformationRepository leadInformationRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private SubDocumentTypeRepository subDocumentTypeRepository;
    @Autowired
    private UploadedDocumentRepository uploadedDocumentRepository;
    @Autowired
    private SignzyService signzyService;
    @Autowired
    private SignzyFetchAndVerificationRepository signzyFetchAndVerificationRepository;
    @Autowired
    private LeadDocumentHistoryRepository leadDocumentHistoryRepository;
    @Autowired
    private FileStorageProperties fileStorageProperties;
    @Autowired
    private AgreementDetailsRepository agreementDetailsRepository;
    @Autowired
    private LeadStatusRepository leadStatusRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private DocumentCategoryRepository documentCategoryRepository;

    @Autowired
    private LeadStatusHistoryRepository leadStatusHistoryRepository;

    @Autowired
    private LeadSourceRepo leadSourceRepo;

    @Autowired
    private LeadSourceMCCAccountTypeRepository leadSourceMCCAccountTypeRepository;

    @Autowired
    private MerchantCategoryCodeRepository merchantCategoryCodeRepository;

    @Autowired
    private DocumentMappingRepository documentMappingRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MarsRentalPlansRepository marsRentalPlansRepository;

    @Autowired
    private MarsMdrChargesRepository marsMdrChargesRepository;

    @Autowired
    private MerchantTypeRepository merchantTypeRepository;

    @Autowired
    private SqrLeadStatusRepository sqrLeadStatusRepository;

    @Autowired
    private MarsDeviceDetailsRepository marsDeviceDetailsRepository;


    @Autowired
    private QrVpaRepository qrVpaRepository;

    @Autowired
    private LanguageSoundboxRepository languageSoundboxRepository;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private AuditingTableRepository auditingTableRepository;
    @Autowired
    private RegionRepository regionRepository;


    @Value("${signzy.email}")
    private String signzyEmail;
    @Value("${signzy.call.back.url}")
    private String signzyCallBackUrl;
    @Value("${digilocker.call.back.url}")
    private String digilockerCallBackUrl;

    @Value("${agg.document.update.url}")
    private String documentUpdateUrl;
    @Value("${agg.subdocument.update.url}")
    private String subDocumentUpdateUrl;

    @Value("${signzy.file.upload.url}")
    private String signzyFileUploadUrl;

    @Value("${reject.manual.agreement}")
    private String rejectAgreementUrl;

    @Value("${call.back.url}")
    private String callBackUrl;

    @Value("${implementation.form.url}")
    private String implementationSmsUrl;

    @Autowired
    private AttributeEncryptor attributeEncryptor;

    @Autowired
    private ByteArrayMultiPartFileServiceImpl byteArrayMultiPartFileService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AggregatorDocumentMappingRepository aggregatorDocumentMappingRepository;

    @Autowired
    private MerchantDocumentTypeRepository merchantDocumentTypeRepository;

    @Autowired
    private AggregatorRepository aggregatorRepository;

    @Autowired
    private SimRepository simRepository;

    @Autowired
    private MailHandler mailHandler;
    @Autowired
    private RiskMccNpciRepo riskMccNpciRepo;

    @Override
    public UploadedDocuments storeLeadDocuments(LeadInformation lead, FileDto file, SubDocumentType subDocumentType, String typeOfDocument, Optional<String> latlng) {

//        LeadDocuments documents = leadDocumentRepository.isSubDocumentTypeExists(lead, subDocumentType);
        LeadDocuments documents = new LeadDocuments();
        UploadedDocuments response = null;

//        if (documents == null) {
//            documents = new LeadDocuments();
        logger.info("Document Status from lead --{}", lead.getLeadStatus().getLeadStatusId());
        documents.setSubDocumentType(subDocumentType);
        documents.setLeadInformation(lead);
        if (subDocumentType.isSignzyVerificationRequired()) {
            documents.setDocumentVerifiedStatus(Constants.DOCUMENT_VERIFICATION_PENDING);
        } else if ((lead.getLeadStatus().getLeadStatusId() == PENDING_MERCHANT_DETAILS  ||
                lead.getLeadStatus().getLeadStatusId() == PENDING_MCC_APPROVAL) && !(lead.isMccApproval())) {
            documents.setDocumentVerifiedStatus(MCC_VERIFICATION_PENDING);
        } else if ((lead.getLeadStatus().getLeadStatusId() == PENDING_MCC_DOCUMENTS_RESUBMISSION) && !(lead.isMccApproval())) {
            documents.setDocumentVerifiedStatus(MCC_DOCUMENT_RE_UPLOAD);
            lead.setDocumentStatus(1);
        } else {
            documents.setDocumentVerifiedStatus(Constants.OPS_VERIFICATION_PENDING);
            if (documents.getDocumentVerifiedStatus() == OPS_VERIFICATION_FAILED) {
                lead.setDocumentStatus(3);
            }
        }
        documents.setUploadedBy(getSessionUser());
        documents = leadDocumentRepository.save(documents);

        if (latlng.isPresent()) {
            documents.setLatlng(latlng.get());
            leadDocumentRepository.save(documents);
        }
        leadInformationRepository.save(lead);

        response = uploadedDocumentsFunction(documents, file, typeOfDocument);
        //Store document history table
        storeLeadDocumentActivityHistory(response, Constants.DOCUMENT_UPLOADED);

//        } else {
//            if (documents.isVerified() && reProcess == 1) {
//                uploadedDocumentRepository.deleteLeadDocumentsRejected(documents);
//            }
//            if (reProcess == 1) {
//                documents.setReprocess(true);
//            }
//            leadDocumentRepository.save(documents);
//            response = uploadedDocumentsFunction(documents, file, typeOfDocument);
        //Store document history table
//            storeLeadDocumentActivityHistory(response, Constants.DOCUMENT_REUPLOADED);

//        }
        // UploadedDocuments response = uploadedDocumentsFunction(documents, file, typeOfDocument);

        return response;
    }

    @Override
    public List<UploadedDocuments> findByLeadDocumentId(LeadDocuments leadDocuments) {
        List<UploadedDocuments> documents = uploadedDocumentRepository.findByDocumentId(leadDocuments);
        return documents;
    }

    @Override
    public void delete(LeadDocuments leadDocId) {
        List<UploadedDocuments> documents = uploadedDocumentRepository.findByDocumentId(leadDocId);
        logger.info("deleted");
        documents.forEach(l -> {
            if (l != null) {
                SignzyFetchAndVerification fetchAndVerification = signzyFetchAndVerificationRepository.findByUploadedDocumentId(l);
                if (fetchAndVerification != null) {
                    signzyFetchAndVerificationRepository.deleteByUploadedDocId(l);
                }
                this.deleteAttachment(l.getFileName());
                uploadedDocumentRepository.deleteByLeadDocId(leadDocId);
            }
        });
        storeLeadDocumentActivityHistory(documents.get(0), Constants.UPLOADED_DOCUMENT_DELETED);
    }

    @Override
    public void deleteAttachment(String fileName) {
        Path path = Paths.get(fileStorageProperties.getUploadDir());
        logger.info("Get uploaded directory to delete file path : {}", path);
        Path fileToDelete = path.resolve(fileName);
        logger.info("File name: {}", fileToDelete);
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SubDocumentType getSubDocumentDetails(long documentType) {
        return subDocumentTypeRepository.findBySubDocumentId(documentType);
    }

    private UploadedDocuments uploadedDocumentsFunction(LeadDocuments documents, FileDto file, String typeOfDocument) {
        //Create an entry in uploaded documents table
//        List<UploadedDocuments> uploadedDocumentsList = new ArrayList<>();
//        fileDtoList.forEach(file ->{
        UploadedDocuments uploadedDocuments = new UploadedDocuments();
        uploadedDocuments.setLeadDocuments(documents);
        uploadedDocuments.setFileName(file.getFileName());
        uploadedDocuments.setFileOriginalName(file.getFileNameOriginal());
        uploadedDocuments.setMimeType(file.getMimeType());
        uploadedDocuments.setTypeOfDocument(typeOfDocument);
        uploadedDocuments = uploadedDocumentRepository.save(uploadedDocuments);
//            uploadedDocumentsList.add(uploadedDocuments);
//        });
        return uploadedDocuments;
    }

    @Override
    public Pair<Integer, Object> verifyDocuments(SubDocumentType subDocumentType, DocumentVerifyDto documentVerifyDto) throws ParseException, IOException {

        HashMap<String, Object> hashMap = new HashMap<>();

        SignzyFetchAndVerification result = new SignzyFetchAndVerification();
        LeadDocuments leadDocuments;
        LeadDocuments documents;

        //store signzy data in signzyFetchAndVerification
        SignzyFetchAndVerification storeSignzyDetails = this.storeSignzyDetails(documentVerifyDto, subDocumentType);
        Gson gson = new Gson();

        // Login into Signzy for Online verification
        SignzyLoginResponseDto responseDto = signzyService.generateAccessToken();// to get access token
        logger.info("access token -- : {}", responseDto.getId());
        logger.info("patron Id -- : {}", responseDto.getUserId());
        if (responseDto.getId() != null) {
            logger.info("login success -- : ");
            SignzyFetchRequestDto signzyFetchRequestDto = new SignzyFetchRequestDto();

            if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_PAN)) {
                logger.info("Pan fetch -- : ");
                //Fetch Process
                signzyFetchRequestDto.setType(subDocumentType.getName());
                signzyFetchRequestDto.setAccessToken(responseDto.getId());
                signzyFetchRequestDto.setUserToken(responseDto.getUserId());
                EssentialFetchDto essentials = new EssentialFetchDto();
                essentials.setNumber(documentVerifyDto.getNumber());
                signzyFetchRequestDto.setEssentials(essentials);
                signzyFetchRequestDto.setTask("fetch");

                Pair<Integer, SignzyFetchResponseDto> response = signzyService.signzyFetch(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                logger.info("pan out -- : {}", response.getSecond());
                //De-Activating the Access Token
                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(responseDto.getId());
                logger.info("Sign out from Signzy -- : {}", responseEntity.getStatusCode().value());
                if (response.getSecond().getResult() != null) {
                    //store signzy fetched data in signzyFetchAndVerification
                    storeSignzyDetails.setPanFetched(response.getSecond().getResult().getIsValid());
//                    storeSignzyDetails.setPanDetails(gson.toJson(response.getSecond().getResult()));
                    storeSignzyDetails.setPanDetails(attributeEncryptor.encrypt(gson.toJson(response.getSecond().getResult())));
                    storeSignzyDetails.setTypeOfHolder(response.getSecond().getResult().getTypeOfHolder());
                    storeSignzyDetails.setMessage(null);
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verified
                    int value = Constants.SIGNZY_VERIFICATION_SUCCESS;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);

                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_SUCCESS);

                    return Pair.of(1, hashMap);
                } else {
                    //store signzy fetch & Verification failed data
                    storeSignzyDetails.setPanFetched("false");
                    storeSignzyDetails.setMessage(response.getSecond().getError().getMessage() == null ? null : response.getSecond().getError().getMessage());
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verification failed
                    int value = Constants.SIGNZY_VERIFICATION_FAILED;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_FAILURE);
                    return Pair.of(0, hashMap);
                }
            } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_VOTER_ID)) {
                logger.info("Voter Id -- : ");
                //Fetch Process
                signzyFetchRequestDto.setType(subDocumentType.getName());
                signzyFetchRequestDto.setAccessToken(responseDto.getId());
                signzyFetchRequestDto.setUserToken(responseDto.getUserId());
                EssentialFetchDto essentials = new EssentialFetchDto();
                essentials.setEpicNumber(documentVerifyDto.getEpicNumber());
                essentials.setState(documentVerifyDto.getState());
                signzyFetchRequestDto.setEssentials(essentials);
                signzyFetchRequestDto.setTask("epicNumberSearch");

                Pair<Integer, SignzyFetchResponseDto> response = signzyService.signzyFetch(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                logger.info("Voter Id -- : {}", response.getSecond().getResult());
                //De-Activating the Access Token
                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(responseDto.getId());
                logger.info("Sign out from Signzy -- : {}", responseEntity.getStatusCode().value());
                if (response.getSecond().getResult() != null) {
                    String name = response.getSecond().getResult().getName();
                    logger.info("name -- : {}", name);

                    //store signzy fetched data in signzyFetchAndVerification
                    storeSignzyDetails.setVoterIdName(name);
                    storeSignzyDetails.setVoterIdFetched(true);
//                    storeSignzyDetails.setVoterIdDetails(gson.toJson(response.getSecond().getResult()));
                    storeSignzyDetails.setVoterIdDetails(attributeEncryptor.encrypt(gson.toJson(response.getSecond().getResult())));
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verified
                    int value = Constants.SIGNZY_VERIFICATION_SUCCESS;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
                    //Update back document
//                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
//                    documents.setDocumentVerifiedStatus(value);
//                    leadDocumentRepository.save(documents);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_SUCCESS);
                    return Pair.of(1, hashMap);
                } else {
                    //store signzy fetch failed data
                    storeSignzyDetails.setVoterIdFetched(false);
                    storeSignzyDetails.setMessage(response.getSecond().getError().getMessage() == null ? null : response.getSecond().getError().getMessage());
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verification failed
                    int value = Constants.SIGNZY_VERIFICATION_FAILED;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
                    //Update back document
//                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
//                    documents.setDocumentVerifiedStatus(value);
//                    leadDocumentRepository.save(documents);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_FAILURE);
                    return Pair.of(0, hashMap);
                }
            } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_PASSPORT)) {
                logger.info("pass port -- : ");
                //Fetch Process
                signzyFetchRequestDto.setType(subDocumentType.getName());
                signzyFetchRequestDto.setAccessToken(responseDto.getId());
                signzyFetchRequestDto.setUserToken(responseDto.getUserId());
                EssentialFetchDto essentialFetchDto = new EssentialFetchDto();
                essentialFetchDto.setFileNumber(documentVerifyDto.getFileNumber());
                essentialFetchDto.setDob(documentVerifyDto.getDob());
                signzyFetchRequestDto.setEssentials(essentialFetchDto);
                signzyFetchRequestDto.setTask("fetch");

                Pair<Integer, SignzyFetchResponseDto> response = signzyService.signzyFetch(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                logger.info("Pass port -- : {}", response.getSecond());
                //De-Activating the Access Token
                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(responseDto.getId());
                logger.info("Sign out from Signzy -- : {}", responseEntity.getStatusCode().value());
                if (response.getSecond().getResult() != null) {
                    String name = response.getSecond().getResult().getName();
                    logger.info("name -- : {}", name);

                    //store signzy fetched data in signzyFetchAndVerification
                    storeSignzyDetails.setPassportName(name);
                    storeSignzyDetails.setPassportFetched(true);
//                    storeSignzyDetails.setPassportDetails(gson.toJson(response.getSecond().getResult()));
                    storeSignzyDetails.setPassportDetails(attributeEncryptor.encrypt(gson.toJson(response.getSecond().getResult())));
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verified
                    int value = Constants.SIGNZY_VERIFICATION_SUCCESS;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
                    //Update back document
//                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
//                    documents.setDocumentVerifiedStatus(value);
//                    leadDocumentRepository.save(documents);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_SUCCESS);
                    return Pair.of(1, hashMap);
                } else {
                    //store signzy fetch failed data
                    storeSignzyDetails.setPassportFetched(false);
                    storeSignzyDetails.setMessage(response.getSecond().getError().getMessage() == null ? null : response.getSecond().getError().getMessage());
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verification failed
                    int value = Constants.SIGNZY_VERIFICATION_FAILED;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
                    //Update back document
//                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
//                    documents.setDocumentVerifiedStatus(value);
//                    leadDocumentRepository.save(documents);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_FAILURE);
                    return Pair.of(0, hashMap);
                }
            } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_DRIVING_LICENCE)) {
                logger.info(" Driving Licence -- : ");
                //Fetch Process
                signzyFetchRequestDto.setType(subDocumentType.getName());
                signzyFetchRequestDto.setAccessToken(responseDto.getId());
                signzyFetchRequestDto.setUserToken(responseDto.getUserId());
                EssentialFetchDto essentialFetchDto = new EssentialFetchDto();
                essentialFetchDto.setNumber(documentVerifyDto.getNumber());
                essentialFetchDto.setDob(documentVerifyDto.getDob());
                signzyFetchRequestDto.setEssentials(essentialFetchDto);
                signzyFetchRequestDto.setTask("fetch");

                Pair<Integer, SignzyFetchResponseDto> response = signzyService.signzyFetch(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                logger.info("Driving License -- : {}", response.getSecond());
                //De-Activating the Access Token
                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(responseDto.getId());
                logger.info("Sign out from Signzy -- : {}", responseEntity.getStatusCode().value());
                if (response.getSecond().getResult() != null) {
                    String date = response.getSecond().getResult().getDetailsOfDrivingLicence().getDateOfIssue();

                    //store signzy fetched data in signzyFetchAndVerification
                    storeSignzyDetails.setdLFetched(true);
                    storeSignzyDetails.setDlIssueDate(date);
//                    storeSignzyDetails.setDlDetails(gson.toJson(response.getSecond().getResult()));
                    storeSignzyDetails.setDlDetails(attributeEncryptor.encrypt(gson.toJson(response.getSecond().getResult())));
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verified
                    int value = Constants.SIGNZY_VERIFICATION_SUCCESS;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
                    //Update back document
//                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
//                    documents.setDocumentVerifiedStatus(value);
//                    leadDocumentRepository.save(documents);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_SUCCESS);
                    return Pair.of(1, hashMap);
                } else {
                    //store signzy fetch failed data
                    storeSignzyDetails.setdLFetched(false);
                    storeSignzyDetails.setMessage(response.getSecond().getError().getMessage() == null ? null : response.getSecond().getError().getMessage());
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verification failed
                    int value = Constants.SIGNZY_VERIFICATION_FAILED;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
                    //Update back document
//                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
//                    documents.setDocumentVerifiedStatus(value);
//                    leadDocumentRepository.save(documents);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_FAILURE);
                    return Pair.of(0, hashMap);
                }
            } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_GST_IN)) {
                logger.info("GST document -- : ");
                //Fetch Process
                signzyFetchRequestDto.setType(subDocumentType.getName());
                signzyFetchRequestDto.setAccessToken(responseDto.getId());
                signzyFetchRequestDto.setUserToken(responseDto.getUserId());
                EssentialFetchDto essentialFetchDto = new EssentialFetchDto();
                essentialFetchDto.setGstin(documentVerifyDto.getGstin());
                signzyFetchRequestDto.setEssentials(essentialFetchDto);
                signzyFetchRequestDto.setTask("gstinSearch");

                Pair<Integer, SignzyFetchResponseDto> response = signzyService.signzyFetch(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                logger.info("Driving License -- : {}", response.getSecond());
                //De-Activating the Access Token
                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(responseDto.getId());
                logger.info("Sign out from Signzy -- : {}", responseEntity.getStatusCode().value());
                if (response.getSecond().getResult() != null) {
                    //store signzy fetched data in signzyFetchAndVerification
                    storeSignzyDetails.setGstinFetched(true);
                    storeSignzyDetails.setMessage(null);
//                    storeSignzyDetails.setGstInDetails(gson.toJson(response.getSecond().getResult()));
                    storeSignzyDetails.setGstInDetails(attributeEncryptor.encrypt(gson.toJson(response.getSecond().getResult())));
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verified
                    int value = Constants.SIGNZY_VERIFICATION_SUCCESS;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_SUCCESS);

                    return Pair.of(1, hashMap);
                } else {
                    //store signzy fetch & Verification failed data in signzyFetchAndVerification
                    storeSignzyDetails.setGstinFetched(false);
                    storeSignzyDetails.setMessage(response.getSecond().getError() == null ? null : response.getSecond().getError().getMessage());
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verification failed
                    int value = Constants.SIGNZY_VERIFICATION_FAILED;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_FAILURE);
                    return Pair.of(0, hashMap);
                }
            } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {
                logger.info("Aadhaar card -- : ");
                //Digilocker
                signzyFetchRequestDto.setType(subDocumentType.getName());
                signzyFetchRequestDto.setAccessToken(responseDto.getId());
                signzyFetchRequestDto.setUserToken(responseDto.getUserId());
                signzyFetchRequestDto.setTask("url");
                signzyFetchRequestDto.setCallbackUrl(digilockerCallBackUrl);

//                Pair<Integer, SignzyFetchResponseDto> response = signzyService.digiLocker(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                Pair<Integer, DigilockerResponseDTO> response = signzyService.digiLockerNew(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                logger.info("Aadhaar out put -- : {}", response.getSecond());
                //De-Activating the Access Token
                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(responseDto.getId());
                logger.info("Sign out from Signzy -- : {}", responseEntity.getStatusCode().value());
                if (response.getSecond().getResult() != null) {
                    //store signzy fetched data in signzyFetchAndVerification
//                    storeSignzyDetails.setAadhaarUrl(response.getSecond().getResult().getUrl());
//                    storeSignzyDetails.setRequest_id(response.getSecond().getResult().getRequestId());
                    storeSignzyDetails.setAadhaarUrl(response.getSecond().getResult().getUrl());
                    storeSignzyDetails.setRequest_id(response.getSecond().getResult().getRequestId());
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verification pending
                    int value = Constants.SIGNZY_VERIFICATION_PENDING;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
                    //Update back document
//                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
//                    documents.setDocumentVerifiedStatus(value);
//                    leadDocumentRepository.save(documents);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_IN_PROCESS);
                    return Pair.of(1, hashMap);
                } else {
                    //store signzy fetch & Verification failed data in signzyFetchAndVerification
                    storeSignzyDetails.setAadhaarUrl(null);
                    storeSignzyDetails.setRequest_id(null);
//                    storeSignzyDetails.setMessage(response.getSecond().getError().getMessage() == null ? null : response.getSecond().getError().getMessage());
                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);

                    //Updating Lead Document table as document verification failed
                    int value = Constants.SIGNZY_VERIFICATION_FAILED;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
                    //Update back document
//                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
//                    documents.setDocumentVerifiedStatus(value);
//                    leadDocumentRepository.save(documents);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_FAILURE);
                    return Pair.of(0, hashMap);
                }
//            }else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {
//                logger.info("Aadhaar card -- : ");
//                //Digilocker
//                signzyFetchRequestDto.setType(subDocumentType.getName());
//                signzyFetchRequestDto.setAccessToken(responseDto.getId());
//                signzyFetchRequestDto.setUserToken(responseDto.getUserId());
//                signzyFetchRequestDto.setTask("url");
//                signzyFetchRequestDto.setCallbackUrl(digilockerCallBackUrl);
//
//                Pair<Integer, SignzyFetchResponseDto> response = signzyService.digiLocker(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
//                logger.info("Aadhaar out put -- : {}", response.getSecond());
//                //De-Activating the Access Token
//                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(responseDto.getId());
//                logger.info("Sign out from Signzy -- : {}", responseEntity.getStatusCode().value());
//                if (response.getSecond().getResult() != null) {
//                    //store signzy fetched data in signzyFetchAndVerification
//                    storeSignzyDetails.setAadhaarUrl(response.getSecond().getResult().getUrl());
//                    storeSignzyDetails.setRequest_id(response.getSecond().getResult().getRequestId());
//                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);
//
//                    //Updating Lead Document table as document verification pending
//                    int value = Constants.SIGNZY_VERIFICATION_PENDING;
//                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
//                    leadDocuments.setDocumentVerifiedStatus(value);
//                    leadDocuments = leadDocumentRepository.save(leadDocuments);
//                    //Update back document
////                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
////                    documents.setDocumentVerifiedStatus(value);
////                    leadDocumentRepository.save(documents);
////                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
//                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
//                    hashMap = this.getVerifiedValues(result, subDocumentType);
//                    //store Document Activity History
//                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_IN_PROCESS);
//                    return Pair.of(1, hashMap);
//                } else {
//                    //store signzy fetch & Verification failed data in signzyFetchAndVerification
//                    storeSignzyDetails.setAadhaarUrl(null);
//                    storeSignzyDetails.setRequest_id(null);
//                    storeSignzyDetails.setMessage(response.getSecond().getError().getMessage() == null ? null : response.getSecond().getError().getMessage());
//                    result = signzyFetchAndVerificationRepository.save(storeSignzyDetails);
//
//                    //Updating Lead Document table as document verification failed
//                    int value = Constants.SIGNZY_VERIFICATION_FAILED;
//                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
//                    leadDocuments.setDocumentVerifiedStatus(value);
//                    leadDocuments = leadDocumentRepository.save(leadDocuments);
//                    //Update back document
////                    documents = leadDocumentRepository.fetchDocumentByLeadWithDocumentTypeAndStatus(leadDocuments.getLeadInformation(), leadDocuments.getSubDocumentType().getDocumentType(), value);
////                    documents.setDocumentVerifiedStatus(value);
////                    leadDocumentRepository.save(documents);
////                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
//                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
//                    hashMap = this.getVerifiedValues(result, subDocumentType);
//                    //store Document Activity History
//                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_FAILURE);
//                    return Pair.of(0, hashMap);
//                }
            }
        } else {
            logger.info("login failed -- :  ");
            return Pair.of(0, "");
        }
        return null;
    }

    private HashMap<String, Object> getVerifiedValues(SignzyFetchAndVerification result, SubDocumentType subDocumentType) {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("uploadedDocuments", result.getUploadedDocuments());
//        hashMap.put("remarks", result.getMessage());
        SignzyUpdateDto signzyUpdateDto = new SignzyUpdateDto();

        if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_PAN)) {

//            hashMap.put("panVerified",result.getPanVerified());
//            hashMap.put("panNumber",result.getPanNumber());
            signzyUpdateDto.setVerified(Boolean.parseBoolean(result.getPanFetched()));
            signzyUpdateDto.setMessage(result.getMessage());
            hashMap.put("signzyUpdateDto", signzyUpdateDto);

        } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {

//            hashMap.put("aadhaarNumber",result.getAadhaar_number());
//            hashMap.put("isAadhaarVerified",result.getAadhaarVerified());
            signzyUpdateDto.setVerified(Boolean.parseBoolean(result.getAadhaarVerified()));
            signzyUpdateDto.setUrl(result.getAadhaarUrl());
            signzyUpdateDto.setRequestId(result.getRequest_id());
            signzyUpdateDto.setMessage(result.getMessage());
            hashMap.put("signzyUpdateDto", signzyUpdateDto);


        } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_PASSPORT)) {

//            hashMap.put("passportName",result.getPassportName());
//            hashMap.put("passportDOB",result.getPassportDOB());
//            hashMap.put("passportFileNumber",result.getPassportFileNumber());
//            hashMap.put("isPassportVerified",result.getPassportVerified());
            signzyUpdateDto.setVerified(result.isPassportFetched());
            signzyUpdateDto.setMessage(result.getMessage());
            hashMap.put("signzyUpdateDto", signzyUpdateDto);

        } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_VOTER_ID)) {

//            hashMap.put("epicNumber",result.getVoterIdEpicNumber());
//            hashMap.put("voterIdName",result.getVoterIdName());
//            hashMap.put("voterIdState",result.getVoterIdState());
//            hashMap.put("isVoterIdVerified",result.isVoterIdVerified());
            signzyUpdateDto.setVerified(result.isVoterIdFetched());
            signzyUpdateDto.setMessage(result.getMessage());
            hashMap.put("signzyUpdateDto", signzyUpdateDto);

        } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_DRIVING_LICENCE)) {

//            hashMap.put("isDLVerified",result.getIsDLVerified());
//            hashMap.put("dLDOB",result.getDlDOB());
//            hashMap.put("dLIssueDate",result.getDlIssueDate());
//            hashMap.put("dLNumber",result.getDlNumber());
            signzyUpdateDto.setVerified(result.isdLFetched());
            signzyUpdateDto.setMessage(result.getMessage());
            hashMap.put("signzyUpdateDto", signzyUpdateDto);

        } else if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_GST_IN)) {

//            hashMap.put("gstInNumber",result.getGstinNumber());
//            hashMap.put("isGSTINVerified",result.isGstINVerified());
            signzyUpdateDto.setVerified(result.isGstinFetched());
            signzyUpdateDto.setMessage(result.getMessage());
            hashMap.put("signzyUpdateDto", signzyUpdateDto);

        }
        return hashMap;
    }

    private SignzyFetchAndVerification storeSignzyDetails(DocumentVerifyDto documentVerifyDto, SubDocumentType subDocumentType) {

        SignzyFetchAndVerification existsDetails = null;
        //verify whether the uploaded documents exists in SignzyFetchAndVerification
        if (documentVerifyDto.getUploadedDocuments() != null) {
            existsDetails = signzyFetchAndVerificationRepository.findByUploadedDocumentId(documentVerifyDto.getUploadedDocuments().get(0));
        }
        if (existsDetails == null) {

            SignzyFetchAndVerification fetchAndVerification = new SignzyFetchAndVerification();

            if (documentVerifyDto.getUploadedDocuments() != null) {
                UploadedDocuments uploadedDocuments = uploadedDocumentRepository.findByUpdloadedDocumentId(documentVerifyDto.getUploadedDocuments().get(0).getUploadedDocumentId());
                fetchAndVerification.setUploadedDocuments(uploadedDocuments);
            }
//            fetchAndVerification.setAadhaar_number(documentVerifyDto.getUid());
//            fetchAndVerification.setPanNumber(documentVerifyDto.getNumber());
//            fetchAndVerification.setVoterIdEpicNumber(documentVerifyDto.getEpicNumber());
//            fetchAndVerification.setVoterIdState(documentVerifyDto.getState());
//            fetchAndVerification.setPassportFileNumber(documentVerifyDto.getFileNumber());
//            fetchAndVerification.setPassportDOB(documentVerifyDto.getDob());
//            fetchAndVerification.setDlNumber(documentVerifyDto.getNumber());
//            fetchAndVerification.setDlDOB(documentVerifyDto.getDob());
//            fetchAndVerification.setGstinNumber(documentVerifyDto.getGstin());
            if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_AADHAAR_CARD)) {
                fetchAndVerification.setAadhaar_number(attributeEncryptor.encrypt(documentVerifyDto.getUid()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_PAN)) {
                fetchAndVerification.setPanNumber(attributeEncryptor.encrypt(documentVerifyDto.getNumber()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_VOTER_ID)) {
                fetchAndVerification.setVoterIdEpicNumber(attributeEncryptor.encrypt(documentVerifyDto.getEpicNumber()));
                fetchAndVerification.setVoterIdState(attributeEncryptor.encrypt(documentVerifyDto.getState()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_PASSPORT)) {
                fetchAndVerification.setPassportFileNumber(attributeEncryptor.encrypt(documentVerifyDto.getFileNumber()));
                fetchAndVerification.setPassportDOB(attributeEncryptor.encrypt(documentVerifyDto.getDob()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_DRIVING_LICENCE)) {
                fetchAndVerification.setDlNumber(attributeEncryptor.encrypt(documentVerifyDto.getNumber()));
                fetchAndVerification.setDlDOB(attributeEncryptor.encrypt(documentVerifyDto.getDob()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_GST_IN)) {
                fetchAndVerification.setGstinNumber(attributeEncryptor.encrypt(documentVerifyDto.getGstin()));
            }

            return signzyFetchAndVerificationRepository.save(fetchAndVerification);

        } else {

            if (documentVerifyDto.getUploadedDocuments() != null) {
                UploadedDocuments uploadedDocuments = uploadedDocumentRepository.findByUpdloadedDocumentId(documentVerifyDto.getUploadedDocuments().get(0).getUploadedDocumentId());
                existsDetails.setUploadedDocuments(uploadedDocuments);
            }
//            existsDetails.setAadhaar_number(documentVerifyDto.getUid());
//            existsDetails.setPanNumber(documentVerifyDto.getNumber());
//            existsDetails.setVoterIdEpicNumber(documentVerifyDto.getEpicNumber());
//            existsDetails.setVoterIdState(documentVerifyDto.getState());
//            existsDetails.setPassportFileNumber(documentVerifyDto.getFileNumber());
//            existsDetails.setPassportDOB(documentVerifyDto.getDob());
//            existsDetails.setDlNumber(documentVerifyDto.getNumber());
//            existsDetails.setDlDOB(documentVerifyDto.getDob());
//            existsDetails.setGstinNumber(documentVerifyDto.getGstin());
            if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_AADHAAR_CARD)) {
                existsDetails.setAadhaar_number(attributeEncryptor.encrypt(documentVerifyDto.getUid()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_PAN)) {
                existsDetails.setPanNumber(attributeEncryptor.encrypt(documentVerifyDto.getNumber()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_VOTER_ID)) {
                existsDetails.setVoterIdEpicNumber(attributeEncryptor.encrypt(documentVerifyDto.getEpicNumber()));
                existsDetails.setVoterIdState(attributeEncryptor.encrypt(documentVerifyDto.getState()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_PASSPORT)) {
                existsDetails.setPassportFileNumber(attributeEncryptor.encrypt(documentVerifyDto.getFileNumber()));
                existsDetails.setPassportDOB(attributeEncryptor.encrypt(documentVerifyDto.getDob()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_DRIVING_LICENCE)) {
                existsDetails.setDlNumber(attributeEncryptor.encrypt(documentVerifyDto.getNumber()));
                existsDetails.setDlDOB(attributeEncryptor.encrypt(documentVerifyDto.getDob()));
            } else if (subDocumentType.getName().equalsIgnoreCase(FETCH_TYPE_GST_IN)) {
                existsDetails.setGstinNumber(attributeEncryptor.encrypt(documentVerifyDto.getGstin()));
            }

            return signzyFetchAndVerificationRepository.save(existsDetails);
        }


    }

    @Override
    public DocumentType getDocumentDetails(long documentType) {
        return documentTypeRepository.findByDocumentId(documentType);
    }

    @Override
    public LeadInformation checkLead(long leadInformation) {
        return leadInformationRepository.findByLeadId(leadInformation);
    }

    @Override
    public List<DocumentType> getCategoryBasedDocumentType(DocumentCategory documentCategory) {
        List<DocumentType> result = subDocumentTypeRepository.fetchDocumentTypeList(documentCategory);
        return result;
    }

    @Override
    public SignzyFetchAndVerification getSignzyDetails(String requestId) {
        return signzyFetchAndVerificationRepository.findVerifiedDetails(requestId);
    }

    @Override
    public SignzyFetchAndVerification saveCallBackDetails(SignzyFetchAndVerification fetchAndVerification) {
        LeadDocuments leadDocuments;
        if (fetchAndVerification.getAadhaarVerified().equalsIgnoreCase(Constants.IS_SUCCESS)) {
            //Updating Lead Document table as document verified
            int value = Constants.SIGNZY_VERIFICATION_SUCCESS;
            leadDocuments = leadDocumentRepository.fetchLeadDocument(fetchAndVerification.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
            leadDocuments.setDocumentVerifiedStatus(value);
            leadDocumentRepository.save(leadDocuments);
        } else {
            //Updating Lead Document table as document verification failed
            int value = Constants.SIGNZY_VERIFICATION_SUCCESS;
            leadDocuments = leadDocumentRepository.fetchLeadDocument(fetchAndVerification.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
            leadDocuments.setDocumentVerifiedStatus(value);
            leadDocumentRepository.save(leadDocuments);
        }
        return signzyFetchAndVerificationRepository.save(fetchAndVerification);
    }

    @Override
    public Pair<Integer, Object> getDataFromSignzy(SubDocumentType subDocumentType, DocumentVerifyDto documentVerifyDto, SignzyFetchAndVerification verification) throws ParseException, JsonProcessingException {
        HashMap<String, Object> hashMap = new HashMap<>();

        SignzyFetchAndVerification result = new SignzyFetchAndVerification();
        LeadDocuments leadDocuments;

        Gson gson = new Gson();

        // Login into Signzy for Online verification
        SignzyLoginResponseDto responseDto = signzyService.generateAccessToken();// to get access token
        logger.info("access token -- : {}", responseDto.getId());
        logger.info("patron Id -- : {}", responseDto.getUserId());
        if (responseDto.getId() != null) {
            logger.info("login success -- : ");
            SignzyFetchRequestDto signzyFetchRequestDto = new SignzyFetchRequestDto();

            if (subDocumentType.getName().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {
                logger.info("Aadhaar details fetch -- : ");
                signzyFetchRequestDto.setType(subDocumentType.getName());
                signzyFetchRequestDto.setAccessToken(responseDto.getId());
                signzyFetchRequestDto.setUserToken(responseDto.getUserId());
                EssentialFetchDto essentials = new EssentialFetchDto();
                essentials.setRequestId(documentVerifyDto.getRequestId());
                signzyFetchRequestDto.setEssentials(essentials);
                signzyFetchRequestDto.setTask("getDetails");
                signzyFetchRequestDto.setRequestId(documentVerifyDto.getRequestId());

//                Pair<Integer, SignzyFetchResponseDto> response = signzyService.getAadhaarDetails(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                Pair<Integer, AadhaarResponseDTO> response = signzyService.getAadhaarDetailsNew(signzyFetchRequestDto, documentVerifyDto.getUploadedDocuments());
                logger.info("Aadhaar details from Signzy -- : {}", response.getSecond());
                //De-Activating the Access Token
                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(responseDto.getId());
                logger.info("Sign out from Signzy -- : {}", responseEntity.getStatusCode().value());
                if (response.getSecond().getResult() != null) {
                    //store signzy fetched data in signzyFetchAndVerification
                    verification.setAadhaarVerified("true");
                    verification.setMessage(null);
//                    verification.setAadhaarDetails(gson.toJson(response.getSecond().getResult()));
                    verification.setAadhaarDetails(attributeEncryptor.encrypt(gson.toJson(response.getSecond().getResult())));
                    result = signzyFetchAndVerificationRepository.save(verification);

                    //Updating Lead Document table as document verified
                    int value = Constants.SIGNZY_VERIFICATION_SUCCESS;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    logger.info("lead document update !");
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);

                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_SUCCESS);

                    return Pair.of(1, hashMap);
                } else {
                    //store signzy fetch & Verification failed data
                    verification.setAadhaarVerified("false");
                    verification.setMessage(null);
                    result = signzyFetchAndVerificationRepository.save(verification);

                    //Updating Lead Document table as document verification failed
                    int value = Constants.SIGNZY_VERIFICATION_FAILED;
                    leadDocuments = leadDocumentRepository.fetchLeadDocument(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId());
                    leadDocuments.setDocumentVerifiedStatus(value);
                    leadDocuments = leadDocumentRepository.save(leadDocuments);
//                    leadDocumentRepository.updateLeadDocumentStatus(result.getUploadedDocuments().getLeadDocuments().getLeadDocumentId(), value);
                    result = signzyFetchAndVerificationRepository.fetchUpdatedDocumentStatus(leadDocuments);
                    hashMap = this.getVerifiedValues(result, subDocumentType);
                    //store Document Activity History
                    storeLeadDocumentActivityHistory(result.getUploadedDocuments(), Constants.DOCUMENT_VERIFICATION_FAILURE);
                    return Pair.of(0, hashMap);
                }
            }
        } else {
            logger.info("login failed -- :  ");
            return Pair.of(0, "");
        }
        return null;
    }

    @Override
    public MultipartFile convertByteArrayOutputStreamToMultiPartFile(ByteArrayOutputStream byteArrayOutputStream, String fileName, String contentType) {
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new ByteArrayMultiPartFile("file", fileName, contentType, byteArray);
    }

    @Override
    public List<UploadedDocuments> findByLeadId(LeadInformation leadInfo) {
        List<LeadDocuments> leadDocuments = leadDocumentRepository.findByLeadId(leadInfo);
//        return uploadedDocumentRepository.findByLeadId(leadInfo);
        return uploadedDocumentRepository.findByLeadDocumentId(leadDocuments);
    }

    @Override
    public LeadInformation findLeadByLeadId(Long leadId) {
        return leadInformationRepository.findByLeadId(leadId);
    }

    @Override
    public List<SignzyFetchAndVerification> getPanDetails(List<UploadedDocuments> uploadedDocumentsList) {
        return signzyFetchAndVerificationRepository.fetchPanDetails(uploadedDocumentsList);
    }

    @Override
    public List<SignzyFetchAndVerification> getGSTDetails(List<UploadedDocuments> uploadedDocumentsList) {
        return signzyFetchAndVerificationRepository.fetchGSTDetails(uploadedDocumentsList);
    }

    @Override
    public AgreementDetails findAgreementDetailsByLeadId(LeadInformation leadInformation) {
        return agreementDetailsRepository.findByLeadId(leadInformation);
    }

    @Override
    public List<AgreementDetails> findAgreementDetailsByPosAndQr(LeadInformation leadInformation) {
        return agreementDetailsRepository.findByLeadPosQr(leadInformation);
    }


    @Override
    public LeadInformation updateLeadStatus(long leadId, long value) {
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        if (leadInformation != null) {
            LeadStatus status = leadStatusRepository.findLeadStatusById(value);
            if (status.getLeadStatusId() == AGREEMENT_SIGNING_COMPLETED) {
                leadInformation.setAgreementSuccess(true);
            } else {
                leadInformation.setAgreementSuccess(false);
            }
            leadInformation.setLeadStatus(status);
            leadInformation = leadInformationRepository.save(leadInformation);
            return leadInformation;
        }
        return null;
    }

    @Override
    public AgreementDetails findByToken(String token) {
        return agreementDetailsRepository.findByToken(token);
    }


    @Override
    public AgreementDetails storePdfFile(MultipartFile multipartFile, LeadInformation leadInformation, boolean qrFlag) {

        AgreementDetails agreementDetails = agreementDetailsRepository.findByLeadId1(leadInformation, qrFlag);
        if (agreementDetails != null) {
            return agreementDetails;
        } else {
            //store input file
            String newFileName = leadInformation.getLeadId() + "_" + multipartFile.getOriginalFilename() + "_" + "inputFile";
            Map<String, String> response = fileStorageService.storeFile(multipartFile, newFileName);

            AgreementDetails details = new AgreementDetails();
            details.setInputFileName(response.get("fileName"));
            details.setLeadInformation(leadInformation);
            details.setQrAgreement(qrFlag);
            return agreementDetailsRepository.save(details);

        }
    }

    @Override
    public String storePdfFileForContract(MultipartFile multipartFile, LeadInformation leadInformation) {

        //store input file
        String newFileName = leadInformation.getLeadId() + "_" + multipartFile.getOriginalFilename() + "_" + "inputFile";
        Map<String, String> response = fileStorageService.storeFile(multipartFile, newFileName);
        return response.get("fileName");
    }

    @Override
    public List<LeadDocuments> documentRejectOrApproval(long leadId, DocumentType documentType, int action, DocumentUpdateDto documentUpdateDto, User curuser) {
        List<LeadDocuments> documents = new ArrayList<>();
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        DocumentType type = documentTypeRepository.findByDocumentId(documentType.getId());
        List<LeadDocuments> lead = leadDocumentRepository.fetchLeadDocumentByLeadAndDocumentType(leadInformation, type);
        logger.info("document list -- : {}", lead.size());
        //Approval process
        if (action == Constants.LEAD_DOCUMENT_APPROVAL) {
            logger.info("approval process -- : ");
            lead.forEach(approve -> {
                LeadDocuments leadDocuments = new LeadDocuments();
                if (approve.getDocumentVerifiedStatus() == OPS_VERIFICATION_PENDING || approve.getDocumentVerifiedStatus() == SIGNZY_VERIFICATION_SUCCESS) {
                    approve.setDocumentVerifiedStatus(Constants.OPS_VERIFICATION_SUCCESS);
                    approve.setApprovedDate(DateUtil.currentDate());
                    leadDocuments = leadDocumentRepository.save(approve);
                    Auditing_Table auditingTable = new Auditing_Table();
                    auditingTable.setAction("LEAD DOCUMENT");
                    auditingTable.setLeadId(leadId);
                    auditingTable.setStatus("LEAD DOCUMENT APPROVED");
                    auditingTable.setUser(curuser);
                    auditingTable.setRequestDate(leadInformation.getCreatedAt());
                    auditingTable.setUpdatedDate(DateUtil.currentDate());
                    auditingTableRepository.save(auditingTable);
                }
//                approve.setApprovedBy(getSessionUser());
                documents.add(leadDocuments);
            });
            // String title= leadInformation.getMerchantName()+leadInformation.getLeadId()+"OPSDocumentApproved";
            // sendPushNotification(title, leadInformation, PUSH_NOTIFICATION_TO_DOCUMENT_APPROVED);

        }
        //Reject Process
        else if (action == Constants.LEAD_DOCUMENT_REJECT) {
            logger.info("reject process -- : ");
            lead.forEach(reject -> {
                LeadDocuments leadDocuments = new LeadDocuments();
                logger.info("document verifucation status -- : {}", reject.getDocumentVerifiedStatus());
                if (reject.getDocumentVerifiedStatus() == OPS_VERIFICATION_PENDING || reject.getDocumentVerifiedStatus() == OPS_VERIFICATION_SUCCESS || reject.getDocumentVerifiedStatus() == SIGNZY_VERIFICATION_SUCCESS) {
                    logger.info("rejected document type and status -- : {} {}", reject.getSubDocumentType(), reject.getDocumentVerifiedStatus());
                    reject.setDocumentVerifiedStatus(OPS_VERIFICATION_FAILED);
                    logger.info("rejected -- : {}", reject.getDocumentVerifiedStatus());
                    reject.setLeadCancellationReason(documentUpdateDto.getCancellationReason());
                    reject.setDocumentRemarks(documentUpdateDto.getDocumentRemarks());
                    reject.setRejectedBy(getSessionUser());
                    reject.setRejectedDate(DateUtil.currentDate());
                    leadDocuments = leadDocumentRepository.save(reject);
                    Auditing_Table auditingTable = new Auditing_Table();
                    if (documentType.getName().equalsIgnoreCase("NEFT/CHECK")) {
                        auditingTable.setAction("OFFLINE DOCUMENT");
                        auditingTable.setLeadId(leadId);
                        auditingTable.setStatus("OFFLINE DOCUMENT REJECTED");
                    }else {
                        auditingTable.setAction("LEAD DOCUMENT");
                        auditingTable.setLeadId(leadId);
                        auditingTable.setStatus("LEAD DOCUMENT REJECTED");
                    }
                    auditingTable.setUser(curuser);
                    auditingTable.setRequestDate(leadInformation.getCreatedAt());
                    auditingTable.setUpdatedDate(DateUtil.currentDate());
                    auditingTable.setRemark(documentUpdateDto.getDocumentRemarks());
                    auditingTableRepository.save(auditingTable);
                }
                documents.add(leadDocuments);
            });
            LeadInformation leadInformation1 = leadInformationRepository.findByLeadId(leadId);
            leadInformation1.setDocumentStatus(4);
            leadInformationRepository.save(leadInformation1);
            String reason = "Document has been Rejected by OPS";
            String title = "Document Rejection";
            logger.info("Document Rejection Push Notification ");
            sendPushNotification(title, reason, leadInformation, PUSH_NOTIFICATION_TO_DOCUMENT_REJECTED);
        }
        return documents;
    }

    @Override
    public boolean checkDocumentVerifiedBySignzy(LeadInformation leadInformation) {
        //fetch signzyVerificationRequired Document Category List type list
        List<Long> valueList = new ArrayList<>();
        valueList.add(DOCUMENT_CATEGORY_PROOF_OF_IDENTITY);
        valueList.add(DOCUMENT_CATEGORY_PROOF_OF_ADDRESS);
        if (!leadInformation.isWithOutGst()) {
            valueList.add(DOCUMENT_CATEGORY_PROOF_OF_BUSINESS);
        }
        logger.info("value -- : {}", valueList);

        List<DocumentCategory> documentCategories = documentCategoryRepository.fetchDocumentCategoryListById(valueList);
        boolean isSignzyVerificationRequired = true;
        for (DocumentCategory documentCategory : documentCategories) {
            if (documentCategory.getId() == DOCUMENT_CATEGORY_PROOF_OF_IDENTITY || documentCategory.getId() == DOCUMENT_CATEGORY_PROOF_OF_ADDRESS
                    || documentCategory.getId() == DOCUMENT_CATEGORY_PROOF_OF_BUSINESS) {
                List<SubDocumentType> documentTypeList = subDocumentTypeRepository.fetchSignzyVerificationRequiredSubDocTypeList(documentCategory, isSignzyVerificationRequired);
                List<LeadDocuments> leadDocuments = leadDocumentRepository.getLeadDocumentsByLeadAndSubDocumentList(leadInformation, documentTypeList);

                if (!leadDocuments.isEmpty()) {
                    logger.info("lead document size --:{} , subdocumentType -- : {}", leadDocuments.size(), leadDocuments.get(0).getSubDocumentType().getName());
                    if (leadDocuments.get(0).getDocumentVerifiedStatus() == SIGNZY_VERIFICATION_FAILED ||
                            leadDocuments.get(0).getDocumentVerifiedStatus() == SIGNZY_VERIFICATION_PENDING ||
                            leadDocuments.get(0).getDocumentVerifiedStatus() == DOCUMENT_VERIFICATION_PENDING) {
                        logger.info("verification failed in -- : {}", leadDocuments.get(0).getSubDocumentType().getName());
                        return false;
                    }
                } else {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public List<LeadDocuments> reUploadOfDocument(long leadId, int action, DocumentUpdateDto documentUpdateDto) {
        List<LeadDocuments> documentsList = new ArrayList<>();
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        if (leadInformation != null) {
            //Document Category details
            List<Integer> mccIndicator = new ArrayList<>();
            mccIndicator.add(MCC_CATEGORY_INDICATOR_BASIC);
            mccIndicator.add(MCC_CATEGORY_INDICATOR_BANK_APPROVAL);
//        mccIndicator.add(MCC_CATEGORY_INDICATOR_MCC);
//        mccIndicator.add(MCC_CATEGORY_INDICATOR_ALL_MCC);
            List<SubDocumentType> subDocumentTypes = subDocumentTypeRepository.findDocumentListByMccIndicator(mccIndicator);
            //fetch lead document with lead and sub document type
            List<LeadDocuments> leadDocuments = leadDocumentRepository.findDocumentsByLeadAndSubDocument(subDocumentTypes, leadInformation);
            if (action == 1) {
                logger.info("approval process -- : ");
                leadDocuments.forEach(approve -> {
                    LeadDocuments documents = new LeadDocuments();
                    approve.setReprocess(false);
                    documents = leadDocumentRepository.save(approve);
                    documentsList.add(documents);
                });

                if (leadInformation.getLeadStatus().getLeadStatusId() == PENDING_DOCUMENT_UPLOAD) {
                    leadInformation.setDocumentDetailsCompleteDate(DateUtil.currentDate());
                    LeadStatus status = this.checkLeadStatus(leadInformation);
                    leadInformation.setLeadStatus(status);
                    leadInformationRepository.save(leadInformation);
                }

            }
            //ReUpload Process
            else if (action == 2) {
                logger.info("reject process -- : ");
                leadDocuments.forEach(reject -> {
                    LeadDocuments documents = new LeadDocuments();
                    reject.setSoCancelReason(documentUpdateDto.getCancellationReason());
                    reject.setSoCancelRemarks(documentUpdateDto.getDocumentRemarks());
                    reject.setReprocess(true);
                    documents = leadDocumentRepository.save(reject);
                    documentsList.add(documents);
                });
            }
        }
        return documentsList;
    }

    private LeadStatus checkLeadStatus(LeadInformation leadInfo) {
        LeadStatus status = null;
        if (leadInfo.getLeadStatus().getLeadStatusId() == PENDING_ICA_APPROVAL) {
            status = leadStatusRepository.findLeadStatusById(PENDING_DOCUMENT_UPLOAD);
        } else if (leadInfo.getDocumentDetailsCompleteDate() == null) {
            status = leadStatusRepository.findLeadStatusById(PENDING_DOCUMENT_UPLOAD);
        } else if (leadInfo.getMdrDetailsCompleteDate() == null) {
            status = leadStatusRepository.findLeadStatusById(PENDING_MDR_AND_RENTAL_PLAN);
        } else if (leadInfo.getBizDetailsCompleteDate() == null) {
            status = leadStatusRepository.findLeadStatusById(PENDING_BIZ_DETAILS);
        } else if (leadInfo.getPaymentDetailsCompleteDate() == null) {
            status = leadStatusRepository.findLeadStatusById(PENDING_PAYMENT_DETAILS);
        } else if (leadInfo.getOsvDetailsCompletedDate() == null) {
            status = leadStatusRepository.findLeadStatusById(LEAD_PENDING_OSV);
        } else {
            status = leadStatusRepository.findLeadStatusById(PENDING_AGREEMENT_SIGNING);
        }
        return status;
    }

    @Override
    public List<SubDocumentType> getSubDocumentTypeList(DocumentType documentType) {
        return subDocumentTypeRepository.fetchSubDocumentTypeList(documentType);
    }

    @Override
    public List<UploadedDocuments> storeOnboardingLeads(DocumentDto documentDto) {

        if (documentDto != null) {
            LeadInformation leadInfo = leadInformationRepository.findByLeadNumber(documentDto.getLeadDto().getLeadNumber());
            logger.info("lead data ------------: {}", leadInfo);

//            if (leadInfo != null){
//                this.aggLeadDelete(leadInfo); // delete if exists
//            }

            LeadInformation leadInfo1 = leadInformationRepository.findByLeadNumber(documentDto.getLeadDto().getLeadNumber());
            logger.info("lead data 2 --------- : {}", leadInfo1);
            if (leadInfo1 == null) {
                Gson gson = new Gson();
                logger.info("lead data ------------: {}", gson.toJson(documentDto.getLeadDto()));
                LeadInformation leadInformation = this.storeFullLeadData(documentDto.getLeadDto());
                //vpa store
                if (documentDto.getLeadDto().getVpa() != null) {
                    QrVpa leadInformation1 = this.storeVpa(documentDto.getLeadDto());
                }
//                QrVpa leadInformation1=this.storeVpa(documentDto.getLeadDto());
                List<UploadedDocuments> model = new ArrayList<>();
                if (documentDto.getDocumentDtoList().size() > 0) {
                    documentDto.getDocumentDtoList().forEach(document -> {
                        try {
                            URL url = new URL(document.getImageUrl());
                            InputStream inputStream = url.openStream();
                            byte[] bytes = IOUtils.toByteArray(inputStream);

                            String fileName = document.getFileName();
                            String contentType = document.getContentType();

                            MultipartFile file = new ByteArrayMultiPartFile("file", fileName, contentType, bytes);

                            Map<String, String> response = fileStorageService.storeFileFromOnboardLead(file, fileName);

                            FileDto fileDto = new FileDto();
                            fileDto.setFileName(response.get("fileName"));
                            fileDto.setMimeType(response.get("mimeType"));
                            fileDto.setFileNameOriginal(response.get("fileNameOriginal"));
                            UploadedDocuments uploadedDocuments = this.storeLeadAndUploadedDocuments(leadInformation, fileDto, document.getDocumentMappingName(), document.getTypeOfDocument(), document.getMerchantDocumentTypeName(), document.getIsQR(), document.getSubDocumentType());
                            model.add(uploadedDocuments);
                        } catch (IOException e) {
                            e.printStackTrace();
                            logger.info("MalFormed Exception---- :{}", e.getMessage());
                        }
                    });
                    return model;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private QrVpa storeVpa(LeadDto leadDto) {
        leadDto.getVpa().forEach(l -> {
            QrVpa qrVpa = new QrVpa();
            LeadInformation leadInformation = leadInformationRepository.findByLeadNumber(leadDto.getLeadNumber());
            qrVpa.setQr_Lead_id(leadInformation);
            qrVpa.setQrVpa(l);
            qrVpa.getVpaStatus(sqrLeadStatusRepository.findSqrStatus("Qr_Pending"));
            qrVpaRepository.save(qrVpa);
        });
        return null;


    }


    private LeadInformation storeFullLeadData(LeadDto leadDto) {
        LeadInformation leadInformation = leadInformationRepository.findByLeadNumber(leadDto.getLeadNumber());
        logger.info("lead Info -------- : {}", leadInformation);
        try {
            if (leadInformation == null) {
                LeadInformation leadInfo = new LeadInformation();
                leadInfo.setMerchantName(leadDto.getMerchantName());
                leadInfo.setContactName(leadDto.getContactName());
                leadInfo.setContactEmail(leadDto.getEmail());
                leadInfo.setContactAddress(leadDto.getContactAddress());
                leadInfo.setContactNumber(leadDto.getContactNumber());
                if (leadDto.getLeadSource() != null) {
                    LeadSource leadSource = leadSourceRepo.findByLeadSourceName(leadDto.getLeadSource());
                    leadInfo.setLeadSource(leadSource);
                } else {
                    leadInfo.setLeadSource(null);
                }
                leadInfo.setMerchantType(merchantTypeRepository.findByName(leadDto.getMerchantType()));
                if (leadDto.getDevice() != null) {
                    leadInfo.setDevice(deviceRepository.findDevice(leadDto.getDevice()));
                } else {
                    leadInfo.setDevice(null);
                }
                leadInfo.setDeviceCount(Integer.valueOf(leadDto.getDeviceCount()));
                if (leadDto.getMcc() != null) {
                    leadInfo.setMcc(merchantCategoryCodeRepository.findByCode(leadDto.getMcc()));
                } else {
                    leadInfo.setMcc(null);
                }
                leadInfo.setLeadNumber(leadDto.getLeadNumber());
//        leadInfo.setOnboardingType(leadDto.getOnboardingType());
                leadInfo.setVasInstanceMapping(leadDto.getVasInstanceMapping());
                String input = leadDto.getVasInstanceMapping();
                boolean ica = false;
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        JSONArray jsonArray = new JSONArray(input);
                        String[] elements = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            elements[i] = jsonArray.getString(i);
                        }
                        for (String element : elements) {
                            if (Objects.equals(element, "ICA")) {
                                logger.info("elements-----------------------{}", element);
                                ica = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (ica) {
                    leadInfo.setIcaApproval(true);
                }
                leadInfo.setQR(leadDto.isQR());
                leadInfo.setSoundBox(leadDto.isSoundBox());
                leadInfo.setPos(leadDto.isPos());
                leadInfo.setPlan(leadDto.getPlan());
                leadInfo.setCorpCC(Double.valueOf(leadDto.getCorpCc()));
                logger.info("corp cc ----: {}", Double.valueOf(leadDto.getCorpCc()));
                leadInfo.setDebitLessThanAmount(Double.parseDouble(leadDto.getDebitLessthanAmount()));
                logger.info("debit less than Amount --- : {}", Double.parseDouble(leadDto.getDebitLessthanAmount()));
                leadInfo.setDebitGreaterThanAmount(Double.parseDouble(leadDto.getDebitGreaterthanAmount()));
                leadInfo.setIntlCC(Double.parseDouble(leadDto.getIntlCc()));
                leadInfo.setSuperPremiumCC(Double.parseDouble(leadDto.getSuperPremiumCc()));
                leadInfo.setStdCC(Double.parseDouble(leadDto.getStdCc()));
                leadInfo.setPremiumCC(Double.parseDouble(leadDto.getPremiumCc()));
                leadInfo.setSetUpFees(Double.parseDouble(leadDto.getSetupFees()));
                leadInfo.setRecurringFees(Double.parseDouble(leadDto.getRecurringFees()));
                leadInfo.setIcAcceptance(leadDto.isIcAcceptance());
                leadInfo.setTipPercentage(leadDto.getTipPercentage());
                if (leadDto.getSimProvider() != null) {
                    leadInfo.setSimProvider(simRepository.findSimByName(leadDto.getSimProvider()));
                } else {
                    leadInfo.setSimProvider(null);
                }
                leadInfo.setSettlementMode(leadDto.getSettlementMode());
                leadInfo.setOmcConvenienceFees(leadDto.isOmcConvenienceFees());
                leadInfo.setYearlyRevenue(leadDto.getYearlyRevenue());
                leadInfo.setLegalName(leadDto.getLegalName());
                leadInfo.setDbaName(leadDto.getDbaName());
                leadInfo.setShop_ownership_status(leadDto.getShop_ownership_status());
                leadInfo.setWeekDayTimingStart(leadDto.getWeekDayTimingStart());
                leadInfo.setWeekdayTimingEnd(leadDto.getWeekdayTimingEnd());
                leadInfo.setWeekendTimingStart(leadDto.getWeekendTimingStart());
                leadInfo.setWeekendTimingEnd(leadDto.getWeekendTimingEnd());
                leadInfo.setMaxMonthlyCardVolume(leadDto.getMaxMonthlyCardVolume());
                leadInfo.setAvgMonthlyCardvolume(leadDto.getAvgMonthlyCardvolume());
                leadInfo.setMemberSince(leadDto.getMemberSince());
                leadInfo.setEstablishYear(leadDto.getEstablishYear());
                leadInfo.setHavePosMpos(leadDto.getHavePosMpos());
                leadInfo.setDebitMdr(leadDto.getDebitMdr());
                leadInfo.setCreditMdr(leadDto.getCreditMdr());
                leadInfo.setAmountCollected(Double.parseDouble(leadDto.getAmountCollected()));
                leadInfo.setTransactionDate(leadDto.getTransactionDate());
                leadInfo.setPaymentOption(Integer.parseInt(leadDto.getPaymentOption()));
                leadInfo.setPaymentContactNumber(leadDto.getPaymentContactNumber());
                leadInfo.setPaymentSuccess(leadDto.isPaymentSuccess());
                leadInfo.setIfscCode(leadDto.getIfscCode());
                leadInfo.setBank(leadDto.getBank());
                leadInfo.setBranch(leadDto.getBranch());
//        leadInfo.setLeadSourceMCCAccountTypeMapping(leadDto.getLeadSourceMCCAccountTypeMapping());
                leadInfo.setAccountNumber(leadDto.getAccountNumber());
                leadInfo.setReferenceNumber(leadDto.getReferenceNumber());
                leadInfo.setLeadCategory(Integer.parseInt(leadDto.getLeadCategory()));
                leadInfo.setReason(leadDto.getReason());
                leadInfo.setPincode(leadDto.getPincode());
                leadInfo.setCity(leadDto.getCity());
                leadInfo.setState(leadDto.getState());
                leadInfo.setOwnerDOB(leadDto.getOwnerDOB());
                leadInfo.setOwnerFirstName(leadDto.getOwnerFirstName());
                leadInfo.setOwnerLastName(leadDto.getOwnerLastName());
                leadInfo.setMerchantStateName(leadDto.getMerchantStateName());
                leadInfo.setLeadStatus(leadStatusRepository.findLeadStatusById(LEAD_SUBMITTED_TID_PENDING));
                leadInfo.setOnboardingVendor(leadDto.getOnboardingVendor());
                leadInfo.setSettlementType(Integer.parseInt(leadDto.getSettlementType()));
                leadInfo.setActive(true);
                leadInfo.setTurnOverLastFinancialYear(leadDto.getTurnOverLastFinancialYear());
                leadInfo.setCreditCardBlock(leadDto.isCreditCardBlock());
                leadInfo.setAvgBillAmount(leadDto.getAvgBillAmount());
                leadInfo.setMdrPlanName(leadDto.getMdrPlanName());
                if (leadDto.getMcc() != null) {
                    MerchantCategoryCode merchantCategoryCode = merchantCategoryCodeRepository.findByCode(leadDto.getMcc());
                    leadInfo.setMcc(merchantCategoryCode);
                } else {
                    leadInfo.setMcc(null);
                }
                if (leadInfo.getMcc() != null && leadInfo.getLeadSource() != null) {
                    leadInfo.setLeadSourceMCCAccountTypeMapping(leadSourceMCCAccountTypeRepository.findAccountType(leadInfo.getLeadSource(), leadInfo.getMcc()));
                }
                if (leadDto.getMarsRentalPlans() != null) {
                    leadInfo.setMarsRentalPlans(marsRentalPlansRepository.findByName(leadDto.getMarsRentalPlans()));
                    leadInfo.setRentalPlanCode(leadDto.getMarsRentalPlans());
                } else {
                    leadInfo.setMarsRentalPlans(null);
                    leadInfo.setRentalPlanCode(null);
                }
                leadInfo.setMasterPass(leadDto.getMasterPass());
                leadInfo.setCashAtPos(leadDto.getCashAtPos());
                leadInfo.setDiners(leadDto.getDiners());
                leadInfo.setOnus(leadDto.getOnus());
                leadInfo.setInternationalCreditCard(leadDto.getInternationalCreditCard());
                leadInfo.setInternationalDebitCard(leadDto.getInternationalDebitCard());
                leadInfo.setCommercialOrCorporate(leadDto.getCommercialOrCorporate());
                leadInfo.setSuperPremiumOrSignature(leadDto.getSuperPremiumOrSignature());
                leadInfo.setPremiumOrPlatinum(leadDto.getPremiumOrPlatinum());
                leadInfo.setStandardOrClassic(leadDto.getStandardOrClassic());
                leadInfo.setDomesticDebitAbove2000(leadDto.getDomesticDebitAbove2000());
                leadInfo.setDomesticDebitUpTo2000(leadDto.getDomesticDebitUpTo2000());
                leadInfo.setUpiCreditUpto2000(leadDto.getUpiCreditUpto2000());
                logger.info("------------------getUpiCreditUpto2000------{}", leadDto.getUpiCreditUpto2000());
                leadInfo.setUpiCreditAbove2000(leadDto.getUpiCreditUpto2000());
                logger.info("------------------getUpiCreditUpto2000------{}", leadDto.getUpiCreditUpto2000());
                leadInfo.setUpiUpto2000(leadDto.getUpiUpto2000());
                leadInfo.setUpiAbove2000(leadDto.getUpiAbove2000());
                logger.info("------------------setAmexDomestic------{}", leadDto.getAmexDomestic());
                leadInfo.setAmexDomestic(leadDto.getAmexDomestic());
                leadInfo.setAmexInternational(leadDto.getAmexInternational());
                if (leadDto.getMarsMdrCharges() != null) {
                    leadInfo.setMarsMdrCharges(marsMdrChargesRepository.findByName(leadDto.getMarsMdrCharges()));
                    leadInfo.setMdrPlancode(leadDto.getMarsMdrCharges());

                    ObjectMapper mapper = new ObjectMapper();

                    MdrChargesDto corpCC = mapper.readValue(leadInfo.getMarsMdrCharges().getCommercialOrCorporate(), MdrChargesDto.class);
                    logger.info("Corp CC ------------ : {}", new Gson().toJson(corpCC));
                    leadInfo.setCorpCC(corpCC.getPercentage());

                    MdrChargesDto debitLessThanAmount = mapper.readValue(leadInfo.getMarsMdrCharges().getDomesticDebitUpTo2000(), MdrChargesDto.class);
                    leadInfo.setDebitLessThanAmount(debitLessThanAmount.getPercentage());
                    logger.info("debit less than Amount --- : {}", new Gson().toJson(debitLessThanAmount));

                    MdrChargesDto debitGreaterThanAmount = mapper.readValue(leadInfo.getMarsMdrCharges().getDomesticDebitAbove2000(), MdrChargesDto.class);
                    leadInfo.setDebitGreaterThanAmount(debitGreaterThanAmount.getPercentage());
                    logger.info("debit less than Amount --- : {}", new Gson().toJson(debitLessThanAmount));

                    MdrChargesDto intlCC = mapper.readValue(leadInfo.getMarsMdrCharges().getInternationalCreditCard(), MdrChargesDto.class);
                    leadInfo.setIntlCC(intlCC.getPercentage());
                    logger.info("international cc ---------- : {}", new Gson().toJson(intlCC));

                    MdrChargesDto superPremiumCC = mapper.readValue(leadInfo.getMarsMdrCharges().getSuperPremiumOrSignature(), MdrChargesDto.class);
                    leadInfo.setSuperPremiumCC(superPremiumCC.getPercentage());
                    logger.info("super premium cc ----------: {}", new Gson().toJson(superPremiumCC));

                    MdrChargesDto stdCC = mapper.readValue(leadInfo.getMarsMdrCharges().getStandardOrClassic(), MdrChargesDto.class);
                    leadInfo.setStdCC(stdCC.getPercentage());
                    logger.info("std cc ---------- : {}", new Gson().toJson(stdCC));

                    MdrChargesDto premiumCC = mapper.readValue(leadInfo.getMarsMdrCharges().getPremiumOrPlatinum(), MdrChargesDto.class);
                    leadInfo.setPremiumCC(premiumCC.getPercentage());
                    logger.info("premium or platinum -------------- : {}", new Gson().toJson(premiumCC));

                } else {
                    leadInfo.setMarsMdrCharges(null);
                    leadInfo.setMdrPlancode(null);
                }
                leadInfo.setProfessionalLead(leadDto.isProfessionalLead());
                leadInfo.setPanNumber(leadDto.getPanNumber());
                leadInfo.setGstNumber(leadDto.getGstNumber());
                leadInfo.setNatureOfBusiness(leadDto.getNatureOfBusiness());
                leadInfo.setAggregator(leadDto.getAggregator());
                leadInfo.setMerchantDistrictName(leadDto.getMerchantDistrictName());
                leadInfo.setMerchantSubDistrictName(leadDto.getMerchantSubDistrictName());
                leadInfo.setMerchantVillageName(leadDto.getMerchantVillageName());
                leadInfo.setMerchantTier(leadDto.getMerchantTier());
                leadInfo.setCategoryType(leadDto.getCategoryType());
                leadInfo.setAggregator(true);
                leadInfo.setVpaName(leadDto.getVpaName());
                leadInfo.setRid(leadDto.getRid());
                leadInfo.setCaseNumber(leadDto.getCaseNumber());
                leadInfo.setZone(leadDto.getZone());
                logger.info("LANGUAGE -- {}", leadDto.getLanguage());
                if (leadDto.getSoundId() != null && leadDto.getLanguage() != null) {
                    LanguageSoundbox languageSoundbox = languageSoundboxRepository.findLanguage(leadDto.getLanguage(), leadDto.getSoundId());
                    leadInfo.setLanguageSoundbox(languageSoundbox);
                }
                leadInfo.setSQR(leadDto.isSQR());
                leadInfo.setMerchantPan(leadDto.getMerchantPan());
                leadInfo.setMerchantDob(leadDto.getMerchantDob());
                leadInfo.setStoreAddress(leadDto.getStoreAddress());
                leadInfo.setMarsDevice(leadDto.getMarsDevice());
                leadInfo.setInstitutionCode(leadDto.getInstitutionCode());
                leadInfo.setMarsRegion(leadDto.getMarsRegion());
                leadInfo.setConstitution(leadDto.getConstitution());
                leadInfo.setEstablishYear(leadDto.getEstablishYear());
                leadInfo.setRegisterNumber(leadDto.getRegisterNumber());
                leadInfo.setMaximumMonthlyUsage(leadDto.getMaximumMonthlyUsage());
                leadInfo.setMerchantTypeCode(leadDto.getMerchantTypeCode());
                leadInfo.setAdvanceRentMode(leadDto.getAdvanceRentMode());
                leadInfo.setDeviceOwnedBy(leadDto.getDeviceOwnedBy());
                leadInfo.setDevicePrice(leadDto.getDevicePrice());
                leadInfo.setEmiTenure(leadDto.getEmiTenure());
                leadInfo.setEmiStartDate(leadDto.getEmiStartDate());
                leadInfo.setGracePeriod(leadDto.getGracePeriod());
                leadInfo.setNoOfMonthRentPaidInAdvance(leadDto.getNoOfMonthRentPaidInAdvance());
                leadInfo.setRentalMode(leadDto.getRentalMode());
                leadInfo.setRentalType(leadDto.getRentalType());
                leadInfo.setTotalEmiAmount(leadDto.getTotalEmiAmount());
                leadInfo.setTerminalModeCode(leadDto.getTerminalModeCode());
                leadInfo.setTerminalType(leadDto.getTerminalType());
                leadInfo.setBqrEnabled(leadDto.getBqrEnabled());
                leadInfo.setCashAtPosEnabled(leadDto.getCashAtPosEnabled());
                leadInfo.setLinkpaymentFlag(leadDto.getLinkpaymentFlag());
                leadInfo.setMatmAllowed(leadDto.getMatmAllowed());
                leadInfo.setPreAuth(leadDto.getPreAuth());
                leadInfo.setSaleFlag(leadDto.getSaleFlag());
                leadInfo.setSettlement(leadDto.getSettlement());
                leadInfo.setTipEnabled(leadDto.getTipEnabled());
                leadInfo.setUpichargeslipEnabled(leadDto.getUpichargeslipEnabled());
                leadInfo.setUpiFlag(leadDto.getUpiFlag());
                leadInfo.setAccountType(leadDto.getAccountType());
                leadInfo.setPosQr(leadDto.isPosQr());
                leadInfo.setNoOfAuthorizer(leadDto.getNoOfAuthorizer());
                leadInfo.setBussinessInLocation(leadDto.getBussinessInLocation());
                leadInfo.setStockverificationConfirmation(leadDto.getStockverificationConfirmation());
                leadInfo.setNoOfStaff(leadDto.getNoOfStaff());
                leadInfo.setRemarks(leadDto.getRemarks());
                leadInfo.setNameOfAuthorizer(leadDto.getNameOfAuthorizer());
                leadInfo.setEmailIdForAuthorizer(leadDto.getEmailIdForAuthorizer());
                leadInfo.setPhoneForAuthorizer(leadDto.getPhoneForAuthorizer());
                leadInfo.setAddressForAuthorizer(leadDto.getAddressForAuthorizer());
                leadInfo.setPanNumberForAuthorizer(leadDto.getPanNumberForAuthorizer());
                leadInfo.setIsAuthorizedForAuthorizer(leadDto.getIsAuthorizedForAuthorizer());
                leadInfo.setRegion(regionRepository.findRegionById(leadDto.getMarsRegion()));

                return leadInformationRepository.save(leadInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadInformation;
    }

    private LeadInformation updateFullLeadData(LeadDto leadDto) {
        LeadInformation leadInfo = leadInformationRepository.findByLeadNumber(leadDto.getLeadNumber());
        logger.info("lead Info -------- : {}", leadInfo);
        try {
            if (leadInfo != null) {
                leadInfo.setMerchantName(leadDto.getMerchantName());
                leadInfo.setContactName(leadDto.getContactName());
                leadInfo.setContactEmail(leadDto.getEmail());
                leadInfo.setContactAddress(leadDto.getContactAddress());
                leadInfo.setContactNumber(leadDto.getContactNumber());
                if (leadDto.getLeadSource() != null) {
                    LeadSource leadSource = leadSourceRepo.findByLeadSourceName(leadDto.getLeadSource());
                    leadInfo.setLeadSource(leadSource);
                } else {
                    leadInfo.setLeadSource(null);
                }
                leadInfo.setMerchantType(merchantTypeRepository.findByName(leadDto.getMerchantType()));
                if (leadDto.getDevice() != null) {
                    leadInfo.setDevice(deviceRepository.findDevice(leadDto.getDevice()));
                } else {
                    leadInfo.setDevice(null);
                }
                leadInfo.setDeviceCount(Integer.valueOf(leadDto.getDeviceCount()));
                if (leadDto.getMcc() != null) {
                    leadInfo.setMcc(merchantCategoryCodeRepository.findByCode(leadDto.getMcc()));
                } else {
                    leadInfo.setMcc(null);
                }
                leadInfo.setLeadNumber(leadDto.getLeadNumber());
//        leadInfo.setOnboardingType(leadDto.getOnboardingType());
                leadInfo.setVasInstanceMapping(leadDto.getVasInstanceMapping());
                leadInfo.setQR(leadDto.isQR());
                leadInfo.setSoundBox(leadDto.isSoundBox());
                leadInfo.setPos(leadDto.isPos());
                leadInfo.setPlan(leadDto.getPlan());
                leadInfo.setCorpCC(Double.valueOf(leadDto.getCorpCc()));
                logger.info("corp cc ----: {}", Double.valueOf(leadDto.getCorpCc()));
                leadInfo.setDebitLessThanAmount(Double.parseDouble(leadDto.getDebitLessthanAmount()));
                logger.info("debit less than Amount --- : {}", Double.parseDouble(leadDto.getDebitLessthanAmount()));
                leadInfo.setDebitGreaterThanAmount(Double.parseDouble(leadDto.getDebitGreaterthanAmount()));
                leadInfo.setIntlCC(Double.parseDouble(leadDto.getIntlCc()));
                leadInfo.setSuperPremiumCC(Double.parseDouble(leadDto.getSuperPremiumCc()));
                leadInfo.setStdCC(Double.parseDouble(leadDto.getStdCc()));
                leadInfo.setPremiumCC(Double.parseDouble(leadDto.getPremiumCc()));
                leadInfo.setSetUpFees(Double.parseDouble(leadDto.getSetupFees()));
                leadInfo.setRecurringFees(Double.parseDouble(leadDto.getRecurringFees()));
                leadInfo.setIcAcceptance(leadDto.isIcAcceptance());
                leadInfo.setTipPercentage(leadDto.getTipPercentage());
                if (leadDto.getSimProvider() != null) {
                    leadInfo.setSimProvider(simRepository.findSimByName(leadDto.getSimProvider()));
                } else {
                    leadInfo.setSimProvider(null);
                }
                leadInfo.setSettlementMode(leadDto.getSettlementMode());
                leadInfo.setOmcConvenienceFees(leadDto.isOmcConvenienceFees());
                leadInfo.setYearlyRevenue(leadDto.getYearlyRevenue());
                leadInfo.setLegalName(leadDto.getLegalName());
                leadInfo.setDbaName(leadDto.getDbaName());
                leadInfo.setShop_ownership_status(leadDto.getShop_ownership_status());
                leadInfo.setWeekDayTimingStart(leadDto.getWeekDayTimingStart());
                leadInfo.setWeekdayTimingEnd(leadDto.getWeekdayTimingEnd());
                leadInfo.setWeekendTimingStart(leadDto.getWeekendTimingStart());
                leadInfo.setWeekendTimingEnd(leadDto.getWeekendTimingEnd());
                leadInfo.setMaxMonthlyCardVolume(leadDto.getMaxMonthlyCardVolume());
                leadInfo.setAvgMonthlyCardvolume(leadDto.getAvgMonthlyCardvolume());
                leadInfo.setMemberSince(leadDto.getMemberSince());
                leadInfo.setHavePosMpos(leadDto.getHavePosMpos());
                leadInfo.setDebitMdr(leadDto.getDebitMdr());
                leadInfo.setCreditMdr(leadDto.getCreditMdr());
                leadInfo.setAmountCollected(Double.parseDouble(leadDto.getAmountCollected()));
                leadInfo.setTransactionDate(leadDto.getTransactionDate());
                leadInfo.setPaymentOption(Integer.parseInt(leadDto.getPaymentOption()));
                leadInfo.setPaymentContactNumber(leadDto.getPaymentContactNumber());
                leadInfo.setPaymentSuccess(leadDto.isPaymentSuccess());
                leadInfo.setIfscCode(leadDto.getIfscCode());
                leadInfo.setBank(leadDto.getBank());
                leadInfo.setBranch(leadDto.getBranch());
//        leadInfo.setLeadSourceMCCAccountTypeMapping(leadDto.getLeadSourceMCCAccountTypeMapping());
                leadInfo.setAccountNumber(leadDto.getAccountNumber());
                leadInfo.setReferenceNumber(leadDto.getReferenceNumber());
                leadInfo.setLeadCategory(Integer.parseInt(leadDto.getLeadCategory()));
                leadInfo.setReason(leadDto.getReason());
                leadInfo.setPincode(leadDto.getPincode());
                leadInfo.setCity(leadDto.getCity());
                leadInfo.setState(leadDto.getState());
                leadInfo.setOwnerDOB(leadDto.getOwnerDOB());
                leadInfo.setOwnerFirstName(leadDto.getOwnerFirstName());
                leadInfo.setOwnerLastName(leadDto.getOwnerLastName());
                leadInfo.setMerchantStateName(leadDto.getMerchantStateName());
                leadInfo.setLeadStatus(leadStatusRepository.findLeadStatusById(LEAD_SUBMITTED_TID_PENDING));
                leadInfo.setOnboardingVendor(leadDto.getOnboardingVendor());
                leadInfo.setSettlementType(Integer.parseInt(leadDto.getSettlementType()));
                leadInfo.setActive(true);
                leadInfo.setTurnOverLastFinancialYear(leadDto.getTurnOverLastFinancialYear());
                leadInfo.setCreditCardBlock(leadDto.isCreditCardBlock());
                leadInfo.setAvgBillAmount(leadDto.getAvgBillAmount());
                leadInfo.setMdrPlanName(leadDto.getMdrPlanName());
                if (leadDto.getMcc() != null) {
                    MerchantCategoryCode merchantCategoryCode = merchantCategoryCodeRepository.findByCode(leadDto.getMcc());
                    leadInfo.setMcc(merchantCategoryCode);
                } else {
                    leadInfo.setMcc(null);
                }
                if (leadInfo.getMcc() != null && leadInfo.getLeadSource() != null) {
                    leadInfo.setLeadSourceMCCAccountTypeMapping(leadSourceMCCAccountTypeRepository.findAccountType(leadInfo.getLeadSource(), leadInfo.getMcc()));
                }
                if (leadDto.getMarsRentalPlans() != null) {
                    leadInfo.setMarsRentalPlans(marsRentalPlansRepository.findByName(leadDto.getMarsRentalPlans()));
                    leadInfo.setRentalPlanCode(leadDto.getMarsRentalPlans());
                } else {
                    leadInfo.setMarsRentalPlans(null);
                    leadInfo.setRentalPlanCode(null);
                }
                leadInfo.setMasterPass(leadDto.getMasterPass());
                leadInfo.setCashAtPos(leadDto.getCashAtPos());
                leadInfo.setDiners(leadDto.getDiners());
                leadInfo.setOnus(leadDto.getOnus());
                leadInfo.setInternationalCreditCard(leadDto.getInternationalCreditCard());
                leadInfo.setInternationalDebitCard(leadDto.getInternationalDebitCard());
                leadInfo.setCommercialOrCorporate(leadDto.getCommercialOrCorporate());
                leadInfo.setSuperPremiumOrSignature(leadDto.getSuperPremiumOrSignature());
                leadInfo.setPremiumOrPlatinum(leadDto.getPremiumOrPlatinum());
                leadInfo.setStandardOrClassic(leadDto.getStandardOrClassic());
                leadInfo.setDomesticDebitAbove2000(leadDto.getDomesticDebitAbove2000());
                leadInfo.setDomesticDebitUpTo2000(leadDto.getDomesticDebitUpTo2000());
                if (leadDto.getMarsMdrCharges() != null) {
                    leadInfo.setMarsMdrCharges(marsMdrChargesRepository.findByName(leadDto.getMarsMdrCharges()));
                    leadInfo.setMdrPlancode(leadDto.getMarsMdrCharges());

//                JSONParser parser = new JSONParser();
//                parser.parse(leadDto.getMarsMdrCharges());
//
//
//                JSONObject object1 = new JSONObject(leadDto.getMarsMdrCharges().get);

//                leadInfo.setCorpCC(Double.valueOf(leadDto.getCorpCc()));
//                logger.info("corp cc ----: {}", Double.valueOf(leadDto.getCorpCc()));
//                leadInfo.setDebitLessThanAmount(Double.parseDouble(leadDto.getDebitLessthanAmount()));
//                logger.info("debit less than Amount --- : {}",Double.parseDouble(leadDto.getDebitLessthanAmount()));
//                leadInfo.setDebitGreaterThanAmount(Double.parseDouble(leadDto.getDebitGreaterthanAmount()));
//                leadInfo.setIntlCC(Double.parseDouble(leadDto.getIntlCc()));
//                leadInfo.setSuperPremiumCC(Double.parseDouble(leadDto.getSuperPremiumCc()));
//                leadInfo.setStdCC(Double.parseDouble(leadDto.getStdCc()));
//                leadInfo.setPremiumCC(Double.parseDouble(leadDto.getPremiumCc()));

                } else {
                    leadInfo.setMarsMdrCharges(null);
                    leadInfo.setMdrPlancode(null);
                }
                leadInfo.setProfessionalLead(leadDto.isProfessionalLead());
                leadInfo.setPanNumber(leadDto.getPanNumber());
                leadInfo.setGstNumber(leadDto.getGstNumber());
                leadInfo.setNatureOfBusiness(leadDto.getNatureOfBusiness());
                leadInfo.setAggregator(leadDto.getAggregator());
                leadInfo.setMerchantDistrictName(leadDto.getMerchantDistrictName());
                leadInfo.setMerchantSubDistrictName(leadDto.getMerchantSubDistrictName());
                leadInfo.setMerchantVillageName(leadDto.getMerchantVillageName());
                leadInfo.setMerchantTier(leadDto.getMerchantTier());
                leadInfo.setCategoryType(leadDto.getCategoryType());

                return leadInformationRepository.save(leadInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadInfo;
    }

    // store aggregator Documents
    @Override
    public UploadedDocuments storeLeadAndUploadedDocuments(LeadInformation lead, FileDto file, String documentMappingName, String typeOfDocument, String merchantDocumentTypeName, int isQr, SubDocumentType subDocumentTypes) {
        logger.info("document mapping name ---------------: {}, merchant type ----------- : {}, merchant document type name -----------:{}", documentMappingName, lead.getMerchantType().getMerchantTypeName(), merchantDocumentTypeName);
        LeadDocuments documents = new LeadDocuments();
        UploadedDocuments response = null;

//        documents.setSubDocumentType(subDocumentType);
        DocumentMapping documentMapping = documentMappingRepository.findMappingName(documentMappingName, lead.getMerchantType(), merchantDocumentTypeName, isQr);
        documents.setDocumentMapping(documentMapping);
        documents.setLeadInformation(lead);
        documents.setDocumentVerifiedStatus(OPS_VERIFICATION_PENDING);
        documents.setUploadedBy(getSessionUser());
        documents = leadDocumentRepository.save(documents);
        if (lead.getLeadSource().getLeadSourceName().contains("LS_Wowpe")) {
            logger.info("wowpe--");
            List<Long> catagory = new ArrayList<>();
            if (subDocumentTypes != null) {
                logger.info("subDocumentType--{}", subDocumentTypes.getSubDocumentTypeId());
                logger.info("subDocumentType--{}", subDocumentTypes.getDocumentType());
                if (subDocumentTypes.getDocumentCategory().getId() == PROOF_OF_BUSSINESS_CATAGORY) {
                    catagory.add(PROOF_OF_BUSSINESS_CATAGORY);
                } else if (subDocumentTypes.getDocumentCategory().getId() == PROOF_OF_BUSSINESS_CATAGORY_OTHER) {
                    catagory.add(PROOF_OF_BUSSINESS_CATAGORY_OTHERS);
                } else if (subDocumentTypes.getDocumentCategory().getId() == Authorized_Signatories1) {
                    catagory.add(AUTH_SIGN1);
                } else if (subDocumentTypes.getDocumentCategory().getId() == Authorized_Signatories2) {
                    catagory.add(AUTH_SIGN2);
                } else if (subDocumentTypes.getDocumentCategory().getId() == Authorized_Signatories3) {
                    catagory.add(AUTH_SIGN3);
                } else if (subDocumentTypes.getDocumentCategory().getId() == Authorized_Signatories4) {
                    catagory.add(AUTH_SIGN4);
                } else if (subDocumentTypes.getDocumentCategory().getId() == Authorized_Signatories5) {
                    catagory.add(AUTH_SIGN5);
                } else {
                    catagory.add(subDocumentTypes.getDocumentCategory().getId());
                }
                List<DocumentCategory> documentCategory = documentCategoryRepository.fetchDocumentCategoryListById(catagory);
                logger.info("docCatagory-{}--subName--{}", documentCategory.get(0).getName(), subDocumentTypes.getName());
                SubDocumentType subDocumentType = new SubDocumentType();
                if (documentCategory.get(0).getId() == PROOF_OF_BUSSINESS_CATAGORY_OTHERS) {
                    subDocumentType = subDocumentTypeRepository.findSubDocNameAndCatagoryOther(subDocumentTypes.getName(), documentCategory.get(0));
                } else if ((documentCategory.get(0).getId() == PROOF_OF_BUSSINESS_CATAGORY)) {
                    List<SubDocumentType> test = subDocumentTypeRepository.findSubDocNameAndCatagoryOtherList(subDocumentTypes.getName(), documentCategory.get(0));
                    if (test.get(0).getMerchantCategoryCode() != null) {
                        subDocumentType = subDocumentTypeRepository.findSubDocNameAndCatagory(subDocumentTypes.getName(), documentCategory.get(0), lead.getMcc());
                    } else {
                        subDocumentType = subDocumentTypeRepository.findSubDocNameAndCatagoryOther(subDocumentTypes.getName(), documentCategory.get(0));
                    }
                }
//                else if ((documentCategory.get(0).getId() == AUTH_SIGN1) || (documentCategory.get(0).getId() == AUTH_SIGN2) || (documentCategory.get(0).getId() == AUTH_SIGN3) || (documentCategory.get(0).getId() == AUTH_SIGN4) || (documentCategory.get(0).getId() == AUTH_SIGN5)) {
//                    subDocumentType = subDocumentTypeRepository.findSubDocNameAndCatagoryOther(subDocumentTypes.getName(), documentCategory.get(0));
//                }
                else {
                    subDocumentType = subDocumentTypeRepository.findSubDocNameAndCatagoryOther(subDocumentTypes.getName(), documentCategory.get(0));
                }
                logger.info("subDocument_type-{}", subDocumentType.getSubDocumentTypeId());
                documents.setSubDocumentType(subDocumentType);
            }
        }
        response = uploadedDocumentsFunction(documents, file, typeOfDocument);
        //Store document history table
        storeLeadDocumentActivityHistory(response, Constants.DOCUMENT_UPLOADED);

        return response;
    }

    @Override
    public List<LeadDocuments> aggDocumentApprovalOrReject(long leadId, Document documentType, int action, DocumentUpdateDto documentUpdateDto) throws MessagingException {
        List<LeadDocuments> documents = new ArrayList<>();
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
//        DocumentType type = documentTypeRepository.findByDocumentId(documentType.getId());
        Document type = documentRepository.fetchDocumentById(documentType.getId());
        List<LeadDocuments> lead = leadDocumentRepository.fetchLeadDocumentByLeadAndDocument(leadInformation, type);
        logger.info("document list -- : {}", lead.size());
        //Approval process
        if (action == Constants.LEAD_DOCUMENT_APPROVAL) {
            logger.info("approval process -- : ");
            lead.forEach(approve -> {
                LeadDocuments leadDocuments = new LeadDocuments();
                if (approve.getDocumentVerifiedStatus() == OPS_VERIFICATION_PENDING) {
                    approve.setDocumentVerifiedStatus(Constants.OPS_VERIFICATION_SUCCESS);
                    approve.setApprovedDate(DateUtil.currentDate());
                    leadDocuments = leadDocumentRepository.save(approve);

                    LeadDocuments finalLeadDocuments = leadDocuments;
                    CompletableFuture.runAsync(() -> {
                        logger.info("update---------------------------------------:");
                        // transfer data to Onboarding Portal
                        this.updateDocumentApproveOrRejectData(leadInformation.getLeadNumber(), finalLeadDocuments.getDocumentMapping(), finalLeadDocuments);
                    });
                }
//                approve.setApprovedBy(getSessionUser());
                documents.add(leadDocuments);
            });
        }        //Reject Process
        else if (action == Constants.LEAD_DOCUMENT_REJECT) {
            logger.info("reject process -- : ");
            lead.forEach(reject -> {
                LeadDocuments leadDocuments = new LeadDocuments();
                logger.info("document verifucation status -- : {}", reject.getDocumentVerifiedStatus());
                if (reject.getDocumentVerifiedStatus() == OPS_VERIFICATION_PENDING || reject.getDocumentVerifiedStatus() == OPS_VERIFICATION_SUCCESS) {
                    logger.info("rejected document type and status -- : {} {}", reject.getSubDocumentType(), reject.getDocumentVerifiedStatus());
                    reject.setDocumentVerifiedStatus(OPS_VERIFICATION_FAILED);
                    logger.info("rejected -- : {}", reject.getDocumentVerifiedStatus());
                    reject.setLeadCancellationReason(documentUpdateDto.getCancellationReason());
                    reject.setDocumentRemarks(documentUpdateDto.getDocumentRemarks());
                    reject.setRejectedBy(getSessionUser());
                    reject.setRejectedDate(DateUtil.currentDate());
                    leadDocuments = leadDocumentRepository.save(reject);

                    CompletableFuture.runAsync(() -> {
                        // transfer data to Onboarding Portal
                        this.updateDocumentApproveOrRejectData(leadInformation.getLeadNumber(), reject.getDocumentMapping(), reject);
                    });
                }
                documents.add(leadDocuments);
            });

            leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(DOCUMENT_REJECTED_BY_OPS));
            leadInformation.setActive(false);
            leadInformationRepository.save(leadInformation);

            mailHandler.sendDocumentRejectedEmail(leadInformation.getContactEmail(), leadInformation.getLeadNumber(), documentType.getName(), leadInformation.getLeadSource().getLeadSourceName());
        }
        return documents;
    }

    private void updateDocumentApproveOrRejectData(String leadNumber, DocumentMapping documentMapping, LeadDocuments documents) {
        ResponseEntity<String> out = null;
        HttpHeaders headers = new HttpHeaders();
        logger.info("headers------------------: {}", headers);
        LeadDocumentDto documentDtoList = this.getRejectedDocumentDetails(documents);
//        String url = "http://192.168.4.227:7077/api/document/documentUpdate/{leadNumber}/{subDocumentType}";
        String url = documentUpdateUrl + "/{leadNumber}?documentMapping=" + documentMapping.getDocumentName();
        logger.info("url---: {}", url);

        //            String documentmapping = URLEncoder.encode(documentMapping.getDocumentName(), "UTF-8");
        logger.info("document mapping -------- : {}", documentMapping.getDocumentName());
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("leadNumber", leadNumber);
        urlParams.put("documentMapping", documentMapping.getDocumentName());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        logger.info("path variables----: {}", builder.buildAndExpand(urlParams).toUri());

        HttpEntity<LeadDocumentDto> entity = new HttpEntity<>(documentDtoList, headers);
        try {
            out = restTemplate.exchange(builder.build(urlParams), HttpMethod.PUT, entity, String.class);
            logger.info("out status code---------------------: {}", out.getStatusCode());
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            logger.info("exception -------: {}", exception.getResponseBodyAsString());
            exception.printStackTrace();
        }
    }

    private LeadDocumentDto getRejectedDocumentDetails(LeadDocuments documents) {
        LeadDocumentDto leadDocumentDto = new LeadDocumentDto();
        logger.info("lead documents dto --------------------------:");
//        leadDocumentDto.setTypeOfDocument();
        leadDocumentDto.setDocumentVerifiedStatus(documents.getDocumentVerifiedStatus());
        leadDocumentDto.setMerchantDocumentTypeName(documents.getDocumentMapping() != null ? documents.getDocumentMapping().getMerchantDocumentType().getDocumentType() : null);
        leadDocumentDto.setIsQR(documents.getDocumentMapping() != null ? documents.getDocumentMapping().getMerchantDocumentType().getIsQR() : 0);
//            leadDocumentDto.setSubDocumentType(documents.getSubDocumentType());
        leadDocumentDto.setDocumentMapping(documents.getDocumentMapping() != null ? documents.getDocumentMapping() : null);
        leadDocumentDto.setSubDocumentType(documents.getSubDocumentType() != null ? documents.getSubDocumentType() : null);
        if (documents.getDocumentVerifiedStatus() == OPS_VERIFICATION_FAILED) {
            leadDocumentDto.setCancellationReason(documents.getLeadCancellationReason().getReason());
        }
        leadDocumentDto.setDocumentRemarks(documents.getDocumentRemarks());
        logger.info("lead Document Dto---: {}", leadDocumentDto);

        return leadDocumentDto;
    }

    @Override
    public LeadInformation findAggLeadNumber(String leadNumber) {
        return leadInformationRepository.findByLeadNumber(leadNumber);
    }

    @Override
    public void aggLeadDelete(LeadInformation leadInformation) {
        LeadInformation leadInfo = leadInformationRepository.findByLeadId(leadInformation.getLeadId());
        List<LeadDocuments> leadDocuments = leadDocumentRepository.findByLeadId(leadInfo);

        if (!leadDocuments.isEmpty()) {
            List<UploadedDocuments> uploadedDocuments = uploadedDocumentRepository.findByLeadDocumentId(leadDocuments);
            if (!uploadedDocuments.isEmpty()) {
                List<Long> uploadedIds = uploadedDocuments.stream().map(UploadedDocuments::getUploadedDocumentId).toList();
                List<String> fileNameList = uploadedDocuments.stream().map(UploadedDocuments::getFileName).toList();
                logger.info("uploaded FileNames --------: {}", fileNameList);
                deleteAllAttachment(fileNameList);  // delete all documents in server
                logger.info("uploaded ID's --------: {}", uploadedIds);
                uploadedDocumentRepository.deleteAllById(uploadedIds); // delete all uploaded Documents
            }
            List<LeadDocumentHistory> documentHistoryList = leadDocumentHistoryRepository.findAllByLeadDocIds(leadDocuments);
            List<Long> leadDocHistoryIds = documentHistoryList.stream().map(LeadDocumentHistory::getLeadDocumentHistoryId).toList();
            leadDocumentHistoryRepository.deleteAllById(leadDocHistoryIds); //delete all Document History Details

            List<Integer> leadDocIds = leadDocuments.stream().map(LeadDocuments::getLeadDocumentId).toList();
            leadDocumentRepository.deleteAllById(leadDocIds);  // delete all Lead Document Details
        }
        List<LeadStatusHistory> leadStatusHistories = leadStatusHistoryRepository.getLeadHistoryDetails(leadInfo);
        List<Long> leadHistoryIds = leadStatusHistories.stream().map(LeadStatusHistory::getId).toList();
        leadStatusHistoryRepository.deleteAllById(leadHistoryIds); // delete lead history details
        leadInformationRepository.deleteById(leadInfo.getLeadId()); //delete lead Info
    }

    @Override
    public void deleteAllAttachment(List<String> fileNameList) {
        fileNameList.forEach(fileName -> {
            Path path = Paths.get(fileStorageProperties.getUploadDir());
            logger.info("Get uploaded directory to delete file path : {}", path);
            Path fileToDelete = path.resolve(fileName);
            logger.info("File name: {}", fileToDelete);
            try {
                Files.deleteIfExists(fileToDelete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public UploadedDocuments updateAggregatorReuploadedDoc(String leadNumber, LeadDocumentDto document) {

        List<UploadedDocuments> model = new ArrayList<>();
        LeadInformation leadInfo = leadInformationRepository.findByLeadNumber(leadNumber);

        try {
            logger.info("image url -------------: {}", document.getImageUrl());
            URL url = new URL(document.getImageUrl());
            InputStream inputStream = url.openStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);

            String fileName = document.getFileName();
            String contentType = document.getContentType();

            MultipartFile file = new ByteArrayMultiPartFile("file", fileName, contentType, bytes);

            Map<String, String> response = fileStorageService.storeFileFromOnboardLead(file, fileName);

            FileDto fileDto = new FileDto();
            fileDto.setFileName(response.get("fileName"));
            fileDto.setMimeType(response.get("mimeType"));
            fileDto.setFileNameOriginal(response.get("fileNameOriginal"));
            logger.info("save uploaded documents ----------- :");
            UploadedDocuments uploadedDocuments = this.storeLeadAndUploadedDocuments(leadInfo, fileDto, document.getDocumentMappingName(), document.getTypeOfDocument(), document.getMerchantDocumentTypeName(), document.getIsQR(), document.getSubDocumentType());
            logger.info("uploaded documents saved ------------ :");
            model.add(uploadedDocuments);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("MalFormed Exception---- :{}", e.getMessage());
        }
        return null;
    }

    @Override
    public AgreementDetails findByContractId(String contractId) {
        return agreementDetailsRepository.findByContractId(contractId);
    }

    @Override
    public AgreementDetails findBySignerId(String signerId) {
        return agreementDetailsRepository.findBySignerId(signerId);
    }
//    @Override
//    public boolean checkAllAggregatorDocumentsVerifiedByOps(LeadInformation leadInfo) {
//        List<SubDocumentType> subDocumentTypes = leadDocumentRepository.fetchSubDocumentsByLeadId(leadInfo);
//        if (subDocumentTypes.size() > 0) {
//            for (SubDocumentType documents : subDocumentTypes){
//                List<LeadDocuments> leadDocumentsList = leadDocumentRepository.getLeadDocumentsBySubDocType(leadInfo,documents);
//                if(leadDocumentsList.size() >0){
//                    if(leadDocumentsList.get(0).getDocumentVerifiedStatus() != OPS_VERIFICATION_SUCCESS){
//                        return false;
//                    }
//                }
//            }
//            return true;
//        }
//        return true;
//    }

    @Override
    public boolean checkAllAggregatorDocumentsVerifiedByOps(LeadInformation leadInfo) {

        LeadInformation lead = leadInformationRepository.findByLeadId(leadInfo.getLeadId());
        if (lead.getLeadSource().getLeadSourceName().equals("LS_Wowpe")) {
//            List<LeadDocuments> leadDocument1 = new ArrayList<>();
//            List<LeadDocuments> leadDocument2 = leadDocumentRepository.findLeadDocByCatagory(lead.getLeadId(), AUTH_SIGN);
//            if (!leadDocument2.isEmpty()) {
//                for (LeadDocuments leadDocuments : leadDocument2) {
//                    if (leadDocuments.getDocumentVerifiedStatus() != 5) {
//                        leadDocument1.add(leadDocuments);
//                    }
//                }
//            }
//            if (lead.getNoOfAuthorizer() != leadDocument1.size()) {
//                logger.info("verify status failed AUTH_SIGN");
//                return false;
//            }
            List<LeadDocuments> leadDocument1 = new ArrayList<>();
            List<Long> categories = Arrays.asList(AUTH_SIGN1, AUTH_SIGN2, AUTH_SIGN3, AUTH_SIGN4, AUTH_SIGN5);
            List<LeadDocuments> leadDocument2 = leadDocumentRepository.findLeadDocByCatagoryList(lead.getLeadId(), categories);
            logger.info("count For leadDocument----{}", leadDocument2.size());
            if (!leadDocument2.isEmpty()) {
                for (LeadDocuments leadDocuments : leadDocument2) {
                    if (leadDocuments.getDocumentVerifiedStatus() == OPS_VERIFICATION_SUCCESS) {
                        leadDocument1.add(leadDocuments);
                    }
                }
            }
            logger.info("no of Authorizer--{}  ---LIST--{}", lead.getNoOfAuthorizer() * 3, leadDocument1.size());
            if (lead.getNoOfAuthorizer() * 3 != leadDocument1.size()) {
                return false;
            }

            //verify GST Document
            if (lead.getLeadSource().getLeadSourceName().equals("LS_Wowpe") && lead.getGstNumber() != null) {
                DocumentCategory documentCategory = documentCategoryRepository.findBusinessDoc().get(0);
                List<LeadDocuments> leadDocuments = leadDocumentRepository.findLeadDocByCatagory(lead.getLeadId(), documentCategory.getId());
                if (leadDocuments.isEmpty() || leadDocuments.get(0).getDocumentVerifiedStatus() == OPS_VERIFICATION_FAILED) {
                    return false;
                }
            }
        }
        if (lead.getLeadSource().getLeadSourceName().equals("LS_Wowpe") && lead.getMerchantType().getMerchantTypeName().equals("Individual") || lead.getLeadSource().getLeadSourceName().equals("LS_Wowpe") && lead.getMerchantType().getMerchantTypeName().equals("Sole Proprietorship")) {
            logger.info("verify status enter Wowpe");
            List<Long> doccatagory = new ArrayList<>();
            List<LeadDocuments> leadDocuments = new ArrayList<>();
            doccatagory.add(PROOF_OF_BUSSINESS_CATAGORY);
            doccatagory.add(PROOF_OF_BUSSINESS_CATAGORY_OTHERS);
            for (long category : doccatagory) {
                List<LeadDocuments> leadDocument = leadDocumentRepository.findLeadDocByCatagory(lead.getLeadId(), category);
                if (!leadDocument.isEmpty()) {
                    leadDocuments.add(leadDocument.get(0));
                }
            }
            logger.info("Enter1-{}", leadDocuments.size());
            if (leadDocuments.size() >= 2) {
                logger.info("Enter");
                for (LeadDocuments leadDocuments1 : leadDocuments) {
                    logger.info("leadDoc-{}", leadDocuments1);
                    if (leadDocuments1.getDocumentVerifiedStatus() == 5) {
                        logger.info("verifyStatus");
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        List<DocumentMapping> mappingList = leadDocumentRepository.fetchDocumentMappingByLeadId(leadInfo);
        if (mappingList.size() > 0) {
            for (DocumentMapping documents : mappingList) {
                List<LeadDocuments> leadDocumentsList = leadDocumentRepository.getLeadDocumentsByDocumentMapping(leadInfo, documents);
                if (leadDocumentsList.size() > 0) {
                    if (leadDocumentsList.get(0).getDocumentVerifiedStatus() != OPS_VERIFICATION_SUCCESS) {
                        return false;
                    }
                }
            }
            return true;
        }
        return true;
    }

    @Override
    public Pair<Integer, Object> aggregatorLeadApproval(long leadId) {

        LeadInformation leadInformation1 = leadInformationRepository.findByLeadId(leadId);
        logger.info("enter into verification part");
        if (leadInformation1 != null) {
            LeadStatus status = leadStatusRepository.findLeadStatusById(PENDING_AGREEMENT_SIGNING);
            leadInformation1.setLeadStatus(status);
            leadInformation1.setLeadApprovedBy(getSessionUser());
            leadInformation1.setLeadApprovedDate(DateUtil.currentDate());
            LeadInformation lead = leadInformationRepository.save(leadInformation1);


            logger.info("check status -- : {}", lead.getLeadStatus().getLeadStatusName());
            return Pair.of(1, lead);
        }
        return Pair.of(0, "");
    }

    @Override
    public boolean checkUploadedDocuments(LeadInformation leadInfo) {
        logger.info("test check -- : ");
        Aggregator aggregator = aggregatorRepository.findAggregator(leadInfo.getAggregator());

        List<MerchantDocumentType> documentMappings = null;
        documentMappings = aggregatorDocumentMappingRepository.findDocumentMappingByAggregator(aggregator, leadInfo.getMerchantType());
        logger.info("document mapping : {}", documentMappings.size());
        if (!documentMappings.isEmpty()) {
            for (MerchantDocumentType documentType : documentMappings) {
                if (ANYONE_DOCUMENT == documentType.getUploadAnyOne()) {
                    logger.info("anyone -- : {}", documentType.getDocumentType());
                    logger.info("professional lead check -- : {}", leadInfo.isProfessionalLead());
                    if (leadInfo.isProfessionalLead() && documentType.getDocumentType().equalsIgnoreCase(professional_lead)) {
                        logger.info("professional document -- : {}", true);
                        List<DocumentMapping> mappingList = aggregatorDocumentMappingRepository.findDocumentsByDocumentType(documentType);
                        List<LeadDocuments> leadDocuments = leadDocumentRepository.findLeadDocumentsByLeadAndDocumentList(leadInfo, mappingList);
                        logger.info("check -- : {}", leadDocuments.size());
                        if (leadDocuments.isEmpty()) {
                            return false;
                        }
                    } else {
                        logger.info("anyone2 -- : {}", documentType.getDocumentType());
                        if (!documentType.getDocumentType().equalsIgnoreCase(professional_lead)) {
                            logger.info("professional document -- : {}", false);
                            List<DocumentMapping> mappingList = aggregatorDocumentMappingRepository.findDocumentsByDocumentType(documentType);
                            List<LeadDocuments> leadDocuments = leadDocumentRepository.findLeadDocumentsByLeadAndDocumentList(leadInfo, mappingList);
                            logger.info("check -- : {}", leadDocuments.size());
                            if (leadDocuments.isEmpty()) {
                                return false;
                            }
                        }
                    }
                } else if (ALL_DOCUMENT == documentType.getUploadAnyOne() || MANDATORY_DOCUMENT == documentType.getUploadAnyOne()) {
                    logger.info("All document & mandatory document -- : {} , : {}", documentType.getUploadAnyOne(), documentType.getUploadAnyOne());
                    List<DocumentMapping> mappingList = aggregatorDocumentMappingRepository.findDocumentsByDocumentType(documentType);
                    for (DocumentMapping documentMapping : mappingList) {
                        List<LeadDocuments> leadDocuments = leadDocumentRepository.findLeadDocumentsByLeadAndDocumentList(leadInfo, Collections.singletonList(documentMapping));

                        if (leadDocuments.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        } else {
            logger.info("info -- : ");
            documentMappings = documentMappingRepository.findTypesByMerchantTypes(leadInfo.getMerchantType());
            logger.info("global mapping -- : {}", documentMappings.size());
            for (MerchantDocumentType documentType : documentMappings) {

                if (Constants.ANYONE_DOCUMENT == documentType.getUploadAnyOne()) {
                    logger.info("anyone -- : {}", documentType.getDocumentType());
                    logger.info("professional lead check -- : {}", leadInfo.isProfessionalLead());
                    if (leadInfo.isProfessionalLead() && documentType.getDocumentType().equalsIgnoreCase(professional_lead)) {
                        logger.info("professional document -- : {}", true);
                        List<DocumentMapping> mappingList = aggregatorDocumentMappingRepository.findDocumentsByDocumentType(documentType);
                        List<LeadDocuments> leadDocuments = leadDocumentRepository.findLeadDocumentsByLeadAndDocumentList(leadInfo, mappingList);
                        logger.info("check -- : {}", leadDocuments.size());
                        if (leadDocuments.isEmpty()) {
                            return false;
                        }
                    } else {
                        logger.info("anyone2 -- : {}", documentType.getDocumentType());
                        if (!documentType.getDocumentType().equalsIgnoreCase(professional_lead)) {
                            logger.info("professional document -- : {}", false);
                            List<DocumentMapping> mappingList = aggregatorDocumentMappingRepository.findDocumentsByDocumentType(documentType);
                            List<LeadDocuments> leadDocuments = leadDocumentRepository.findLeadDocumentsByLeadAndDocumentList(leadInfo, mappingList);
                            logger.info("check -- : {}", leadDocuments.size());
                            if (leadDocuments.isEmpty()) {
                                return false;
                            }
                        }
                    }

                } else if (ALL_DOCUMENT == documentType.getUploadAnyOne() || MANDATORY_DOCUMENT == documentType.getUploadAnyOne()) {
                    logger.info("All document & mandatory document -- : {} , : {}", documentType.getUploadAnyOne(), documentType.getUploadAnyOne());
                    List<DocumentMapping> mappingList = documentMappingRepository.findDocumentsByMerchantDocumentType(documentType);
                    for (DocumentMapping mapping : mappingList) {
                        List<LeadDocuments> leadDocuments = leadDocumentRepository.findLeadDocumentsByLeadAndDocumentList(leadInfo, Collections.singletonList(mapping));
                        if (leadDocuments.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public AgreementDetails storeUploadedPdfFile(MultipartFile file, LeadInformation leadInformation) throws JsonProcessingException {
        //store input file
        String newFileName = leadInformation.getLeadId() + "_" + file.getOriginalFilename() + "_" + "inputFile";
        Map<String, String> response = fileStorageService.storeFile(file, newFileName);

        ResponseEntity<String> out = null;
        String url = null;
        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        Resource resource = file.getResource();
        map.add("file", resource);
        map.add("ttl", "infinity");
        httpHeaders.set("Content-Type", "multipart/form-data");
        httpHeaders.set("Content-Disposition", "form-data");
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(map, httpHeaders);

        out = restTemplate.exchange(signzyFileUploadUrl, HttpMethod.POST, entity, String.class);
        logger.info("pdfUrl Response out -- : {}", out.getBody());
        EsignPdfResponseDto pdfResponseDto = objectMapper.readValue(out.getBody(), EsignPdfResponseDto.class);

        AgreementDetails agreementDetails = agreementDetailsRepository.findByLeadId(leadInformation);
        if (agreementDetails != null) {
            agreementDetails.setResponse(true);
//            agreementDetails.setInputFileName(response.get("fileName"));
            agreementDetails.setSignedFileName(response.get("fileName"));
            agreementDetails.setSignedFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
            agreementDetails.setLeadInformation(leadInformation);

            leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(AGREEMENT_SIGNING_COMPLETED));
            leadInformationRepository.save(leadInformation);

            return agreementDetailsRepository.save(agreementDetails);
        } else {
            AgreementDetails details = new AgreementDetails();
            details.setResponse(true);
//            details.setInputFileName(response.get("fileName"));
            details.setSignedFileName(response.get("fileName"));
            details.setLeadInformation(leadInformation);

            leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(AGREEMENT_SIGNING_COMPLETED));
            leadInformationRepository.save(leadInformation);

            return agreementDetailsRepository.save(details);

        }
    }

    @Override
    public AgreementDetails storeAggPdfFile(MultipartFile multipartFile, LeadInformation leadInformation) {

        AgreementDetails agreementDetails = agreementDetailsRepository.findByLeadId(leadInformation);
        if (agreementDetails != null) {
            //store input file
            String newFileName = leadInformation.getLeadId() + "_" + multipartFile.getOriginalFilename() + "_" + "inputFile";
            Map<String, String> response = fileStorageService.storeAgreementFile(multipartFile, newFileName);

            agreementDetails.setInputFileName(response.get("fileName"));
            agreementDetails.setLeadInformation(leadInformation);
            return agreementDetails;

        } else {
            //store input file
            String newFileName = leadInformation.getLeadId() + "_" + multipartFile.getOriginalFilename() + "_" + "inputFile";
            Map<String, String> response = fileStorageService.storeAgreementFile(multipartFile, newFileName);

            AgreementDetails details = new AgreementDetails();
            details.setInputFileName(response.get("fileName"));
            details.setLeadInformation(leadInformation);
            return agreementDetailsRepository.save(details);

        }
    }

    @Override
    public AgreementDetails manualUpload(LeadInformation leadInformation, AgreementDetailsDto detailsDto) {
        AgreementDetails agreementDetails = agreementDetailsRepository.findByLeadId(leadInformation);

        if (agreementDetails != null) {
            try {
                URL url = new URL(detailsDto.getSignedFileDownUrl());
                InputStream inputStream = url.openStream();
                byte[] bytes = IOUtils.toByteArray(inputStream);

                String fileName = detailsDto.getSignedFileName();
                String contentType = "multipart/form-file";

                MultipartFile file = new ByteArrayMultiPartFile("file", fileName, contentType, bytes);
                Map<String, String> response = fileStorageService.storeFileFromOnboardLead(file, fileName);

                agreementDetails.setResponse(detailsDto.isResponse());
                agreementDetails.setSignedFileName(detailsDto.getSignedFileName());
                agreementDetails.setSignedFilePdfUrl(detailsDto.getSignedFilePdfUrl());

                leadInformation.setAgreementSuccess(true);
                leadInformation.setActive(true);
                leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(AGREEMENT_SIGNING_COMPLETED));
                leadInformation.setManualAgreement(true);
                leadInformationRepository.save(leadInformation);

                return agreementDetailsRepository.save(agreementDetails);
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("MalFormed Exception---- :{}", e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    public AgreementDetails rejectManualAgreement(LeadInformation leadInformation, DocumentUpdateDto documentUpdateDto) {

        AgreementDetails agreementDetails = agreementDetailsRepository.findByLeadId(leadInformation);
        if (agreementDetails != null) {

            ResponseEntity<String> out = null;
            String url = rejectAgreementUrl + "{leadNumber}";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            Map<String, String> urlParams = new HashMap<>();
            urlParams.put("leadNumber", leadInformation.getLeadNumber());

            AgreementDetailsDto detailsDto = new AgreementDetailsDto();
            detailsDto.setRemarks(documentUpdateDto.getDocumentRemarks());

            HttpEntity<AgreementDetailsDto> entity = new HttpEntity<>(detailsDto, httpHeaders);

            try {
                out = restTemplate.exchange(builder.build(urlParams), HttpMethod.PUT, entity, String.class);
                int responseCode = out.getStatusCode().value();
                String responseBody = out.getBody();
                logger.info("response code ----------- :{} , response out -------- : {}", responseCode, responseBody);
                if (out.getStatusCodeValue() == 200) {
                    agreementDetails.setResponse(false);
                    agreementDetails.setRemarks(documentUpdateDto.getDocumentRemarks());
                    AgreementDetails details = agreementDetailsRepository.save(agreementDetails);

                    leadInformation.setAgreementSuccess(false);
                    leadInformation.setActive(false);
                    leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(AGREEMENT_SIGNING_FAILED));
                    LeadInformation result = leadInformationRepository.save(leadInformation);

                    return details;
                } else {
                    return null;
                }
            } catch (HttpClientErrorException httpClientErrorException) {
                int responseCode = httpClientErrorException.getStatusCode().value();
                String clientErrorValue = httpClientErrorException.getResponseBodyAsString();
                logger.info("response code-------------- : {} , client Error Response --------------- :{}", responseCode, clientErrorValue);
                return null;
            } catch (HttpServerErrorException httpServerErrorException) {
                int responseCode = httpServerErrorException.getStatusCode().value();
                String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
                logger.info("response code --------- : {}, server error response --------- : {}", responseCode, serverErrorResponse);
                return null;
            }
        }
        return null;
    }

    @Override
    public List<LeadDocuments> findLeadDocument(LeadInformation leadInformation) {
        return leadDocumentRepository.fetchLeadDocumentsByLead(leadInformation, OPS_VERIFICATION_SUCCESS);
    }

    @Override
    public LeadInformation updateResubmittedLead(String leadNumber, LeadDto leadDto) {
        LeadInformation leadInfo = leadInformationRepository.findByLeadNumber(leadNumber);
        logger.info("lead Info -------- : {}", leadInfo);
        try {
            if (leadInfo != null) {
                leadInfo.setMerchantName(leadDto.getMerchantName());
                leadInfo.setContactName(leadDto.getContactName());
                leadInfo.setContactEmail(leadDto.getEmail());
                leadInfo.setContactAddress(leadDto.getContactAddress());
                leadInfo.setContactNumber(leadDto.getContactNumber());
                if (leadDto.getLeadSource() != null) {
                    LeadSource leadSource = leadSourceRepo.findByLeadSourceName(leadDto.getLeadSource());
                    leadInfo.setLeadSource(leadSource);
                } else {
                    leadInfo.setLeadSource(null);
                }
                leadInfo.setMerchantType(merchantTypeRepository.findByName(leadDto.getMerchantType()));
                if (leadDto.getDevice() != null) {
                    leadInfo.setDevice(deviceRepository.findDevice(leadDto.getDevice()));
                } else {
                    leadInfo.setDevice(null);
                }
                leadInfo.setDeviceCount(Integer.valueOf(leadDto.getDeviceCount()));
                if (leadDto.getMcc() != null) {
                    leadInfo.setMcc(merchantCategoryCodeRepository.findByCode(leadDto.getMcc()));
                } else {
                    leadInfo.setMcc(null);
                }
                leadInfo.setLeadNumber(leadDto.getLeadNumber());
//        leadInfo.setOnboardingType(leadDto.getOnboardingType());
                leadInfo.setVasInstanceMapping(leadDto.getVasInstanceMapping());
                leadInfo.setQR(leadDto.isQR());
                leadInfo.setSoundBox(leadDto.isSoundBox());
                leadInfo.setPos(leadDto.isPos());
                leadInfo.setPlan(leadDto.getPlan());
                leadInfo.setCorpCC(Double.valueOf(leadDto.getCorpCc()));
                logger.info("corp cc ----: {}", Double.valueOf(leadDto.getCorpCc()));
                leadInfo.setDebitLessThanAmount(Double.parseDouble(leadDto.getDebitLessthanAmount()));
                logger.info("debit less than Amount --- : {}", Double.parseDouble(leadDto.getDebitLessthanAmount()));
                leadInfo.setDebitGreaterThanAmount(Double.parseDouble(leadDto.getDebitGreaterthanAmount()));
                leadInfo.setIntlCC(Double.parseDouble(leadDto.getIntlCc()));
                leadInfo.setSuperPremiumCC(Double.parseDouble(leadDto.getSuperPremiumCc()));
                leadInfo.setStdCC(Double.parseDouble(leadDto.getStdCc()));
                leadInfo.setPremiumCC(Double.parseDouble(leadDto.getPremiumCc()));
                leadInfo.setSetUpFees(Double.parseDouble(leadDto.getSetupFees()));
                leadInfo.setRecurringFees(Double.parseDouble(leadDto.getRecurringFees()));
                leadInfo.setIcAcceptance(leadDto.isIcAcceptance());
                leadInfo.setTipPercentage(leadDto.getTipPercentage());
                if (leadDto.getSimProvider() != null) {
                    leadInfo.setSimProvider(simRepository.findSimByName(leadDto.getSimProvider()));
                } else {
                    leadInfo.setSimProvider(null);
                }
                leadInfo.setSettlementMode(leadDto.getSettlementMode());
                leadInfo.setOmcConvenienceFees(leadDto.isOmcConvenienceFees());
                leadInfo.setYearlyRevenue(leadDto.getYearlyRevenue());
                leadInfo.setLegalName(leadDto.getLegalName());
                leadInfo.setDbaName(leadDto.getDbaName());
                leadInfo.setShop_ownership_status(leadDto.getShop_ownership_status());
                leadInfo.setWeekDayTimingStart(leadDto.getWeekDayTimingStart());
                leadInfo.setWeekdayTimingEnd(leadDto.getWeekdayTimingEnd());
                leadInfo.setWeekendTimingStart(leadDto.getWeekendTimingStart());
                leadInfo.setWeekendTimingEnd(leadDto.getWeekendTimingEnd());
                leadInfo.setMaxMonthlyCardVolume(leadDto.getMaxMonthlyCardVolume());
                leadInfo.setAvgMonthlyCardvolume(leadDto.getAvgMonthlyCardvolume());
                leadInfo.setMemberSince(leadDto.getMemberSince());
                leadInfo.setHavePosMpos(leadDto.getHavePosMpos());
                leadInfo.setDebitMdr(leadDto.getDebitMdr());
                leadInfo.setCreditMdr(leadDto.getCreditMdr());
                leadInfo.setAmountCollected(Double.parseDouble(leadDto.getAmountCollected()));
                leadInfo.setTransactionDate(leadDto.getTransactionDate());
                leadInfo.setPaymentOption(Integer.parseInt(leadDto.getPaymentOption()));
                leadInfo.setPaymentContactNumber(leadDto.getPaymentContactNumber());
                leadInfo.setPaymentSuccess(leadDto.isPaymentSuccess());
                leadInfo.setIfscCode(leadDto.getIfscCode());
                leadInfo.setBank(leadDto.getBank());
                leadInfo.setBranch(leadDto.getBranch());
//        leadInfo.setLeadSourceMCCAccountTypeMapping(leadDto.getLeadSourceMCCAccountTypeMapping());
                leadInfo.setAccountNumber(leadDto.getAccountNumber());
                leadInfo.setReferenceNumber(leadDto.getReferenceNumber());
                leadInfo.setLeadCategory(Integer.parseInt(leadDto.getLeadCategory()));
                leadInfo.setReason(leadDto.getReason());
                leadInfo.setPincode(leadDto.getPincode());
                leadInfo.setCity(leadDto.getCity());
                leadInfo.setState(leadDto.getState());
                leadInfo.setOwnerDOB(leadDto.getOwnerDOB());
                leadInfo.setOwnerFirstName(leadDto.getOwnerFirstName());
                leadInfo.setOwnerLastName(leadDto.getOwnerLastName());
                leadInfo.setMerchantStateName(leadDto.getMerchantStateName());
                leadInfo.setLeadStatus(leadStatusRepository.findLeadStatusById(LEAD_SUBMITTED_TID_PENDING));
                leadInfo.setOnboardingVendor(leadDto.getOnboardingVendor());
                leadInfo.setSettlementType(Integer.parseInt(leadDto.getSettlementType()));
                leadInfo.setActive(true);
                leadInfo.setTurnOverLastFinancialYear(leadDto.getTurnOverLastFinancialYear());
                leadInfo.setCreditCardBlock(leadDto.isCreditCardBlock());
                leadInfo.setAvgBillAmount(leadDto.getAvgBillAmount());
                leadInfo.setMdrPlanName(leadDto.getMdrPlanName());
                if (leadDto.getMcc() != null) {
                    MerchantCategoryCode merchantCategoryCode = merchantCategoryCodeRepository.findByCode(leadDto.getMcc());
                    leadInfo.setMcc(merchantCategoryCode);
                } else {
                    leadInfo.setMcc(null);
                }
                if (leadInfo.getMcc() != null && leadInfo.getLeadSource() != null) {
                    leadInfo.setLeadSourceMCCAccountTypeMapping(leadSourceMCCAccountTypeRepository.findAccountType(leadInfo.getLeadSource(), leadInfo.getMcc()));
                }
                if (leadDto.getMarsRentalPlans() != null) {
                    leadInfo.setMarsRentalPlans(marsRentalPlansRepository.findByName(leadDto.getMarsRentalPlans()));
                    leadInfo.setRentalPlanCode(leadDto.getMarsRentalPlans());
                } else {
                    leadInfo.setMarsRentalPlans(null);
                    leadInfo.setRentalPlanCode(null);
                }
                leadInfo.setMasterPass(leadDto.getMasterPass());
                leadInfo.setCashAtPos(leadDto.getCashAtPos());
                leadInfo.setDiners(leadDto.getDiners());
                leadInfo.setOnus(leadDto.getOnus());
                leadInfo.setInternationalCreditCard(leadDto.getInternationalCreditCard());
                leadInfo.setInternationalDebitCard(leadDto.getInternationalDebitCard());
                leadInfo.setCommercialOrCorporate(leadDto.getCommercialOrCorporate());
                leadInfo.setSuperPremiumOrSignature(leadDto.getSuperPremiumOrSignature());
                leadInfo.setPremiumOrPlatinum(leadDto.getPremiumOrPlatinum());
                leadInfo.setStandardOrClassic(leadDto.getStandardOrClassic());
                leadInfo.setDomesticDebitAbove2000(leadDto.getDomesticDebitAbove2000());
                leadInfo.setDomesticDebitUpTo2000(leadDto.getDomesticDebitUpTo2000());
                logger.info("AMEX--{}DOMESTIC--{}", leadDto.getAmexDomestic(), (leadDto.getAmexInternational()));
                leadInfo.setAmexDomestic(leadDto.getAmexDomestic());
                leadInfo.setAmexInternational(leadDto.getAmexInternational());
                if (leadDto.getMarsMdrCharges() != null) {
                    leadInfo.setMarsMdrCharges(marsMdrChargesRepository.findByName(leadDto.getMarsMdrCharges()));
                    leadInfo.setMdrPlancode(leadDto.getMarsMdrCharges());

                    ObjectMapper mapper = new ObjectMapper();

                    MdrChargesDto corpCC = mapper.readValue(leadInfo.getMarsMdrCharges().getCommercialOrCorporate(), MdrChargesDto.class);
                    logger.info("Corp CC ------------ : {}", new Gson().toJson(corpCC));
                    leadInfo.setCorpCC(corpCC.getPercentage());

                    MdrChargesDto debitLessThanAmount = mapper.readValue(leadInfo.getMarsMdrCharges().getDomesticDebitUpTo2000(), MdrChargesDto.class);
                    leadInfo.setDebitLessThanAmount(debitLessThanAmount.getPercentage());
                    logger.info("debit less than Amount --- : {}", new Gson().toJson(debitLessThanAmount));

                    MdrChargesDto debitGreaterThanAmount = mapper.readValue(leadInfo.getMarsMdrCharges().getDomesticDebitAbove2000(), MdrChargesDto.class);
                    leadInfo.setDebitGreaterThanAmount(debitGreaterThanAmount.getPercentage());
                    logger.info("debit less than Amount --- : {}", new Gson().toJson(debitLessThanAmount));

                    MdrChargesDto intlCC = mapper.readValue(leadInfo.getMarsMdrCharges().getInternationalCreditCard(), MdrChargesDto.class);
                    leadInfo.setIntlCC(intlCC.getPercentage());
                    logger.info("international cc ---------- : {}", new Gson().toJson(intlCC));

                    MdrChargesDto superPremiumCC = mapper.readValue(leadInfo.getMarsMdrCharges().getSuperPremiumOrSignature(), MdrChargesDto.class);
                    leadInfo.setSuperPremiumCC(superPremiumCC.getPercentage());
                    logger.info("super premium cc ----------: {}", new Gson().toJson(superPremiumCC));

                    MdrChargesDto stdCC = mapper.readValue(leadInfo.getMarsMdrCharges().getStandardOrClassic(), MdrChargesDto.class);
                    leadInfo.setStdCC(stdCC.getPercentage());
                    logger.info("std cc ---------- : {}", new Gson().toJson(stdCC));

                    MdrChargesDto premiumCC = mapper.readValue(leadInfo.getMarsMdrCharges().getPremiumOrPlatinum(), MdrChargesDto.class);
                    leadInfo.setPremiumCC(premiumCC.getPercentage());
                    logger.info("premium or platinum -------------- : {}", new Gson().toJson(premiumCC));

                } else {
                    leadInfo.setMarsMdrCharges(null);
                    leadInfo.setMdrPlancode(null);
                }
                leadInfo.setProfessionalLead(leadDto.isProfessionalLead());
                leadInfo.setPanNumber(leadDto.getPanNumber());
                leadInfo.setGstNumber(leadDto.getGstNumber());
                leadInfo.setNatureOfBusiness(leadDto.getNatureOfBusiness());
                leadInfo.setAggregator(leadDto.getAggregator());
                leadInfo.setMerchantDistrictName(leadDto.getMerchantDistrictName());
                leadInfo.setMerchantSubDistrictName(leadDto.getMerchantSubDistrictName());
                leadInfo.setMerchantVillageName(leadDto.getMerchantVillageName());
                leadInfo.setMerchantTier(leadDto.getMerchantTier());
                leadInfo.setCategoryType(leadDto.getCategoryType());
                leadInfo.setAggregator(true);
                leadInfo.setVpaName(leadDto.getVpaName());
                leadInfo.setRid(leadDto.getRid());
                leadInfo.setCaseNumber(leadDto.getCaseNumber());
                leadInfo.setZone(leadDto.getZone());
                leadInfo.setStoreAddress(leadDto.getStoreAddress());
                leadInfo.setMerchantDob(leadDto.getMerchantDob());
                leadInfo.setMerchantPan(leadDto.getMerchantPan());
                leadInfo.setRegion(leadDto.getRegion());

                return leadInformationRepository.save(leadInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LeadInformation approveAgreementManually(long leadId) {
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        if (leadInformation != null) {
            leadInformation.setManualApproved(true);
            return leadInformationRepository.save(leadInformation);
        }
        return null;
    }

    @Override
    public List<QrVpa> storeVpaByLead(VpaShareDto vpaShareDto) throws ParseException, JsonProcessingException {

        List<QrVpa> vpas = new ArrayList<>();
        vpaShareDto.getVpas().forEach(l -> {
            QrVpa vpa = new QrVpa();
            vpa.setQr_Lead_id(leadInformationRepository.findByLeadNumber(vpaShareDto.getLeadNumber()));
            vpa.setQrVpa(l);
            vpa.setVpaStatus(sqrLeadStatusRepository.findSqrStatus(vpaShareDto.getVpaStatus()));
            logger.info("------------------------------------ : {}", vpaShareDto.getVpaStatus());
            vpas.add(vpa);
        });

        if (!vpas.isEmpty()) {
            qrVpaRepository.saveAll(vpas);

        }
        LeadInformation li = leadInformationRepository.findByLeadNumber(vpaShareDto.getLeadNumber());
        logger.info("-------------------------------------------------leadid---{}", li.getLeadId());
        li.setLeadStatus(leadStatusRepository.findLeadStatusById(QR_AGREEMENT_PENDING));
        logger.info("-----------------------------------------------leadStatus---{}", li.getLeadStatus());
        li.setPosQr(false);
        leadInformationRepository.save(li);

        // SQR agreement generate function

        AgreementDetails agreementDetails = documentService.getAggrement(li.getLeadId());//to generate sqr pdf
        //flag for identify Qr agg or Pos agg
        boolean qrFlag = true;

        SqrPdfReport report = new SqrPdfReport();
        ByteArrayOutputStream byteArrayOutputStream = report.createSqrAgreementPDF(li, agreementDetails);
        int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
        logger.info("page count ----- : {}", pageCount);
        String fileName = "AgreementForm.pdf";
        String contentType = MediaType.APPLICATION_PDF_VALUE;
        logger.info("contentType --: {}", contentType);
        MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
        AgreementDetails details = documentService.storePdfFile(multipartFile, li, qrFlag);
        logger.info("page count -----file stored --------------------------{}", details);
        AgreementDetails agreementDetails1 = agreementDetailsRepository.findLead1(li.getLeadId());// signature type
        String signType = agreementDetails1.getSignatureType();
        int action;
        if (Objects.equals(signType, "AADHAARESIGN-OTP")) {
            action = 1;
        } else {
            action = 0;
        }
        Pair<Integer, Object> result = signzyService.signzy360AggContract1(action, multipartFile, li, details, pageCount);
        logger.info("page count -----sign comple----------- {}", result.getFirst());

        if (result.getFirst() == 1) {
            return vpas;
        } else {
            li.setLeadStatus(leadStatusRepository.findLeadStatusById(LEAD_COMPLETED));
            qrVpaRepository.deleteByIds(li.getLeadId());
            return null;
        }
    }

    public static int getPageCountFromByteArrayOutputStream(ByteArrayOutputStream outputStream) {
        try {
            // Create a PdfReader from the ByteArrayOutputStream
            PdfReader pdfReader = new PdfReader(outputStream.toByteArray());

            // Get the total number of pages
//            int pageCount = getNumberOfPages(pdfReader);
            int pageCount = pdfReader.getNumberOfPages();
            logger.info("page count---------- : {}", pageCount);
            return pageCount;
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return 0; // Return a sentinel value or handle the error accordingly
        }
    }

    @Override
    public List<String> findsLeadByLeadId(long leadId) {
        List<String> vpa = qrVpaRepository.getVpa(leadId);
        return vpa;
    }

    @Override
    public MarsDeviceDetails getMarsDeviceDetails(long leadId) {
        MarsDeviceDetails marsDeviceDetails = marsDeviceDetailsRepository.getMarsDeviceDetails(leadId);
        return marsDeviceDetails;
    }

    @Override
    public int getCount(long leadId) {
        return qrVpaRepository.getCount(leadId);
    }

    @Override
    public QrVpa getQrVpa(long leadId, String l) {
        QrVpa qrVpa = qrVpaRepository.vpaById(leadId, l);
        return qrVpa;
    }

    @Override
    public LeadStatus getLeadStatus(long qrAgreementPending) {
        LeadStatus status = leadStatusRepository.getStatus(qrAgreementPending);
        return status;
    }

    @Override
    public AgreementDetails getAggrement(long leadId) {
        AgreementDetails agreementDetails = agreementDetailsRepository.findLead(leadId);
        return agreementDetails;
    }

    @Override
    public List<LeadDocuments> aggDocumentApprovalOrRejectMcc(long leadId, long subDocumentType, int action, DocumentUpdateDto documentUpdateDto, User curuser) throws MessagingException {
        List<LeadDocuments> documents = new ArrayList<>();
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        SubDocumentType subDocumentType1 = subDocumentTypeRepository.findBySubDocumentId(subDocumentType);
        List<LeadDocuments> lead = leadDocumentRepository.fetchLeadDocumentByLeadAndDocument1(leadInformation, subDocumentType1.getSubDocumentTypeId());
        logger.info("document list -- : {}", lead.size());
        //Approval process
        if (action == Constants.LEAD_DOCUMENT_APPROVAL) {
            logger.info("approval process -- : ");
            lead.forEach(approve -> {
                LeadDocuments leadDocuments = new LeadDocuments();
                if (approve.getDocumentVerifiedStatus() == MCC_VERIFICATION_PENDING || approve.getDocumentVerifiedStatus() == MCC_DOCUMENT_RE_UPLOAD) {
                    approve.setDocumentVerifiedStatus(Constants.MCC_VERIFICATION_APPROVE);
                    approve.setApprovedDate(DateUtil.currentDate());
                    leadDocuments = leadDocumentRepository.save(approve);
                }
                documents.add(leadDocuments);
            });
            Auditing_Table auditingTable = new Auditing_Table();
            auditingTable.setAction("MCC");
            auditingTable.setLeadId(leadInformation.getLeadId());
            auditingTable.setMccRequested(leadInformation.getMcc());
            auditingTable.setMccApproved(leadInformation.getOpsMcc());
            auditingTable.setStatus("MCC DOCUMENT APPROVED");
            auditingTable.setRequestDate(leadInformation.getCreatedAt());
            auditingTable.setUpdatedDate(DateUtil.currentDate());
            auditingTable.setUser(curuser);
            auditingTableRepository.save(auditingTable);
//            String reason = "Mcc Document has been Approved by OPS";
//            String title = "Mcc Document Approved";
//            sendPushNotification(title, reason, leadInformation, PUSH_NOTIFICATION_TO_DOCUMENT_REJECTED);
            logger.info("");
        }
        //Reject Process
        else if (action == Constants.LEAD_DOCUMENT_REJECT) {
            logger.info("reject process -- : ");
            lead.forEach(reject -> {
                LeadDocuments leadDocuments = new LeadDocuments();
                logger.info("document verifucation status -- : {}", reject.getDocumentVerifiedStatus());
                if (reject.getDocumentVerifiedStatus() == MCC_VERIFICATION_PENDING || reject.getDocumentVerifiedStatus() == MCC_VERIFICATION_APPROVE || reject.getDocumentVerifiedStatus() == MCC_DOCUMENT_RE_UPLOAD) {
                    logger.info("rejected document type and status -- : {} {}", reject.getSubDocumentType(), reject.getDocumentVerifiedStatus());
                    reject.setDocumentVerifiedStatus(MCC_VERIFICATION_REJECT);
                    logger.info("rejected -- : {}", reject.getDocumentVerifiedStatus());
                    reject.setLeadCancellationReason(documentUpdateDto.getCancellationReason());
                    reject.setDocumentRemarks(documentUpdateDto.getDocumentRemarks());
                    reject.setRejectedBy(getSessionUser());
                    reject.setRejectedDate(DateUtil.currentDate());
                    leadDocuments = leadDocumentRepository.save(reject);
                }
                documents.add(leadDocuments);
            });
            Auditing_Table auditingTable = new Auditing_Table();
            auditingTable.setAction("MCC");
            auditingTable.setLeadId(leadInformation.getLeadId());
            auditingTable.setMccRequested(leadInformation.getMcc());
            auditingTable.setMccApproved(leadInformation.getOpsMcc());
            auditingTable.setStatus("MCC DOCUMENT REJECTED");
            auditingTable.setRequestDate(leadInformation.getCreatedAt());
            auditingTable.setUpdatedDate(DateUtil.currentDate());
            auditingTable.setUser(curuser);
            auditingTableRepository.save(auditingTable);
//            leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(Pending_MCC_documents_resubmission));
            leadInformation.setActive(true);
//            leadInformation.setDocumentStatus(2);
            leadInformationRepository.save(leadInformation);
//            String reason = "Your lead"+leadId +"documents have been rejected by OPS. Please reupload to continue. ";
//            String title = "Lead "+leadId+" documents are rejected!";
//            sendPushNotification(title, reason, leadInformation, PUSH_NOTIFICATION_TO_PENDING_MERCHANT_DETAILS_PENDING);
        }
            return documents;
        }

        @Override
        public List<RiskMccNpci> getMccNpci () {
            return riskMccNpciRepo.findAll();
        }

        @Override
        public MerchantCategoryCode findMccCode1 (String mcc){
            return merchantCategoryCodeRepository.findByCode(mcc);
        }

        @Override
        public RiskMccNpci saveLead (RiskMccNpci riskMccNpci){
            return riskMccNpciRepo.save(riskMccNpci);
        }

        @Override
        public LeadInformation saveStatus ( long leadId){
            LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
            leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(Pending_MCC_documents_resubmission));
            leadInformation.setDocumentStatus(2);
            LeadInformation leadInformation1 = leadInformationRepository.save(leadInformation);
            String reason = "Your lead" + leadId + "documents have been rejected by OPS. Please reupload to continue. ";
            String title = "Lead " + leadId + " documents are rejected!";
            sendPushNotification(title, reason, leadInformation, PUSH_NOTIFICATION_TO_PENDING_MERCHANT_DETAILS_PENDING);
            return leadInformation1;
        }

        @Override
        public List<LeadDocuments> aggDocumentApprovalOrRejectWowPe ( long leadId, long subDocType,
        int action, DocumentUpdateDto documentUpdateDto) throws MessagingException {
            List<LeadDocuments> documents = new ArrayList<>();
            LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
            SubDocumentType subDocumentType = subDocumentTypeRepository.findBySubDocumentId(subDocType);
            List<LeadDocuments> lead = leadDocumentRepository.getLeadDocumentsByLeadAndSubDocument(leadInformation, subDocumentType);
            logger.info("document list -- : {}", lead.size());
            //Approval process
            if (action == Constants.LEAD_DOCUMENT_APPROVAL) {
                logger.info("approval process -- : ");
                lead.forEach(approve -> {
                    LeadDocuments leadDocuments = new LeadDocuments();
                    if (approve.getDocumentVerifiedStatus() == OPS_VERIFICATION_PENDING) {
                        approve.setDocumentVerifiedStatus(Constants.OPS_VERIFICATION_SUCCESS);
                        approve.setApprovedDate(DateUtil.currentDate());
                        leadDocuments = leadDocumentRepository.save(approve);

                        LeadDocuments finalLeadDocuments = leadDocuments;
//                    CompletableFuture.runAsync(() -> {
                        logger.info("update---------------------------------------:");
                        // transfer data to Onboarding Portal
                        this.updateDocumentApproveOrRejectDataWowPe(leadInformation.getLeadNumber(), approve.getSubDocumentType(), finalLeadDocuments);
//                    });
                    }
//                approve.setApprovedBy(getSessionUser());
                    documents.add(leadDocuments);
                });
            }        //Reject Process
            else if (action == Constants.LEAD_DOCUMENT_REJECT) {
                logger.info("reject process -- : ");
                lead.forEach(reject -> {
                    LeadDocuments leadDocuments = new LeadDocuments();
                    logger.info("document verifucation status -- : {}", reject.getDocumentVerifiedStatus());
                    if (reject.getDocumentVerifiedStatus() == OPS_VERIFICATION_PENDING || reject.getDocumentVerifiedStatus() == OPS_VERIFICATION_SUCCESS) {
                        logger.info("rejected document type and status -- : {} {}", reject.getSubDocumentType(), reject.getDocumentVerifiedStatus());
                        reject.setDocumentVerifiedStatus(OPS_VERIFICATION_FAILED);
                        logger.info("rejected -- : {}", reject.getDocumentVerifiedStatus());
                        reject.setLeadCancellationReason(documentUpdateDto.getCancellationReason());
                        reject.setDocumentRemarks(documentUpdateDto.getDocumentRemarks());
                        reject.setRejectedBy(getSessionUser());
                        reject.setRejectedDate(DateUtil.currentDate());
                        leadDocuments = leadDocumentRepository.save(reject);

                        CompletableFuture.runAsync(() -> {
                            // transfer data to Onboarding Portal
                            this.updateDocumentApproveOrRejectDataWowPe(leadInformation.getLeadNumber(), reject.getSubDocumentType(), reject);
                        });
                    }
                    documents.add(leadDocuments);
                });

                leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(DOCUMENT_REJECTED_BY_OPS));
                leadInformation.setActive(false);
                leadInformationRepository.save(leadInformation);

                mailHandler.sendDocumentRejectedEmail(leadInformation.getContactEmail(), leadInformation.getLeadNumber(), subDocumentType.getName(), leadInformation.getLeadSource().getLeadSourceName());
            }
            return documents;
        }

        private void updateDocumentApproveOrRejectDataWowPe (String leadNumber, SubDocumentType
        subDocumentType, LeadDocuments reject){
            ResponseEntity<String> out = null;
            HttpHeaders headers = new HttpHeaders();
            logger.info("headers------------------: {}", headers);
            LeadDocumentDto documentDtoList = this.getRejectedDocumentDetails(reject);
//        String url = "http://192.168.4.227:7077/api/document/documentUpdate/{leadNumber}/{subDocumentType}";
            String url = subDocumentUpdateUrl + "/{leadNumber}/{DocCatagory}?subDocName=" + subDocumentType.getName();
            logger.info("url---: {}", url);

            //            String documentmapping = URLEncoder.encode(documentMapping.getDocumentName(), "UTF-8");
            logger.info("document mapping -------- : {}", subDocumentType.getName());

            HttpEntity<LeadDocumentDto> entity = new HttpEntity<>(documentDtoList, headers);
            try {
                Map<String, String> urlParams = new HashMap<>();
                urlParams.put("leadNumber", leadNumber);
                urlParams.put("DocCatagory", String.valueOf(subDocumentType.getDocumentCategory().getId()));
                urlParams.put("subDocName", subDocumentType.getName());
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                logger.info("path variables----: {}", builder.buildAndExpand(urlParams).toUri());
                out = restTemplate.exchange(builder.build(urlParams), HttpMethod.PUT, entity, String.class);
                logger.info("out status code---------------------: {}", out.getStatusCode());
            } catch (HttpClientErrorException | HttpServerErrorException exception) {
                logger.info("exception -------: {}", exception.getResponseBodyAsString());
                exception.printStackTrace();
            }
        }

        @Override
        public DocumentMapping getDocument ( long subDocumentType){
            return documentMappingRepository.findDocumentMappingById(subDocumentType);
        }

        @Override
        public UploadedDocuments storeLeadDocuments1 (LeadInformation lead, FileDto fileDto, DocumentMapping
        subDocument, String typeOfDocument){
            LeadDocuments documents = new LeadDocuments();
            UploadedDocuments response = null;

            logger.info("check document exits or not ----------:");
            // check whether the documents already exits
            List<LeadDocuments> checkDocumentExists = leadDocumentRepository.fetchLeadDocumentByLeadAndDocumentMapping(lead, subDocument);
            logger.info("checkDocumentExists----------: {}", checkDocumentExists);
            if (!checkDocumentExists.isEmpty()) {
                logger.info("getDocumentVerifiedStatus 1----------: {}", checkDocumentExists.get(0).getDocumentVerifiedStatus());
                if (checkDocumentExists.get(0).getDocumentVerifiedStatus() == OPS_VERIFICATION_FAILED) {
                    documents.setDocumentMapping(subDocument);
                    documents.setLeadInformation(lead);
                    documents.setDocumentVerifiedStatus(Constants.OPS_VERIFICATION_PENDING);
                    documents.setUploadedBy(getSessionUser());
                    documents = leadDocumentRepository.save(documents);
                    response = uploadedDocumentsFunction(documents, fileDto, typeOfDocument);
                } else if (checkDocumentExists.get(0).getDocumentVerifiedStatus() == OPS_VERIFICATION_PENDING) {
//                documents.setSubDocumentType(subDocument);
                    documents.setLeadInformation(lead);
                    documents.setDocumentMapping(subDocument);
                    documents.setDocumentVerifiedStatus(Constants.OPS_VERIFICATION_PENDING);
                    documents.setUploadedBy(getSessionUser());
                    documents = leadDocumentRepository.save(documents);
                    response = uploadedDocumentsFunction(documents, fileDto, typeOfDocument);

                }
            } else {
                documents.setLeadInformation(lead);
                documents.setDocumentMapping(subDocument);
                documents.setDocumentVerifiedStatus(Constants.OPS_VERIFICATION_PENDING);
                documents.setUploadedBy(getSessionUser());
                documents = leadDocumentRepository.save(documents);
                response = uploadedDocumentsFunction(documents, fileDto, typeOfDocument);

            }
            return response;
        }

        @Override
        public LeadDocuments aggApprovalDocumentReject ( long leadId, LeadDocuments reject, DocumentUpdateDto
        documentUpdateDto){
            LeadDocuments leadDocuments = new LeadDocuments();
            logger.info("document verifucation status -- : {}", reject.getDocumentVerifiedStatus());
            if (reject.getDocumentVerifiedStatus() == OPS_VERIFICATION_SUCCESS) {
                logger.info("rejected document type and status -- : {} {}", reject.getSubDocumentType(), reject.getDocumentVerifiedStatus());
                reject.setDocumentVerifiedStatus(OPS_VERIFICATION_PENDING);
                logger.info("rejected -- : {}", reject.getDocumentVerifiedStatus());
                reject.setLeadCancellationReason(documentUpdateDto.getCancellationReason());
                reject.setDocumentRemarks(documentUpdateDto.getDocumentRemarks());
                reject.setRejectedBy(getSessionUser());
                reject.setRejectedDate(DateUtil.currentDate());
                leadDocuments = leadDocumentRepository.save(reject);
            }
            return leadDocuments;

        }

        @Override
        public List<DirectorDTO> getAuthorizerDocuments (LeadInformation leadInformation){
            List<Long> categories = Arrays.asList(AUTH_SIGN1, AUTH_SIGN2, AUTH_SIGN3, AUTH_SIGN4, AUTH_SIGN5);
            List<DocumentCategory> documentCategories = documentCategoryRepository.fetchDocumentCategoryListById(categories);
            List<DirectorDTO> directorDTOList = new ArrayList<>();
            int i = 1;
            for (DocumentCategory documentCategory : documentCategories) {
                logger.info("Category Nmae---{}", documentCategory.getName());
                List<UploadedDocuments> uploadedDocuments = uploadedDocumentRepository.findAuthorizerDocument(leadInformation, documentCategory);
//            List<UploadedDocuments> mandatoryDocs = uploadedDocuments.stream()
//                    .filter(doc -> doc.getLeadDocuments().getSubDocumentType().isMandatory())
//                    .toList();
//            List<Boolean> mandatoryFlags = mandatoryDocs.stream()
//                    .map(doc -> doc.getLeadDocuments().getDocumentVerifiedStatus()!=5)
//                    .toList();


                DirectorDTO director = new DirectorDTO();
                director.setAuthorizerNumber(i);
                director.setName(documentCategory.getName());
                DirectorDTO.Documents documents = new DirectorDTO.Documents();
                List<DirectorDTO.SubDocument> subDocuments = new ArrayList<>();
                if (!uploadedDocuments.isEmpty()) {
                    for (UploadedDocuments uploadedDocuments1 : uploadedDocuments) {
                        logger.info("Document Name--{}", uploadedDocuments1.getLeadDocuments().getSubDocumentType().getName());
                        logger.info("Category--{}", uploadedDocuments1.getLeadDocuments().getSubDocumentType().getSubDocumentTypeId());
                        DirectorDTO.SubDocument subDocument = new DirectorDTO.SubDocument();
                        if (uploadedDocuments1.getLeadDocuments().getSubDocumentType().getName().equals("Pan Card") && documents.getPan() == null) {
                            subDocument.setSubDocumentTypeId(uploadedDocuments1.getLeadDocuments().getSubDocumentType().getSubDocumentTypeId());
                            subDocument.setName(uploadedDocuments1.getLeadDocuments().getSubDocumentType().getName());
                            subDocument.setUploadedFileName(uploadedDocuments1.getFileName());
                            subDocument.setLeadDocumentId(uploadedDocuments1.getLeadDocuments().getLeadDocumentId());
                            subDocument.setStatus(uploadedDocuments1.getLeadDocuments().getDocumentVerifiedStatus());
                            subDocument.setUploaded(true);
                            documents.setPan(subDocument);
                        } else if (!uploadedDocuments1.getLeadDocuments().getSubDocumentType().isMandatory() && subDocuments.isEmpty()) {
                            DirectorDTO.SubDocument subDocument1 = new DirectorDTO.SubDocument();
                            subDocument1.setSubDocumentTypeId(uploadedDocuments1.getLeadDocuments().getSubDocumentType().getSubDocumentTypeId());
                            subDocument1.setName(uploadedDocuments1.getLeadDocuments().getSubDocumentType().getName());
                            subDocument1.setUploadedFileName(uploadedDocuments1.getFileName());
                            subDocument1.setLeadDocumentId(uploadedDocuments1.getLeadDocuments().getLeadDocumentId());
                            subDocument1.setStatus(uploadedDocuments1.getLeadDocuments().getDocumentVerifiedStatus());
                            subDocument1.setUploaded(true);
                            subDocuments.add(subDocument1);
                        } else if (!uploadedDocuments1.getLeadDocuments().getSubDocumentType().getName().equals("Pan Card") && uploadedDocuments1.getLeadDocuments().getSubDocumentType().isMandatory() && documents.getSign() == null) {
                            subDocument.setSubDocumentTypeId(uploadedDocuments1.getLeadDocuments().getSubDocumentType().getSubDocumentTypeId());
                            subDocument.setName(uploadedDocuments1.getLeadDocuments().getSubDocumentType().getName());
                            subDocument.setUploadedFileName(uploadedDocuments1.getFileName());
                            subDocument.setLeadDocumentId(uploadedDocuments1.getLeadDocuments().getLeadDocumentId());
                            subDocument.setStatus(uploadedDocuments1.getLeadDocuments().getDocumentVerifiedStatus());
                            subDocument.setUploaded(true);
                            documents.setSign(subDocument);
                        }
                    }
                    documents.setUploadAnyOne(subDocuments);
                    director.setDocuments(documents);
                    directorDTOList.add(director);
                }
                i++;
            }
            return directorDTOList;
        }

        @Override
        public GstDocumentDTO getGst (LeadInformation leadInformation){
            GstDocumentDTO gstDocumentDTO = new GstDocumentDTO();
            List<UploadedDocuments> uploadedDocumentList = new ArrayList<>();
            List<UploadedDocuments> rejectedDocumentList = new ArrayList<>();
            DocumentCategory documentCategory = documentCategoryRepository.findBusinessDoc().get(0);
            List<UploadedDocuments> uploadedDocuments = uploadedDocumentRepository.findAuthorizerDocument(leadInformation, documentCategory);
            if (!uploadedDocuments.isEmpty()) {
                for (UploadedDocuments uploadedDocuments1 : uploadedDocuments) {
                    if (uploadedDocuments1.getLeadDocuments().getDocumentVerifiedStatus() == OPS_VERIFICATION_PENDING || uploadedDocuments1.getLeadDocuments().getDocumentVerifiedStatus() == OPS_VERIFICATION_SUCCESS) {
                        uploadedDocumentList.add(uploadedDocuments1);
                    } else if (uploadedDocuments1.getLeadDocuments().getDocumentVerifiedStatus() == OPS_VERIFICATION_FAILED) {
                        rejectedDocumentList.add(uploadedDocuments1);
                    }
                }
            }
            gstDocumentDTO.setUploadedDocuments(uploadedDocumentList);
            gstDocumentDTO.setRejectedDocument(rejectedDocumentList);
            return gstDocumentDTO;
        }

        @Override
        public DocumentCategory getDocumentCategory () {
            return documentCategoryRepository.getDocById(ADDITIONAL_DOCUMENT);
        }

        @Override
        public List<SubDocumentType> getCategoryBasedDocument (DocumentCategory documentCategory){
            return subDocumentTypeRepository.fetchDocumentType(documentCategory);
        }

    @Override
    public boolean verifyAdditionalDocuments(DocumentCategory documentCategory, long leadId) {
        List<SubDocumentType> subDocumentType=subDocumentTypeRepository.fetchDocumentType(documentCategory);
        LeadInformation leadInformation=leadInformationRepository.findByLeadId(leadId);
        for (SubDocumentType l :subDocumentType) {
            if (l.getName().equals("ICA Document")) {
                List<LeadDocuments> leadDocuments = leadDocumentRepository.getLeadDocumentsByLeadAndSubDocument(leadInformation, l);
                if (leadDocuments.size() > 0) {
                    if (leadDocuments.get(0).getDocumentVerifiedStatus() != OPS_VERIFICATION_SUCCESS) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    @Override
    public DocumentCategory getDocumentCategoryForOPS() {
        return documentCategoryRepository.getDocById(ADDITIONAL_DOCUMENT_BY_OPS);
    }

    @Override
    public DocumentCategory getDocumentCategoryForOfflinePayment() {
        return documentCategoryRepository.getDocById(OFFLINE_PAYMENT_DOCUMENT);
    }

    @Override
    public void deleteUnMaskedImage(String unMaskedFileName) {
        this.deleteAttachment(unMaskedFileName);
        logger.info("unMasked Document Deleted");
    }

    public int sms(String phn,String messageTemplate) {
        int result=0;
        try {
            SmsRequestDto smsRequestDto = new SmsRequestDto();
            smsRequestDto.setTo(phn);
            smsRequestDto.setSender("BIJLPY");
            smsRequestDto.setService("SI");
            smsRequestDto.setCountry_code("+91");
            smsRequestDto.setTemplate_id("1177178963912247815");
            smsRequestDto.setMessage(messageTemplate);
            smsRequestDto.setShorten_url("1");
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer 630e4cc055707c99c2055ccb1bc8549b");
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<SmsRequestDto> entity = new HttpEntity<>(smsRequestDto, headers);
            logger.info("request body ---------------: {}", smsRequestDto);
            logger.info("qikberry entity ----------------- : {}", entity);
            ResponseEntity<String> response = restTemplate.exchange("https://rest.qikberry.ai/v1/sms/messages", HttpMethod.POST, entity, String.class);
            logger.info("response out ---------------------: {}", response.getBody());
            logger.info("response from qikberry--{}", response.getStatusCodeValue());
            Gson gson = new Gson();
            String out = gson.toJson(response);
            logger.info("qikberry response body--{}", out);
            result=1;
        } catch (Exception e) {
            logger.info("ERROR OCCURED DURING SMS OTP");
            e.printStackTrace();
            return result;
        }
        return result;
    }


    @Override
    public UploadedDocuments storeDigitalLeadDocuments(LeadInformation lead, FileDto file, SubDocumentType subDocumentType, String typeOfDocument) {

        LeadDocuments documents = new LeadDocuments();
        UploadedDocuments response = null;
        logger.info("Document Status from lead --{}", lead.getLeadStatus().getLeadStatusId());
        documents.setSubDocumentType(subDocumentType);
        documents.setLeadInformation(lead);
        if (subDocumentType.isSignzyVerificationRequired()) {
            documents.setDocumentVerifiedStatus(Constants.DOCUMENT_VERIFICATION_PENDING);
        } else if ((lead.getLeadStatus().getLeadStatusId() == PENDING_MERCHANT_DETAILS  ||
                lead.getLeadStatus().getLeadStatusId() == PENDING_MCC_APPROVAL) && !(lead.isMccApproval())) {
            documents.setDocumentVerifiedStatus(MCC_VERIFICATION_PENDING);
        } else if ((lead.getLeadStatus().getLeadStatusId() == PENDING_MCC_DOCUMENTS_RESUBMISSION) && !(lead.isMccApproval())) {
            documents.setDocumentVerifiedStatus(MCC_DOCUMENT_RE_UPLOAD);
            lead.setDocumentStatus(1);
        } else {
            documents.setDocumentVerifiedStatus(Constants.OPS_VERIFICATION_PENDING);
            if (documents.getDocumentVerifiedStatus() == OPS_VERIFICATION_FAILED) {
                lead.setDocumentStatus(3);
            }
        }
        documents.setUploadedBy(getSessionUser());
        documents = leadDocumentRepository.save(documents);
        leadInformationRepository.save(lead);
        response = uploadedDocumentsFunction(documents, file, typeOfDocument);
        storeLeadDocumentActivityHistory(response, Constants.DOCUMENT_UPLOADED);
        return response;
    }


    @Override
    public List<MarsDeviceDetails> getMarsDeviceDetailsList(long leadId) {
        List<MarsDeviceDetails> marsDeviceDetails = marsDeviceDetailsRepository.getMarsDeviceDetailsList(leadId);
        return marsDeviceDetails;
    }
}
