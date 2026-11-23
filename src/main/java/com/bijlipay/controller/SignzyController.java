package com.bijlipay.controller;

import com.bijlipay.config.Constants;
import com.bijlipay.model.*;
import com.bijlipay.model.dto.*;
import com.bijlipay.model.dto.signzy.extraction.SignzyExtractionRequestDto;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchRequestDto;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchResponseDto;
import com.bijlipay.model.dto.signzy.identity.SignzyIdentityRequestDto;
import com.bijlipay.model.dto.signzy.identity.SignzyIdentityResponseDto;
import com.bijlipay.model.dto.signzy.pennydrop.AccountTransferRequestDto;
import com.bijlipay.model.dto.signzy.verification.SignzyVerificationRequestDto;
import com.bijlipay.model.dto.signzy.verification.SignzyVerificationResponseDto;
import com.bijlipay.property.FileConfig;
import com.bijlipay.repository.*;
import com.bijlipay.service.*;
import com.bijlipay.util.*;

import com.bijlipay.util.AttributeEncryptor;
import com.bijlipay.util.MailHandler;
import com.bijlipay.util.PdfReportDo;
//import com.ctc.wstx.shaded.msv_core.util.Uri;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfReader;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.bijlipay.config.Constants.*;

@RestController
@RequestMapping("/document/signzy")
public class SignzyController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SignzyController.class);
    @Autowired
    private SignzyService signzyService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private LeadInformationRepository leadInformationRepository;

    @Autowired
    private SignzyFetchAndVerificationRepository signzyFetchAndVerificationRepository;
    @Autowired
    private AgreementDetailsRepository agreementDetailsRepository;
    @Autowired
    private ByteArrayMultiPartFileService byteArrayMultiPartFileService;
    @Autowired
    private MailHandler mailHandler;
    @Autowired
    private AttributeEncryptor attributeEncryptor;
    @Autowired
    private UserAuthenticationService userAuthenticateService;

    @Autowired
    private VasRepository vasRepository;

    @Autowired
    private MerchantTypeRepository merchantTypeRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private QrVpaRepository qrVpaRepository;
    @Autowired
    private MarsDeviceDetailsRepository marsDeviceDetailsRepository;

    @Autowired
    private SubDocumentTypeRepository subDocumentTypeRepository;

    @Autowired
    private UploadedDocumentRepository uploadedDocumentRepository;

    @Autowired
    private SignzyExtractionDataRepo signzyExtractionDataRepo;

    @Value("${file.url}")
    private String fileUrl;

    @Value("${agg.agreement.url}")
    private String aggAgreementUrl;

    @Value("${agg.event.callback..url}")
    private String aggEventCallbackUrl;

    @Value("${agg.contract.data.url}")
    private String aggContractDataUrl;

    @Value("${agg.contract.failed.url}")
    private String aggContractFailedUrl;

    @Value("${agg.generate.tid.url}")
    private String aggGenerateTidUrl;
    @Value("${agg.generates.tid.url}")
    private String aggGenerateVpaTidUrl;
    @Value("${agg.generates.Multi.url}")
    private String GenerateMultiTerminalPos;
    @Autowired
    private SMSPaymentDetailsRepository smsPaymentDetailsRepository;
    @Autowired
    private InternalSubventionRepository internalSubventionRepository;
    @Value("${sub.document.pan}")
    private int subDocumentPan;
    @Value("${sub.document.adhaar.front}")
    private int subDocumentAdhaarFront;
    @Value("${sub.document.adhaar.back}")
    private int subDocumentAdhaarBack;
    @Value("${sub.document.gst}")
    private int subDocumentGst;
    @Autowired
    private FileConfig fileConfig;

    @Value("${sub.document.drivingLicense.front}")
    private int subDocumentDrivingLicenseFront;
    @Value("${sub.document.drivingLicense.back}")
    private int subDocumentDrivingLicenseBack;
    @Value("${sub.document.passport.front}")
    private int subDocumentPassportFront;
    @Value("${sub.document.passport.back}")
    private int subDocumentPassportBack;
    @Value("${sub.document.voterID.front}")
    private int subDocumentVoterIDFront;
    @Value("${sub.document.voterID.back}")
    private int subDocumentVoterIDBack;

    @Autowired
    private OfflinePaymentRepo offlinePaymentRepo;

    @Autowired
    private RazorPayQRStatusRepo razorPayQRStatusRepo;



    /**
     * Signzy login API
     */

    @PostMapping("/login")
    public ResponseEntity<?> login() throws Exception {

        SignzyLoginResponseDto responseDto = signzyService.generateAccessToken();
        if (responseDto != null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", responseDto), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "please check the login credentials"), HttpStatus.BAD_REQUEST);
    }

    /**
     * Signzy logout API
     */

    @PostMapping("/logout/{id}")
    public ResponseEntity<?> logout(HttpServletRequest request, @PathVariable("id") String id) {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            try {
                ResponseEntity<Object> responseEntity = signzyService.deActivateAccessToken(id);
                if (responseEntity.getStatusCode().value() == 204) {//signzy logout API returns
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "logged out successfully"), HttpStatus.OK);
                }
            } catch (HttpClientErrorException e) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "unAuthorized"), HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Service Unavailable"), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Identity object creation for snoop request
     * 1- Success Response
     * 0 - Failure Response
     */

    @PostMapping("/identity")
    public ResponseEntity<?> signzyIdentification(HttpServletRequest request, @RequestBody SignzyIdentityRequestDto signzyIdentityRequestDto) throws ParseException, JsonProcessingException {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            Pair<Integer, SignzyIdentityResponseDto> result = signzyService.signzyIdentification(signzyIdentityRequestDto);
            if (result.getFirst() == 1) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond().getError()), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Extraction snoop request
     * 1 - Success Response
     * 0 - Failure Response
     */

    @PostMapping("/extraction/{leadDocId}")
    public ResponseEntity<?> signzyExtraction(HttpServletRequest request, @RequestBody SignzyExtractionRequestDto signzyExtractionRequestDto, @PathVariable("leadDocId") LeadDocuments leadDocuments) throws ParseException {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            List<UploadedDocuments> uploadedDocuments = documentService.findByLeadDocumentId(leadDocuments);
            Pair<Integer, Object> result = signzyService.signzyExtraction(signzyExtractionRequestDto, uploadedDocuments);
            if (result.getFirst() == 1) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Signzy Verification snoop request
     * 1 - Success Response
     * 0 - Failure Response
     */

    @PostMapping("/verification/{leadDocId}")
    public ResponseEntity<?> signzyVerification(HttpServletRequest request, @RequestBody SignzyVerificationRequestDto signzyVerificationRequestDto, @PathVariable("leadDocId") LeadDocuments leadDocuments) throws ParseException, JsonProcessingException {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {


            List<UploadedDocuments> uploadedDocuments = documentService.findByLeadDocumentId(leadDocuments);
            Pair<Integer, SignzyVerificationResponseDto> result = signzyService.signzyVerification(signzyVerificationRequestDto, uploadedDocuments);
            if (result.getFirst() == 1) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond().getError()), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Signzy Fetch API
     * 1 - Success Response
     * 0 - Failure Response
     */

    @PostMapping("/fetch/{leadDocId}")
    public ResponseEntity<?> signzyFetch(HttpServletRequest request, @RequestBody SignzyFetchRequestDto signzyFetchRequestDto, @PathVariable("leadDocId") LeadDocuments leadDocuments) throws Exception {

        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {

            List<UploadedDocuments> uploadedDocuments = documentService.findByLeadDocumentId(leadDocuments);
            Pair<Integer, SignzyFetchResponseDto> result = signzyService.signzyFetch(signzyFetchRequestDto, uploadedDocuments);
            if (result.getFirst() == 1) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond().getError()), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Signzy Esign API
     * 0 - normalEsign
     * 1 - adhaarEsign
     */
    @PostMapping("/esign/{leadId}/{action}")
    public ResponseEntity<?> EsignContract(HttpServletRequest request, @PathVariable("leadId") Long leadId, @PathVariable("action") int action, @RequestParam("name") String esignName) throws ParseException, JsonProcessingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            logger.info("leadDetails -- : {}", leadInformation);
            boolean isDocumentVerifiedBySignzy = documentService.checkDocumentVerifiedBySignzy(leadInformation);
            logger.info("isVerifiedBySignzy -- : {}", isDocumentVerifiedBySignzy);

            if (leadInformation.getLeadSubStatus() != null && leadInformation.getLeadSubStatus().getLeadSubStatusId() == Constants.LEAD_PENDING_WITH_NATIONAL_HEAD) {
                String title = "NH Approval";
                String reason = "NH Approval has been Pending";
                sendPushNotification(title, reason, leadInformation, PUSH_NOTIFICATION_TO_NH_APPROVAL_PENDING);
            }


            if (leadInformation != null && (leadInformation.getLeadSubStatus() == null || (leadInformation.getLeadSubStatus().getLeadSubStatusId() != Constants.LEAD_PENDING_WITH_NATIONAL_HEAD &&
                    leadInformation.getLeadSubStatus().getLeadSubStatusId() != Constants.LEAD_REJECTED_BY_NATIONAL_LEAD)) && isDocumentVerifiedBySignzy) {
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
                    List<UploadedDocuments> documents = uploadedDocumentsList.stream().filter(l -> l.getLeadDocuments().getDocumentVerifiedStatus() == Constants.OPS_VERIFICATION_SUCCESS).toList();
                    if (!documents.isEmpty()) {
                        pdfEsignData.setUploadedDocuments(documents);
                        logger.info("document list --------- : {}", documents);
                    }
                }
                pdfEsignData.setVasList(vasRepository.getAll(true));
                pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAll());

                PdfReportDo report = new PdfReportDo();
                ByteArrayOutputStream byteArrayOutputStream = report.createAgreementPDF(leadInformation, pdfEsignData);

                int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
                logger.info("page count ----- : {}", pageCount);

                logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
                String fileName = "AgreementForm.pdf";
                String contentType = MediaType.APPLICATION_PDF_VALUE;
                logger.info("contentType --: {}", contentType);
                MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
                boolean flag = false;
                AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, flag);

                Pair<Integer, Object> result = signzyService.signzyEsign(action, esignName, multipartFile, details, pageCount);
                if (result.getFirst() == 0) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
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

    @PostMapping("/resend-agreement-url/{leadId}")
    public ResponseEntity<?> resendAgreementUrl(HttpServletRequest request, @PathVariable("leadId") long leadId) throws MessagingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            if (leadInformation != null) {
                AgreementDetails agreementDetails = documentService.findAgreementDetailsByLeadId(leadInformation);
                if (agreementDetails != null && agreementDetails.getUrl() != null) {
                    try {
                        mailHandler.sendAgreementUrlToMerchant(agreementDetails.getLeadInformation().getContactEmail(), agreementDetails.getUrl());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        logger.info("MessageException -- :{}", e.getMessage());
                    }
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Mail sent successfully"), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "url not found"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "lead not found"), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Event CALLBACK FROM Signzy
     */

    @PostMapping("/eSign-event-callbackDetails")
    public void esignCallBackDetails(@RequestBody String callbackResponse, HttpServletResponse response) throws Exception {
        logger.info("callbck response ---:{}", callbackResponse);
        JSONObject jsonObject = new JSONObject(callbackResponse);
        logger.info("callback data---: {}", jsonObject);

        JSONObject esignData = jsonObject.getJSONObject("esignData");
        logger.info("esign data ----: {}", esignData);
        String message = jsonObject.getString("message");
        logger.info("message ---: {}", message);
        String token = jsonObject.getString("token");
        logger.info("token ---: {}", token);
        AgreementDetails agreementDetails = documentService.findByToken(token);
        if (agreementDetails != null) {
            boolean eSignResponse = esignData.getBoolean("success");
            if (eSignResponse) {
                agreementDetails.setResponse(true);
                String signedFileUrl = esignData.getString("esignedFile");
                // store file in server
                String signedFile = byteArrayMultiPartFileService.convertUrlToPdf(signedFileUrl, agreementDetails.getLeadInformation()); // convert URL to pdf and store pdf in server
                agreementDetails.setSignedFileName(signedFile); // pdf Name
                agreementDetails.setSignedFilePdfUrl(signedFileUrl); //pdf URL
                agreementDetails.setEventCallbackDetails(callbackResponse);
                if (agreementDetails.getLeadInformation() != null) {
                    long value = Constants.AGREEMENT_SIGNING_COMPLETED;
                    LeadInformation leadInformation = documentService.updateLeadStatus(agreementDetails.getLeadInformation().getLeadId(), value);
                    logger.info("leadInfo --: {}", leadInformation);
                }
                mailHandler.sendEmail(agreementDetails.getLeadInformation().getContactEmail(), signedFile);
            } else {
                agreementDetails.setResponse(false);
                if (agreementDetails.getLeadInformation() != null) {
                    long value = Constants.AGREEMENT_SIGNING_FAILED;
                    LeadInformation leadInformation = documentService.updateLeadStatus(agreementDetails.getLeadInformation().getLeadId(), value);
                    logger.info("leadInfo --: {}", leadInformation);
                }
            }
            agreementDetails = agreementDetailsRepository.save(agreementDetails);
            logger.info("agreementDetails ---: {} ", agreementDetails);

            if (agreementDetails.getLeadInformation().isAggregator()) {
                AgreementDetails finalAgreementDetails = agreementDetails;
                CompletableFuture.runAsync(() -> {
                    this.updateEventCallbackData(finalAgreementDetails);
                });
            }
        }
    }

    private void updateEventCallbackData(AgreementDetails finalAgreementDetails) {
        ResponseEntity<String> out = null;
        String url = aggEventCallbackUrl + "{leadNumber}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("leadNumber", finalAgreementDetails.getLeadInformation().getLeadNumber());
        AgreementDetailsDto detailsDto = new AgreementDetailsDto();

        if (finalAgreementDetails.isResponse()) {
            detailsDto.setResponse(true);
            detailsDto.setSignedFileName(finalAgreementDetails.getSignedFileName());
            detailsDto.setSignedFilePdfUrl(finalAgreementDetails.getSignedFilePdfUrl());
            detailsDto.setEventCallbackDetails(finalAgreementDetails.getEventCallbackDetails());
            detailsDto.setSignedFileDownUrl(fileUrl + finalAgreementDetails.getSignedFileName());
        } else {
            detailsDto.setResponse(false);
        }
        try {
            HttpEntity<AgreementDetailsDto> entity = new HttpEntity<>(detailsDto, httpHeaders);
            out = restTemplate.exchange(builder.build(urlParams), HttpMethod.PUT, entity, String.class);
            logger.info("status code ------------ : {} ", out.getStatusCodeValue());
            logger.info("response out -------------- : {}", out.getBody());
            if (out.getStatusCodeValue() == 200) {
                if (finalAgreementDetails.getLeadInformation().getPosQr() == true) {
                    CompletableFuture.runAsync(() -> {
                        this.generateTidMid(finalAgreementDetails);
                    });
                } else if (!finalAgreementDetails.getLeadInformation().getPosQr() == false) {
                    CompletableFuture.runAsync(() -> {
                        this.generateVpaTidMid(finalAgreementDetails.getLeadInformation().getLeadId());
                    });
                } else if (finalAgreementDetails.getLeadInformation().getLeadSource().getLeadSourceName().equals("LS_Wowpe")) {
                    this.generateMultiTerminal(finalAgreementDetails);
                }
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            int responseCode = httpClientErrorException.getRawStatusCode();
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("response code ----------- : {}, client Error Response -------------: {}", responseCode, clientErrorResponse);
        } catch (HttpServerErrorException httpServerErrorException) {
            int responseCode = httpServerErrorException.getRawStatusCode();
            String clientErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("response code ----------- : {}, server Error Response -------------: {}", responseCode, clientErrorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("exception -------------- : {} ", e.getMessage());
        }

    }

    //Qr vpa marsAi call function
    @PostMapping("/vpaResubmitApi/{leadId}")
    public ResponseEntity<?> generateVpaTidMid(@PathVariable("leadId") Long leadId) {
        logger.info("---------------------qrApi Activated--------------------------------{}", leadId);
//        long leadId = finalAgreementDetails.getLeadId();
        logger.info("-----------------------------------------------------{}", leadId);
        int count = documentService.getCount(leadId);
        logger.info("--------------------------------------------------------{}", count);
//        for (int i = 0; i < count; i++) {
        List<String> ls = documentService.findsLeadByLeadId(leadId);
        if (ls == null) {
            LeadInformation leadInformation1 = documentService.findLeadByLeadId(leadId);
            leadInformation1.setLeadStatus(documentService.getLeadStatus(LEAD_COMPLETED));
            MarsDeviceDetails marsDeviceDetails = documentService.getMarsDeviceDetails(leadId);
            marsDeviceDetails.setDeviceStatus(MARS_DEVICE_STATUS_TID_GENERATED);
        } else {
            logger.info("--------------------------------------------------------{}", ls);
            for (String l : ls) {


                logger.info("-----------------------------------------------------{}", l);
                MarsDeviceDetails marsDeviceDetails = documentService.getMarsDeviceDetails(leadId);
                logger.info("---------------------------------------------------------{}", marsDeviceDetails.getLeadInformation().getLeadId());
                TidVpaMap tidVpaMap = new TidVpaMap();
                tidVpaMap.setVpa(l);
                tidVpaMap.setLead_id(leadId);
                logger.info("---------------------------------------------------------{}", tidVpaMap.getLead_id());
                tidVpaMap.setTid(marsDeviceDetails.getTid());
                logger.info("------------------------------------tiid---------------------{}", tidVpaMap.getTid());
                ResponseEntity<String> out = null;
//                String url = aggGenerateVpaTidUrl;
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<TidVpaMap> entity = new HttpEntity<>(tidVpaMap, httpHeaders);
                LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
                try {
//                    LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
                    leadInformation.setIsMarsApiTriggered(MARS_API_TRIGGERED);
                    logger.info("------------------------------------marsApi--------------------{}", tidVpaMap.getTid());
                    leadInformationRepository.save(leadInformation);
                    out = restTemplate.exchange(aggGenerateVpaTidUrl, HttpMethod.POST, entity, String.class);
                    int responseCode = out.getStatusCode().value();
                    logger.info("----------------------------------------------{}", responseCode);
                    String responseData = out.getBody();
                    logger.info("-----------------------------------------------{}", responseData);
                    if (responseCode == 200) {
                        QrVpa qrVpa = documentService.getQrVpa(leadId, l);
                        logger.info("-------------------leadId-----------------------------{}", qrVpa.getQr_Lead_id());
                        qrVpa.setIs_Active(true);
                        logger.info("---------------------------------------------------{}", qrVpa.getIs_Active());
                        qrVpaRepository.save(qrVpa);
                    } else if (responseCode == 500) {
                        leadInformation.setLeadStatus(documentService.getLeadStatus(SQR_ONBOARDING_FAILED));
                        leadInformation.setActive(false);
                        leadInformationRepository.save(leadInformation);
                        logger.info("-----------------failed----------------------------{}", leadInformation.getLeadStatus());
                        return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed"), HttpStatus.INTERNAL_SERVER_ERROR);

                    } else {
                        leadInformation.setLeadStatus(documentService.getLeadStatus(SQR_ONBOARDING_FAILED));
                        leadInformation.setActive(false);
                        leadInformationRepository.save(leadInformation);
                        logger.info("-----------------failed----------------------------{}", leadInformation.getLeadStatus());
                        return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "Failed"), HttpStatus.CONFLICT);
                    }
//                    logger.info("response code -------------:{},response data -----------: {}", responseCode, responseData);
                } catch (HttpServerErrorException httpServerErrorException) {
                    int responseCode = httpServerErrorException.getStatusCode().value();
                    String serverResponse = httpServerErrorException.getResponseBodyAsString();
                    leadInformation.setLeadStatus(documentService.getLeadStatus(SQR_ONBOARDING_FAILED));
                    leadInformation.setActive(false);
                    leadInformationRepository.save(leadInformation);
                    logger.info("response code -------------:{},client error response --------:{}", responseCode, serverResponse);
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "500-Failed"), HttpStatus.CONFLICT);
                } catch (HttpClientErrorException httpClientErrorException) {
                    int responseCode = httpClientErrorException.getStatusCode().value();
                    String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
                    leadInformation.setLeadStatus(documentService.getLeadStatus(SQR_ONBOARDING_FAILED));
                    leadInformation.setActive(false);
                    leadInformationRepository.save(leadInformation);
                    logger.info("response code -------------:{},client error response --------:{}", responseCode, clientErrorResponse);
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "400-Failed"), HttpStatus.CONFLICT);

                }

            }
            int countFinal = documentService.getCount(leadId);
            logger.info("--------------{}", countFinal);
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);

            if (countFinal == 0) {
//                logger.info("-------------------leadid-------{}",leadInformation.getLeadId());
                LeadStatus leadStatus = documentService.getLeadStatus(LEAD_COMPLETED);
                boolean active = true;
                logger.info("-----------{}", leadStatus.getLeadStatusId());
                int update = leadInformationRepository.update(leadId, leadStatus, active);
                MarsDeviceDetails marsDeviceDetails = marsDeviceDetailsRepository.getMarsDeviceDetails(leadId);
                marsDeviceDetails.setDeviceStatus(MARS_DEVICE_STATUS_TID_GENERATED);
                marsDeviceDetailsRepository.save(marsDeviceDetails);
                logger.info("--------leadstatus--{}", update);

            } else {
                LeadStatus leadStatus = documentService.getLeadStatus(SQR_ONBOARDING_FAILED);
                boolean active = false;
                int update = leadInformationRepository.update(leadId, leadStatus, active);
                logger.info("-----{}----", update);
                return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "Qr-Pending"), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
    }

    private void generateTidMid(AgreementDetails finalAgreementDetails) {
        ResponseEntity<String> out = null;

        String url = aggGenerateTidUrl + "{leadId}";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("leadId", finalAgreementDetails.getLeadInformation().getLeadId());

        HttpEntity<Void> entity = new HttpEntity<>(null, httpHeaders);

        try {
            LeadInformation leadInformation = documentService.findLeadByLeadId(finalAgreementDetails.getLeadInformation().getLeadId());
            leadInformation.setIsMarsApiTriggered(MARS_API_TRIGGERED);
            leadInformationRepository.save(leadInformation);
            out = restTemplate.exchange(builder.build(urlParams), HttpMethod.PUT, entity, String.class);
            int responseCode = out.getStatusCode().value();
            String responseData = out.getBody();
            logger.info("response code -------------:{},response data -----------: {}", responseCode, responseData);
//            //Qr Api Call Auto
//            if (responseCode == 200 && leadInformation.getPosQr() == true) {
//                CompletableFuture.runAsync(() -> {
//                    this.generateVpaTidMid(finalAgreementDetails.getLeadInformation().getLeadId());
//                });
////                LeadInformation information = documentService.findLeadByLeadId(finalAgreementDetails.getLeadInformation().getLeadId());
////                information.setIsMarsApiTriggered(MARS_RESPONSE_TID_CREATED);
////                leadInformationRepository.save(leadInformation);
//            }
        } catch (HttpServerErrorException httpServerErrorException) {
//            LeadInformation leadInformation = documentService.findLeadByLeadId(finalAgreementDetails.getLeadInformation().getLeadId());
//            leadInformation.setIsMarsApiTriggered(MARS_API_NOT_TRIGGERED);
//            leadInformationRepository.save(leadInformation);
            int responseCode = httpServerErrorException.getStatusCode().value();
            String serverResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("response code ----------- :{}, server error response ---------- :{}", responseCode, serverResponse);
        } catch (HttpClientErrorException httpClientErrorException) {
//            LeadInformation leadInformation = documentService.findLeadByLeadId(finalAgreementDetails.getLeadInformation().getLeadId());
//            leadInformation.setIsMarsApiTriggered(MARS_API_NOT_TRIGGERED);
//            leadInformationRepository.save(leadInformation);
            int responseCode = httpClientErrorException.getStatusCode().value();
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("response code -------------:{},client error response --------:{}", responseCode, clientErrorResponse);
        }

    }

    /**
     * Esign Callback
     */
    @PostMapping("/eSign-callbackDetails")
    @ResponseBody
    public void callBackDetails(@RequestBody String callbackResponse, HttpServletResponse response) throws Exception {

        logger.info("callBack details -- : {}", callbackResponse);
        JSONObject object = new JSONObject(callbackResponse);
        JSONObject jsonObject = object.getJSONObject("esignParameters");

        String token = jsonObject.getString("token");
        AgreementDetails agreementDetails = documentService.findByToken(token);
        if (agreementDetails != null) {
            JSONObject jsonObject1 = object.getJSONObject("result");
            String esignedFile = jsonObject1.getString("esignedFile");
            agreementDetails.setSignedFileName(esignedFile);
            agreementDetails.setCallBackDetails(callbackResponse);
            agreementDetails = agreementDetailsRepository.save(agreementDetails);
            logger.info("agreementDetails ---: {} ", agreementDetails);
        }
    }

    /**
     * Verify Agreement Signing
     *
     * @param leadId
     * @return
     */
    @GetMapping("get-agreement-details/{leadId}")
    public ResponseEntity<?> fetchAgreementDetails(HttpServletRequest request, @PathVariable("leadId") long leadId) {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
//            AgreementDetails details = documentService.findAgreementDetailsByLeadId(leadInformation);
            List<AgreementDetails> details = documentService.findAgreementDetailsByPosAndQr(leadInformation);
            logger.info("details --: {}", details.size());
            logger.info("LeadInformation -- : {}", details.stream().map(l -> l.getLeadInformation()).collect(Collectors.toList()));
            if (details != null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", details), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("get-agreement-details-onboard/{leadNumber}")
    public ResponseEntity<?> fetchAgreementDetailsOnboardPortal(HttpServletRequest request, @PathVariable("leadNumber") String leadNumber) {
            LeadInformation leadInformation = leadInformationRepository.findByLeadNumber(leadNumber);
            List<AgreementDetails> details = documentService.findAgreementDetailsByPosAndQr(leadInformation);
            logger.info("details --: {}", details.size());
            logger.info("LeadInformation -- : {}", details.stream().map(l -> l.getLeadInformation()).collect(Collectors.toList()));
            if (details != null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", details), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
    }


    /**
     * Signzy Penny Drop Verification
     * ACCOUNT TRANSFER & VERIFY TRANSFER
     */
    @PostMapping("/penny-drop/accountTransfer/{leadId}")
    public ResponseEntity<?> accountTransfer(HttpServletRequest request, @PathVariable("leadId") long leadId, @RequestBody AccountTransferRequestDto accountTransferRequestDto) throws ParseException, JsonProcessingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
            if (leadInformation != null) {
                Pair<Integer, Object> result = signzyService.signzyAccountTransfer(leadInformation, accountTransferRequestDto);
                if (result.getFirst() == 0) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "lead not found"), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }
//    @PostMapping("/penny-drop/verifyTransfer/{leadId}")
//    public ResponseEntity<?> verifyTransfer(HttpServletRequest request,@PathVariable("leadId") long leadId, @RequestBody VerifyTransferRequestDto verifyTransferRequestDto) throws ParseException, JsonProcessingException {
//        String authtoken = request.getHeader("Authorization");
//        logger.info("authtoken==={}", authtoken);
//        User curuser = getSessionUser();
//        logger.info("Session User {}", curuser);
//        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
//        if (authtoken.equals(userauth.getToken())) {
//            LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
//            if (leadInformation != null) {
//                Pair<Integer, Object> result = signzyService.verifyTransfer(leadInformation, verifyTransferRequestDto);
//                if (result.getFirst() == 0) {
//                    return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
//                }
//                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
//            }
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "lead not found"), HttpStatus.BAD_REQUEST);
//        } else {
//
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
//        }
//    }

    /**
     * Aadhaar Digilocker
     */
    @PostMapping("/digilocker/{leadDocId}")
    public ResponseEntity<?> digiLocker(HttpServletRequest request, @RequestBody SignzyFetchRequestDto signzyFetchRequestDto, @PathVariable("leadDocId") LeadDocuments leadDocuments) throws Exception {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            List<UploadedDocuments> uploadedDocuments = documentService.findByLeadDocumentId(leadDocuments);
            Pair<Integer, SignzyFetchResponseDto> result = signzyService.digiLocker(signzyFetchRequestDto, uploadedDocuments);
            if (result.getFirst() == 1) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Aadhaar details collect API
     */
    @PostMapping("/getDetails/{leadDocId}")
    public ResponseEntity<?> getDetails(HttpServletRequest request, @RequestBody SignzyFetchRequestDto signzyFetchRequestDto, @PathVariable("leadDocId") LeadDocuments leadDocuments) throws Exception {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            List<UploadedDocuments> uploadedDocuments = documentService.findByLeadDocumentId(leadDocuments);
            Pair<Integer, SignzyFetchResponseDto> result = signzyService.getAadhaarDetails(signzyFetchRequestDto, uploadedDocuments);
            if (result.getFirst() == 1) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = {"/contract-sign/{leadId}/{action}", "/contract-sign/{leadId}/{action}/{isMultiTid}"})
    public ResponseEntity<?> contractSign(@PathVariable("leadId") long leadId, @PathVariable("action") int action, @PathVariable("isMultiTid") Optional<Boolean> isMultiTid) throws ParseException, JsonProcessingException {
        LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
        logger.info("leadDetails -- : {}", leadInformation);
        boolean isDocumentVerifiedBySignzy = documentService.checkDocumentVerifiedBySignzy(leadInformation);
        logger.info("isVerifiedBySignzy -- : {}", isDocumentVerifiedBySignzy);

        if (leadInformation.getLeadSubStatus() != null && leadInformation.getLeadSubStatus().getLeadSubStatusId() == Constants.LEAD_PENDING_WITH_NATIONAL_HEAD) {
            String title = "NH Approval";
            String reason = "NH Approval has been Pending";
            sendPushNotification(title, reason, leadInformation, PUSH_NOTIFICATION_TO_NH_APPROVAL_PENDING);
        }
        if (leadInformation != null && (leadInformation.getLeadSubStatus() == null || (leadInformation.getLeadSubStatus().getLeadSubStatusId() != Constants.LEAD_PENDING_WITH_NATIONAL_HEAD &&
                leadInformation.getLeadSubStatus().getLeadSubStatusId() != Constants.LEAD_REJECTED_BY_NATIONAL_LEAD)) && isDocumentVerifiedBySignzy) {
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
                } else {
                    List<UploadedDocuments> documents = uploadedDocumentsList.stream().filter(l -> l.getLeadDocuments().getDocumentVerifiedStatus() == Constants.OPS_VERIFICATION_SUCCESS).toList();
                    if (!documents.isEmpty()) {
                        pdfEsignData.setUploadedDocuments(documents);
                        logger.info("document list --------- : {}", documents);
                    }
                }
                if(leadInformation.isDigital()){
                    if (leadInformation.getIcaEnabled()==2) {
                        List<UploadedDocuments> data = uploadedDocumentsList.stream().filter(l -> l.getLeadDocuments() != null
                                && l.getLeadDocuments().getSubDocumentType() != null
                                && l.getLeadDocuments().getSubDocumentType().getName() != null
                                && !l.getLeadDocuments().getSubDocumentType().getName().equalsIgnoreCase("ICA Document")).toList();
                        pdfEsignData.setAllUploadedDocuments(data);
                        logger.info("All document list size--------- :{}", uploadedDocumentsList.size());
                    }
                    else {
                        pdfEsignData.setAllUploadedDocuments(uploadedDocumentsList);
                        logger.info("All document list size--------- :{}", uploadedDocumentsList.size());
                    }
                    }

            }
            pdfEsignData.setVasList(vasRepository.getAll(true));
            pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAllActive());
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

            List<OfflinePaymentData> offlinePaymentData=offlinePaymentRepo.getByLeadId(leadId);
            if(offlinePaymentData.size()>0){
                pdfEsignData.setOfflineDate(String.valueOf(offlinePaymentData.get(0).getCreatedAt()));
                if(offlinePaymentData.get(0).getChequeNumber()!=null){
                    pdfEsignData.setOfflineReferenceNo(offlinePaymentData.get(0).getChequeNumber());
                } else if (offlinePaymentData.get(0).getNeftId()!=null) {
                    pdfEsignData.setOfflineReferenceNo(offlinePaymentData.get(0).getNeftId());
                }else {
                    pdfEsignData.setOfflineReferenceNo(offlinePaymentData.get(0).getUtrNumber());
                }
                pdfEsignData.setOfflineAmount(offlinePaymentData.get(0).getOfflineAmount());
            }

            List<SMSPaymentDetails> smsPaymentDetails = smsPaymentDetailsRepository.findByLeadId(leadInformation);
            if (smsPaymentDetails.size() > 0) {
                if (smsPaymentDetails.get(0).getCallBackResponse() != null) {
                    JSONObject jsonObject = new JSONObject(smsPaymentDetails.get(0).getCallBackResponse());
                    JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("payments"));
                    JSONObject object1 = (JSONObject) jsonArray.get(0);
                    double amount = object1.getDouble("amount") / 100;
                    pdfEsignData.setAmount(amount);
                    pdfEsignData.setPaymentDate(DateUtil.formattedTime(DateUtil.longToDate(object1.getLong("created_at")), "dd-MM-yyyy HH:mm:ss"));
                }
                pdfEsignData.setReferenceNumber(smsPaymentDetails.get(0).getSmsReferenceId());
            }

            List<RazorPayQRStatus> razorPayQRStatuses=razorPayQRStatusRepo.getByLead(leadId);
            if(razorPayQRStatuses.size()>0){
                if(razorPayQRStatuses.get(0).getCallBackData()!=null){
                    JSONObject jsonObject = new JSONObject(razorPayQRStatuses.get(0).getCallBackData());
                    JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("items"));
                    JSONObject object1 = (JSONObject) jsonArray.get(0);
                    double amount = object1.getDouble("amount") / 100;
                    pdfEsignData.setAmount(amount);
                    pdfEsignData.setPaymentDate(DateUtil.formattedTime(DateUtil.longToDate(object1.getLong("created_at")), "dd-MM-yyyy HH:mm:ss"));
                    pdfEsignData.setReferenceNumber(object1.getString("id"));
                }
            }
            InternalSubvention internalSubvention=!internalSubventionRepository.getDetailByLeadIds(leadInformation.getLeadId()).isEmpty()?internalSubventionRepository.getDetailByLeadIds(leadInformation.getLeadId()).get(0):null;
            if (internalSubvention!=null) {
                pdfEsignData.setInternalSubvention(internalSubvention);
            }
            pdfEsignData.setFileConfig(fileConfig);

            PdfReportDo report = new PdfReportDo();
            ByteArrayOutputStream byteArrayOutputStream = null;
            Pair<Integer, Object> result = null;
            if (isMultiTid.isPresent() && isMultiTid.get()) {
                if ((leadInformation.getDevice() != null && (leadInformation.getStickerDevice() == null && leadInformation.getSqrDevice() == null)) || leadInformation.isGeeNeePos()) {
                    logger.info("AddOn Pos  ----- ");
                    byteArrayOutputStream = report.createAgreementPDF(leadInformation, pdfEsignData);
                    int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
                    logger.info("page count ----- : {}", pageCount);

                    logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
                    String fileName = "AgreementForm.pdf";
                    String contentType = MediaType.APPLICATION_PDF_VALUE;

                    logger.info("contentType --: {}", contentType);
                    MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
                    boolean qrFlag = false;
                    AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);
                    result = signzyService.signzy360AggContractDoPOS(action, multipartFile, leadInformation, details, pageCount);
                } else if (leadInformation.getDevice() == null && (leadInformation.getStickerDevice() != null || leadInformation.getSqrDevice() != null)) {
                    logger.info("AddOn QR  ----- ");
                    byteArrayOutputStream = report.createAgreementPDFQRANDSOUNDBOX(leadInformation, pdfEsignData);
                    int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
                    logger.info("page count ----- : {}", pageCount);
                    logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
                    String fileName = "AgreementForm.pdf";
                    String contentType = MediaType.APPLICATION_PDF_VALUE;
                    logger.info("contentType --: {}", contentType);
                    MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
                    boolean qrFlag = false;
                    AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);
                    result = signzyService.signzy360AggContractDoQR(action, multipartFile, leadInformation, details, pageCount);
                } else {
                    logger.info("AddOn PosQRSB  ----- ");
                    byteArrayOutputStream = report.createAgreementPDFPOSANDQRANDSOUNDBOX(leadInformation, pdfEsignData);
                    int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
                    logger.info("page count ----- : {}", pageCount);
                    logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
                    String fileName = "AgreementForm.pdf";
                    String contentType = MediaType.APPLICATION_PDF_VALUE;
                    logger.info("contentType --: {}", contentType);
                    MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
                    boolean qrFlag = false;
                    AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);
                    result = signzyService.signzy360AggContractDoPOSQRSB(action, multipartFile, leadInformation, details, pageCount);
                }

            } else {
                if (leadInformation.isPosFlag()) {
                    byteArrayOutputStream = report.createAgreementPDF(leadInformation, pdfEsignData);
                    int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
                    logger.info("page count ----- : {}", pageCount);

                    logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
                    String fileName = "AgreementForm.pdf";
                    String contentType = MediaType.APPLICATION_PDF_VALUE;
                    logger.info("contentType --: {}", contentType);
                    MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
                    boolean qrFlag = false;
                    AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);
                    result = signzyService.signzy360AggContractDoPOS(action, multipartFile, leadInformation, details, pageCount);
                } else {
                    logger.info("Calling Q181 Agrement ------");
                    byteArrayOutputStream = report.createAgreementPDFForQR(leadInformation, pdfEsignData);
                    int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
                    logger.info("page count ----- : {}", pageCount);

                    logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
                    String fileName = "AgreementForm.pdf";
                    String contentType = MediaType.APPLICATION_PDF_VALUE;
                    logger.info("contentType --: {}", contentType);
                    MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
                    boolean qrFlag = false;
                    AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);
                    result = signzyService.signzy360AggContractDoQR(action, multipartFile, leadInformation, details, pageCount);
                }
            }
