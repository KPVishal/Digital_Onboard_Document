package com.bijlipay.controller;

import com.bijlipay.config.Constants;
import com.bijlipay.model.*;
import com.bijlipay.model.dto.*;
import com.bijlipay.model.dto.signzy.esign.EsignPdfResponseDto;
import com.bijlipay.property.FileConfig;
import com.bijlipay.repository.*;
import com.bijlipay.service.*;
import com.bijlipay.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.bijlipay.config.Constants.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/document/upload")
public class DocumentController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    @Value("${file.upload-dir}")
    private String uploadedDir;

    @Value("${implementation.form.url}")
    private String implementationSmsUrl;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private SignzyService signzyService;
    @Autowired
    private LeadInformationRepository leadInformationRepository;
    @Autowired
    private LeadDocumentHistoryService leadDocumentHistoryService;
    @Autowired
    private LeadDocumentRepository leadDocumentRepository;
    @Autowired
    private UploadedDocumentRepository uploadedDocumentRepository;
    @Autowired
    private SignzyFetchAndVerificationRepository signzyFetchAndVerificationRepository;
    @Autowired
    private AttributeEncryptor attributeEncryptor;

    @Autowired
    private UserAuthenticationService userAuthenticateService;

    @Autowired
    private VasRepository vasRepository;

    @Autowired
    private MerchantTypeRepository merchantTypeRepository;

    @Autowired
    private ByteArrayMultiPartFileService byteArrayMultiPartFileService;
    @Value("${signzy.file.upload.url}")
    private String signzyFileUploadUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SubDocumentTypeRepository subDocumentTypeRepository;
    @Autowired
    private SMSPaymentDetailsRepository smsPaymentDetailsRepository;
    @Autowired
    private InternalSubventionRepository internalSubventionRepository;
    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private CounterManagementRepository counterManagementRepository;

    @Autowired
    private ImplementationDocumentReposistory implementationDocumentReposistory;



    /***
     * leadId - lead information primary key
     * documentType - type of document uploaded
     */

    @PostMapping({"/upload-lead-document/{leadId}/{subDocumentType}", "/upload-lead-document/{leadId}/{subDocumentType}/{latlng}"})
    @Transactional
    public ResponseEntity<?> uploadLeadDocument(HttpServletRequest request, @PathVariable("leadId") long leadInformation, @PathVariable("subDocumentType") long subDocumentType,
                                                @RequestParam("file") MultipartFile file, @PathVariable("reProcess") Optional<Integer> reprocess,
                                                @RequestParam(value = "typeOfDocument", required = false) String typeOfDocument, @PathVariable("latlng") Optional<String> latlng, @RequestParam(value = "filePayload", required = false) String filePayload) throws Exception {


        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {


            if (file.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "file is empty"), HttpStatus.OK);
            }