//            int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
//            logger.info("page count ----- : {}", pageCount);
//
//            logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
//            String fileName = "AgreementForm.pdf";
//            String contentType = MediaType.APPLICATION_PDF_VALUE;
//            logger.info("contentType --: {}", contentType);
//            MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
//            boolean qrFlag = false;
//            AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);

//            Pair<Integer, Object> result = signzyService.signzyContract(action, multipartFile, leadInformation,details,pageCount);
//            Pair<Integer, Object> result = signzyService.signzy360AggContractDoPOS(action, multipartFile, leadInformation, details, pageCount);
//            Pair<Integer, Object> result = signzyService.signzy360AggContractDoQR(action, multipartFile, leadInformation, details, pageCount);

            if (result.getFirst() == 0) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/contract-sign-util/{leadId}/{action}")
    public ResponseEntity<?> contractSignUtil(@PathVariable("leadId") long leadId, @PathVariable("action") int action) throws ParseException, JsonProcessingException {
        LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
        logger.info("leadDetails -- : {}", leadInformation);
//        boolean isDocumentVerifiedBySignzy = documentService.checkDocumentVerifiedBySignzy(leadInformation);
        boolean isDocumentVerifiedBySignzy = true;
        logger.info("isVerifiedBySignzy -- : {}", isDocumentVerifiedBySignzy);

        if (leadInformation.getLeadSubStatus() != null && leadInformation.getLeadSubStatus().getLeadSubStatusId() == Constants.LEAD_PENDING_WITH_NATIONAL_HEAD) {
            String title = "NH Approval";
            String reason = "NH Approval has been Pending";
            sendPushNotification(title, reason, leadInformation, PUSH_NOTIFICATION_TO_NH_APPROVAL_PENDING);
        }
        if (leadInformation != null && (leadInformation.getLeadSubStatus() == null || (leadInformation.getLeadSubStatus().getLeadSubStatusId() != Constants.LEAD_PENDING_WITH_NATIONAL_HEAD &&
                leadInformation.getLeadSubStatus().getLeadSubStatusId() != Constants.LEAD_REJECTED_BY_NATIONAL_LEAD)) && isDocumentVerifiedBySignzy) {
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
                } else {
                    List<UploadedDocuments> documents = uploadedDocumentsList.stream().filter(l -> l.getLeadDocuments().getDocumentVerifiedStatus() == Constants.OPS_VERIFICATION_SUCCESS).toList();
                    if (!documents.isEmpty()) {
                        pdfEsignData.setUploadedDocuments(documents);
                        logger.info("document list --------- : {}", documents);
                    }
                }


            }
            pdfEsignData.setVasList(vasRepository.getAll(true));
            pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAll());
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

            List<SMSPaymentDetails> smsPaymentDetails = smsPaymentDetailsRepository.findByLeadId(leadInformation);
            if (smsPaymentDetails.size() > 0) {
                if (smsPaymentDetails.get(0).getCallBackResponse() != null) {
                    JSONObject jsonObject = new JSONObject(smsPaymentDetails.get(0).getCallBackResponse());
                    JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("payments"));
                    JSONObject object1 = (JSONObject) jsonArray.get(0);
                    pdfEsignData.setAmount(object1.getLong("amount"));
                    pdfEsignData.setPaymentDate(DateUtil.formattedTime(DateUtil.longToDate(object1.getLong("created_at")), "dd-MM-yyyy HH:mm:ss"));
                }
                pdfEsignData.setReferenceNumber(smsPaymentDetails.get(0).getSmsReferenceId());
            }

            PdfReportDo report = new PdfReportDo();
            ByteArrayOutputStream byteArrayOutputStream = report.createAgreementPDFUtil(leadInformation, pdfEsignData);
            int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
            logger.info("page count ----- : {}", pageCount);

            logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
            String fileName = "AgreementForm.pdf";
            String contentType = MediaType.APPLICATION_PDF_VALUE;
            logger.info("contentType --: {}", contentType);
            MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
            boolean qrFlag = false;
            AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);