//        int reProcess = 0;
//        if (reprocess.isPresent()) {
//            reProcess = reprocess.get();
//        }
            //Lead check
            LeadInformation lead = documentService.checkLead(leadInformation);
            if (filePayload != null) {
                String[] data = filePayload.split(",");
                lead.setAdditionalDocumentName(data[0]);
                lead.setAdditionalDocumentRemark(data[1]);
                leadInformationRepository.save(lead);
            }
            if (lead != null) {
                //Document type check
                //DocumentType document = documentService.getDocumentDetails(documentType);
                SubDocumentType subDocument = documentService.getSubDocumentDetails(subDocumentType);
                if (subDocument != null) {
                    logger.info("file -- : {}", file.getOriginalFilename());
                    //Saving each file separately in directory
//                List<FileDto> fileDtoList = new ArrayList<>();
//                file.forEach(l -> {
                    FileDto fileDto = new FileDto();
                    logger.info("file -- : {}", file.getOriginalFilename());
                    String newFileName = lead.getLeadId() + "_" + subDocument.getName();
                    Map<String, String> response = fileStorageService.storeFile(file, newFileName);
                    fileDto.setFileName(response.get("fileName"));
                    fileDto.setMimeType(response.get("mimeType"));
                    fileDto.setFileNameOriginal(response.get("fileNameOriginal"));
                    //osv entry for agreement generation
                    if (subDocument.getMccCategoryIndicator() == MCC_CATEGORY_INDICATOR_OSV) {
                        fileStorageService.storeOSVFile(file, fileDto);
                    }
//                    fileDtoList.add(fileDto);
//                });

                    if (response != null) {
                        logger.info("files stored successfully ");
//            // Generate file Url to share with signzy
//            List<String> imageList = new ArrayList<>();
//            fileDtoList.stream().forEach(l ->{
//                String imageUrl = getFileUrl(l.getFileName());
//                imageList.add(imageUrl);
//                logger.info("image url -- : {}", imageUrl);
//            });

                        //Storing lead and uploaded documents table
                        UploadedDocuments model = documentService.storeLeadDocuments(lead, fileDto, subDocument, typeOfDocument, latlng);

                        if (model != null) {

                            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", model), HttpStatus.OK);
                        }
                        return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "documents details not updated in table"), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "files not stored !"), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Document type not found"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "lead not found"), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /***
     * leadDocuments
     * leadDocumentHistory
     */
    @PostMapping("/create-lead-document-history/{leadDocumentId}")
    public ResponseEntity<?> documentHistoryDetails(HttpServletRequest request, @PathVariable("leadDocumentId") LeadDocuments leadDocuments, @RequestBody LeadDocumentHistory leadDocumentHistory) throws Exception {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            logger.info("into create Lead document history");
            LeadDocumentHistory leadDocumentHistoryDetail = leadDocumentHistoryService.createLeadDocumentHistory(leadDocuments, leadDocumentHistory);

            if (leadDocumentHistoryDetail != null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "History Created Successfully", leadDocumentHistoryDetail), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "History not created", null), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }


    }

    /**
     * Verify Uploaded Documents
     */

    @PostMapping("/verify-documents/{subDocumentType}")
    public ResponseEntity<?> verifyDocuments(HttpServletRequest request, @PathVariable("subDocumentType") SubDocumentType subDocumentType, @RequestBody DocumentVerifyDto documentVerifyDto) throws Exception {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            SubDocumentType subDocument = documentService.getSubDocumentDetails(subDocumentType.getSubDocumentTypeId());
            if (subDocument.isSignzyVerificationRequired()) {
                Pair<Integer, Object> result = documentService.verifyDocuments(subDocumentType, documentVerifyDto);
                if (result.getFirst() == 1) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "verify not successful", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "signzy verification not required"), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Delete uploaded document
     */
    @DeleteMapping("/delete-uploaded-document/{leadDocId}")
    public ResponseEntity<?> deleteUploadedDocument(HttpServletRequest request, @PathVariable("leadDocId") LeadDocuments leadDocId) {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            logger.info("delete Document");
            List<UploadedDocuments> uploadedDocuments = documentService.findByLeadDocumentId(leadDocId);
            if (uploadedDocuments != null) {
                documentService.delete(leadDocId);
            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get Document Type List Based On Document Category for SAT Portal
     * 1 - Proof of Identity
     * 2 - Proof of Address
     * 3 - Proof of Business
     * 4 - Proof of Business Category
     */
    @GetMapping("category-based-document-type/{documentCategory}")
    public ResponseEntity<?> getDocumentTypeBasedOnCategoryList(HttpServletRequest request, @PathVariable("documentCategory") DocumentCategory documentCategory) {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            List<DocumentType> documentTypeList = documentService.getCategoryBasedDocumentType(documentCategory);
            if (documentTypeList.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", documentTypeList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * upload-lead-document
     * SubDocument Details based on Document type
     */

    @GetMapping("subDocumentList/{documentType}")
    public ResponseEntity<?> subDocumentList(HttpServletRequest request, @PathVariable("documentType") DocumentType documentType) {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            List<SubDocumentType> subDocumentTypeList = documentService.getSubDocumentTypeList(documentType);
            if (subDocumentTypeList.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "data not found"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", subDocumentTypeList), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /***
     *  call back details store for Aadhaar card digilockers
     */
    @PostMapping("/callBackDetails")
    public ResponseEntity<?> callBackDetails(@RequestBody String callbackResponse, HttpServletResponse response) throws Exception {

        JSONObject jsonObject = new JSONObject(callbackResponse);
        String requestId = jsonObject.getString("requestId");
        SignzyFetchAndVerification fetchAndVerification = documentService.getSignzyDetails(requestId);

        if (fetchAndVerification != null) {
            String validate = "success";// Aadhaar verification process
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase(validate)) {
                fetchAndVerification.setAadhaarVerified("true");
                fetchAndVerification.setMessage(jsonObject.getString("message"));
            } else {
                fetchAndVerification.setAadhaarVerified("false");
                fetchAndVerification.setMessage(jsonObject.getString("message"));
            }
//            fetchAndVerification.setAadhaarDetails(new Gson().toJson(requestParams));
            fetchAndVerification.setAadhaarDetails(attributeEncryptor.encrypt(callbackResponse));
            SignzyFetchAndVerification signzyFetchAndVerification = documentService.saveCallBackDetails(fetchAndVerification);
            logger.info("call back data updated -- : {}", signzyFetchAndVerification.getAadhaarVerified());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "requestId not found"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("getDetails/{id}")
    public ResponseEntity<?> data(@PathVariable("id") String id) {
        SignzyFetchAndVerification data = signzyFetchAndVerificationRepository.findVerifiedDetails(id);
        return new ResponseEntity<>(new ApiResponse(OK, "", attributeEncryptor.decrypt(data.getAadhaarDetails())), OK);
    }

    /*
     *  Get Details Api of Digilocker for Aadhaar Card
     *  SO will initiate this Api
     */
    @PostMapping("/getAadhaarDetails/{subDocumentType}")
    public ResponseEntity<?> getAadhaarDetails(HttpServletRequest request, @PathVariable("subDocumentType") SubDocumentType subDocumentType, @RequestBody DocumentVerifyDto documentVerifyDto) throws Exception {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            SignzyFetchAndVerification verification = documentService.getSignzyDetails(documentVerifyDto.getRequestId());
            if (verification != null) {
                Pair<Integer, Object> result = documentService.getDataFromSignzy(subDocumentType, documentVerifyDto, verification);
                if (result.getFirst() == 1) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "verify not successful", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "requestId not found"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Generate pdf for Agreement Signing
     */
    @GetMapping("/generate-agreement-pdf/{leadId}")
    public ResponseEntity<?> generateAgreementSigningPdf(@PathVariable("leadId") Long leadId) throws IOException {
        LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
        logger.info("leadDetails -- : {}", leadInformation);
//        File file = new File(AGREEMENT_DOCUMENT_PATH);
//        if (!file.exists()) {
//            logger.info("create file path");
//            file.mkdir();
//        }
        if (leadInformation != null) {
            PdfEsignData pdfEsignData = new PdfEsignData();
            List<UploadedDocuments> uploadedDocumentsList = documentService.findByLeadId(leadInformation);
            if (!uploadedDocumentsList.isEmpty()) {
                List<SignzyFetchAndVerification> panDetails = documentService.getPanDetails(uploadedDocumentsList);
                if (!panDetails.isEmpty()) {
//                    pdfEsignData.setPan(panDetails.get(0).getPanNumber());
                    pdfEsignData.setPan(attributeEncryptor.decrypt(panDetails.get(0).getPanNumber()));
//                    logger.info("pan NUmber ---> : {}", panDetails.get(0).getPanNumber());
                    logger.info("pan Number -- : {}", attributeEncryptor.decrypt(panDetails.get(0).getPanNumber()));
                }
                List<SignzyFetchAndVerification> gstDetails = documentService.getGSTDetails(uploadedDocumentsList);
                if (!gstDetails.isEmpty()) {
//                    JSONObject jsonObject = new JSONObject(gstDetails.get(0).getGstInDetails());
                    JSONObject jsonObject = new JSONObject(attributeEncryptor.decrypt(gstDetails.get(0).getGstInDetails()));
                    JSONObject object1 = jsonObject.getJSONObject("gstnDetailed");
//                    pdfEsignData.setGstin(gstDetails.get(0).getGstinNumber());
                    pdfEsignData.setGstin(attributeEncryptor.decrypt(gstDetails.get(0).getGstinNumber()));
                    pdfEsignData.setNatureOfBusiness(object1.getString("tradeNameOfBusiness"));
                }
                if (leadInformation.getLeadCategory() == 0 || leadInformation.getLeadCategory() == 2) {
                    List<Integer> mccIndicator = new ArrayList<>();
                    mccIndicator.add(MCC_CATEGORY_INDICATOR_OSV);
                    List<SubDocumentType> subDocumentTypes = subDocumentTypeRepository.findDocumentListByMccIndicator(mccIndicator);
                    List<UploadedDocuments> upDocument = new ArrayList<>();
                    subDocumentTypes.forEach(document -> {
                        logger.info("document data --- : {}--- {}", document.getSubDocumentTypeId(), document.getName());
                        //To select last uploaded document with Document Type
                        if (document.getName().equals(SUB_DOCUMENT_TYPE_SO_SIGN)) {
                            List<UploadedDocuments> uploadedDocuments = uploadedDocumentRepository.findDocumentByLeadAndDocumentType(leadInformation, document);
                            if (uploadedDocuments.size() > 0) {
                                upDocument.add(uploadedDocuments.get(0));
                                logger.info("Upload documents for so signature size --{}----{}", upDocument.size(), upDocument.get(0).getFileName());
                            }
                        }
                    });
                    pdfEsignData.setUploadedDocuments(upDocument);
                    pdfEsignData.setAllUploadedDocuments(uploadedDocumentsList);
                    List<SubDocumentType> bankProofList = subDocumentTypeRepository.findDocumentListByMccIndicator(Collections.singletonList(MCC_CATEGORY_INDICATOR_BANK_APPROVAL));
                    List<UploadedDocuments> uploadedDocuments = uploadedDocumentRepository.findDocumentByLeadAndDocumentTypeList(leadInformation, bankProofList);
                    if (uploadedDocuments.size() > 0) {
                        if (uploadedDocuments.get(0).getLeadDocuments().getSubDocumentType().getName().equalsIgnoreCase("Bank Statement")) {
                            pdfEsignData.setBankProof(1);
                        } else if (uploadedDocuments.get(0).getLeadDocuments().getSubDocumentType().getName().equalsIgnoreCase("Cancelled Cheque Leaf")) {
                            pdfEsignData.setBankProof(2);
                        } else {
                            pdfEsignData.setBankProof(0);
                        }
                    }
                } else {
                    List<UploadedDocuments> documents = uploadedDocumentsList.stream().filter(l -> l.getLeadDocuments().getDocumentVerifiedStatus() == Constants.OPS_VERIFICATION_SUCCESS).collect(Collectors.toList());
                    if (!documents.isEmpty()) {
                        pdfEsignData.setUploadedDocuments(documents);
                        logger.info("document list --------- : {}", documents);
                    }
                }
            }
            List<SMSPaymentDetails> smsPaymentDetails = smsPaymentDetailsRepository.findByLeadId(leadInformation);
            if (smsPaymentDetails.size() > 0) {
                if (smsPaymentDetails.get(0).getCallBackResponse() != null) {
                    JSONObject jsonObject = new JSONObject(smsPaymentDetails.get(0).getCallBackResponse());
                    JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("payments"));
                    JSONObject object1 = (JSONObject) jsonArray.get(0);
                    pdfEsignData.setAmount(object1.getDouble("amount"));
                    logger.info("Razzor Pay--{}", pdfEsignData.getAmount());
                    logger.info("Razzor Pay 1234--{}", object1.getDouble("amount"));
                    pdfEsignData.setPaymentDate(DateUtil.formattedTime(DateUtil.longToDate(object1.getLong("created_at")), "dd-MM-yyyy HH:mm:ss"));
                }
                pdfEsignData.setReferenceNumber(smsPaymentDetails.get(0).getSmsReferenceId());
            }
            logger.info("payment Details--{}", pdfEsignData.toString());
            pdfEsignData.setVasList(vasRepository.getAll(true));
            if (leadInformation.getLeadSource().getLeadSourceName().equals("LS_Wowpe")) {
                pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAllActiveList());
            } else {
                pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAllActive());
            }
            InternalSubvention internalSubvention = !internalSubventionRepository.getDetailByLeadIds(leadInformation.getLeadId()).isEmpty() ? internalSubventionRepository.getDetailByLeadIds(leadInformation.getLeadId()).get(0) : null;
            if (internalSubvention != null) {
                pdfEsignData.setInternalSubvention(internalSubvention);
            }
            pdfEsignData.setFileConfig(fileConfig);
//            pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAllActive());
            PdfReportDo report = new PdfReportDo();
//            ByteArrayOutputStream result = report.createAgreementPDFPOSANDQRANDSOUNDBOX(leadInformation, pdfEsignData);
//            PdfReport report = new PdfReport();
            ByteArrayOutputStream result = report.createAgreementPDF(leadInformation, pdfEsignData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "Agreement.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(result.toByteArray());
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
    }

    /**
     * Lead Document approve or reject for Ops Portal
     * 1 - Approve
     * 2-  Reject
     */

    @PutMapping("/documentUpdate/{leadId}/{documentType}/{action}")
    public ResponseEntity<?> documentUpdate(HttpServletRequest request, @PathVariable("documentType") DocumentType documentType, @PathVariable("action") int action,
                                            @RequestBody DocumentUpdateDto documentUpdateDto, @PathVariable("leadId") long leadId) throws Exception {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            logger.info("document approval or reject -- : {}", action);
            LeadInformation leadInformation = documentService.checkLead(leadId);
            if (leadInformation != null) {
                List<LeadDocuments> documents = documentService.documentRejectOrApproval(leadId, documentType, action, documentUpdateDto, curuser);
                if (documents != null) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", documents), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "status not updated"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "lead not found"), HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Document re upload or confirm function in SO App
     * 1- Confirm
     * 2 - ReUpload
     */

    @PutMapping("/reUploadOfDocument/{leadId}/{action}")
    public ResponseEntity<?> reUploadOfDocument(HttpServletRequest request, @PathVariable("leadId") long leadId, @PathVariable("action") int action, @RequestBody DocumentUpdateDto documentUpdateDto) throws Exception {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            logger.info("re upload or confirm -- : ");
            LeadInformation leadInformation = documentService.checkLead(leadId);
            if (leadInformation != null) {
                List<LeadDocuments> documents = documentService.reUploadOfDocument(leadId, action, documentUpdateDto);
                if (documents != null) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", documents), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "status not updated"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "lead not found"), HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/uploadDocumentFromOnboardLead")
    public ResponseEntity<?> uploadDocumentsFromOnboardLead(@RequestBody DocumentDto documentDto) {
        logger.info("documentDto --- : {} ", documentDto.getDocumentDtoList().get(0).getFileOriginalName());
        List<UploadedDocuments> result = documentService.storeOnboardingLeads(documentDto);
        if (result != null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Aggregator Lead Document approve or reject for OPS Portal
     * 1 - Approve
     * 2 - Reject m
     */
    @PutMapping("/agg-document-update/{leadId}/{documentType}/{action}")
    public ResponseEntity<?> aggDocumentUpdate(HttpServletRequest request, @PathVariable("leadId") long leadId, @PathVariable("documentType") Document documentType,
                                               @PathVariable("action") int action, @RequestBody DocumentUpdateDto documentUpdateDto) throws MessagingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            logger.info("document approval or reject ----------: {}", action);
            LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
            if (leadInformation != null) {
                List<LeadDocuments> documents = documentService.aggDocumentApprovalOrReject(leadId, documentType, action, documentUpdateDto);
                if (documents != null) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", documents), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Document not updated"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead not Found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/agg-subDocument-update/{leadId}/{SubDocType}/{action}")
    public ResponseEntity<?> aggDocumentUpdateWopwPe(HttpServletRequest request, @PathVariable("leadId") long leadId, @PathVariable("SubDocType") long SubDocType,
                                                     @PathVariable("action") int action, @RequestBody DocumentUpdateDto documentUpdateDto) throws MessagingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            logger.info("document approval or reject ----------: {}", action);
            LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
            if (leadInformation != null) {
                List<LeadDocuments> documents = documentService.aggDocumentApprovalOrRejectWowPe(leadId, SubDocType, action, documentUpdateDto);
                if (documents != null) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", documents), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Document not updated"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead not Found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("removeLead/{leadNumber}")
    public ResponseEntity<?> removeAggLead(@PathVariable("leadNumber") String leadNumber) {
        LeadInformation leadInformation = documentService.findAggLeadNumber(leadNumber);
        if (leadInformation != null) {
            documentService.aggLeadDelete(leadInformation);
            return new ResponseEntity<>(new ApiResponse(OK, "lead removed"), NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse(NOT_FOUND, "lead Not Found"), NOT_FOUND);
    }

    /**
     * Aggregator Document ReUpload from Onboard Portal
     */

    @PostMapping("agg-reupload-document/{leadNumber}")
    public ResponseEntity<?> aggReuploadDocument(@PathVariable("leadNumber") String leadNumber, @RequestBody LeadDocumentDto leadDocumentDto) {
        LeadInformation information = documentService.findAggLeadNumber(leadNumber);
        logger.info("information -------------: {}", information);
        if (information != null) {
            UploadedDocuments uploadedDocuments = documentService.updateAggregatorReuploadedDoc(leadNumber, leadDocumentDto);
            if (uploadedDocuments != null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Technical Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(NOT_FOUND, "lead not Found"), NOT_FOUND);
    }

    /**
     * Aggregator Agreement Generate
     */
    @PostMapping("agg-agreement-view/{leadId}")
    public ResponseEntity<?> aggAgreementView(@PathVariable("leadId") Long leadId, HttpServletRequest request) throws JsonProcessingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            logger.info("leadDetails -- : {}", leadInformation);
            if (leadInformation != null) {
                boolean isAllDocumentsVerified = documentService.checkAllAggregatorDocumentsVerifiedByOps(leadInformation);
//                boolean isAllDocumentsVerified = documentService.checkUploadedDocuments(leadInformation);
                if (isAllDocumentsVerified) {
                    PdfEsignData pdfEsignData = new PdfEsignData();
                    List<UploadedDocuments> uploadedDocumentsList = documentService.findByLeadId(leadInformation);
                    if (!uploadedDocumentsList.isEmpty()) {
                        List<SignzyFetchAndVerification> panDetails = documentService.getPanDetails(uploadedDocumentsList);
                        if (!panDetails.isEmpty()) {
//                    pdfEsignData.setPan(panDetails.get(0).getPanNumber());
                            pdfEsignData.setPan(attributeEncryptor.decrypt(panDetails.get(0).getPanNumber()));
//                    logger.info("pan NUmber ---> : {}", panDetails.get(0).getPanNumber());
                            logger.info("pan Number -- : {}", attributeEncryptor.decrypt(panDetails.get(0).getPanNumber()));
                        } else {
                            pdfEsignData.setPan(leadInformation.getPanNumber());
                        }
                        List<SignzyFetchAndVerification> gstDetails = documentService.getGSTDetails(uploadedDocumentsList);
                        if (!gstDetails.isEmpty()) {
//                    JSONObject jsonObject = new JSONObject(gstDetails.get(0).getGstInDetails());
                            JSONObject jsonObject = new JSONObject(attributeEncryptor.decrypt(gstDetails.get(0).getGstInDetails()));
                            JSONObject object1 = jsonObject.getJSONObject("gstnDetailed");
//                    pdfEsignData.setGstin(gstDetails.get(0).getGstinNumber());
                            pdfEsignData.setGstin(attributeEncryptor.decrypt(gstDetails.get(0).getGstinNumber()));
                            pdfEsignData.setNatureOfBusiness(object1.getString("tradeNameOfBusiness"));
                        } else {
                            pdfEsignData.setGstin(leadInformation.getGstNumber());
                            pdfEsignData.setNatureOfBusiness(leadInformation.getNatureOfBusiness());
                        }
//                        List<String> documentList = uploadedDocumentsList.stream().filter(l-> l.getLeadDocuments().getDocumentVerifiedStatus()== Constants.OPS_VERIFICATION_SUCCESS).map(UploadedDocuments :: getFileName).toList();
//                        if (!documentList.isEmpty()){
//                            pdfEsignData.setDocumentList(documentList);
//                            logger.info("document list ---------- : {}",documentList);
//                        }
//                        List<LeadDocuments> uploadedDocuments = documentService.findLeadDocument(leadInformation);
                        List<UploadedDocuments> documents = uploadedDocumentsList.stream().filter(l -> l.getLeadDocuments().getDocumentVerifiedStatus() == Constants.OPS_VERIFICATION_SUCCESS).collect(Collectors.toList());
                        if (!documents.isEmpty()) {
                            pdfEsignData.setUploadedDocuments(documents);
                            logger.info("document list --------- : {}", documents);
                        }
                    }
                    pdfEsignData.setVasList(vasRepository.getAll(true));
                    pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAll());
                    PdfReport report = new PdfReport();
                    AggPdfReport report1 = new AggPdfReport();
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    logger.info("leadsource  ---{}", leadInformation.getLeadSource().getLeadSourceName());
                    if (leadInformation.getLeadSource().getLeadSourceName().equals("LS_FidyPay")) {
                        logger.info("test fidypay ---");
                        result = report1.createAgreementPDF(leadInformation, pdfEsignData);
                    } else {
                        result = report.createAgreementPDF(leadInformation, pdfEsignData);
                    }

                    int pageCount = getPageCountFromByteArrayOutputStream(result);
                    logger.info("page count ----- : {}", pageCount);

                    String fileName = "AgreementForm.pdf";
                    String contentType = MediaType.APPLICATION_PDF_VALUE;
                    MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(result, fileName, contentType);


                    boolean qrFlag = false;
                    AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);

                    ResponseEntity<String> out = null;
                    String url = null;
                    HttpHeaders httpHeaders = new HttpHeaders();

                    ObjectMapper objectMapper = new ObjectMapper();
                    LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                    Resource resource = multipartFile.getResource();
                    map.add("file", resource);
                    map.add("ttl", "infinity");
                    httpHeaders.set("Content-Type", "multipart/form-data");
                    httpHeaders.set("Content-Disposition", "form-data");
                    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(map, httpHeaders);

                    out = restTemplate.exchange(signzyFileUploadUrl, HttpMethod.POST, entity, String.class);
                    logger.info("pdfUrl Response out -- : {}", out.getBody());
                    EsignPdfResponseDto pdfResponseDto = objectMapper.readValue(out.getBody(), EsignPdfResponseDto.class);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("filename", "Agreement.pdf");
//                    return ResponseEntity.ok().headers(headers).body(result.toByteArray());
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", pdfResponseDto), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(CONFLICT, "Documents not verified !"), CONFLICT);
            }
            return new ResponseEntity<>(new ApiResponse(NOT_FOUND, "Lead Not Found"), NOT_FOUND);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
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

    private static int getNumberOfPages(PdfReader reader) {
        int pages = 0;
        PdfObject obj;
        for (int i = 1; i <= reader.getXrefSize(); i++) {
            obj = reader.getPdfObject(i);
            if (obj != null && obj.isStream()) {
                PRStream stream = (PRStream) obj;
                byte[] b;
                try {
                    b = PdfReader.getStreamBytes(stream);
                    PdfReader pdfReader = new PdfReader(b);
                    pages += pdfReader.getNumberOfPages();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("exception occurred --------- : {}", e.getMessage());
                }
            }
        }
        if (pages == 0) {
            pages = reader.getNumberOfPages();
        }
        return pages;
    }


    /**
     * Aggregator Lead Approve
     */
    @PostMapping("agg-lead-approve/{leadId}")
    public ResponseEntity<?> aggLeadApprove(@PathVariable("leadId") Long leadId, HttpServletRequest request) {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            logger.info("leadDetails -- : {}", leadInformation);
            if (leadInformation != null) {
                boolean isAllDocumentsVerified = documentService.checkAllAggregatorDocumentsVerifiedByOps(leadInformation);
//                boolean isAllDocumentsVerified = documentService.checkUploadedDocuments(leadInformation);
                if (isAllDocumentsVerified) {
                    Pair<Integer, Object> lead = documentService.aggregatorLeadApproval(leadId);
                    if (lead.getFirst() == 1) {
                        return new ResponseEntity<>(new ApiResponse(OK, "success", lead.getSecond()), OK);
                    }
                    return new ResponseEntity<>(new ApiResponse(CONFLICT, "Lead not approved"), CONFLICT);
                }
                return new ResponseEntity<>(new ApiResponse(CONFLICT, "Documents not verified"), CONFLICT);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead Not Found"), NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("upload-agreement/{leadId}")
    public ResponseEntity<?> uploadAgreement(@PathVariable("leadId") Long leadId, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws JsonProcessingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            if (file.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "file is empty"), HttpStatus.OK);
            }
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            if (leadInformation != null) {
                AgreementDetails details = documentService.storeUploadedPdfFile(file, leadInformation);
                if (details != null) {
                    return new ResponseEntity<>(new ApiResponse(OK, "Success", details), OK);
                }
                return new ResponseEntity<>(new ApiResponse(CONFLICT, "File not Stored"), CONFLICT);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "lead not found"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/agreement-manual-upload/{leadNumber}")
    public ResponseEntity<?> manualUpload(@PathVariable("leadNumber") String leadNumber, @RequestBody AgreementDetailsDto detailsDto) {
        LeadInformation leadInformation = documentService.findAggLeadNumber(leadNumber);
        if (leadInformation != null) {
            AgreementDetails agreementDetails = documentService.findAgreementDetailsByLeadId(leadInformation);
            if (agreementDetails != null) {
                AgreementDetails details = documentService.manualUpload(leadInformation, detailsDto);
                if (details != null) {
                    return new ResponseEntity<>(new ApiResponse(OK, "success", agreementDetails), OK);
                }
                return new ResponseEntity<>(new ApiResponse(CONFLICT, "agreement details not updated"), CONFLICT);
            }
            return new ResponseEntity<>(new ApiResponse(CONFLICT, "Agreement details not found"), CONFLICT);
        }
        return new ResponseEntity<>(new ApiResponse(CONFLICT, "Lead not found"), CONFLICT);
    }

    @PutMapping("reject-manual-agreement/{leadId}")
    public ResponseEntity<?> rejectManualAgreement(@PathVariable("leadId") long leadId, @RequestBody DocumentUpdateDto documentUpdateDto) {
        LeadInformation leadInformation = documentService.checkLead(leadId);
        if (leadInformation != null) {
            AgreementDetails agreementDetails = documentService.findAgreementDetailsByLeadId(leadInformation);
            if (agreementDetails != null) {
                AgreementDetails details = documentService.rejectManualAgreement(leadInformation, documentUpdateDto);
                if (details != null) {
                    return new ResponseEntity<>(new ApiResponse(OK, "success", details), OK);
                }
                return new ResponseEntity<>(new ApiResponse(CONFLICT, "agreement not rejected"), CONFLICT);
            }
            return new ResponseEntity<>(new ApiResponse(CONFLICT, "agreement details not found"), CONFLICT);
        }
        return new ResponseEntity<>(new ApiResponse(CONFLICT, "lead not found"), CONFLICT);
    }

    /**
     * Resubmit lead from Onboard Portal
     */

    @PutMapping("agg-lead-resubmit/{leadNumber}")
    public ResponseEntity<?> aggLeadReSubmitData(@PathVariable("leadNumber") String leadNumber, @RequestBody LeadDto leadDto) {
        logger.info("leadDto --- : {} ", leadDto.getLeadNumber());
        logger.info("leadDto--{}", leadDto);
        LeadInformation leadInformation = documentService.findAggLeadNumber(leadNumber);
        if (leadInformation != null) {
            LeadInformation result = documentService.updateResubmittedLead(leadNumber, leadDto);
            if (result != null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(BAD_REQUEST, "Lead not Found"), HttpStatus.BAD_REQUEST);
    }

    /**
     * Agreement Manual Approve
     *
     * @param leadId
     * @return
     */
    @PutMapping("approve-agreement-manually/{leadId}")
    public ResponseEntity<?> approveAgreementManually(@PathVariable("leadId") long leadId) {
        LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
        if (leadInformation != null && leadInformation.isManualAgreement()) {
            LeadInformation result = documentService.approveAgreementManually(leadId);
            if (result != null) {
                return new ResponseEntity<>(new ApiResponse(OK, "Agreement approved successfully", result), OK);
            }
            return new ResponseEntity<>(new ApiResponse(CONFLICT, "error"), CONFLICT);
        }
        return new ResponseEntity<>(new ApiResponse(CONFLICT, "lead not found"), CONFLICT);
    }

    /**
     * VPA's transfer to OPs portal
     */
    @PostMapping("/vpaShareUrl")
    public ResponseEntity<?> vpaShareUrl(@RequestBody VpaShareDto vpaShareDto) throws Exception {
        logger.info("vpa store -- : ");
        List<QrVpa> qrVpas = documentService.storeVpaByLead(vpaShareDto);
        if (!qrVpas.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(OK, "success", qrVpas), OK);
        }
        return new ResponseEntity<>(new ApiResponse(INTERNAL_SERVER_ERROR, "data not stored"), INTERNAL_SERVER_ERROR);
    }

    /**
     * check agreement
     */
    @PostMapping("/sqrAgreement")
    public ResponseEntity<?> sqrAgreement(@RequestBody VpaShareDto vpaShareDto) throws Exception {
        LeadInformation leadInformation = documentService.findAggLeadNumber(vpaShareDto.getLeadNumber());
        SqrPdfReport report = new SqrPdfReport();
        AgreementDetails agreementDetails = documentService.getAggrement(leadInformation.getLeadId());
        ByteArrayOutputStream result = report.createSqrAgreementPDF(leadInformation, agreementDetails);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "Agreement.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .body(result.toByteArray());
    }

    @PutMapping("/mcc-doc-update/{leadId}/{type}/{action}")
    public ResponseEntity<?> mccApproveOrReject(HttpServletRequest request, @PathVariable("leadId") long leadId, @PathVariable("type") long subDocumentType,
                                                @PathVariable("action") int action, @RequestBody DocumentUpdateDto documentUpdateDto) throws MessagingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            logger.info("document approval or reject ----------: {}", action);
            LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
            if (leadInformation != null) {
                List<LeadDocuments> documents = documentService.aggDocumentApprovalOrRejectMcc(leadId, subDocumentType, action, documentUpdateDto, curuser);
                if (documents != null) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", documents), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Document not updated"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead not Found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/saveStatus/{leadId}")
    public ResponseEntity<?> saveStatus(HttpServletRequest request, @PathVariable("leadId") long leadId) {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
            logger.info("save {}", leadInformation.getLeadId());
            if (leadInformation != null) {
                LeadInformation leadInformation1 = documentService.saveStatus(leadInformation.getLeadId());
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", leadInformation1), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead not Found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/downloadNpciMcc")
    public ResponseEntity<?> npciMcc(HttpServletRequest request) {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            List<RiskMccNpci> riskMccNpci = documentService.getMccNpci();
            if (!riskMccNpci.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(OK, "success", riskMccNpci), OK);
            }
            return new ResponseEntity<>(new ApiResponse(CONFLICT, "Not Found"), CONFLICT);
        }
        return new ResponseEntity<>(new ApiResponse(UNAUTHORIZED, "UnAuthorized"), UNAUTHORIZED);
    }

    @PostMapping("/riskMccUpload/{mcc}")
    public ResponseEntity<?> riskMccUpload(@PathVariable String mcc, HttpServletRequest request) {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            logger.info("--mcc--{}", mcc);
            MerchantCategoryCode merchantCategoryCode = documentService.findMccCode1(mcc);
            if (merchantCategoryCode == null) {

                return new ResponseEntity<>(new ApiResponse(CONFLICT, "invalid Mcc", null), CONFLICT);
            }
            RiskMccNpci riskMccNpci = new RiskMccNpci();
            riskMccNpci.setMcc(merchantCategoryCode);
            RiskMccNpci riskMcc1 = documentService.saveLead(riskMccNpci);
            if (riskMcc1.getMcc() != null) {
                return new ResponseEntity<>(new ApiResponse(OK, "success", riskMcc1), OK);
            }
            return new ResponseEntity<>(new ApiResponse(CONFLICT, "not found", null), CONFLICT);

        }
        return new ResponseEntity<>(new ApiResponse(UNAUTHORIZED, "UnAuthorized!!!", null), UNAUTHORIZED);
    }

    @PutMapping("/agg-approveDoc-reject/{leadId}/{LeadDoc}")
    public ResponseEntity<?> aggDocumentReject(HttpServletRequest request, @PathVariable("leadId") long leadId, @PathVariable("LeadDoc") int documentType,
                                               @RequestBody DocumentUpdateDto documentUpdateDto) throws MessagingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadDocuments leadDocuments = leadDocumentRepository.fetchLeadDocument(documentType);
            if (leadDocuments != null) {
                LeadDocuments documents = documentService.aggApprovalDocumentReject(leadId, leadDocuments, documentUpdateDto);
                if (documents != null) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", documents), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Document not updated"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead not Found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("get-Authorizer/{leadId}")
    public ResponseEntity<?> getAuthorizer(@PathVariable("leadId") long leadId) {
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        if (leadInformation != null) {
            List<DirectorDTO> directorDTOList = documentService.getAuthorizerDocuments(leadInformation);
            return new ResponseEntity<>(new ApiResponse(OK, "success", directorDTOList), OK);
        }
        return new ResponseEntity<>(new ApiResponse(NOT_FOUND, "LeadId not found"), NOT_FOUND);
    }

    @GetMapping("get-Gst/{leadId}")
    public ResponseEntity<?> getGst(@PathVariable("leadId") long leadId) {
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        if (leadInformation != null) {
            GstDocumentDTO gstDocumentDTO = documentService.getGst(leadInformation);
            return new ResponseEntity<>(new ApiResponse(OK, "Success", gstDocumentDTO), OK);
        }
        return new ResponseEntity<>(new ApiResponse(NOT_FOUND, "LeadId not found"), NOT_FOUND);
    }

    @GetMapping("get-additional")
    public ResponseEntity<?> getAdditionalDocument() {
        DocumentCategory documentCategory = documentService.getDocumentCategory();
        List<SubDocumentType> subDocumentTypeList = documentService.getCategoryBasedDocument(documentCategory);
        return new ResponseEntity<>(new ApiResponse(OK, "Success", subDocumentTypeList), OK);
    }

    @GetMapping("check-additional-document/{leadId}")
    public ResponseEntity<?> checkDocument(@PathVariable("leadId") long leadId) {
        DocumentCategory documentCategory = documentService.getDocumentCategory();
        boolean isVerified = documentService.verifyAdditionalDocuments(documentCategory, leadId);
        if (isVerified) {
            return new ResponseEntity<>(new ApiResponse(OK, "Success"), OK);
        }
        return new ResponseEntity<>(new ApiResponse(CONFLICT, "Documents not verified"), CONFLICT);
    }

    @GetMapping("get-additional-by-ops")
    public ResponseEntity<?> getAdditionalDocumentByOPS() {
        DocumentCategory documentCategory = documentService.getDocumentCategoryForOPS();
        List<SubDocumentType> subDocumentTypeList = documentService.getCategoryBasedDocument(documentCategory);
        if (subDocumentTypeList.size() > 0) {
            return new ResponseEntity<>(new ApiResponse(OK, "Success", subDocumentTypeList), OK);
        }
        return new ResponseEntity<>(new ApiResponse(NOT_FOUND, "Document not found"), NOT_FOUND);
    }

    @GetMapping("get-offline-payment-doc")
    public ResponseEntity<?> getOfflinePaymentDocument() {
        DocumentCategory documentCategory = documentService.getDocumentCategoryForOfflinePayment();
        List<SubDocumentType> subDocumentTypeList = documentService.getCategoryBasedDocument(documentCategory);
        if (subDocumentTypeList.size() > 0) {
            return new ResponseEntity<>(new ApiResponse(OK, "Success", subDocumentTypeList), OK);
        }
        return new ResponseEntity<>(new ApiResponse(NOT_FOUND, "Document not found"), NOT_FOUND);
    }

    @GetMapping("get-digital-implementation-form/{leadId}")
    public ResponseEntity<?> getDigitalImplementationForm(@PathVariable("leadId") long leadId) {
        try {

            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            List<MarsDeviceDetails> marsDeviceDetails = documentService.getMarsDeviceDetailsList(leadId);
            String tids="";
            String mids="";
            String serialNos="";
            String sim = "";
            String deviceType="";
            if(leadInformation!=null){
                Device posDevice=leadInformation.getDevice();
                Device strickerDevice=leadInformation.getStickerDevice();
                Device sqrDevice=leadInformation.getSqrDevice();
                if(posDevice!=null){
                   deviceType=deviceType.concat("," + posDevice.getDeviceName());
                }
                if(strickerDevice!=null){
                    deviceType=deviceType.concat("," + strickerDevice.getDeviceName());
                }
                if(sqrDevice!=null){
                    deviceType=deviceType.concat("," + sqrDevice.getDeviceName());
                }
            }
            deviceType=deviceType.replaceFirst(",","");
            if(marsDeviceDetails.size()>0) {
                for (MarsDeviceDetails m : marsDeviceDetails) {
                    String tid = m.getTid();
                    tids = tids.concat("," + tid);
                    String mid = m.getMid();
                    mids = mids.concat("," + mid);
                    if (m.getTerminalSNo() != null) {
                        serialNos = serialNos.concat("," + m.getTerminalSNo());
                    } else {
                        CounterManagement counterManagement = counterManagementRepository.findByVpa(m.getSqrVpa(), m.getLeadInformation().getLeadId());
                        if (counterManagement != null) {
                            serialNos = serialNos.concat("," + counterManagement.getSoundBoxSno());
                        }
                    }
                    String sim1 = m.getSim();
                    CounterManagement counterManagement = counterManagementRepository.findByVpa(m.getSqrVpa(), m.getLeadInformation().getLeadId());
                    CounterManagement counterManagement1 = counterManagementRepository.findByTid(m.getTid(), m.getLeadInformation().getLeadId());
                    if (sim1 != null) {
                        sim = sim.concat("," + sim1);
                    } else if(counterManagement!=null){
                        sim=sim.concat(","+counterManagement.getSim()!=null?counterManagement.getSim():"");
                    }else if(counterManagement1!=null){
                        sim=sim.concat(","+counterManagement1.getSim()!=null?counterManagement1.getSim():"");
                    }else{
                        logger.info("Empty sim");
                    }
                }
            }
            tids=tids.replaceFirst(",","");
            mids=mids.replaceFirst(",","");
            serialNos=serialNos.replaceFirst(",","");
            sim=sim.replaceFirst(",","");

            ImplementationDocument implementationDocument = implementationDocumentReposistory.getAllByLeads(leadId);
            if (implementationDocument == null) {
                if (leadInformation != null) {
                    ImplementationPdfReport implementationPdfReport = new ImplementationPdfReport();
                    ByteArrayOutputStream byteArrayOutputStream = null;
                    byteArrayOutputStream = implementationPdfReport.createImplementationFormPDF(leadInformation, marsDeviceDetails,tids,mids,serialNos,sim,deviceType);
                    int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
                    logger.info("page count ----- : {}", pageCount);

                    logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
                    String fileName = "ImplementationForm.pdf";
                    String contentType = MediaType.APPLICATION_PDF_VALUE;

                    logger.info("contentType --: {}", contentType);
                    MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
                    String newFileName = leadId + "_" + "ImplementationForm";
                    Map<String, String> response = fileStorageService.storeFile(multipartFile, newFileName);
                    SubDocumentType subDocumentType = subDocumentTypeRepository.findSubDocNameAndMccIndicator("Implementation Form", MCC_CATEGORY_INDICATOR_IMPLEMENT);
                    FileDto fileDto = new FileDto();
                    fileDto.setFileName(response.get("fileName"));
                    fileDto.setFileNameOriginal(response.get("mimeType"));
                    fileDto.setMimeType(response.get("fileNameOriginal"));
                    String typeOfDocument = "";
                    UploadedDocuments uploadedDocuments=null;
                    ImplementationDocument implementationDocument1 = new ImplementationDocument();
                    if (response != null) {
                         uploadedDocuments = documentService.storeDigitalLeadDocuments(leadInformation, fileDto, subDocumentType, typeOfDocument);
                    }
                    if(uploadedDocuments!=null){
                        implementationDocument1.setLeadId(leadId);
                        implementationDocument1.setFormFlag(0);
                        implementationDocument1.setImplementationFormPicture(response.get("fileName"));
                        implementationDocumentReposistory.save(implementationDocument1);
                    }
                    String messageTemplate = "Complete your Bijlipay POS installation form:"+implementationSmsUrl+leadId+". Fill in the details to complete the process. Team - -Bijlipay";
                    int result = documentService.sms(leadInformation.getContactNumber(), messageTemplate);
                    if (result == 1) {
                        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Send SMS to merchant Contact number", implementationDocument1), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Something went wrong in the SMS send..."), HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead Not Found"), HttpStatus.NOT_FOUND);
            } else {
                String messageTemplate = "Complete your Bijlipay POS installation form:"+implementationSmsUrl+leadId+". Fill in the details to complete the process. Team - -Bijlipay";
                int result = documentService.sms(leadInformation.getContactNumber(), messageTemplate);
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "ReSend SMS to merchant Contact number", implementationDocument), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.info("Digital Implementation Error---{}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("digital-implementation-verify/{leadId}")
    public ResponseEntity<?> digitalFormVerify(@PathVariable("leadId") long leadId) {
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        if (leadInformation != null) {
            ImplementationDocument implementationDocument = implementationDocumentReposistory.getAllByLeads(leadId);
            if (implementationDocument != null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", implementationDocument), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "please generate the implementation form"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead Not Found"), HttpStatus.NOT_FOUND);
    }

    @PostMapping("update-implementation-form/{leadId}")
    public ResponseEntity<?> updateImplementationForm(@PathVariable("leadId") long leadId, @RequestBody ImplementationSignDto implementationSignDto) throws IOException {
        ImplementationDocument implementationDocument = implementationDocumentReposistory.getAllByLeads(leadId);
        if (implementationDocument != null) {
            implementationDocument.setMerchantName(implementationSignDto.getMerchantName());
            implementationDocument.setDeviceName(implementationSignDto.getDeviceName());
            implementationDocument.setIpAddress(implementationSignDto.getIpAddress());
            implementationDocument.setDateTime(implementationSignDto.getDateTime());
            implementationDocument.setLatLong(implementationSignDto.getLatLong());
            implementationDocument=implementationDocumentReposistory.save(implementationDocument);
        }else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "please first generate the implementation form"), HttpStatus.NOT_FOUND);
        }
        ImplementationPdfReport implementationPdfReport=new ImplementationPdfReport();
        String filePath = uploadedDir + "/" + implementationDocument.getImplementationFormPicture();
        try{
            implementationPdfReport.updateStoredPdfWithExtraData(filePath,implementationSignDto);
        }catch (Exception e){
            logger.info("pdf read exception---{}",e.getMessage());
            e.printStackTrace();
        }
        implementationDocument.setFormFlag(1);
        implementationDocumentReposistory.save(implementationDocument);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Success"), HttpStatus.OK);
    }



}