//            Pair<Integer, Object> result = signzyService.signzyContract(action, multipartFile, leadInformation,details,pageCount);
            Pair<Integer, Object> result = signzyService.signzy360AggContractDo(action, multipartFile, leadInformation, details, pageCount);

            if (result.getFirst() == 0) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/contract-callbackDetails")
    @ResponseBody
    public void contractCallbackDetails(@RequestBody String callbackResponse) throws IOException {
        logger.info("callbackResponse ------------ : {} ", callbackResponse);
        JSONObject jsonObject = new JSONObject(callbackResponse);
        logger.info("jsonObject ------------------: {} ", jsonObject);
        String contractId = jsonObject.getString("contractId");
        AgreementDetails agreementDetails = documentService.findByContractId(contractId);
        if (agreementDetails != null) {
            agreementDetails.setResponse(true);
            String signedFileUrl = jsonObject.getString("finalSignedContract");
            String finalSignedName = byteArrayMultiPartFileService.convertUrlToPdfForContract(signedFileUrl, agreementDetails.getLeadInformation(), "FinalSigned"); // convert URL to pdf and store pdf in server
            agreementDetails.setFinalSignedName(finalSignedName);

            String auditCertificate = jsonObject.getString("auditCertificate");
            String auditCertificateName = byteArrayMultiPartFileService.convertUrlToPdfForContract(auditCertificate, agreementDetails.getLeadInformation(), "AuditCertificate");
            agreementDetails.setAuditCertificateName(auditCertificateName);

            agreementDetails.setFinalSignedContract(signedFileUrl);
            agreementDetails.setAuditCertificate(auditCertificate);
            agreementDetails.setContractCallbackDetails(callbackResponse);
            logger.info("leadInfo details 1-------- : {}", agreementDetails.getLeadInformation().toString());
            logger.info("leadInfo details 2-------- : {}", agreementDetails.getLeadInformation().getLeadId());

            if (agreementDetails.getLeadInformation() != null) {
                long value = Constants.AGREEMENT_SIGNING_COMPLETED;
                LeadInformation leadInformation = documentService.updateLeadStatus(agreementDetails.getLeadInformation().getLeadId(), value);
                logger.info("leadInfo --: {}", leadInformation);
            }
            agreementDetails = agreementDetailsRepository.save(agreementDetails);
            logger.info("agreementDetails ---: {} ", agreementDetails);
            if (agreementDetails.getLeadInformation().isAggregator()) {
                AgreementDetails finalAgreementDetails = agreementDetails;
                CompletableFuture.runAsync(() -> {
                    this.updateContractCallback(finalAgreementDetails);
                });
            }
        }
    }

    private void updateContractCallback(AgreementDetails finalAgreementDetails) {
        logger.info("entered into Agg-callback");
        ResponseEntity<String> out = null;
        String url = aggContractDataUrl + "{leadNumber}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AgreementDetailsDto detailsDto = new AgreementDetailsDto();

        detailsDto.setFinalSignedContract(finalAgreementDetails.getFinalSignedContract());
        detailsDto.setAuditCertificate(finalAgreementDetails.getAuditCertificate());
        detailsDto.setContractCallbackDetails(finalAgreementDetails.getContractCallbackDetails());
        detailsDto.setFinalSignedName(finalAgreementDetails.getFinalSignedName());
        detailsDto.setAuditCertificate(finalAgreementDetails.getAuditCertificate());
        detailsDto.setFinalSignedDownUrl(fileUrl + finalAgreementDetails.getFinalSignedName());
        detailsDto.setAuditCertificateDownUrl(fileUrl + finalAgreementDetails.getAuditCertificateName());
        detailsDto.setResponse(true);

        HttpEntity<AgreementDetailsDto> entity = new HttpEntity<>(detailsDto, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("leadNumber", finalAgreementDetails.getLeadInformation().getLeadNumber());

        try {
            out = restTemplate.exchange(builder.build(urlParams), HttpMethod.PUT, entity, String.class);
            logger.info("response code -------------------- : {}", out.getStatusCodeValue());
            logger.info("response out --------------------- : {}", out.getBody());
            if (out.getStatusCodeValue() == 200) {
                if (finalAgreementDetails.getLeadInformation().getPosQr() && !finalAgreementDetails.getLeadInformation().getLeadSource().getLeadSourceName().equals("LS_Wowpe")) {
                    logger.info("----------posOnbording ----- : {}", finalAgreementDetails.getLeadInformation().getPosQr());
//                    if(finalAgreementDetails.isResponse())
                    CompletableFuture.runAsync(() -> {
                        this.generateTidMid(finalAgreementDetails);
                    });
                } else if (!finalAgreementDetails.getLeadInformation().getPosQr() && !finalAgreementDetails.getLeadInformation().getLeadSource().getLeadSourceName().equals("LS_Wowpe")) {
                    logger.info("----------qrOnbording ----- : {}", finalAgreementDetails.getLeadInformation().getPosQr());
                    CompletableFuture.runAsync(() -> {
                        this.generateVpaTidMid(finalAgreementDetails.getLeadInformation().getLeadId());
                    });
                } else if (finalAgreementDetails.getLeadInformation().getLeadSource().getLeadSourceName().equals("LS_Wowpe")) {
                    logger.info("Multi-Tid Triggered");
                    this.generateMultiTerminal(finalAgreementDetails);
                }
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            int statusCode = httpClientErrorException.getRawStatusCode();
            logger.info("status code ---------- : {}", statusCode);
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("client Error Response ------------ : {}", clientErrorResponse);
        } catch (HttpServerErrorException httpServerErrorException) {
            int statusCode = httpServerErrorException.getRawStatusCode();
            logger.info("status code ------------ : {}", statusCode);
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("server Error Response ----------- : {}", serverErrorResponse);
        }
    }


    @PostMapping("/contract-signer-callbackDetails")
    @ResponseBody
    public void signerCallbackDetails(@RequestBody String signerCallbackDetails) throws IOException {
        logger.info("signerCallbackDetails --------------- : {} ", signerCallbackDetails);
        JSONObject jsonObject = new JSONObject(signerCallbackDetails);
        logger.info("jsonObject ------------------: {} ", jsonObject);
        String signerId = jsonObject.getString("signerId");
        AgreementDetails agreementDetails = documentService.findBySignerId(signerId);
        if (agreementDetails != null) {
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
//                agreementDetails.setResponse(true);
                agreementDetails.setContractId(jsonObject.getString("contractId"));
                agreementDetails = agreementDetailsRepository.save(agreementDetails);
                String signedFileUrl = jsonObject.getString("signerSignedContract");
                String signedFile = byteArrayMultiPartFileService.convertUrlToPdfForContract(signedFileUrl, agreementDetails.getLeadInformation(), "SignerSigned"); // convert URL to pdf and store pdf in server
                agreementDetails.setSignerSignedPdfUrl(signedFileUrl);
                agreementDetails.setSignerCallbackDetails(signerCallbackDetails);
            } else {
                agreementDetails.setResponse(false);
                if (agreementDetails.getLeadInformation() != null) {
                    long value = Constants.AGREEMENT_SIGNING_FAILED;
                    LeadInformation leadInformation = documentService.updateLeadStatus(agreementDetails.getLeadInformation().getLeadId(), value);
                    logger.info("leadInfo --: {}", leadInformation);
                }
            }
            agreementDetails = agreementDetailsRepository.save(agreementDetails);
            logger.info("signer signed agreementDetails ----------: {} ", agreementDetails);
            if (agreementDetails.getLeadInformation().isAggregator() &&
                    agreementDetails.getLeadInformation().getLeadStatus().getLeadStatusId() == Constants.AGREEMENT_SIGNING_FAILED) {
                AgreementDetails finalAgreementDetails = agreementDetails;
                CompletableFuture.runAsync(() -> {
                    this.updateContractFailed(finalAgreementDetails);
                });
            }
        }
    }

    private void updateContractFailed(AgreementDetails finalAgreementDetails) {
        ResponseEntity<String> out = null;
        String url = aggContractFailedUrl + "{leadNumber}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        AgreementDetailsDto detailsDto = new AgreementDetailsDto();
        detailsDto.setLeadNumber(finalAgreementDetails.getLeadInformation().getLeadNumber());
        detailsDto.setResponse(finalAgreementDetails.isResponse());

        HttpEntity<AgreementDetailsDto> entity = new HttpEntity<>(detailsDto, httpHeaders);
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("leadNumber", finalAgreementDetails.getLeadInformation().getLeadNumber());

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        try {
            out = restTemplate.exchange(builder.build(urlParams), HttpMethod.PUT, entity, String.class);
            logger.info("response code ----------- : {},response out----------------- : {}", out.getStatusCode(), out.getBody());
        } catch (HttpClientErrorException httpClientErrorException) {
            int responseCode = httpClientErrorException.getRawStatusCode();
            logger.info("response code ----------------- : {}", responseCode);
            String clientError = httpClientErrorException.getResponseBodyAsString();
            logger.info("client error ----------- : {}", clientError);
        } catch (HttpServerErrorException httpServerErrorException) {
            int responseCode = httpServerErrorException.getRawStatusCode();
            String serverResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("response code---------- : {} , server error response ---------- : {}", responseCode, serverResponse);
        }
    }

    /**
     * Verify Agreement Signing
     *
     * @param leadId
     * @return
     */
    @GetMapping("get-contract-details/{leadId}")
    public ResponseEntity<?> fetchContracttDetails(HttpServletRequest request, @PathVariable("leadId") long leadId) {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            AgreementDetails details = documentService.findAgreementDetailsByLeadId(leadInformation);
            logger.info("details --: {}", details);
            logger.info("LeadInformation -- : {}", details.getLeadInformation());
            if (details != null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", details), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Approve Aggregator for Contract Sign
     * 0-Normal-Esign
     * 1-Aadhaar-Esign
     */
    @PostMapping("approve-agg-contract-sign/{leadId}/{action}")
    public ResponseEntity<?> approveContractSign(@PathVariable("leadId") long leadId, @PathVariable("action") int action, HttpServletRequest request) throws ParseException, JsonProcessingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            logger.info("leadDetails -- : {}", leadInformation);
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
                List<UploadedDocuments> documents = uploadedDocumentsList.stream().filter(l -> l.getLeadDocuments().getDocumentVerifiedStatus() == Constants.OPS_VERIFICATION_SUCCESS).toList();
                if (!documents.isEmpty()) {
                    pdfEsignData.setUploadedDocuments(documents);
                    logger.info("document list --------- : {}", documents);
                }
            }
            pdfEsignData.setVasList(vasRepository.getAll(true));
            if(leadInformation.getLeadSource().getLeadSourceName().equals("LS_Wowpe")){
                pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAllActiveList());
            }else {
                pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAll());
            }
            pdfEsignData.setFileConfig(fileConfig);
//            pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAll());

            PdfReport report = new PdfReport();
//            AggPdfReport report = new AggPdfReport();
//        SqrPdfReport report = new SqrPdfReport();
            AgreementDetails agreementDetails = documentService.getAggrement(leadInformation.getLeadId());
            ByteArrayOutputStream byteArrayOutputStream = report.createAgreementPDF(leadInformation, pdfEsignData);
//        ByteArrayOutputStream byteArrayOutputStream = report.createSqrAgreementPDF(leadInformation, agreementDetails);
            int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
            logger.info("page count ----- : {}", pageCount);

            logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
            String fileName = "AgreementForm.pdf";
            String contentType = MediaType.APPLICATION_PDF_VALUE;
            logger.info("contentType --: {}", contentType);
            MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
            boolean qrFlag = false;
            AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);

//            Pair<Integer, Object> result = signzyService.signzyAggContract(action, multipartFile, leadInformation,details,pageCount);
            Pair<Integer, Object> result = null;
            if (leadInformation.getLeadSource().getLeadSourceName().equals("LS_Wowpe")) {
                result = signzyService.signzy360AggContractMultiSign(action, multipartFile, leadInformation, details, pageCount);
            } else {
                result = signzyService.signzy360AggContract(action, multipartFile, leadInformation, details, pageCount);
            }

            if (result.getFirst() == 0) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
            }
            Pair<Integer, Object> finalResult = result;
            CompletableFuture.runAsync(() -> {
                this.createContractAgreementForOnboard(details, finalResult);
            });
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("approve-agg-contract360-sign/{leadId}/{action}")
    public ResponseEntity<?> approveContract360Sign(@PathVariable("leadId") long leadId, @PathVariable("action") int action, HttpServletRequest request) throws ParseException, JsonProcessingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            logger.info("leadDetails -- : {}", leadInformation);
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
                List<UploadedDocuments> documents = uploadedDocumentsList.stream().filter(l -> l.getLeadDocuments().getDocumentVerifiedStatus() == Constants.OPS_VERIFICATION_SUCCESS).toList();
                if (!documents.isEmpty()) {
                    pdfEsignData.setUploadedDocuments(documents);
                    logger.info("document list --------- : {}", documents);
                }
            }
            pdfEsignData.setVasList(vasRepository.getAll(true));
            pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAll());

            PdfReport report = new PdfReport();
//            AggPdfReport report = new AggPdfReport();
            ByteArrayOutputStream byteArrayOutputStream = report.createAgreementPDF(leadInformation, pdfEsignData);


            int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
            logger.info("page count ----- : {}", pageCount);

            logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
            String fileName = "AgreementForm.pdf";
            String contentType = MediaType.APPLICATION_PDF_VALUE;
            logger.info("contentType --: {}", contentType);
            MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
            boolean qrFlag = false;
            AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);

//            Pair<Integer, Object> result = signzyService.signzyAggContract(action, multipartFile, leadInformation,details,pageCount);

            Pair<Integer, Object> result = signzyService.signzy360AggContract(action, multipartFile, leadInformation, details, pageCount);

            if (result.getFirst() == 0) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error", result.getSecond()), HttpStatus.BAD_REQUEST);
            }
            CompletableFuture.runAsync(() -> {
                this.createContractAgreementForOnboard(details, result);
            });
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Approve Aggregator for Contract Sign
     * 0-Normal-Esign
     * 1-Aadhaar-Esign
     */
    @PostMapping("approve-agg-esign/{leadId}/{action}")
    public ResponseEntity<?> approveAggESign(@PathVariable("leadId") long leadId, @PathVariable("action") int action, HttpServletRequest request) throws ParseException, JsonProcessingException {
        String authtoken = request.getHeader("Authorization");
        logger.info("authtoken==={}", authtoken);
        User curuser = getSessionUser();
        logger.info("Session User {}", curuser);
        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
        if (authtoken.equals(userauth.getToken())) {
            LeadInformation leadInformation = documentService.findLeadByLeadId(leadId);
            logger.info("leadDetails -- : {}", leadInformation);
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
            }
            pdfEsignData.setVasList(vasRepository.getAll(true));
            pdfEsignData.setMerchantTypeList(merchantTypeRepository.getAll());

            PdfReport report = new PdfReport();
            ByteArrayOutputStream byteArrayOutputStream = report.createAgreementPDF(leadInformation, pdfEsignData);
            int pageCount = getPageCountFromByteArrayOutputStream(byteArrayOutputStream);
            logger.info("page count ----- : {}", pageCount);

            logger.info("pdf data -- : {}", byteArrayOutputStream.toByteArray());
            String fileName = "AgreementForm.pdf";
            String contentType = MediaType.APPLICATION_PDF_VALUE;
            logger.info("contentType --: {}", contentType);
            MultipartFile multipartFile = byteArrayMultiPartFileService.convertByteArrayOutputStreamToMultiPartFile(byteArrayOutputStream, fileName, contentType);
//            AgreementDetails details = documentService.storeAggPdfFile(multipartFile, leadInformation);
            boolean qrFlag = false;
            AgreementDetails details = documentService.storePdfFile(multipartFile, leadInformation, qrFlag);


            Pair<Integer, Object> result = signzyService.signzyEsign(action, leadInformation.getMerchantName(), multipartFile, details, pageCount);
            if (result.getFirst() == 0) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Technical issue !!!.Please try again later", result.getSecond()), HttpStatus.BAD_REQUEST);
            }
            CompletableFuture.runAsync(() -> {
                this.createAgreementForOnboard(details, result);
            });
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result.getSecond()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "UnAuthorized!!!", null), HttpStatus.UNAUTHORIZED);
        }
    }

    private void createContractAgreementForOnboard(AgreementDetails details, Pair<Integer, Object> result) {
        ResponseEntity<String> out = null;

        String url = aggAgreementUrl + "{leadNumber}";

        AgreementDetailsDto detailsDto = new AgreementDetailsDto();
        detailsDto.setLeadNumber(details.getLeadInformation().getLeadNumber());
        detailsDto.setInputFile(details.getInputFileName());
        detailsDto.setResponse(false);
        detailsDto.setInputFileUrl(fileUrl + details.getInputFileName());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result.getSecond());
            JsonNode jsonNode = mapper.readTree(json);
//            JsonNode idNode = jsonNode.get("result");
            JsonNode pdfNode = jsonNode.get("pdf");
            String pdfUrl = pdfNode.asText();
            logger.info("pdf Url-----------------------------  : {}", pdfUrl);
            detailsDto.setInputFilePdfUrl(pdfUrl);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("leadNumber", details.getLeadInformation().getLeadNumber());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AgreementDetailsDto> entity = new HttpEntity<>(detailsDto, headers);
        try {
            out = restTemplate.exchange(builder.build(urlParams), HttpMethod.POST, entity, String.class);
            logger.info("out --------------- : {}", out.getBody());
            if (out.getStatusCodeValue() == 200) {
                logger.info("check status -- : {}", details.getLeadInformation().getLeadStatus().getLeadStatusName());
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientException = httpClientErrorException.getResponseBodyAsString();
            logger.info("client Exception ------------ : {}", clientException);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverException = httpServerErrorException.getResponseBodyAsString();
            logger.info("server Exception ------------ : {}", serverException);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("exception occurred ------------- : {}", e.getMessage());
        }
    }

    private void createAgreementForOnboard(AgreementDetails details, Pair<Integer, Object> result) {
        ResponseEntity<String> out = null;

        String url = aggAgreementUrl + "{leadNumber}";

        AgreementDetailsDto detailsDto = new AgreementDetailsDto();
        detailsDto.setLeadNumber(details.getLeadInformation().getLeadNumber());
        detailsDto.setInputFile(details.getInputFileName());
        detailsDto.setResponse(false);
        detailsDto.setInputFileUrl(fileUrl + details.getInputFileName());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result.getSecond());
            JsonNode jsonNode = mapper.readTree(json);
            JsonNode idNode = jsonNode.get("result");
            JsonNode pdfNode = idNode.get("inputFile");
            String pdfUrl = pdfNode.asText();
            logger.info("pdf Url-----------------------------  : {}", pdfUrl);
            detailsDto.setInputFilePdfUrl(pdfUrl);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("leadNumber", details.getLeadInformation().getLeadNumber());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AgreementDetailsDto> entity = new HttpEntity<>(detailsDto, headers);
        try {
            out = restTemplate.exchange(builder.build(urlParams), HttpMethod.POST, entity, String.class);
            logger.info("out --------------- : {}", out.getBody());
            if (out.getStatusCodeValue() == 200) {
                logger.info("check status -- : {}", details.getLeadInformation().getLeadStatus().getLeadStatusName());
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientException = httpClientErrorException.getResponseBodyAsString();
            logger.info("client Exception ------------ : {}", clientException);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverException = httpServerErrorException.getResponseBodyAsString();
            logger.info("server Exception ------------ : {}", serverException);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("exception occurred ------------- : {}", e.getMessage());
        }
    }

    @PostMapping("/contract360-callbackDetails")
    @ResponseBody
    public void contract360CallbackDetails(@RequestBody String callbackResponse) throws IOException {
        logger.info("callbackResponse ------------ : {} ", callbackResponse);
        JSONObject jsonObject = new JSONObject(callbackResponse);
        logger.info("jsonObject ------------------: {} ", jsonObject);
        String contractId = jsonObject.getString("contractId");
        AgreementDetails agreementDetails = documentService.findByContractId(contractId);
        if (agreementDetails != null) {
            if (!(agreementDetails.getLeadInformation().getLeadSource().getLeadSourceName().equals("LS_Wowpe") && agreementDetails.isResponse())) {
                agreementDetails.setResponse(true);
                String signedFileUrl = jsonObject.getString("finalSignedContract");
                String finalSignedName = byteArrayMultiPartFileService.convertUrlToPdfForContract(signedFileUrl, agreementDetails.getLeadInformation(), "FinalSigned"); // convert URL to pdf and store pdf in server
                agreementDetails.setFinalSignedName(finalSignedName);
//
                String auditCertificate = jsonObject.getString("auditCertificateUrl");
                String auditCertificateName = byteArrayMultiPartFileService.convertUrlToPdfForContract(auditCertificate, agreementDetails.getLeadInformation(), "AuditCertificate");
                agreementDetails.setAuditCertificateName(auditCertificateName);
//
                agreementDetails.setFinalSignedContract(signedFileUrl);
                agreementDetails.setAuditCertificate(auditCertificate);
                agreementDetails.setContractCallbackDetails(callbackResponse);
                logger.info("leadInfo details 1-------- : {}", agreementDetails.getLeadInformation().toString());
                logger.info("leadInfo details 2-------- : {}", agreementDetails.getLeadInformation().getLeadId());
//
                if (agreementDetails.getLeadInformation() != null && !agreementDetails.getLeadInformation().isPullTriggered()) {
                    long value = Constants.AGREEMENT_SIGNING_COMPLETED;
                    LeadInformation leadInformation = documentService.updateLeadStatus(agreementDetails.getLeadInformation().getLeadId(), value);
                    logger.info("leadInfo --: {}", leadInformation);
                }
                agreementDetails = agreementDetailsRepository.save(agreementDetails);
                logger.info("agreementDetails ---: {} ", agreementDetails);
                if (agreementDetails.getLeadInformation().isAggregator()) {
                    AgreementDetails finalAgreementDetails = agreementDetails;
                    CompletableFuture.runAsync(() -> {
                        this.updateContractCallback(finalAgreementDetails);   // aggregator leads
                    });
                }
            }
        }
    }

    @PostMapping("/contract360-signer-callbackDetails")
    @ResponseBody
    public void contract360SignerCallbackDetails(@RequestBody String signerCallbackDetails) throws IOException {
        logger.info("signerCallbackDetails --------------- : {} ", signerCallbackDetails);
        JSONObject jsonObject = new JSONObject(signerCallbackDetails);
        logger.info("jsonObject ------------------: {} ", jsonObject);
        String signerId = jsonObject.getString("signerId");
        AgreementDetails agreementDetails = documentService.findBySignerId(signerId);
        if (agreementDetails != null) {
            if (!(agreementDetails.getLeadInformation().getLeadSource().getLeadSourceName().equals("LS_Wowpe") && agreementDetails.getContractCallbackDetails()==null)) {
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("SUCCESS")) {
                    agreementDetails.setResponse(true);
                    String signedFileUrl = jsonObject.getString("signerSignedContract");
                    String signedFile = byteArrayMultiPartFileService.convertUrlToPdfForContract(signedFileUrl, agreementDetails.getLeadInformation(), "SignerSigned"); // convert URL to pdf and store pdf in server
                    agreementDetails.setSignerSignedPdfUrl(signedFileUrl);
                    agreementDetails.setSignerCallbackDetails(signerCallbackDetails);
                } else {
                    agreementDetails.setResponse(false);
                    if (agreementDetails.getLeadInformation() != null) {
                        long value = Constants.AGREEMENT_SIGNING_FAILED;
                        LeadInformation leadInformation = documentService.updateLeadStatus(agreementDetails.getLeadInformation().getLeadId(), value);
                        logger.info("leadInfo --: {}", leadInformation);
                    }
                }
                agreementDetails = agreementDetailsRepository.save(agreementDetails);
                logger.info("signer signed agreementDetails ----------: {} ", agreementDetails);
                if (agreementDetails.getLeadInformation().isAggregator() &&
                        agreementDetails.getLeadInformation().getLeadStatus().getLeadStatusId() == Constants.AGREEMENT_SIGNING_FAILED) {
                    AgreementDetails finalAgreementDetails = agreementDetails;
                    CompletableFuture.runAsync(() -> {
                        this.updateContractFailed(finalAgreementDetails);
                    });
                }
            }
        }
    }

    private void generateMultiTerminal(AgreementDetails finalAgreementDetails) {
        ResponseEntity<String> out = null;

        String url = GenerateMultiTerminalPos + "{leadId}";
        logger.info("LeadInformation Status  :{}", finalAgreementDetails.getLeadInformation().getLeadStatus().getLeadStatusName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("leadId", finalAgreementDetails.getLeadInformation().getLeadId());

        HttpEntity<Void> entity = new HttpEntity<>(null, httpHeaders);

        try {
            LeadInformation leadInformation = documentService.findLeadByLeadId(finalAgreementDetails.getLeadInformation().getLeadId());
            leadInformation.setIsMarsApiTriggered(MARS_API_TRIGGERED);
            leadInformationRepository.save(leadInformation);
            logger.info("Multi-Terminal Triggered :{}", builder.build(urlParams));
            out = restTemplate.exchange(builder.build(urlParams), HttpMethod.PUT, entity, String.class);
            int responseCode = out.getStatusCode().value();
            String responseData = out.getBody();
            logger.info("response code -------------:{},response data -----------: {}", responseCode, responseData);
        } catch (HttpServerErrorException httpServerErrorException) {
            int responseCode = httpServerErrorException.getStatusCode().value();
            String serverResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("response code ----------- :{}, server error response ---------- :{}", responseCode, serverResponse);
        } catch (HttpClientErrorException httpClientErrorException) {
            int responseCode = httpClientErrorException.getStatusCode().value();
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("response code -------------:{},client error response --------:{}", responseCode, clientErrorResponse);
        }

    }

    @PostMapping("pull-contract/{contractId}")
    public ResponseEntity<?> pullContract(@PathVariable("contractId") String contractId) throws IOException {
        logger.info("Thread.sleep(120000)--{}", contractId);
        AgreementDetails agreementDetails = agreementDetailsRepository.findByContractId(contractId);
        if (agreementDetails != null) {
            Pair<String, Integer> pair = signzyService.pullContract(contractId);
            if (pair.getSecond() == 4) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", pair.getFirst()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "failed", pair.getFirst()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "contractId Not Found"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("getContractId/{leadId}")
    public ResponseEntity<?> contractId(@PathVariable("leadId") long leadId) {
        LeadInformation leadInformation = leadInformationRepository.findByLeadId(leadId);
        if (leadInformation != null) {
            AgreementDetails agreementDetails = agreementDetailsRepository.findByLeadId(leadInformation);
            if (agreementDetails != null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "SUCESS", agreementDetails.getContractId()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "InValid Contract ID"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead Not Found"), HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/pan-extraction-signzy/{leadId}/{subDocumentType}", "/pan-extraction-signzy/{leadId}/{subDocumentType}/{action}"})
    public Object uploadEncryptedFiles(@RequestPart("file") MultipartFile files, HttpServletRequest requests, @PathVariable("leadId") long leadInformation, @PathVariable("subDocumentType") long subDocumentType,
                                                  @RequestParam(value = "typeOfDocument", required = false) String typeOfDocument, @PathVariable("latlng") Optional<String> latlng, @PathVariable("action") Optional<Integer> action) throws Exception {

        if (files!=null){
//            Object response=signzyService.extractPan(files,leadInformation,subDocumentType,typeOfDocument,latlng);
//            return response;
        }
        return null;
    }

    @PostMapping({"/gst-extraction-signzy/{leadId}/{subDocumentType}", "/gst-extraction-signzy/{leadId}/{subDocumentType}/{action}"})
    public Object gstExtraction(@RequestPart("file") MultipartFile files, HttpServletRequest requests, @PathVariable("leadId") long leadInformation, @PathVariable("subDocumentType") long subDocumentType,
                                       @RequestParam(value = "typeOfDocument", required = false) String typeOfDocument, @PathVariable("latlng") Optional<String> latlng, @PathVariable("action") Optional<Integer> action) throws Exception {

//        if (files!=null){
//            return signzyService.extractGst(files,leadInformation,subDocumentType,typeOfDocument,latlng);
//        }
        return null;
    }


//    @PostMapping({"/extraction-signzy/{leadId}/{subDocumentType}", "/adhaar-extraction-signzy/{leadId}/{subDocumentType}/{action}"})
//    public ResponseEntity<?> adhaarExtraction(@RequestPart("file") MultipartFile files,@RequestPart("fileMasked") Optional<MultipartFile> fileMasked ,HttpServletRequest requests, @PathVariable("leadId") long leadInformation, @PathVariable("subDocumentType") long subDocumentType,
//                                   @RequestParam(value = "typeOfDocument", required = false) String typeOfDocument, @PathVariable("latlng") Optional<String> latlng, @PathVariable("action") Optional<Integer> action) throws Exception {
//
//        Pair<SignzyExtractionResponse,Integer> response=null;
//        if (files!=null && subDocumentType==subDocumentPan){
//            response= signzyService.extractPan(files,leadInformation,subDocumentType,typeOfDocument,latlng);
//        }
//        else if (files!=null && subDocumentType==subDocumentGst){
//             response=  signzyService.extractGst(files,leadInformation,subDocumentType,typeOfDocument,latlng);
//
//        }
//        else if (files!=null && fileMasked.isPresent() && (subDocumentType==subDocumentAdhaarFront || subDocumentType==subDocumentAdhaarBack)){
//             response=  signzyService.extractAdhaar(files,fileMasked.get(),leadInformation,subDocumentType,typeOfDocument,latlng);
//        }
//        if (response!=null && response.getSecond()==0){
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",response.getFirst()),HttpStatus.OK);
//        } else if (response!=null && response.getSecond()==2) {
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,"Invalid Document"),HttpStatus.BAD_REQUEST);
//        }else  {
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT,"Something Went Wrong"),HttpStatus.CONFLICT);
//        }
//    }


    @PostMapping({"/extraction-signzy/{leadId}/{subDocumentType}", "/adhaar-extraction-signzy/{leadId}/{subDocumentType}/{action}"})
    public ResponseEntity<?> extractDocumentOcr(
            @RequestPart("file") MultipartFile files,
            @RequestPart("fileMasked") Optional<MultipartFile> fileUnMasked ,
            @RequestPart("fileMasked") Optional<MultipartFile> fileMasked,
            HttpServletRequest requests,
            @PathVariable("leadId") long leadInformation,
            @PathVariable("subDocumentType") long subDocumentType,
            @RequestParam(value = "typeOfDocument", required = false) String typeOfDocument,
            @PathVariable(value = "latlng", required = false) Optional<String> latlng,
            @PathVariable(value = "action", required = false) Optional<Integer> action) {

        try {
            Pair<SignzyExtractionResponse, Integer> response = null;

            // 1. Existing Extraction Service Calls
         if (files!=null && subDocumentType==subDocumentPan){
            response= signzyService.extractDocument(files,leadInformation,subDocumentType,typeOfDocument,latlng);
        }
        else if (files!=null && subDocumentType==subDocumentGst){
            response=  signzyService.extractDocument(files,leadInformation,subDocumentType,typeOfDocument,latlng);
        }
        else if (files!=null && fileUnMasked.isPresent() && (subDocumentType==subDocumentAdhaarFront || subDocumentType==subDocumentAdhaarBack)){
            response=  signzyService.extractAdhaarDocument(files,fileUnMasked.get(),leadInformation,subDocumentType,typeOfDocument,latlng);
        }
        else if (files!=null && (subDocumentType == subDocumentDrivingLicenseFront || subDocumentType == subDocumentDrivingLicenseBack)) {
            response= signzyService.extractDocument(files,leadInformation,subDocumentType,typeOfDocument,latlng);
        }
        else if (files!=null && (subDocumentType == subDocumentPassportFront || subDocumentType == subDocumentPassportBack)) {
            response= signzyService.extractDocument(files,leadInformation,subDocumentType,typeOfDocument,latlng);
        }
        else if (files!=null && (subDocumentType == subDocumentVoterIDFront || subDocumentType == subDocumentVoterIDBack)) {
            response= signzyService.extractDocument(files,leadInformation,subDocumentType,typeOfDocument,latlng);
        }

            // 2. Success Case (OCR Extracted Successfully)
            if (response != null && response.getSecond() == 0) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", response.getFirst()), HttpStatus.OK);
            }

            // 3. OCR Failed Case -> Do NOT block SO agent (Soft Fallback)
            logger.warn("OCR failed or invalid document for Lead ID: {}", leadInformation);
            SignzyExtractionData signzyExtractionData = signzyExtractionDataRepo.findByleadAndSubDocument(leadInformation,subDocumentType);
            if (signzyExtractionData == null){
                logger.info("in this is document is first time upload");
            }else {
                logger.info("in this is document already present now null all field");
                signzyExtractionData.setNumber(null);
                signzyExtractionData.setName(null);
                signzyExtractionData.setDob(null);
                signzyExtractionData.setAddress(null);
                signzyExtractionData.setFatherName(null);
                signzyExtractionData.setDateOfExpiry(null);
                signzyExtractionData.setDateOfIssue(null);
                signzyExtractionData.setDbaName(null);
                signzyExtractionDataRepo.save(signzyExtractionData);
            }
            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK,
                    "Sorry, OCR is not working. Please fill details manually",
                    new OcrFallbackResponse(false, true) // ocrSuccess = false, allowManualEntry = true
            ));

        } catch (Exception ex) {
            // 4. Any Exception / Signzy API Timeout -> Fallback to Manual Entry
            logger.error("Exception in OCR service for Lead ID: {}", leadInformation, ex);
            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK,
                    "Sorry, OCR is not working. Please fill details manually",
                    new OcrFallbackResponse(false, true)
            ));
        }
    }



}
