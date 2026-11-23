package com.bijlipay.service.implement;

import com.bijlipay.config.Constants;
import com.bijlipay.model.*;
import com.bijlipay.model.dto.*;
import com.bijlipay.model.dto.signzy.PennyDropDto;
import com.bijlipay.model.dto.signzy.contract.*;
import com.bijlipay.model.dto.signzy.digilocker.AadhaarResponseDTO;
import com.bijlipay.model.dto.signzy.digilocker.DigilockerResponseDTO;
import com.bijlipay.model.dto.signzy.digilocker.DigitalCertificateRequestDTO;
import com.bijlipay.model.dto.signzy.esign.EsignPdfResponseDto;
import com.bijlipay.model.dto.signzy.esign.SignaturesDto;
import com.bijlipay.model.dto.signzy.esign.SignzyAadhaarEsignRequestDto;
import com.bijlipay.model.dto.signzy.esign.SignzyAadhaarEsignResponseDto;
import com.bijlipay.model.dto.signzy.extraction.*;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchRequestDto;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchResponseDto;
import com.bijlipay.model.dto.signzy.identity.SignzyIdentityRequestDto;
import com.bijlipay.model.dto.signzy.identity.SignzyIdentityResponseDto;
import com.bijlipay.model.dto.signzy.pennydrop.*;
import com.bijlipay.model.dto.signzy.verification.EssentialDto;
import com.bijlipay.model.dto.signzy.verification.SignzyVerificationRequestDto;
import com.bijlipay.model.dto.signzy.verification.SignzyVerificationResponseDto;
import com.bijlipay.repository.*;
import com.bijlipay.service.DocumentService;
import com.bijlipay.service.FileStorageService;
import com.bijlipay.service.SignzyService;
import com.bijlipay.util.MailHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.core.io.ByteArrayResource;


@Service
public class SignzyServiceImpl implements SignzyService {
    private static final Logger logger = LoggerFactory.getLogger(SignzyServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;
    @Value("${signzy.base.url}")
    private String signzyBaseUrl;
    @Value("${signzy.username}")
    private String signzyUserName;
    @Value("${signzy.password}")
    private String signzyPassword;
    @Value("${signzy.esign.base.url}")
    private String signzyEsignBaseUrl;
    @Value("${signzy.file.upload.url}")
    private String signzyFileUploadUrl;

    @Value("${signzy.contract.base.url}")
    private String signzyContractBaseUrl;

    @Value("${contract.callback.url}")
    private String contractCallbackUrl;
    @Value("${contract.signer.callback.url}")
    private String contractSignerCallbackUrl;

    @Value("${signzy.360.contract.base.url}")
    private String signzy360ContractBaseUrl;

    @Value("${contract360.access.token}")
    private String contract360AccessToken;

    @Value("${contract360.signer.callback.url}")
    private String contract360SignerCallBackUrl;

    @Value("${contract360.callback.url}")
    private String contract360CallbackUrl;

    @Value("${digilocker.aadhar}")
    private String digiLockerAadhar;

    @Value("${verifai.extract.url}")
    private String verifaiExtractUrl;

    @Value("${Newextraction.access.token}")
    private String NewextractionAccessToken;

    @Value("${verifai.upload.url}")
    private String verifaiUploadUrl;

    @Autowired
    private PennyDropVerificationRepository pennyDropVerificationRepository;

    @Autowired
    private LeadInformationRepository leadInformationRepository;
    @Autowired
    private AgreementDetailsRepository agreementDetailsRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private MailHandler mailHandler;
    @Value("${esign.call.back}")
    private String esignCallBack;
    @Value("${esign.event.call.back}")
    private String esignEventCallBack;

    @Value("${logo.url}")
    private String logoUrl;
    @Autowired
    private LeadStatusRepository leadStatusRepository;
    @Value("${pull.contract}")
    private String pullContract;
    @Value("${signzy.adhaar.url}")
    private String signzyAdhaarUrl;
    @Value("${signzy.pan.url}")
    private String signzyPanUrl;
    @Value("${signzy.gst.url}")
    private String signzyGstUrl;

    @Value("${signzy.download.url}")
    private String signzyDownloadUrl;

    @Value("${extraction.access.token}")
    private String extractionAccessToken;

    @Value("${fetch.aadhaar}")
    private String fetchAadhaar;
    @Autowired
    private SignzyExtractionDataRepo signzyExtractionDataRepo;

    @Autowired
    private ByteArrayMultiPartFileServiceImpl byteArrayMultiPartFileService;
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public SignzyLoginResponseDto generateAccessToken() {
        String url = signzyBaseUrl + "patrons/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        SignzyLoginDto signzyLoginDto = new SignzyLoginDto();
        signzyLoginDto.setUsername(signzyUserName);
        signzyLoginDto.setPassword(signzyPassword);
        HttpEntity<SignzyLoginDto> entity = new HttpEntity(signzyLoginDto, headers);
        ResponseEntity<SignzyLoginResponseDto> result = restTemplate.exchange(url, HttpMethod.POST, entity, SignzyLoginResponseDto.class);
        if (result.hasBody()) {
            SignzyLoginResponseDto responseDto = new SignzyLoginResponseDto();
            responseDto.setId(result.getBody().getId());
            responseDto.setUserId(result.getBody().getUserId());
            return responseDto;
        }
        return null;
    }

    @Override
    public ResponseEntity<Object> deActivateAccessToken(String id) throws HttpClientErrorException {
        String url = signzyBaseUrl + "patrons/logout?access_token=" + id;
        ResponseEntity<Object> result = restTemplate.exchange(url, HttpMethod.POST, null, Object.class);
        return result;
    }

    @Override
    public Pair<Integer, SignzyIdentityResponseDto> signzyIdentification(SignzyIdentityRequestDto signzyIdentityRequestDto) throws JsonProcessingException, ParseException {

        ResponseEntity<String> out = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("authorization", signzyIdentityRequestDto.getAccessToken());

        SignzyIdentityRequestDto identityDto = new SignzyIdentityRequestDto();
        identityDto.setType(signzyIdentityRequestDto.getType() != null ? signzyIdentityRequestDto.getType() : "");
        identityDto.setEmail(signzyIdentityRequestDto.getEmail() != null ? signzyIdentityRequestDto.getEmail() : "");
        identityDto.setCallbackUrl(signzyIdentityRequestDto.getCallbackUrl() != null ? signzyIdentityRequestDto.getCallbackUrl() : "");
        identityDto.setImages(signzyIdentityRequestDto.getImages());

        String url = signzyBaseUrl + "patrons/" + signzyIdentityRequestDto.getPatronId() + "/identities";
        HttpEntity<SignzyIdentityRequestDto> entity = new HttpEntity<>(identityDto, httpHeaders);

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();

        SignzyIdentityResponseDto responseDto = new SignzyIdentityResponseDto();

        try {

            out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            logger.info("response from signzy for Identity -- : {}", out.getBody());

            responseDto = objectMapper.readValue(out.getBody(), SignzyIdentityResponseDto.class);
            logger.info("responseDto -- : {}", responseDto.getAccessToken());

            return Pair.of(1, responseDto);

        } catch (HttpClientErrorException httpClientErrorException) {

            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("httpClientErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);

            responseDto = objectMapper.readValue(response, SignzyIdentityResponseDto.class);

            return Pair.of(0, responseDto);

        } catch (HttpServerErrorException httpServerErrorException) {

            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("httpServerErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);

            return Pair.of(0, responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Pair.of(0, responseDto);
        }
    }

    @Override
    public Pair<Integer, Object> signzyExtraction(SignzyExtractionRequestDto signzyExtractionRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException {

        ResponseEntity<String> out = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("authorization", signzyExtractionRequestDto.getAccessToken());

        SignzyExtractionRequestDto extractionRequestDto = new SignzyExtractionRequestDto();
        extractionRequestDto.setService(signzyExtractionRequestDto.getService() == null ? "" : signzyExtractionRequestDto.getService());
        extractionRequestDto.setItemId(signzyExtractionRequestDto.getItemId() == null ? "" : signzyExtractionRequestDto.getItemId());
        extractionRequestDto.setTask(signzyExtractionRequestDto.getTask() == null ? "" : signzyExtractionRequestDto.getTask());
        extractionRequestDto.setAccessToken(signzyExtractionRequestDto.getAccessToken() == null ? "" : signzyExtractionRequestDto.getAccessToken());
        extractionRequestDto.setEssentials(signzyExtractionRequestDto.getEssentials() == null ? null : signzyExtractionRequestDto.getEssentials());

        HttpEntity<SignzyExtractionRequestDto> entity = new HttpEntity<>(extractionRequestDto, httpHeaders);
        String url = signzyBaseUrl + "snoops";

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (signzyExtractionRequestDto.getType().equalsIgnoreCase(Constants.IDENTITY_TYPE_AADHAAR)) {

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Extraction response -- : {}", out.getBody());
                AadhaarExtractionResponseDto aadhaarResponseDto = objectMapper.readValue(out.getBody(), AadhaarExtractionResponseDto.class);
                logger.info("SignzyExtractionResponseDto -- : {} ", aadhaarResponseDto);

                return Pair.of(1, aadhaarResponseDto.getResponse().getResult());

            } else if (signzyExtractionRequestDto.getType().equalsIgnoreCase(Constants.IDENTITY_TYPE_INDIVIDUAL_PAN) || signzyExtractionRequestDto.getType().equalsIgnoreCase(Constants.IDENTITY_TYPE_BUSINESS_PAN)) {
                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Extraction response -- : {}", out.getBody());
                PanExtractionResponseDto panResponseDto = objectMapper.readValue(out.getBody(), PanExtractionResponseDto.class);
                logger.info("SignzyExtractionResponseDto -- : {} ", panResponseDto);

                return Pair.of(1, panResponseDto.getResponse().getResult());

            } else if (signzyExtractionRequestDto.getType().equalsIgnoreCase(Constants.IDENTITY_TYPE_PASSPORT)) {
                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Extraction response -- : {}", out.getBody());
                PassportExtractionResponseDto passportResponseDto = objectMapper.readValue(out.getBody(), PassportExtractionResponseDto.class);
                logger.info("SignzyExtractionResponseDto -- : {} ", passportResponseDto);

                return Pair.of(1, passportResponseDto.getResponse().getResult());

            } else if (signzyExtractionRequestDto.getType().equalsIgnoreCase(Constants.IDENTITY_TYPE_DRIVING_LICENCE)) {
                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Extraction response -- : {}", out.getBody());
                DrivingLicenceExtractionResponseDto dLResponseDto = objectMapper.readValue(out.getBody(), DrivingLicenceExtractionResponseDto.class);
                logger.info("SignzyExtractionResponseDto -- : {} ", dLResponseDto);

                return Pair.of(1, dLResponseDto.getResponse().getResult());

            } else if (signzyExtractionRequestDto.getType().equalsIgnoreCase(Constants.IDENTITY_TYPE_VOTER_ID)) {
                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Extraction response -- : {}", out.getBody());
                VoterIdExtractionResponseDto voterIdResponseDto = objectMapper.readValue(out.getBody(), VoterIdExtractionResponseDto.class);
                logger.info("SignzyExtractionResponseDto -- : {} ", voterIdResponseDto);

                return Pair.of(1, voterIdResponseDto.getResponse().getResult());
            }
        } catch (HttpClientErrorException httpClientErrorException) {

            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("httpClientErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpClientErrorException  -- : {}", jsonObject);

            return Pair.of(0, jsonObject);

        } catch (HttpServerErrorException httpServerErrorException) {
            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("httpServerErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpServerErrorException  -- : {}", jsonObject);

            return Pair.of(0, jsonObject);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return Pair.of(0, e.getMessage());
        }
        return Pair.of(0, "");
    }

    @Override
    public Pair<Integer, SignzyVerificationResponseDto> signzyVerification(SignzyVerificationRequestDto signzyVerificationRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException, JsonProcessingException {

        ResponseEntity<String> out = null;
        SignzyVerificationResponseDto responseDto = new SignzyVerificationResponseDto();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("authorization", signzyVerificationRequestDto.getAccessToken());

        String url = null;
        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();

//        SignzyExtractionAndVerification signzyVerification = new SignzyExtractionAndVerification();
//        SignzyExtractionAndVerification extractionAndVerification = signzyExtractionAndVerificationRepository.findByDocumentId(uploadedDocuments.get(0));

        SignzyVerificationRequestDto requestDto = new SignzyVerificationRequestDto();
        requestDto.setService(signzyVerificationRequestDto.getService() == null ? "" : signzyVerificationRequestDto.getService());
        requestDto.setItemId(signzyVerificationRequestDto.getItemId() == null ? "" : signzyVerificationRequestDto.getItemId());
        requestDto.setAccessToken(signzyVerificationRequestDto.getAccessToken() == null ? "" : signzyVerificationRequestDto.getAccessToken());
        requestDto.setTask(signzyVerificationRequestDto.getTask() == null ? "" : signzyVerificationRequestDto.getTask());

        try {
            EssentialDto essentialDto = new EssentialDto();

            if (signzyVerificationRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {
                essentialDto.setUid(signzyVerificationRequestDto.getEssentials().getUid() == null ? "" : signzyVerificationRequestDto.getEssentials().getUid());
                requestDto.setEssentials(essentialDto);

                HttpEntity<SignzyVerificationRequestDto> entity = new HttpEntity<>(requestDto, httpHeaders);
                url = signzyBaseUrl + "snoops";

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Aadhaar Verification response out -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyVerificationResponseDto.class);
                logger.info("Aadhaar Signzy Verification result -- : {}", responseDto.getResponse().getResult());

                return Pair.of(1, responseDto);

            } else if (signzyVerificationRequestDto.getType().equalsIgnoreCase(Constants.IDENTITY_TYPE_INDIVIDUAL_PAN)) {
                essentialDto.setNumber(signzyVerificationRequestDto.getEssentials().getNumber() == null ? "" : signzyVerificationRequestDto.getEssentials().getNumber());
                essentialDto.setName(signzyVerificationRequestDto.getEssentials().getName() == null ? "" : signzyVerificationRequestDto.getEssentials().getName());
                essentialDto.setFuzzy(signzyVerificationRequestDto.getEssentials().getFuzzy() == null ? "" : signzyVerificationRequestDto.getEssentials().getFuzzy());
                essentialDto.setPanStatus(signzyVerificationRequestDto.getEssentials().getPanStatus() == null ? "" : signzyVerificationRequestDto.getEssentials().getPanStatus());
                requestDto.setEssentials(essentialDto);

                HttpEntity<SignzyVerificationRequestDto> entity = new HttpEntity<>(requestDto, httpHeaders);
                url = signzyBaseUrl + "snoops";

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("IndividualPan Verification response out -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyVerificationResponseDto.class);
                logger.info("IndividualPan Signzy Verification result -- : {}", responseDto.getResponse().getResult());

                return Pair.of(1, responseDto);

            } else if (signzyVerificationRequestDto.getType().equalsIgnoreCase(Constants.IDENTITY_TYPE_BUSINESS_PAN)) {
                essentialDto.setNumber(signzyVerificationRequestDto.getEssentials().getNumber() == null ? "" : signzyVerificationRequestDto.getEssentials().getNumber());
                essentialDto.setName(signzyVerificationRequestDto.getEssentials().getName() == null ? "" : signzyVerificationRequestDto.getEssentials().getName());
                essentialDto.setPanStatus(signzyVerificationRequestDto.getEssentials().getPanStatus() == null ? "" : signzyVerificationRequestDto.getEssentials().getPanStatus());
                requestDto.setEssentials(essentialDto);

                HttpEntity<SignzyVerificationRequestDto> entity = new HttpEntity<>(requestDto, httpHeaders);
                url = signzyBaseUrl + "snoops";

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("BusinessPan Verification response out -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyVerificationResponseDto.class);
                logger.info("BusinessPan Signzy Verification result -- : {}", responseDto.getResponse().getResult());

                return Pair.of(1, responseDto);

            } else if (signzyVerificationRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_PASSPORT)) {
                essentialDto.setName(signzyVerificationRequestDto.getEssentials().getName() == null ? "" : signzyVerificationRequestDto.getEssentials().getName());
                essentialDto.setDob(signzyVerificationRequestDto.getEssentials().getDob() == null ? "" : signzyVerificationRequestDto.getEssentials().getDob());
                essentialDto.setFuzzy(signzyVerificationRequestDto.getEssentials().getFuzzy() == null ? "" : signzyVerificationRequestDto.getEssentials().getFuzzy());
                essentialDto.setFileNumber(signzyVerificationRequestDto.getEssentials().getFileNumber() == null ? "" : signzyVerificationRequestDto.getEssentials().getFileNumber());
                requestDto.setEssentials(essentialDto);

                HttpEntity<SignzyVerificationRequestDto> entity = new HttpEntity<>(requestDto, httpHeaders);
                url = signzyBaseUrl + "patrons/" + signzyVerificationRequestDto.getItemId() + "/passportV2";

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Passport Verification response out -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyVerificationResponseDto.class);
                logger.info("Passport Signzy Verification result -- : {}", responseDto);

                return Pair.of(1, responseDto);

            } else if (signzyVerificationRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_DRIVING_LICENCE)) {
                essentialDto.setNumber(signzyVerificationRequestDto.getEssentials().getNumber() == null ? "" : signzyVerificationRequestDto.getEssentials().getNumber());
                essentialDto.setDob(signzyVerificationRequestDto.getEssentials().getDob() == null ? "" : signzyVerificationRequestDto.getEssentials().getDob());
                essentialDto.setIssueDate(signzyVerificationRequestDto.getEssentials().getIssueDate() == null ? "" : signzyVerificationRequestDto.getEssentials().getIssueDate());
                requestDto.setEssentials(essentialDto);

                HttpEntity<SignzyVerificationRequestDto> entity = new HttpEntity<>(requestDto, httpHeaders);
                url = signzyBaseUrl + "patrons/" + signzyVerificationRequestDto.getItemId() + "/drivingLicenceV2";

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("DrivingLicence Verification response out -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyVerificationResponseDto.class);
                logger.info("DrivingLicence Signzy Verification result -- : {}", responseDto);

                return Pair.of(1, responseDto);

            } else if (signzyVerificationRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_VOTER_ID)) {
                essentialDto.setEpicNumber(signzyVerificationRequestDto.getEssentials().getEpicNumber() == null ? "" : signzyVerificationRequestDto.getEssentials().getEpicNumber());
                essentialDto.setName(signzyVerificationRequestDto.getEssentials().getName() == null ? "" : signzyVerificationRequestDto.getEssentials().getName());
                essentialDto.setState(signzyVerificationRequestDto.getEssentials().getState() == null ? "" : signzyVerificationRequestDto.getEssentials().getState());
                requestDto.setEssentials(essentialDto);

                HttpEntity<SignzyVerificationRequestDto> entity = new HttpEntity<>(requestDto, httpHeaders);
                url = signzyBaseUrl + "patrons/" + signzyVerificationRequestDto.getItemId() + "/voterIdV2";


                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("VoterId Verification response out -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyVerificationResponseDto.class);
                logger.info("VoterId Signzy Verification result -- : {}", responseDto);

                return Pair.of(1, responseDto);
            }
        } catch (HttpClientErrorException httpClientErrorException) {

            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("httpClientErrorException -- : {} ", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpClientErrorException -- : {}", jsonObject);

            responseDto = objectMapper.readValue(response, SignzyVerificationResponseDto.class);

            return Pair.of(0, responseDto);

        } catch (HttpServerErrorException httpServerErrorException) {

            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("httpServerErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpServerErrorException -- : {}", jsonObject);

            return Pair.of(0, responseDto);

        } catch (Exception e) {
            logger.info("exception -- : {}", e.getMessage());
            return Pair.of(0, responseDto);
        }
        return Pair.of(0, responseDto);
    }

    @Override
    public Pair<Integer, SignzyFetchResponseDto> signzyFetch(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException, IOException {

        ResponseEntity<String> out = null;
        String url = null;
        SignzyFetchResponseDto responseDto = new SignzyFetchResponseDto();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("authorization", signzyFetchRequestDto.getAccessToken());

        JSONObject jsonObject;

        JSONParser jsonParser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();

        SignzyFetchRequestDto requestDto = new SignzyFetchRequestDto();

        try {
            if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_GST_IN)) {

                url = signzyBaseUrl + "/patrons/" + signzyFetchRequestDto.getUserToken() + "/gstns";

                requestDto.setTask(signzyFetchRequestDto.getTask() == null ? "" : signzyFetchRequestDto.getTask());
                requestDto.setEssentials(signzyFetchRequestDto.getEssentials() == null ? null : signzyFetchRequestDto.getEssentials());

                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(requestDto, headers);

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("GSTIN response out -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyFetchResponseDto.class);
                return Pair.of(1, responseDto);

            } else if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_PAN)) {

                url = signzyBaseUrl + "/patrons/" + signzyFetchRequestDto.getUserToken() + "/panv2";

                requestDto.setTask(signzyFetchRequestDto.getTask() == null ? "" : signzyFetchRequestDto.getTask());
                requestDto.setEssentials(signzyFetchRequestDto.getEssentials() == null ? null : signzyFetchRequestDto.getEssentials());

                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(requestDto, headers);

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Pan response out -- : {}", out.getBody());

                responseDto = objectMapper.readValue(out.getBody(), SignzyFetchResponseDto.class);
                return Pair.of(1, responseDto);

            } else if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_DRIVING_LICENCE)) {

                url = signzyBaseUrl + "/patrons/" + signzyFetchRequestDto.getUserToken() + "/drivingLicenceV2";

                requestDto.setTask(signzyFetchRequestDto.getTask() == null ? "" : signzyFetchRequestDto.getTask());
                requestDto.setEssentials(signzyFetchRequestDto.getEssentials() == null ? null : signzyFetchRequestDto.getEssentials());

                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(requestDto, headers);

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("Driving License details -- : {}", out.getBody());

                responseDto = objectMapper.readValue(out.getBody(), SignzyFetchResponseDto.class);
                return Pair.of(1, responseDto);

            } else if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_PASSPORT)) {

                url = signzyBaseUrl + "/patrons/" + signzyFetchRequestDto.getUserToken() + "/passportV2";

                requestDto.setTask(signzyFetchRequestDto.getTask() == null ? "" : signzyFetchRequestDto.getTask());
                requestDto.setEssentials(signzyFetchRequestDto.getEssentials() == null ? null : signzyFetchRequestDto.getEssentials());

                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(requestDto, headers);

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("pass port details   -- : {}", out.getBody());

                responseDto = objectMapper.readValue(out.getBody(), SignzyFetchResponseDto.class);
                return Pair.of(1, responseDto);

            } else if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_VOTER_ID)) {

                url = signzyBaseUrl + "/patrons/" + signzyFetchRequestDto.getUserToken() + "/voterIdV2";

                requestDto.setTask(signzyFetchRequestDto.getTask() == null ? "" : signzyFetchRequestDto.getTask());
                requestDto.setEssentials(signzyFetchRequestDto.getEssentials() == null ? null : signzyFetchRequestDto.getEssentials());

                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(requestDto, headers);

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("voter Id details --- : {}", out.getBody());

                responseDto = objectMapper.readValue(out.getBody(), SignzyFetchResponseDto.class);
                return Pair.of(1, responseDto);
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("httpClientErrorException String response -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpClientErrorException response -- : {}", jsonObject);

            responseDto = objectMapper.readValue(response, SignzyFetchResponseDto.class);

            return Pair.of(0, responseDto);
        } catch (HttpServerErrorException httpServerErrorException) {
            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("httpServerErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            return Pair.of(0, responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception -- : {}", e.getMessage());
            return Pair.of(0, responseDto);
        }
        return Pair.of(0, responseDto);
    }

    @Override
    public Pair<Integer, Object> signzyEsign(int action, String eSignName, MultipartFile multipartFile, AgreementDetails agreementDetails, int pageCount) throws JsonProcessingException, ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        SignzyLoginResponseDto loginResponse = new SignzyLoginResponseDto();
        try {
            // eSign login part
            logger.info("eSign login part ---:");
            loginResponse = this.generateEsignAccessToken();
            logger.info("login Response -- : {}", loginResponse);
            logger.info("user id -- {}", loginResponse.getUserId());
            logger.info("id -- {}", loginResponse.getId());

            if (loginResponse.getId() != null) {

                //generate pdf url
                logger.info("eSign pdf url part --- :");

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

                // aadhaar esign
                if (pdfResponseDto.getFile().getDirectURL() != null) {
                    logger.info("esignpart --- : ");

                    url = signzyEsignBaseUrl + loginResponse.getUserId() + "/aadhaaresigns";

                    httpHeaders.set("Content-Type", "application/json");
                    httpHeaders.set("Authorization", loginResponse.getId());

                    SignzyAadhaarEsignRequestDto esignRequestDto = new SignzyAadhaarEsignRequestDto();
                    esignRequestDto.setTask("url");
                    esignRequestDto.setUid("");
                    esignRequestDto.setMultiPages("true");
                    esignRequestDto.setName(eSignName);
                    esignRequestDto.setInputFile(pdfResponseDto.getFile().getDirectURL());
//                    esignRequestDto.setCallbackUrl("https://webhook.site/84d6b903-7388-42e0-91cb-f5264d140844");
//                    esignRequestDto.setRedirectUrl("https://webhook.site/84d6b903-7388-42e0-91cb-f5264d140844");
//                    esignRequestDto.setEventCallbackUrl("https://webhook.site/84d6b903-7388-42e0-91cb-f5264d140844");

                    esignRequestDto.setCallbackUrl(esignCallBack);
                    esignRequestDto.setEventCallbackUrl(esignEventCallBack);
                    List<SignaturesDto> signaturesList = new ArrayList<>();

                    if (action == Constants.AADHAAR_ESIGN) {
                        esignRequestDto.setSignatureType("aadhaaresign");

                        SignaturesDto signaturesDto1 = new SignaturesDto();

                        List<Integer> pageNo = new ArrayList<>();
                        if (pageCount != 0) {
                            for (int i = 1; i <= pageCount; i++) {
                                pageNo.add(i);
                            }
                            logger.info("pageNumber----------:{}", pageNo);
                            signaturesDto1.setPageNo(pageNo);
                        } else {
                            if (agreementDetails.getLeadInformation().getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && agreementDetails.getLeadInformation().getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                            } else if (agreementDetails.getLeadInformation().getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || agreementDetails.getLeadInformation().getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                            } else {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
                            }
                        }

                        signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto1.setHeight(20);
                        signaturesDto1.setWidth(60);
                        signaturesDto1.setxCoordinate(Arrays.asList(470, 470));
                        signaturesDto1.setyCoordinate(Arrays.asList(53, 53));
                        signaturesList.add(signaturesDto1);

                        SignaturesDto signaturesDto2 = new SignaturesDto();
                        signaturesDto2.setPageNo(List.of(3));
                        signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto2.setHeight(20);
                        signaturesDto2.setWidth(60);
                        signaturesDto2.setxCoordinate(Arrays.asList(480, 480));
                        signaturesDto2.setyCoordinate(Arrays.asList(480, 480));
                        signaturesList.add(signaturesDto2);

                        esignRequestDto.setSignatures(signaturesList);

                        logger.info("Signature Type -- : {}", esignRequestDto.getSignatureType());
                    } else {

                        esignRequestDto.setSignatureType("esign");

                        SignaturesDto signaturesDto1 = new SignaturesDto();

                        List<Integer> pageNo = new ArrayList<>();
                        if (pageCount != 0) {
                            for (int i = 1; i <= pageCount; i++) {
                                pageNo.add(i);
                            }
                            logger.info("pageNumber----------:{}", pageNo);
                            signaturesDto1.setPageNo(pageNo);
                        } else {
                            if (agreementDetails.getLeadInformation().getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && agreementDetails.getLeadInformation().getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                            } else if (agreementDetails.getLeadInformation().getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || agreementDetails.getLeadInformation().getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                            } else {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                            }
                        }
                        signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto1.setHeight(20);
                        signaturesDto1.setWidth(60);
                        signaturesDto1.setxCoordinate(Arrays.asList(450, 450));
                        signaturesDto1.setyCoordinate(Arrays.asList(53, 53));
                        signaturesList.add(signaturesDto1);

                        SignaturesDto signaturesDto2 = new SignaturesDto();
                        signaturesDto2.setPageNo(List.of(2));
                        signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto2.setHeight(20);
                        signaturesDto2.setWidth(60);
                        signaturesDto2.setxCoordinate(Arrays.asList(470, 470));
                        signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                        signaturesList.add(signaturesDto2);

                        SignaturesDto signaturesDto3 = new SignaturesDto();
                        signaturesDto3.setPageNo(List.of(3));
                        signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto3.setHeight(20);
                        signaturesDto3.setWidth(60);
                        signaturesDto3.setxCoordinate(Arrays.asList(480, 480));
                        signaturesDto3.setyCoordinate(Arrays.asList(480, 480));
                        signaturesList.add(signaturesDto3);

                        SignaturesDto signaturesDto4 = new SignaturesDto();
                        signaturesDto4.setPageNo(List.of(3));
                        signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto4.setHeight(20);
                        signaturesDto4.setWidth(60);
                        signaturesDto4.setxCoordinate(Arrays.asList(450, 450));
                        signaturesDto4.setyCoordinate(Arrays.asList(43, 43));
                        signaturesList.add(signaturesDto4);

                        esignRequestDto.setSignatures(signaturesList);

                        logger.info("Signature Type -- : {}", esignRequestDto.getSignatureType());

//                        esignRequestDto.setSignatureType("esign");
//
//                        SignaturesDto signaturesDto1 = new SignaturesDto();
//                        signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
//                        signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
//                        signaturesDto1.setHeight(10);
//                        signaturesDto1.setWidth(40);
//                        signaturesDto1.setxCoordinate(Arrays.asList(60, 60));
//                        signaturesDto1.setyCoordinate(Arrays.asList(35, 35));
//                        signaturesList.add(signaturesDto1);
//
//                        SignaturesDto signaturesDto2 = new SignaturesDto();
//                        signaturesDto2.setPageNo(List.of(3));
//                        signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
//                        signaturesDto2.setHeight(10);
//                        signaturesDto2.setWidth(40);
//                        signaturesDto2.setxCoordinate(Arrays.asList(140, 140));
//                        signaturesDto2.setyCoordinate(Arrays.asList(335, 335));
//                        signaturesList.add(signaturesDto2);
//
//                        esignRequestDto.setSignatures(signaturesList);
//
//                        logger.info("Signature Type -- : {}", esignRequestDto.getSignatureType());
                    }
                    HttpEntity<SignzyAadhaarEsignRequestDto> httpEntity = new HttpEntity<>(esignRequestDto, httpHeaders);

                    Gson gson = new Gson();
                    String request = gson.toJson(esignRequestDto);
                    logger.info("esign request Dto ------------ : {}", request);

                    out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                    logger.info("eSign Response out -- : {}", out.getBody());

                    SignzyAadhaarEsignResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyAadhaarEsignResponseDto.class);
                    logger.info("eSign responseDto -- : {}", responseDto);

                    agreementDetails.setToken(responseDto.getResult().getToken());
                    agreementDetails.setSignatureType(responseDto.getSignatureType());
                    agreementDetails.setUrl(responseDto.getResult().getUrl());
                    agreementDetails.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                    agreementDetails.setResponse(false);
                    AgreementDetails details = agreementDetailsRepository.save(agreementDetails);
                    logger.info("agreement Details -- : {}", details);

                    if (responseDto.getResult().getUrl() != null) {  // check whether the url is present or not
                        //trigger sms to Merchant
//                        this.sendSmsToMerchantForAggrementSign(details);
//                        this.sendSmsToMerchantForAgreementSign(details);
                        String template = "Congratulations! Your agreement is ready. Please click on this link " + details.getUrl() + " to digitally sign to complete the process. Team Bijlipay";
                        String templateId = "1107170487192856206";
                        this.sms(template, details.getLeadInformation().getContactNumber(), templateId);

                        //trigger mail to merchant
                        Pair<Integer, String> mailResponse = mailHandler.sendAgreementUrlToMerchant(agreementDetails.getLeadInformation().getContactEmail(), responseDto.getResult().getUrl());
                        logger.info("Mail send successfully to merchant---: {}", agreementDetails.getLeadInformation().getContactEmail());
                        if (mailResponse.getFirst() == 0) {
                            return Pair.of(0, mailResponse.getSecond());
                        }
                        ResponseEntity<Object> responseEntity = this.deActivateEsignAccessToken(loginResponse.getId());
                        logger.info("Logout --- : {}", responseEntity.getBody());
                        return Pair.of(1, responseDto);
                    } else {
                        return Pair.of(0, "URL not found");
                    }
//                    return Pair.of(1, responseDto);
                }
                ResponseEntity<Object> responseEntity = this.deActivateEsignAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
                return Pair.of(0, "pdf url not generated");
            }
            return Pair.of(0, "eSign login failed");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateEsignAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }
            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateEsignAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateEsignAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }

            return Pair.of(0, e.getMessage());
        }
    }

    @Override
    public void sendSmsToMerchantForAgreementSign(AgreementDetails details) {
        try {
            LeadInformation lead = leadInformationRepository.findByLeadId(details.getLeadInformation().getLeadId());
            logger.info("The lead is {}", lead);
            String a = lead.getMerchantName();
            String helpline = "18004200235";
            logger.info("the string a is {}..", a);
            logger.info("the data is {}{}{}", lead.getMerchantName(), lead.getContactNumber());
            String data = "";
            data += "method=sendMessage";
            data += "&userid=2000111180"; // your loginId
            data += "&password=" + URLEncoder.encode("abc123", "UTF-8"); // your password
            data += "&msg=" + URLEncoder.encode("Congratulations! Your agreement is ready. Please click on this link " + details.getUrl() + " to digitally sign to complete the process. Team Bijlipay", "UTF-8");

            logger.info(("The meassage is..{}" + data));
            data += "&send_to=" + URLEncoder.encode(lead.getContactNumber(), "UTF-8"); // a valid 10 digit phone no.
            logger.info("The send no is {}" + data);
            data += "&v=1.1";
            data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
            data += "&auth_scheme=PLAIN";
            logger.info("SND SMS : ------------- : {}", "https://enterprise.smsgupshup.com/GatewayAPI/rest?" + data);
            URL url = new URL("https://enterprise.smsgupshup.com/GatewayAPI/rest?" + data);
            logger.info("The url is {}.." + data);
            logger.info("SMS Gupshup URL Triggered");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            rd.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SignzyLoginResponseDto generateEsignAccessToken() {
        String url = signzyEsignBaseUrl + "login";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        SignzyLoginDto signzyLoginDto = new SignzyLoginDto();
        signzyLoginDto.setUsername(signzyUserName);
        signzyLoginDto.setPassword(signzyPassword);
        HttpEntity<SignzyLoginDto> entity = new HttpEntity(signzyLoginDto, headers);
        ResponseEntity<SignzyLoginResponseDto> result = restTemplate.exchange(url, HttpMethod.POST, entity, SignzyLoginResponseDto.class);
        if (result.hasBody()) {
            SignzyLoginResponseDto responseDto = new SignzyLoginResponseDto();
            responseDto.setId(result.getBody().getId());
            responseDto.setUserId(result.getBody().getUserId());
            return responseDto;
        }
        return null;
    }

    @Override
    public ResponseEntity<Object> deActivateEsignAccessToken(String id) throws HttpClientErrorException {
        String url = signzyEsignBaseUrl + "logout?access_token=" + id;
        ResponseEntity<Object> result = restTemplate.exchange(url, HttpMethod.POST, null, Object.class);
        return result;
    }

    @Override
    public Pair<Integer, Object> signzyAccountTransfer(LeadInformation leadInformation, AccountTransferRequestDto accountTransferRequestDto) throws JsonProcessingException, ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        Gson gson = new Gson();
        PennyDropDto result = new PennyDropDto();


//        PennyDropVerification pennyDropVerification = new PennyDropVerification();
//        PennyDropVerification pennyDrop =  pennyDropVerificationRepository.findByLeadId(leadInformation);
//        if(pennyDrop == null){
//            pennyDropVerification.setLeadInformation(leadInformation);
//            pennyDropVerification.setAccountNumber(accountTransferRequestDto.getEssentials().getBeneficiaryAccount());
//            pennyDropVerification.setIfsc(accountTransferRequestDto.getEssentials().getBeneficiaryIFSC());
//            pennyDropVerification = pennyDropVerificationRepository.save(pennyDropVerification);
//        }else {
//            pennyDropVerification = pennyDrop;
//        }

        PennyDropVerification pennyDrop = new PennyDropVerification();
        pennyDrop.setLeadInformation(leadInformation);
        pennyDrop.setAccountNumber(accountTransferRequestDto.getEssentials().getBeneficiaryAccount());
        pennyDrop.setIfsc(accountTransferRequestDto.getEssentials().getBeneficiaryIFSC());
        PennyDropVerification pennyDropVerification = pennyDropVerificationRepository.save(pennyDrop);

        try {
            // login part
            logger.info("login part -- :");
            SignzyLoginResponseDto loginResponseDto = this.generateAccessToken();
            logger.info("loginResponse -- : {}", loginResponseDto);
            //Account Transfer Part
            if (loginResponseDto.getId() != null) {
                logger.info("Account Transfer Part -- : ");
                url = signzyBaseUrl + "patrons/" + loginResponseDto.getUserId() + "/bankaccountverifications";
                httpHeaders.set("Content-type", "application/json");
                httpHeaders.set("Authorization", loginResponseDto.getId());
                AccountTransferRequestDto requestDto = new AccountTransferRequestDto();
                requestDto.setTask("bankTransfer");
                requestDto.setEssentials(accountTransferRequestDto.getEssentials());
                HttpEntity<AccountTransferRequestDto> httpEntity = new HttpEntity<>(requestDto, httpHeaders);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("account Transfer Response out -- : {}", out.getBody());
                AccountTransferResponseDto responseDto = objectMapper.readValue(out.getBody(), AccountTransferResponseDto.class);
                logger.info("Account Transfer response -- : {}", responseDto);

                //Verification Part
                if (responseDto.getResult() != null) {

                    pennyDropVerification.setActive(responseDto.getResult().getActive());
                    pennyDropVerification.setReason(responseDto.getResult().getReason());
                    pennyDropVerification.setSignzyReferencedId(responseDto.getResult().getSignzyReferenceId());
                    pennyDropVerification.setAccountTransferDetails(gson.toJson(responseDto.getResult()));
                    if (responseDto.getResult().getBankTransfer() != null) {
                        pennyDropVerification.setResponse(responseDto.getResult().getBankTransfer().getResponse());
                        pennyDropVerification.setName(responseDto.getResult().getBankTransfer().getBeneName());
                    }
                    pennyDropVerification = pennyDropVerificationRepository.save(pennyDropVerification);

////
//                    VerifyTransferRequestDto verifyTransferRequestDto = new VerifyTransferRequestDto();
//                    VerifyTransferEssentialDto essentialDto = new VerifyTransferEssentialDto();
//
//                    essentialDto.setAmount("1.00");
//                    essentialDto.setSignzyId(responseDto.getResult().getSignzyReferenceId());
//
//                    verifyTransferRequestDto.setTask("verifyAmount");
//                    verifyTransferRequestDto.setEssentials(essentialDto);
//
//
//                    HttpEntity<VerifyTransferRequestDto> entity = new HttpEntity<>(verifyTransferRequestDto, httpHeaders);
//                    out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//                    logger.info("Verify Transfer Response out -- : {}", out.getBody());
//                    VerifyTransferResponseDto verifyResponse = objectMapper.readValue(out.getBody(), VerifyTransferResponseDto.class);
//                    logger.info("verifyResponse -- : {}", verifyResponse);
//
//                    if (verifyResponse.getResult() != null) {
//                        if (verifyResponse.getResult().getAmountMatch().equalsIgnoreCase(Constants.PENNY_DROP_AMOUNT_MATCHED_SUCCESS)) {
//                            LeadInformation leadInfo = leadInformationRepository.findByLeadId(leadInformation.getLeadId());
//                            if (leadInfo != null) {
//                                leadInfo.setPennyDropVerified(true);
//                                leadInformationRepository.save(leadInfo);
//                            }
//                        }
//                        pennyDropVerification.setAmountMatch(verifyResponse.getResult().getAmountMatch());
//                        pennyDropVerification.setVerifyTransferDetails(gson.toJson(verifyResponse.getResult()));
//                        pennyDropVerification = pennyDropVerificationRepository.save(pennyDropVerification);
//                    }
////
                    result = this.getPennyDropValues(pennyDropVerification);
                    // sign out part
                    logger.info("sign out part -- :");
                    ResponseEntity<Object> responseEntity = this.deActivateAccessToken(loginResponseDto.getId());
                    logger.info("sign out response -- : {}", responseEntity);
                    return Pair.of(1, result);
                }
                // sign out part
                logger.info("sign out part -- :");
                ResponseEntity<Object> responseEntity = this.deActivateAccessToken(loginResponseDto.getId());
                logger.info("sign out response -- : {}", responseEntity);
                return Pair.of(0, result);
            }
            return Pair.of(0, "");
        } catch (HttpClientErrorException httpClientErrorException) {
            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("ClientErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpClientErrorException -- : {}", jsonObject);
            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());
            return Pair.of(0, "");
        }
    }

    private PennyDropDto getPennyDropValues(PennyDropVerification pennyDropVerification) {
        PennyDropDto pennyDropDto = new PennyDropDto();
        pennyDropDto.setActive(pennyDropVerification.getActive());
        pennyDropDto.setReason(pennyDropVerification.getReason());
        pennyDropDto.setAmountMatch(pennyDropVerification.getAmountMatch());
        pennyDropDto.setName(pennyDropVerification.getName());
        return pennyDropDto;
    }

    @Override
    public Pair<Integer, DigilockerResponseDTO> digiLockerNew(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException, JsonProcessingException {
        ResponseEntity<String> out = null;
        String url = null;
        SignzyFetchResponseDto responseDto = new SignzyFetchResponseDto();
        DigilockerResponseDTO digilockerResponseDTO = new DigilockerResponseDTO();

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("authorization", signzyFetchRequestDto.getAccessToken());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",contract360AccessToken);
        headers.set("x-client-unique-id", "eniyan@bijlipay.co.in");
        headers.set("Content-Type", "application/json");

        JSONObject jsonObject;

        JSONParser jsonParser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();

        SignzyFetchRequestDto requestDto = new SignzyFetchRequestDto();

        try {
            if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {

//                url = signzyBaseUrl + "/patrons/" + signzyFetchRequestDto.getUserToken() + "/digilockers";
//                url = "https://api-preproduction.signzy.app/api/v3/digilocker/createUrl";

//                requestDto.setTask(signzyFetchRequestDto.getTask() == null ? "" : signzyFetchRequestDto.getTask());
//                requestDto.setEssentials(signzyFetchRequestDto.getEssentials() == null ? null : signzyFetchRequestDto.getEssentials());
                Gson gson = new Gson();
                logger.info("ggssooonnn---{}", gson.toJson(requestDto));
                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(requestDto, headers);
//                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(headers);

                out = restTemplate.exchange(digiLockerAadhar, HttpMethod.POST, entity, String.class);
                logger.info("out of digi locker -- : {}", out.getBody());
//                responseDto = objectMapper.readValue(out.getBody(), SignzyFetchResponseDto.class);
//                return Pair.of(1, responseDto);
                digilockerResponseDTO = objectMapper.readValue(out.getBody(), DigilockerResponseDTO.class);
                return Pair.of(1, digilockerResponseDTO);
            }

        } catch (HttpClientErrorException httpClientErrorException) {
            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("httpClientErrorException String response -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpClientErrorException response -- : {}", jsonObject);

            responseDto = objectMapper.readValue(response, SignzyFetchResponseDto.class);

            return Pair.of(0, digilockerResponseDTO);
        } catch (HttpServerErrorException httpServerErrorException) {
            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("httpServerErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            return Pair.of(0, digilockerResponseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception -- : {}", e.getMessage());
            return Pair.of(0, digilockerResponseDTO);
        }
        return Pair.of(0, digilockerResponseDTO);
    }

    @Override
    public Pair<Integer, SignzyFetchResponseDto> digiLocker(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException, JsonProcessingException {
        ResponseEntity<String> out = null;
        String url = null;
        SignzyFetchResponseDto responseDto = new SignzyFetchResponseDto();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("authorization", signzyFetchRequestDto.getAccessToken());

        JSONObject jsonObject;

        JSONParser jsonParser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();

        SignzyFetchRequestDto requestDto = new SignzyFetchRequestDto();

        try {
            if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {

                url = signzyBaseUrl + "/patrons/" + signzyFetchRequestDto.getUserToken() + "/digilockers";

                requestDto.setTask(signzyFetchRequestDto.getTask() == null ? "" : signzyFetchRequestDto.getTask());
                requestDto.setEssentials(signzyFetchRequestDto.getEssentials() == null ? null : signzyFetchRequestDto.getEssentials());

                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(requestDto, headers);


                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("out of digi locker -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyFetchResponseDto.class);
                return Pair.of(1, responseDto);
            }

        } catch (HttpClientErrorException httpClientErrorException) {
            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("httpClientErrorException String response -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpClientErrorException response -- : {}", jsonObject);

            responseDto = objectMapper.readValue(response, SignzyFetchResponseDto.class);

            return Pair.of(0, responseDto);
        } catch (HttpServerErrorException httpServerErrorException) {
            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("httpServerErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            return Pair.of(0, responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception -- : {}", e.getMessage());
            return Pair.of(0, responseDto);
        }
        return Pair.of(0, responseDto);
    }

    @Override
    public Pair<Integer, SignzyFetchResponseDto> getAadhaarDetails(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws JsonProcessingException, ParseException {

        ResponseEntity<String> out = null;
        String url = null;
        SignzyFetchResponseDto responseDto = new SignzyFetchResponseDto();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("authorization", signzyFetchRequestDto.getAccessToken());

        JSONObject jsonObject;

        JSONParser jsonParser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();

        SignzyFetchRequestDto requestDto = new SignzyFetchRequestDto();


        try {
            if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {

                url = signzyBaseUrl + "/patrons/" + signzyFetchRequestDto.getUserToken() + "/digilockers";

                requestDto.setTask(signzyFetchRequestDto.getTask() == null ? "" : signzyFetchRequestDto.getTask());
                requestDto.setEssentials(signzyFetchRequestDto.getEssentials() == null ? null : signzyFetchRequestDto.getEssentials());

                HttpEntity<SignzyFetchRequestDto> entity = new HttpEntity<>(requestDto, headers);

                out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("out of digi locker -- : {}", out.getBody());
                responseDto = objectMapper.readValue(out.getBody(), SignzyFetchResponseDto.class);
                return Pair.of(1, responseDto);
            }

        } catch (HttpClientErrorException httpClientErrorException) {
            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("httpClientErrorException String response -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpClientErrorException response -- : {}", jsonObject);

            responseDto = objectMapper.readValue(response, SignzyFetchResponseDto.class);

            return Pair.of(0, responseDto);
        } catch (HttpServerErrorException httpServerErrorException) {
            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("httpServerErrorException -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            return Pair.of(0, responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception -- : {}", e.getMessage());
            return Pair.of(0, responseDto);
        }
        return Pair.of(0, responseDto);
    }

    @Override
    public Pair<Integer, AadhaarResponseDTO> getAadhaarDetailsNew(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws JsonProcessingException, ParseException {

        ResponseEntity<String> out = null;
        String url = null;
        SignzyFetchResponseDto responseDto = new SignzyFetchResponseDto();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("authorization", contract360AccessToken);
        headers.set("x-client-unique-id", "eniyan@bijlipay.co.in");


        JSONObject jsonObject;

        JSONParser jsonParser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();

        SignzyFetchRequestDto requestDto = new SignzyFetchRequestDto();
        AadhaarResponseDTO aadhaarResponseDTO = new AadhaarResponseDTO();


        try {
            if (signzyFetchRequestDto.getType().equalsIgnoreCase(Constants.FETCH_TYPE_AADHAAR_CARD)) {

//                url = "https://api-preproduction.signzy.app/api/v3/digilocker/geteaadhaar";
                DigitalCertificateRequestDTO digitalCertificateRequestDTO = new DigitalCertificateRequestDTO();
                digitalCertificateRequestDTO.setRequestId(signzyFetchRequestDto.getRequestId());
                logger.info("requestId--{}", signzyFetchRequestDto.getRequestId());
                digitalCertificateRequestDTO.setExtraDigitalCertificateParams(true);
                HttpEntity<DigitalCertificateRequestDTO> entity = new HttpEntity<>(digitalCertificateRequestDTO, headers);

                logger.info("singzy triggered----");
                out = restTemplate.exchange(fetchAadhaar, HttpMethod.POST, entity, String.class);
                logger.info("out of digi locker -- : {}", out.getBody());
                aadhaarResponseDTO = objectMapper.readValue(out.getBody(), AadhaarResponseDTO.class);
                logger.info("aadhaarResponseDTO----{}", aadhaarResponseDTO);
                return Pair.of(1, aadhaarResponseDTO);
            }

        } catch (HttpClientErrorException httpClientErrorException) {
            String response = httpClientErrorException.getResponseBodyAsString();
            logger.info("httpClientErrorException String response -- : {}", response);
            jsonObject = (JSONObject) jsonParser.parse(response);
            logger.info("httpClientErrorException response -- : {}", jsonObject);

            aadhaarResponseDTO = objectMapper.readValue(response, AadhaarResponseDTO.class);

            return Pair.of(0, aadhaarResponseDTO);
        } catch (HttpServerErrorException httpServerErrorException) {
            String response = httpServerErrorException.getResponseBodyAsString();
            logger.info("httpServerErrorException -- : {}", aadhaarResponseDTO);
            jsonObject = (JSONObject) jsonParser.parse(response);
            return Pair.of(0, aadhaarResponseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception -- : {}", e.getMessage());
            return Pair.of(0, aadhaarResponseDTO);
        }
        return Pair.of(0, aadhaarResponseDTO);
    }

    @Override
    public Pair<Integer, Object> signzyContract(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        SignzyLoginResponseDto loginResponse = new SignzyLoginResponseDto();
        try {
            // eSign login part
            logger.info("eSign login part ---:");
            loginResponse = this.generateContractAccessToken();
            logger.info("login Response -- : {}", loginResponse);
            logger.info("user id -- {}", loginResponse.getUserId());
            logger.info("id -- {}", loginResponse.getId());

            if (loginResponse.getId() != null) {

                //generate pdf url
                logger.info("eSign pdf url part --- :");

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

                url = signzyContractBaseUrl + loginResponse.getUserId() + "/contractdetails";

                HttpHeaders headers = new HttpHeaders();
                // aadhaar esign
                if (pdfResponseDto.getFile().getDirectURL() != null) {
                    logger.info("esignpart --- : ");


                    headers.add("Content-Type", "application/json");
                    headers.add("Authorization", loginResponse.getId());

                    SignzyContractRequestDto contractRequestDto = new SignzyContractRequestDto();
                    contractRequestDto.setPdfurl(pdfResponseDto.getFile().getDirectURL());
                    contractRequestDto.setMultiPages("true");
                    contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                    contractRequestDto.setLogoUrl(logoUrl);
                    List<Integer> pageNo = new ArrayList<>();
                    contractRequestDto.setPageNo(pageNo);
                    logger.info("contractRequestDto Page No ----------- : {} ", pageNo);

                    List<SignerDetail> signerDetailList = new ArrayList<>();

                    SignerDetail signerDetail = new SignerDetail();

                    if (action == Constants.AADHAAR_ESIGN) {

                        signerDetail.setSignerName(leadInformation.getMerchantName());
                        signerDetail.setSignerEmail(leadInformation.getContactEmail());
                        signerDetail.setSignatureOptions(List.of("aadhaaresign"));
//                        signerDetail.setxCoordinate("470");
//                        signerDetail.setyCoordinate("53");
//                        signerDetail.setHeight("20");
//                        signerDetail.setWidth("60");

                        List<SignaturesDto> signaturesList = new ArrayList<>();
                        SignaturesDto signaturesDto1 = new SignaturesDto();

                        List<Integer> pageNumber = new ArrayList<>();
                        if (pageCount != 0) {
                            for (int i = 1; i <= pageCount; i++) {
                                pageNumber.add(i);
                            }
                            logger.info("pageNumber----------:{}", pageNumber);
                            signaturesDto1.setPageNo(pageNumber);
                        } else {
                            if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                            } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                            } else {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
                            }
                        }
                        signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto1.setHeight(20);
                        signaturesDto1.setWidth(60);
                        signaturesDto1.setxCoordinate(Arrays.asList(470, 470));
                        signaturesDto1.setyCoordinate(Arrays.asList(53, 53));
                        signaturesList.add(signaturesDto1);

                        SignaturesDto signaturesDto2 = new SignaturesDto();
                        signaturesDto2.setPageNo(List.of(3));
                        signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto2.setHeight(20);
                        signaturesDto2.setWidth(60);
                        signaturesDto2.setxCoordinate(Arrays.asList(480, 480));
                        signaturesDto2.setyCoordinate(Arrays.asList(480, 480));
                        signaturesList.add(signaturesDto2);

                        SignaturesDto signaturesDto3 = new SignaturesDto();
                        signaturesDto3.setPageNo(List.of(10));
                        signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto3.setHeight(20);
                        signaturesDto3.setWidth(60);
                        signaturesDto3.setxCoordinate(Arrays.asList(380, 380));
                        signaturesDto3.setyCoordinate(Arrays.asList(710, 710));
                        signaturesList.add(signaturesDto3);

                        signerDetail.setSignatures(signaturesList);

                        signerDetailList.add(signerDetail);

                        contractRequestDto.setSignerdetail(signerDetailList);
                        contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                        contractRequestDto.setFileFormat("directURL");
//                        contractRequestDto.setLogoUrl(Constants.LOGO_IMAGE);
//                        contractRequestDto.setCallbackUrl("https://webhook.site/5ec31e6a-9a59-417d-973b-b22bb77dd674");
//                        contractRequestDto.setSignerCallbackUrl("https://webhook.site/5ec31e6a-9a59-417d-973b-b22bb77dd674");
                        contractRequestDto.setCustomerCallbackExtraParameters(List.of("auditCertificate"));
                        contractRequestDto.setCallbackUrl(contractCallbackUrl);
                        contractRequestDto.setSignerCallbackUrl(contractSignerCallbackUrl);
                        contractRequestDto.setIsParallel("false");
                    } else {
                        signerDetail.setSignerName(leadInformation.getMerchantName());
                        signerDetail.setSignerEmail(leadInformation.getContactEmail());
                        signerDetail.setSignatureOptions(List.of("esign"));
//                        signerDetail.setxCoordinate("470");
//                        signerDetail.setyCoordinate("53");
//                        signerDetail.setHeight("20");
//                        signerDetail.setWidth("60");

                        List<SignaturesDto> signaturesList = new ArrayList<>();

                        SignaturesDto signaturesDto1 = new SignaturesDto();

                        List<Integer> pageNumber = new ArrayList<>();
                        if (pageCount != 0) {
                            for (int i = 1; i <= pageCount; i++) {
                                if (i != 2 && i != 3) {
                                    pageNumber.add(i);
                                }
                            }
                            logger.info("pageNumber----------:{}", pageNumber);
                            signaturesDto1.setPageNo(pageNumber);
                        } else {
                            if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                            } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                            } else {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                            }
                        }
                        signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto1.setHeight(20);
                        signaturesDto1.setWidth(60);
                        signaturesDto1.setxCoordinate(Arrays.asList(450, 450));
                        signaturesDto1.setyCoordinate(Arrays.asList(53, 53));
                        signaturesList.add(signaturesDto1);

                        SignaturesDto signaturesDto2 = new SignaturesDto();
                        signaturesDto2.setPageNo(List.of(2));
                        signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto2.setHeight(20);
                        signaturesDto2.setWidth(60);
                        signaturesDto2.setxCoordinate(Arrays.asList(470, 470));
                        signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                        signaturesList.add(signaturesDto2);

                        SignaturesDto signaturesDto3 = new SignaturesDto();
                        signaturesDto3.setPageNo(List.of(3));
                        signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto3.setHeight(20);
                        signaturesDto3.setWidth(60);
                        signaturesDto3.setxCoordinate(Arrays.asList(480, 480));
                        signaturesDto3.setyCoordinate(Arrays.asList(480, 480));
                        signaturesList.add(signaturesDto3);

                        SignaturesDto signaturesDto4 = new SignaturesDto();
                        signaturesDto4.setPageNo(List.of(3));
                        signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto4.setHeight(20);
                        signaturesDto4.setWidth(60);
                        signaturesDto4.setxCoordinate(Arrays.asList(450, 450));
                        signaturesDto4.setyCoordinate(Arrays.asList(43, 43));
                        signaturesList.add(signaturesDto4);

                        SignaturesDto signaturesDto5 = new SignaturesDto();
                        signaturesDto5.setPageNo(List.of(10));
                        signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto5.setHeight(20);
                        signaturesDto5.setWidth(60);
                        signaturesDto5.setxCoordinate(Arrays.asList(380, 380));
                        signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                        signaturesList.add(signaturesDto5);

                        signerDetail.setSignatures(signaturesList);
                        signerDetailList.add(signerDetail);

                        contractRequestDto.setSignerdetail(signerDetailList);
                        contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                        contractRequestDto.setFileFormat("directURL");
//                        contractRequestDto.setLogoUrl(Constants.LOGO_IMAGE);
//                        contractRequestDto.setCallbackUrl("https://webhook.site/5ec31e6a-9a59-417d-973b-b22bb77dd674");
//                        contractRequestDto.setSignerCallbackUrl("https://webhook.site/5ec31e6a-9a59-417d-973b-b22bb77dd674");
                        contractRequestDto.setCustomerCallbackExtraParameters(List.of("auditCertificate"));
                        contractRequestDto.setCallbackUrl(contractCallbackUrl);
                        contractRequestDto.setSignerCallbackUrl(contractSignerCallbackUrl);
                        contractRequestDto.setIsParallel("true");
                        logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                    }
                    Gson gson = new Gson();
                    String request = gson.toJson(contractRequestDto);
                    logger.info("contract request DTO -------------------- : {}", request);
                    url = signzyContractBaseUrl + loginResponse.getUserId() + "/contractdetails";
                    logger.info("url --------------- : {}", url);
                    HttpEntity<SignzyContractRequestDto> httpEntity = new HttpEntity<>(contractRequestDto, headers);

//                    url = signzyContractBaseUrl + loginResponse.getUserId() + "/contractdetails";
                    out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

                    logger.info("contract Response out -- : {}", out.getBody());

                    SignzyContractResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContractResponseDto.class);
                    logger.info("contract responseDto -- : {}", responseDto);

//                    List<AgreementDetails> details = new ArrayList<>();
//                    responseDto.getSignerdetail().forEach(l -> {
//                        AgreementDetails agreementDetails = new AgreementDetails();
//                        agreementDetails.setSignerId(l.getSignerId());
//                        agreementDetails.setSignatureType(l.getSignatureOptions().toString());
//                        agreementDetails.setUrl(l.getContractUrl());
//                        agreementDetails.setLeadInformation(leadInformation);
//                        agreementDetails.setInputFileName(documentService.storePdfFileForContract(multipartFile,leadInformation));
//                        details.add(agreementDetails);
//                    });

//                    List<AgreementDetails> agreementDetailsList = agreementDetailsRepository.saveAll(details);
//                    logger.info("agreement Details-------------- : {}",agreementDetailsList);
                    details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                    details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                    details.setSignatureType(responseDto.getSignerdetail().get(0).getSignerId());
                    details.setUrl(responseDto.getSignerdetail().get(0).getContractUrl());
                    details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                    details.setResponse(false);
                    AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
                    logger.info("agreement Details -- : {}", agreementDetails);

                    ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                    logger.info("Logout --- : {}", responseEntity.getBody());
                    return Pair.of(1, responseDto);
                }
                ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
                return Pair.of(0, "pdf url not generated");
            }
            return Pair.of(0, "eSign login failed");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }
            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }

            return Pair.of(0, e.getMessage());
        }
    }

    @Override
    public Pair<Integer, Object> signzyAggContract(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        SignzyLoginResponseDto loginResponse = new SignzyLoginResponseDto();
        try {
            // eSign login part
            logger.info("eSign login part ---:");
            loginResponse = this.generateContractAccessToken();
            logger.info("login Response -- : {}", loginResponse);
            logger.info("user id -- {}", loginResponse.getUserId());
            logger.info("id -- {}", loginResponse.getId());

            if (loginResponse.getId() != null) {

                //generate pdf url
                logger.info("eSign pdf url part --- :");

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

                url = signzyContractBaseUrl + loginResponse.getUserId() + "/contractdetails";

                HttpHeaders headers = new HttpHeaders();
                // aadhaar esign
                if (pdfResponseDto.getFile().getDirectURL() != null) {
                    logger.info("esignpart --- : ");


                    headers.add("Content-Type", "application/json");
                    headers.add("Authorization", loginResponse.getId());

                    SignzyContractRequestDto contractRequestDto = new SignzyContractRequestDto();
                    contractRequestDto.setPdfurl(pdfResponseDto.getFile().getDirectURL());
                    contractRequestDto.setMultiPages("true");
                    contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                    contractRequestDto.setLogoUrl(logoUrl);
                    List<Integer> pageNo = new ArrayList<>();
                    contractRequestDto.setPageNo(pageNo);
                    logger.info("contractRequestDto Page No ----------- : {} ", pageNo);

                    List<SignerDetail> signerDetailList = new ArrayList<>();

                    SignerDetail signerDetail = new SignerDetail();

                    if (action == Constants.AADHAAR_ESIGN) {

                        signerDetail.setSignerName(leadInformation.getMerchantName());
                        signerDetail.setSignerEmail(leadInformation.getContactEmail());
                        signerDetail.setSignatureOptions(List.of("aadhaaresign"));
//                        signerDetail.setxCoordinate("470");
//                        signerDetail.setyCoordinate("53");
//                        signerDetail.setHeight("20");
//                        signerDetail.setWidth("60");

                        List<SignaturesDto> signaturesList = new ArrayList<>();
                        SignaturesDto signaturesDto1 = new SignaturesDto();

                        List<Integer> pageNumber = new ArrayList<>();
                        if (pageCount != 0) {
                            for (int i = 1; i <= pageCount; i++) {
                                pageNumber.add(i);
                            }
                            logger.info("pageNumber----------:{}", pageNumber);
                            signaturesDto1.setPageNo(pageNumber);
                        } else {
                            if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                            } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                            } else {
                                signaturesDto1.setPageNo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
                            }
                        }
                        signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto1.setHeight(20);
                        signaturesDto1.setWidth(60);
                        signaturesDto1.setxCoordinate(Arrays.asList(470, 470));
                        signaturesDto1.setyCoordinate(Arrays.asList(53, 53));
                        signaturesList.add(signaturesDto1);

                        SignaturesDto signaturesDto2 = new SignaturesDto();
                        signaturesDto2.setPageNo(List.of(3));
                        signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto2.setHeight(20);
                        signaturesDto2.setWidth(60);
                        signaturesDto2.setxCoordinate(Arrays.asList(490, 490));
                        signaturesDto2.setyCoordinate(Arrays.asList(520, 520));
                        signaturesList.add(signaturesDto2);

                        SignaturesDto signaturesDto3 = new SignaturesDto();
                        signaturesDto3.setPageNo(List.of(10));
                        signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto3.setHeight(20);
                        signaturesDto3.setWidth(60);
                        signaturesDto3.setxCoordinate(Arrays.asList(270, 270));
                        signaturesDto3.setyCoordinate(Arrays.asList(710, 710));
                        signaturesList.add(signaturesDto3);

                        signerDetail.setSignatures(signaturesList);

                        signerDetailList.add(signerDetail);

                        contractRequestDto.setSignerdetail(signerDetailList);
                        contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                        contractRequestDto.setFileFormat("directURL");
//                        contractRequestDto.setLogoUrl(Constants.LOGO_IMAGE);
//                        contractRequestDto.setCallbackUrl("https://webhook.site/5ec31e6a-9a59-417d-973b-b22bb77dd674");
//                        contractRequestDto.setSignerCallbackUrl("https://webhook.site/5ec31e6a-9a59-417d-973b-b22bb77dd674");
                        contractRequestDto.setCustomerCallbackExtraParameters(List.of("auditCertificate"));
                        contractRequestDto.setCallbackUrl(contractCallbackUrl);
                        contractRequestDto.setSignerCallbackUrl(contractSignerCallbackUrl);
                        contractRequestDto.setIsParallel("false");
                    } else {
                        signerDetail.setSignerName(leadInformation.getMerchantName());
                        signerDetail.setSignerEmail(leadInformation.getContactEmail());
                        signerDetail.setSignatureOptions(List.of("esign"));
//                        signerDetail.setxCoordinate("470");
//                        signerDetail.setyCoordinate("53");
//                        signerDetail.setHeight("20");
//                        signerDetail.setWidth("60");

                        List<SignaturesDto> signaturesList = new ArrayList<>();

                        SignaturesDto signaturesDto1 = new SignaturesDto();

                        List<Integer> pageNumber = new ArrayList<>();
                        if (pageCount != 0) {
                            for (int i = 1; i <= pageCount; i++) {
                                if (i != 2 && i != 3) {
                                    pageNumber.add(i);
                                }
                            }
                            logger.info("pageNumber----------:{}", pageNumber);
                            signaturesDto1.setPageNo(pageNumber);
                        } else {
                            if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                            } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                            } else {
                                signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                            }
                        }
                        signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto1.setHeight(20);
                        signaturesDto1.setWidth(60);
                        signaturesDto1.setxCoordinate(Arrays.asList(450, 450));
                        signaturesDto1.setyCoordinate(Arrays.asList(53, 53));
                        signaturesList.add(signaturesDto1);

                        SignaturesDto signaturesDto2 = new SignaturesDto();
                        signaturesDto2.setPageNo(List.of(2));
                        signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto2.setHeight(20);
                        signaturesDto2.setWidth(60);
                        signaturesDto2.setxCoordinate(Arrays.asList(470, 470));
                        signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                        signaturesList.add(signaturesDto2);

                        SignaturesDto signaturesDto3 = new SignaturesDto();
                        signaturesDto3.setPageNo(List.of(3));
                        signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto3.setHeight(20);
                        signaturesDto3.setWidth(60);
                        signaturesDto3.setxCoordinate(Arrays.asList(490, 490));
                        signaturesDto3.setyCoordinate(Arrays.asList(520, 520));
                        signaturesList.add(signaturesDto3);

                        SignaturesDto signaturesDto4 = new SignaturesDto();
                        signaturesDto4.setPageNo(List.of(3));
                        signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto4.setHeight(20);
                        signaturesDto4.setWidth(60);
                        signaturesDto4.setxCoordinate(Arrays.asList(450, 450));
                        signaturesDto4.setyCoordinate(Arrays.asList(43, 43));
                        signaturesList.add(signaturesDto4);

                        SignaturesDto signaturesDto5 = new SignaturesDto();
                        signaturesDto5.setPageNo(List.of(10));
                        signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                        signaturesDto5.setHeight(20);
                        signaturesDto5.setWidth(60);
                        signaturesDto5.setxCoordinate(Arrays.asList(270, 270));
                        signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                        signaturesList.add(signaturesDto5);

                        signerDetail.setSignatures(signaturesList);
                        signerDetailList.add(signerDetail);

                        contractRequestDto.setSignerdetail(signerDetailList);
                        contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                        contractRequestDto.setFileFormat("directURL");
//                        contractRequestDto.setLogoUrl(Constants.LOGO_IMAGE);
//                        contractRequestDto.setCallbackUrl("https://webhook.site/5ec31e6a-9a59-417d-973b-b22bb77dd674");
//                        contractRequestDto.setSignerCallbackUrl("https://webhook.site/5ec31e6a-9a59-417d-973b-b22bb77dd674");
                        contractRequestDto.setCustomerCallbackExtraParameters(List.of("auditCertificate"));
                        contractRequestDto.setCallbackUrl(contractCallbackUrl);
                        contractRequestDto.setSignerCallbackUrl(contractSignerCallbackUrl);
                        contractRequestDto.setIsParallel("true");
                        logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                    }
                    Gson gson = new Gson();
                    String request = gson.toJson(contractRequestDto);
                    logger.info("contract request DTO -------------------- : {}", request);
                    url = signzyContractBaseUrl + loginResponse.getUserId() + "/contractdetails";
                    logger.info("url --------------- : {}", url);
                    HttpEntity<SignzyContractRequestDto> httpEntity = new HttpEntity<>(contractRequestDto, headers);

//                    url = signzyContractBaseUrl + loginResponse.getUserId() + "/contractdetails";
                    out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

                    logger.info("contract Response out -- : {}", out.getBody());

                    SignzyContractResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContractResponseDto.class);
                    logger.info("contract responseDto -- : {}", responseDto);

//                    List<AgreementDetails> details = new ArrayList<>();
//                    responseDto.getSignerdetail().forEach(l -> {
//                        AgreementDetails agreementDetails = new AgreementDetails();
//                        agreementDetails.setSignerId(l.getSignerId());
//                        agreementDetails.setSignatureType(l.getSignatureOptions().toString());
//                        agreementDetails.setUrl(l.getContractUrl());
//                        agreementDetails.setLeadInformation(leadInformation);
//                        agreementDetails.setInputFileName(documentService.storePdfFileForContract(multipartFile,leadInformation));
//                        details.add(agreementDetails);
//                    });

//                    List<AgreementDetails> agreementDetailsList = agreementDetailsRepository.saveAll(details);
//                    logger.info("agreement Details-------------- : {}",agreementDetailsList);
                    details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                    details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                    details.setSignatureType(responseDto.getSignerdetail().get(0).getSignerId());
                    details.setUrl(responseDto.getSignerdetail().get(0).getContractUrl());
                    details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                    details.setResponse(false);
                    AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
                    logger.info("agreement Details -- : {}", agreementDetails);

                    ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                    logger.info("Logout --- : {}", responseEntity.getBody());
                    return Pair.of(1, responseDto);
                }
                ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
                return Pair.of(0, "pdf url not generated");
            }
            return Pair.of(0, "eSign login failed");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }
            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            if (loginResponse.getId() != null) {
                ResponseEntity<Object> responseEntity = this.deActivateContractAccessToken(loginResponse.getId());
                logger.info("Logout --- : {}", responseEntity.getBody());
            }

            return Pair.of(0, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deActivateContractAccessToken(String id) {
        String url = signzyContractBaseUrl + "logout?access_token=" + id;
        ResponseEntity<Object> result = restTemplate.exchange(url, HttpMethod.POST, null, Object.class);
        return result;
    }

    @Override
    public SignzyLoginResponseDto generateContractAccessToken() {
        String url = signzyContractBaseUrl + "login";
        logger.info("login URL --{}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        SignzyLoginDto signzyLoginDto = new SignzyLoginDto();
        signzyLoginDto.setUsername(signzyUserName);
        signzyLoginDto.setPassword(signzyPassword);
        HttpEntity<SignzyLoginDto> entity = new HttpEntity(signzyLoginDto, headers);
        ResponseEntity<SignzyLoginResponseDto> result = restTemplate.exchange(url, HttpMethod.POST, entity, SignzyLoginResponseDto.class);
        logger.info("login Response --{}", result.getBody());
        if (result.hasBody()) {
            SignzyLoginResponseDto responseDto = new SignzyLoginResponseDto();
            responseDto.setId(result.getBody().getId());
            responseDto.setUserId(result.getBody().getUserId());
            return responseDto;
        }
        return null;
    }

    @Override
    public Pair<Integer, Object> signzy360AggContract(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException, JsonProcessingException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        try {
            //generate pdf url
            logger.info("eSign pdf url part --- :");

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

            url = "https://api-preproduction.signzy.app/api/v3/contract/initiate";

            HttpHeaders headers = new HttpHeaders();
            // aadhaar esign
            if (pdfResponseDto.getFile().getDirectURL() != null) {
                logger.info("esignpart --- : ");


                headers.add("Content-Type", "application/json");
                headers.add("Authorization", contract360AccessToken);

                SignzyContract360AggRequestDto contractRequestDto = new SignzyContract360AggRequestDto();
                contractRequestDto.setPdf(pdfResponseDto.getFile().getDirectURL());
                contractRequestDto.setContractName(leadInformation.getContactName());
                contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                contractRequestDto.setCallbackUrl(contract360CallbackUrl);
                contractRequestDto.setParallel(false);
                contractRequestDto.setSignerCallbackUrl(contract360SignerCallBackUrl);
                contractRequestDto.setWorkflow(true);
                contractRequestDto.seteSignProvider("cdsl");
//                contractRequestDto.setShowAadhaarNameOnSignature(true);
                List<SignerDetails360> signerDetailList = new ArrayList<>();
                SignerDetails360 signerDetail = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 2 && i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(3));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                } else {
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 2 && i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

//                        signaturesDto1.setPageNo(Arrays.asList(1, 4,5,6,7,8,9,10,11,12,13));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(3));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                }
                Gson gson = new Gson();
                String request = gson.toJson(contractRequestDto);
                logger.info("contract request DTO -------------------- : {}", request);
                url = signzy360ContractBaseUrl + "contract/initiate";
                logger.info("url --------------- : {}", url);

                HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("contract Response out -- : {}", out.getBody());

                SignzyContract360ResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContract360ResponseDto.class);
                logger.info("contract responseDto -- : {}", responseDto);

                details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignatureType(responseDto.getSignerdetail().get(0).getSignatureType());
                details.setUrl(responseDto.getSignerdetail().get(0).getWorkflowUrl());
                details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                details.setContractId(responseDto.getContractId());
                details.setResponse(false);
                AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
                logger.info("agreement Details Contract Id-- : {}", agreementDetails.getContractId());

                return Pair.of(1, responseDto);
            }
            return Pair.of(0, "pdf url not generated");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            return Pair.of(0, e.getMessage());
        }
    }

    @Override
    public Pair<Integer, Object> signzy360AggContractDo(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        try {
            //generate pdf url
            logger.info("eSign pdf url part --- :");

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

            url = "https://api-preproduction.signzy.app/api/v3/contract/initiate";

            HttpHeaders headers = new HttpHeaders();
            // aadhaar esign
            if (pdfResponseDto.getFile().getDirectURL() != null) {
                logger.info("esignpart --- : ");


                headers.add("Content-Type", "application/json");
                headers.add("Authorization", contract360AccessToken);

                SignzyContract360AggRequestDto contractRequestDto = new SignzyContract360AggRequestDto();
                contractRequestDto.setPdf(pdfResponseDto.getFile().getDirectURL());
                contractRequestDto.setContractName(leadInformation.getContactName());
                contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                contractRequestDto.setCallbackUrl(contract360CallbackUrl);
                contractRequestDto.setParallel(false);
                contractRequestDto.setSignerCallbackUrl(contract360SignerCallBackUrl);
                contractRequestDto.setWorkflow(true);
                contractRequestDto.seteSignProvider("cdsl");
//                contractRequestDto.setShowAadhaarNameOnSignature(true);
                List<SignerDetails360> signerDetailList = new ArrayList<>();
                SignerDetails360 signerDetail = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

//                    SignaturesDto signaturesDto2 = new SignaturesDto();
//                    signaturesDto2.setPageNo(List.of(2));
//                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
//                    signaturesDto2.setHeight(20);
//                    signaturesDto2.setWidth(60);
//                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
//                    signaturesDto2.setyCoordinate(Arrays.asList(20, 20));
//                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(3));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(310, 310));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                } else {
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

//                        signaturesDto1.setPageNo(Arrays.asList(1, 4,5,6,7,8,9,10,11,12,13));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

//                    SignaturesDto signaturesDto2 = new SignaturesDto();
//                    signaturesDto2.setPageNo(List.of(2));
//                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
//                    signaturesDto2.setHeight(20);
//                    signaturesDto2.setWidth(60);
//                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
//                    signaturesDto2.setyCoordinate(Arrays.asList(120, 120));
//                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(3));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                }
                Gson gson = new Gson();
                String request = gson.toJson(contractRequestDto);
                logger.info("contract request DTO -------------------- : {}", request);
                url = signzy360ContractBaseUrl + "contract/initiate";
                logger.info("url --------------- : {}", url);

                HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("contract Response out -- : {}", out.getBody());

                SignzyContract360ResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContract360ResponseDto.class);
                logger.info("contract responseDto -- : {}", responseDto);

                details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignatureType(responseDto.getSignerdetail().get(0).getSignatureType());
                details.setUrl(responseDto.getSignerdetail().get(0).getWorkflowUrl());
                details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                details.setContractId(responseDto.getContractId());
                details.setResponse(false);
                AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
//               leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(Constants.PENDING_AGREEMENT_SIGNING));
                leadInformationRepository.save(leadInformation);
                logger.info("agreement Details Contract Id-- : {}", agreementDetails.getContractId());

                return Pair.of(1, responseDto);
            }
            return Pair.of(0, "pdf url not generated");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            return Pair.of(0, e.getMessage());
        }
    }

    @Override
    public Pair<Integer, Object> signzy360AggContractDoQR(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        try {
            //generate pdf url
            logger.info("eSign pdf url part --- :");

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

            url = "https://api-preproduction.signzy.app/api/v3/contract/initiate";

            HttpHeaders headers = new HttpHeaders();
            // aadhaar esign
            if (pdfResponseDto.getFile().getDirectURL() != null) {
                logger.info("esignpart --- : ");


                headers.add("Content-Type", "application/json");
                headers.add("Authorization", contract360AccessToken);

                SignzyContract360AggRequestDto contractRequestDto = new SignzyContract360AggRequestDto();
                contractRequestDto.setPdf(pdfResponseDto.getFile().getDirectURL());
                contractRequestDto.setContractName(leadInformation.getContactName());
                contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                contractRequestDto.setCallbackUrl(contract360CallbackUrl);
                contractRequestDto.setParallel(false);
                contractRequestDto.setSignerCallbackUrl(contract360SignerCallBackUrl);
                contractRequestDto.setWorkflow(true);
                contractRequestDto.seteSignProvider("cdsl");
//                contractRequestDto.setShowAadhaarNameOnSignature(true);
                List<SignerDetails360> signerDetailList = new ArrayList<>();
                SignerDetails360 signerDetail = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    logger.info("====== Aadhar Esign =======");
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 2 && i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(500, 500));
                    signaturesDto2.setyCoordinate(Arrays.asList(62, 62));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(480, 480));
                    signaturesList.add(signaturesDto3);

//                    SignaturesDto signaturesDto4 = new SignaturesDto();
//                    signaturesDto4.setPageNo(List.of(3));
//                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
//                    signaturesDto4.setHeight(20);
//                    signaturesDto4.setWidth(60);
//                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
//                    signaturesDto4.setyCoordinate(Arrays.asList(250, 250));
//                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                } else {

                    logger.info("====== Digital Esign =======");
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 2 && i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

//                        signaturesDto1.setPageNo(Arrays.asList(1, 4,5,6,7,8,9,10,11,12,13));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(500, 500));
                    signaturesDto2.setyCoordinate(Arrays.asList(62, 62));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(480, 480));
                    signaturesList.add(signaturesDto3);

//                    SignaturesDto signaturesDto4 = new SignaturesDto();
//                    signaturesDto4.setPageNo(List.of(3));
//                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
//                    signaturesDto4.setHeight(20);
//                    signaturesDto4.setWidth(60);
//                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
//                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
//                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                }
                Gson gson = new Gson();
                String request = gson.toJson(contractRequestDto);
                logger.info("contract request DTO -------------------- : {}", request);
                url = signzy360ContractBaseUrl + "contract/initiate";
                logger.info("url --------------- : {}", url);

                HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("contract Response out -- : {}", out.getBody());

                SignzyContract360ResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContract360ResponseDto.class);
                logger.info("contract responseDto -- : {}", responseDto);

                details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignatureType(responseDto.getSignerdetail().get(0).getSignatureType());
                details.setUrl(responseDto.getSignerdetail().get(0).getWorkflowUrl());
                details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                details.setContractId(responseDto.getContractId());
                details.setResponse(false);
                AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
//               leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(Constants.PENDING_AGREEMENT_SIGNING));
                leadInformationRepository.save(leadInformation);
                logger.info("agreement Details Contract Id-- : {}", agreementDetails.getContractId());

                return Pair.of(1, responseDto);
            }
            return Pair.of(0, "pdf url not generated");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            return Pair.of(0, e.getMessage());
        }
    }

    @Override
    public Pair<Integer, Object> signzy360AggContractDoPOS(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        try {
            //generate pdf url
            logger.info("eSign pdf url part --- :");

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

            url = "https://api-preproduction.signzy.app/api/v3/contract/initiate";

            HttpHeaders headers = new HttpHeaders();
            // aadhaar esign
            if (pdfResponseDto.getFile().getDirectURL() != null) {
                logger.info("esignpart --- : ");


                headers.add("Content-Type", "application/json");
                headers.add("Authorization", contract360AccessToken);

                SignzyContract360AggRequestDto contractRequestDto = new SignzyContract360AggRequestDto();
                contractRequestDto.setPdf(pdfResponseDto.getFile().getDirectURL());
                contractRequestDto.setContractName(leadInformation.getContactName());
                contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                contractRequestDto.setCallbackUrl(contract360CallbackUrl);
                contractRequestDto.setParallel(false);
                contractRequestDto.setSignerCallbackUrl(contract360SignerCallBackUrl);
                contractRequestDto.setWorkflow(true);
                contractRequestDto.seteSignProvider("cdsl");
//                contractRequestDto.setShowAadhaarNameOnSignature(true);
                List<SignerDetails360> signerDetailList = new ArrayList<>();
                SignerDetails360 signerDetail = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 2 && i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(15, 15));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(3));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(300, 300));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                } else {
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 2 && i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

//                        signaturesDto1.setPageNo(Arrays.asList(1, 4,5,6,7,8,9,10,11,12,13));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(15, 15));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(3));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(300, 300));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                }
                Gson gson = new Gson();
                String request = gson.toJson(contractRequestDto);
                logger.info("contract request DTO -------------------- : {}", request);
                url = signzy360ContractBaseUrl + "contract/initiate";
                logger.info("url --------------- : {}", url);

                HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("contract Response out -- : {}", out.getBody());

                SignzyContract360ResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContract360ResponseDto.class);
                logger.info("contract responseDto -- : {}", responseDto);

                details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignatureType(responseDto.getSignerdetail().get(0).getSignatureType());
                details.setUrl(responseDto.getSignerdetail().get(0).getWorkflowUrl());
                details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                details.setContractId(responseDto.getContractId());
                details.setResponse(false);
                AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
//               leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(Constants.PENDING_AGREEMENT_SIGNING));
                leadInformationRepository.save(leadInformation);
                logger.info("agreement Details Contract Id-- : {}", agreementDetails.getContractId());

                return Pair.of(1, responseDto);
            }
            return Pair.of(0, "pdf url not generated");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            return Pair.of(0, e.getMessage());
        }
    }


    public Pair<Integer, Object> signzy360AggContractUtil(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        try {
            //generate pdf url
            logger.info("eSign pdf url part --- :");

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

            url = "https://api-preproduction.signzy.app/api/v3/contract/initiate";

            HttpHeaders headers = new HttpHeaders();
            // aadhaar esign
            if (pdfResponseDto.getFile().getDirectURL() != null) {
                logger.info("esignpart ---Util : ");


                headers.add("Content-Type", "application/json");
                headers.add("Authorization", contract360AccessToken);

                SignzyContract360AggRequestDto contractRequestDto = new SignzyContract360AggRequestDto();
                contractRequestDto.setPdf(pdfResponseDto.getFile().getDirectURL());
                contractRequestDto.setContractName(leadInformation.getContactName());
                contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                contractRequestDto.setCallbackUrl(contract360CallbackUrl);
                contractRequestDto.setParallel(false);
                contractRequestDto.setSignerCallbackUrl(contract360SignerCallBackUrl);
                contractRequestDto.setWorkflow(true);
//                contractRequestDto.seteSignProvider("vsign");
//                contractRequestDto.setShowAadhaarNameOnSignature(true);
                List<SignerDetails360> signerDetailList = new ArrayList<>();
                SignerDetails360 signerDetail = new SignerDetails360();
                logger.info("---------action---", action);
                if (action == Constants.AADHAAR_ESIGN) {

                    logger.info("---------aadhar---");
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    signaturesDto1.setPageNo(Collections.singletonList(1));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(20, 20));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(620, 620));
                    signaturesList.add(signaturesDto3);
                    signerDetail.setSignatures(signaturesList);


                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                } else {
                    logger.info("---------noaadhar---");
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    signaturesDto1.setPageNo(List.of(1));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(300, 300));
                    signaturesDto3.setyCoordinate(Arrays.asList(210, 210));
                    signaturesList.add(signaturesDto3);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                }
                Gson gson = new Gson();
                String request = gson.toJson(contractRequestDto);
                logger.info("contract request DTO -------------------- : {}", request);
                url = signzy360ContractBaseUrl + "contract/initiate";
                logger.info("url --------------- : {}", url);

                HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("contract Response out -- : {}", out.getBody());

                SignzyContract360ResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContract360ResponseDto.class);
                logger.info("contract responseDto -- : {}", responseDto);

                details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignatureType(responseDto.getSignerdetail().get(0).getSignatureType());
                details.setUrl(responseDto.getSignerdetail().get(0).getWorkflowUrl());
                details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                details.setContractId(responseDto.getContractId());
                details.setResponse(false);
                AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
                leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(Constants.PENDING_AGREEMENT_SIGNING));
                leadInformationRepository.save(leadInformation);
                logger.info("agreement Details Contract Id-- : {}", agreementDetails.getContractId());

                return Pair.of(1, responseDto);
            }
            return Pair.of(0, "pdf url not generated");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            return Pair.of(0, e.getMessage());
        }
    }


    @Override
    public Pair<Integer, Object> signzy360AggContract1(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        try {
            //generate pdf url
            logger.info("eSign pdf url part --- :");

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

            url = "https://api-preproduction.signzy.app/api/v3/contract/initiate";

            HttpHeaders headers = new HttpHeaders();
            // aadhaar esign
            if (pdfResponseDto.getFile().getDirectURL() != null) {
                logger.info("esignpart --- : ");


                headers.add("Content-Type", "application/json");
                headers.add("Authorization", contract360AccessToken);

                SignzyContract360AggRequestDto contractRequestDto = new SignzyContract360AggRequestDto();
                contractRequestDto.setPdf(pdfResponseDto.getFile().getDirectURL());
                contractRequestDto.setContractName(leadInformation.getContactName());
                contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                contractRequestDto.setCallbackUrl(contract360CallbackUrl);
                contractRequestDto.setParallel(false);
                contractRequestDto.setSignerCallbackUrl(contract360SignerCallBackUrl);
                contractRequestDto.setWorkflow(true);
                contractRequestDto.seteSignProvider("cdsl");
//                contractRequestDto.setShowAadhaarNameOnSignature(true);
                List<SignerDetails360> signerDetailList = new ArrayList<>();
                SignerDetails360 signerDetail = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    signaturesDto1.setPageNo(Collections.singletonList(1));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(300, 300));
                    signaturesDto3.setyCoordinate(Arrays.asList(210, 210));
                    signaturesList.add(signaturesDto3);
                    signerDetail.setSignatures(signaturesList);
                } else {
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    signaturesDto1.setPageNo(List.of(1));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(2));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(300, 300));
                    signaturesDto3.setyCoordinate(Arrays.asList(210, 210));
                    signaturesList.add(signaturesDto3);

                    signerDetail.setSignatures(signaturesList);


                }
                signerDetailList.add(signerDetail);
                contractRequestDto.setSignerdetail(signerDetailList);
                contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                logger.debug("contract request DTO -------------------- : {}", contractRequestDto);

                Gson gson = new Gson();
                String request = gson.toJson(contractRequestDto);
                logger.info("contract request DTO -------------------- : {}", request);
                url = signzy360ContractBaseUrl + "contract/initiate";
                logger.info("url --------------- : {}", url);

                HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("contract Response out -- : {}", out.getBody());

                SignzyContract360ResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContract360ResponseDto.class);
                logger.info("contract responseDto -- : {}", responseDto);

                details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignatureType(responseDto.getSignerdetail().get(0).getSignatureType());
                details.setUrl(responseDto.getSignerdetail().get(0).getWorkflowUrl());
                details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                details.setContractId(responseDto.getContractId());
                details.setResponse(false);
                AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
                logger.info("agreement Details Contract Id-- : {}", agreementDetails.getContractId());

                return Pair.of(1, responseDto);
            }
            return Pair.of(0, "pdf url not generated");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            return Pair.of(0, e.getMessage());
        }
    }

//    @Override
//    public Pair<Integer, Object> verifyTransfer(LeadInformation leadInformation, VerifyTransferRequestDto verifyTransferRequestDto) throws JsonProcessingException, ParseException {
//        ResponseEntity<String> out = null;
//        String url = null;
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JSONObject jsonObject;
//        JSONParser jsonParser = new JSONParser();
//
//        Gson gson = new Gson();
//        PennyDropDto result = new PennyDropDto();
//
//
////        PennyDropVerification pennyDrop = new PennyDropVerification();
//        PennyDropVerification pennyDropVerification = pennyDropVerificationRepository.findByLeadId(leadInformation);
//        try {
//            // login part
//            logger.info("login part -- :");
//            SignzyLoginResponseDto loginResponseDto = this.generateAccessToken();
//            logger.info("loginResponse -- : {}", loginResponseDto);
//            //Account Transfer Part
//            if (loginResponseDto.getId() != null) {
//                logger.info("Account Transfer Part -- : ");
//                url = signzyBaseUrl + "patrons/" + loginResponseDto.getUserId() + "/bankaccountverifications";
//                httpHeaders.set("Content-type", "application/json");
//                httpHeaders.set("Authorization", loginResponseDto.getId());
//
////                    VerifyTransferRequestDto verifyTransferRequestDto = new VerifyTransferRequestDto();
//                    VerifyTransferEssentialDto essentialDto = new VerifyTransferEssentialDto();
//
//                    essentialDto.setAmount(verifyTransferRequestDto.getEssentials().getAmount());
//                    essentialDto.setSignzyId(pennyDropVerification.getSignzyReferencedId());
//
//                    verifyTransferRequestDto.setTask("verifyAmount");
//                    verifyTransferRequestDto.setEssentials(essentialDto);
//
//
//                    HttpEntity<VerifyTransferRequestDto> entity = new HttpEntity<>(verifyTransferRequestDto, httpHeaders);
//                    out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//                    logger.info("Verify Transfer Response out -- : {}", out.getBody());
//                    VerifyTransferResponseDto verifyResponse = objectMapper.readValue(out.getBody(), VerifyTransferResponseDto.class);
//                    logger.info("verifyResponse -- : {}", verifyResponse);
//
//                    if (verifyResponse.getResult() != null) {
//                        if (verifyResponse.getResult().getAmountMatch().equalsIgnoreCase(Constants.PENNY_DROP_AMOUNT_MATCHED_SUCCESS)) {
//                            LeadInformation leadInfo = leadInformationRepository.findByLeadId(leadInformation.getLeadId());
//                            if (leadInfo != null) {
//                                leadInfo.setPennyDropVerified(true);
//                                leadInformationRepository.save(leadInfo);
//                            }
//                        }
//                        pennyDropVerification.setAmountMatch(verifyResponse.getResult().getAmountMatch());
//                        pennyDropVerification.setVerifyTransferDetails(gson.toJson(verifyResponse.getResult()));
//                        pennyDropVerification = pennyDropVerificationRepository.save(pennyDropVerification);
//                    }
//                    result = this.getPennyDropValues(pennyDropVerification);
//                    // sign out part
//                    logger.info("sign out part -- :");
//                    ResponseEntity<Object> responseEntity = this.deActivateAccessToken(loginResponseDto.getId());
//                    logger.info("sign out response -- : {}", responseEntity);
//                    return Pair.of(1, result);
//                }
//                // sign out part
//                logger.info("sign out part -- :");
//                ResponseEntity<Object> responseEntity = this.deActivateAccessToken(loginResponseDto.getId());
//                logger.info("sign out response -- : {}", responseEntity);
//                return Pair.of(0, result);
//        } catch (HttpClientErrorException httpClientErrorException) {
//            String response = httpClientErrorException.getResponseBodyAsString();
//            logger.info("ClientErrorException -- : {}", response);
//            jsonObject = (JSONObject) jsonParser.parse(response);
//            logger.info("httpClientErrorException -- : {}", jsonObject);
//            return Pair.of(0, jsonObject);
//        } catch (HttpServerErrorException httpServerErrorException) {
//            String response = httpServerErrorException.getResponseBodyAsString();
//            logger.info("serverErrorException -- : {}", response);
//            jsonObject = (JSONObject) jsonParser.parse(response);
//            logger.info("httpServerErrorException -- : {}", jsonObject);
//            return Pair.of(0, jsonObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info("Exception -- : {}", e.getMessage());
//            return Pair.of(0, "");
//        }
//    }

    @Override
    public Pair<Integer, Object> signzy360AggContractDoPOSQRSB(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException {
        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        try {
            //generate pdf url
            logger.info("eSign pdf url part --- :");

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

            url = "https://api-preproduction.signzy.app/api/v3/contract/initiate";

            HttpHeaders headers = new HttpHeaders();
            // aadhaar esign
            if (pdfResponseDto.getFile().getDirectURL() != null) {
                logger.info("esignpart --- : ");


                headers.add("Content-Type", "application/json");
                headers.add("Authorization", contract360AccessToken);

                SignzyContract360AggRequestDto contractRequestDto = new SignzyContract360AggRequestDto();
                contractRequestDto.setPdf(pdfResponseDto.getFile().getDirectURL());
                contractRequestDto.setContractName(leadInformation.getContactName());
                contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                contractRequestDto.setCallbackUrl(contract360CallbackUrl);
                contractRequestDto.setParallel(false);
                contractRequestDto.setSignerCallbackUrl(contract360SignerCallBackUrl);
                contractRequestDto.setWorkflow(true);
                contractRequestDto.seteSignProvider("cdsl");
//                contractRequestDto.setShowAadhaarNameOnSignature(true);
                List<SignerDetails360> signerDetailList = new ArrayList<>();
                SignerDetails360 signerDetail = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(620, 620));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(3));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);


                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                } else {
                    signerDetail.setSignerName(leadInformation.getMerchantName());
                    signerDetail.setSignerEmail(leadInformation.getContactEmail());
                    signerDetail.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

//                        signaturesDto1.setPageNo(Arrays.asList(1, 4,5,6,7,8,9,10,11,12,13));
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
//                        signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
//                        signaturesDto1.setyCoordinate(Arrays.asList(40, 40));
                    signaturesDto1.setxCoordinate(Arrays.asList(200, 200));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

//                    SignaturesDto signaturesDto2 = new SignaturesDto();
//                    signaturesDto2.setPageNo(List.of(2));
//                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
//                    signaturesDto2.setHeight(20);
//                    signaturesDto2.setWidth(60);
//                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
//                    signaturesDto2.setyCoordinate(Arrays.asList(120, 120));
//                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(3));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(620, 620));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(3));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(4));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(11));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);

                    contractRequestDto.setSignerdetail(signerDetailList);
                    contractRequestDto.setCustomerMailList(List.of(leadInformation.getContactEmail()));
                    contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                    logger.debug("contract request DTO -------------------- : {}", contractRequestDto);
                }
                Gson gson = new Gson();
                String request = gson.toJson(contractRequestDto);
                logger.info("contract request DTO -------------------- : {}", request);
                url = signzy360ContractBaseUrl + "contract/initiate";
                logger.info("url --------------- : {}", url);

                HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("contract Response out -- : {}", out.getBody());

                SignzyContract360ResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContract360ResponseDto.class);
                logger.info("contract responseDto -- : {}", responseDto);

                details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignatureType(responseDto.getSignerdetail().get(0).getSignatureType());
                details.setUrl(responseDto.getSignerdetail().get(0).getWorkflowUrl());
                details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                details.setContractId(responseDto.getContractId());
                details.setResponse(false);
                AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
//               leadInformation.setLeadStatus(leadStatusRepository.findLeadStatusById(Constants.PENDING_AGREEMENT_SIGNING));
                leadInformationRepository.save(leadInformation);
                logger.info("agreement Details Contract Id-- : {}", agreementDetails.getContractId());

                return Pair.of(1, responseDto);
            }
            return Pair.of(0, "pdf url not generated");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();

            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            return Pair.of(0, e.getMessage());

        }
    }

    public void sms(String smsTemplate, String phn, String templateId) {
        SmsRequestDto smsRequestDto = new SmsRequestDto();
        smsRequestDto.setTo(phn);
        smsRequestDto.setSender("BIJLPY");
        smsRequestDto.setService("SI");
        smsRequestDto.setCountry_code("+91");
        smsRequestDto.setTemplate_id(templateId);
        smsRequestDto.setMessage(smsTemplate);
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
    }

    @Override
    public Pair<String, Integer> pullContract(String contractId) throws IOException {
        AgreementDetails agreementDetail = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", contract360AccessToken);
            PullContractDto pullContractDto = new PullContractDto();
            pullContractDto.setContractId(contractId);
            HttpEntity<PullContractDto> entity = new HttpEntity(pullContractDto, headers);
            String url = pullContract;
            ResponseEntity<String> out = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (out.getStatusCodeValue() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(out.getBody());
                if (root.has("signerdetail") && root.get("signerdetail").isArray()) {
                    JsonNode signerNode = root.get("signerdetail").get(0);
                    String signerId = signerNode.get("signerId").asText();
                    AgreementDetails agreementDetails = documentService.findBySignerId(signerId);
                    if (agreementDetails != null) {
                        String status = signerNode.get("status").asText();
                        if (status.equalsIgnoreCase("SUCCESS")) {
                            agreementDetails.setResponse(true);
                            String signedFileUrl = signerNode.get("signerSignedContract").asText();
                            String signedFile = byteArrayMultiPartFileService.convertUrlToPdfForContract(signedFileUrl, agreementDetails.getLeadInformation(), "SignerSigned"); // convert URL to pdf and store pdf in server
                            agreementDetails.setSignerSignedPdfUrl(signedFileUrl);
                        } else {
                            return Pair.of(status, 1);
                        }
                        if (root.has("finalSignedContract") && root.has("auditCertificateUrl")) {
                            agreementDetails.setResponse(true);
                            String signedFileUrl = root.get("finalSignedContract").asText();
                            String finalSignedName = byteArrayMultiPartFileService.convertUrlToPdfForContract(signedFileUrl, agreementDetails.getLeadInformation(), "FinalSigned"); // convert URL to pdf and store pdf in server
                            agreementDetails.setFinalSignedName(finalSignedName);
                            String auditCertificate = root.get("auditCertificateUrl").asText();
                            String auditCertificateName = byteArrayMultiPartFileService.convertUrlToPdfForContract(auditCertificate, agreementDetails.getLeadInformation(), "AuditCertificate");
                            agreementDetails.setAuditCertificateName(auditCertificateName);
                            agreementDetails.setFinalSignedContract(signedFileUrl);
                            agreementDetails.setAuditCertificate(auditCertificate);
                        } else {
                            return Pair.of("auditCertificateUrl or finalSignedContract not generated", 2);
                        }
                        agreementDetail = agreementDetailsRepository.save(agreementDetails);
                        Gson g = new Gson();
                        logger.info("agreementDetails--{}", g.toJson(agreementDetail));
                        if (agreementDetail.getLeadInformation() != null) {
                            long value = Constants.AGREEMENT_SIGNING_COMPLETED;
                            LeadInformation leadInformation = documentService.updateLeadStatus(agreementDetails.getLeadInformation().getLeadId(), value);
                            leadInformation.setPullTriggered(true);
                            LeadInformation leadInformations = leadInformationRepository.save(leadInformation);
                            logger.info("leadInfo --: {}", leadInformations);
                        }
                        return Pair.of("success", 4);
                    }
                    return Pair.of("SignerId NOt Found", 6);
                }

            }
        } catch (HttpClientErrorException e) {
            return Pair.of(e.getLocalizedMessage(), 3);
        } catch (HttpServerErrorException e) {
            return Pair.of(e.getLocalizedMessage(), 3);
        }
        return Pair.of("Contract Request Failed", 3);
    }

    @Override
    public Pair<Integer, Object> signzy360AggContractMultiSign(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException, JsonProcessingException {

        ResponseEntity<String> out = null;
        String url = null;

        HttpHeaders httpHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();

        try {
            //generate pdf url
            logger.info("eSign pdf url part --- :");

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
            // aadhaar esign
            if (pdfResponseDto.getFile().getDirectURL() != null) {
                logger.info("esignpart --- : ");


                headers.add("Content-Type", "application/json");
                headers.add("Authorization", contract360AccessToken);

                SignzyContract360AggRequestDto contractRequestDto = new SignzyContract360AggRequestDto();
                contractRequestDto.setPdf(pdfResponseDto.getFile().getDirectURL());
                contractRequestDto.setContractName(leadInformation.getContactName());
                contractRequestDto.setContractExecuterName("SKILWORTH TECHNOLOGIES PVT LTD");
                contractRequestDto.setCallbackUrl(contract360CallbackUrl);
                contractRequestDto.setParallel(false);
                contractRequestDto.setSignerCallbackUrl(contract360SignerCallBackUrl);
                contractRequestDto.setWorkflow(true);
                contractRequestDto.seteSignProvider("cdsl");
                String[] nameOfAuthorizer = leadInformation.getNameOfAuthorizer() != null
                        ? leadInformation.getNameOfAuthorizer().split(",")
                        : new String[0];
                String[] emailForAuthorizer = leadInformation.getEmailIdForAuthorizer() != null
                        ? leadInformation.getEmailIdForAuthorizer().split(",")
                        : new String[0];
                List<SignerDetails360> signerDetailList = new ArrayList<>();
                SignerDetails360 signerDetail = new SignerDetails360();

                //getAuthorized Authorizers
                List<Integer> indexes = new ArrayList<>();
                List<String> authName = new ArrayList<>();
                List<String> authEmail = new ArrayList<>();
                String[] authorizer = leadInformation.getIsAuthorizedForAuthorizer().split(",");
                for (int i = 0; i < authorizer.length; i++) {
                    if (Objects.equals(authorizer[i], "true")) {
                        indexes.add(i);
                        authName.add((nameOfAuthorizer.length > i && !nameOfAuthorizer[i].trim().isEmpty())
                                ? nameOfAuthorizer[i].trim()
                                : "null");
                        authEmail.add((emailForAuthorizer.length > i && !emailForAuthorizer[i].trim().isEmpty())
                                ? emailForAuthorizer[i].trim()
                                : "null");
                    }
                }

                //action 1------------------------------------------
                if (action == Constants.AADHAAR_ESIGN) {
                    signerDetail.setSignerName(authName.size() >= 1 ? authName.get(0) : "null");
                    signerDetail.setSignerEmail(authEmail.size() >= 1 ? authEmail.get(0) : "null");
                    signerDetail.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);
                } else {
                    signerDetail.setSignerName(authName.size() >= 1 ? authName.get(0) : "null");
                    signerDetail.setSignerEmail(authEmail.size() >= 1 ? authEmail.get(0) : "null");
                    signerDetail.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400, 400));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710, 710));
                    signaturesList.add(signaturesDto5);

                    signerDetail.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail);
                }

                //signer2------------------------------------------------------

                SignerDetails360 signerDetail1 = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    signerDetail1.setSignerName(authName.size() >= 2 ? authName.get(1) : "null");
                    signerDetail1.setSignerEmail(authEmail.size() >= 2 ? authEmail.get(1) : "null");
                    ;
                    signerDetail1.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710 - 31, 710 - 31));
                    signaturesList.add(signaturesDto5);

                    signerDetail1.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail1);
                } else {
                    signerDetail1.setSignerName(authName.size() >= 2 ? authName.get(1) : "null");
                    signerDetail1.setSignerEmail(authEmail.size() >= 2 ? authEmail.get(1) : "null");
                    ;
                    signerDetail1.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
//                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400 - 144, 400 - 144));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710 - 31, 710 - 31));
                    signaturesList.add(signaturesDto5);

                    signerDetail1.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail1);
                }


                //action 3----------------------------------------

                SignerDetails360 signerDetail2 = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    signerDetail2.setSignerName(authName.size() >= 3 ? authName.get(2) : "null");
                    signerDetail2.setSignerEmail(authEmail.size() >= 3 ? authEmail.get(2) : "null");
                    signerDetail2.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400 - 270, 400 - 270));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400 - 258, 400 - 258));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400 - 258, 400 - 258));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400 - 258, 400 - 258));
                    signaturesDto4.setyCoordinate(Arrays.asList(280 - 144, 280 - 144));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400 - 258, 400 - 258));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710 - 72, 710 - 72));
                    signaturesList.add(signaturesDto5);

                    signerDetail2.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail2);
                } else {
                    signerDetail2.setSignerName(authName.size() >= 3 ? authName.get(2) : "null");
                    signerDetail2.setSignerEmail(authEmail.size() >= 3 ? authEmail.get(2) : "null");
                    ;
                    signerDetail2.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
//                        logger.info("pageNumber----------:{}", pageNumber);
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

//                        signaturesDto1.setPageNo(Arrays.asList(1, 4,5,6,7,8,9,10,11,12,13));
                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(400 - 270, 400 - 270));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400 - 258, 400 - 258));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400 - 258, 400 - 258));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400 - 258, 400 - 258));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400 - 258, 400 - 258));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710 - 72, 710 - 72));
                    signaturesList.add(signaturesDto5);

                    signerDetail2.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail2);
                }


                //action 4---------------------------------------------------------

                SignerDetails360 signerDetail3 = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {

                    signerDetail3.setSignerName(authName.size() >= 4 ? authName.get(3) : "null");
                    signerDetail3.setSignerEmail(authEmail.size() >= 4 ? authEmail.get(3) : "null");
                    signerDetail3.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(50, 50));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710 - 102, 710 - 102));
                    signaturesList.add(signaturesDto5);

                    signerDetail3.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail3);
                } else {
                    signerDetail3.setSignerName(authName.size() >= 4 ? authName.get(3) : "null");
                    signerDetail3.setSignerEmail(authEmail.size() >= 4 ? authEmail.get(3) : "null");
                    signerDetail3.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(50, 50));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710 - 102, 710 - 102));
                    signaturesList.add(signaturesDto5);
                    signerDetail3.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail3);
                }


                //action 5------------------------------------------------

                SignerDetails360 signerDetail4 = new SignerDetails360();
                if (action == Constants.AADHAAR_ESIGN) {
                    signerDetail4.setSignerName(authName.size() >= 5 ? authName.get(4) : "null");
                    signerDetail4.setSignerEmail(authEmail.size() >= 5 ? authEmail.get(4) : "null");
                    signerDetail4.setSignatureType("AADHAARESIGN-OTP");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }

                    signaturesDto1.setPageNo(pageNumber);
                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(0, 0));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710 - 133, 710 - 133));
                    signaturesList.add(signaturesDto5);

                    signerDetail4.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail4);
                } else {
                    signerDetail4.setSignerName(authName.size() >= 5 ? authName.get(4) : "null");
                    signerDetail4.setSignerEmail(authEmail.size() >= 5 ? authEmail.get(4) : "null");
                    signerDetail4.setSignatureType("SMARTESIGN");
                    List<SignaturesDto> signaturesList = new ArrayList<>();
                    SignaturesDto signaturesDto1 = new SignaturesDto();

                    List<Integer> pageNumber = new ArrayList<>();
                    if (pageCount != 0) {
                        for (int i = 1; i <= pageCount; i++) {
                            if (i != 3 && i != 4) {
                                pageNumber.add(i);
                            }
                        }
                        signaturesDto1.setPageNo(pageNumber);
                    } else {
                        if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L && leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
                        } else if (leadInformation.getYearlyRevenue() == Constants.YEARLY_REVENUE_LESS_THAN_20L || leadInformation.getMcc().isFuelMcc()) {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11, 12));
                        } else {
                            signaturesDto1.setPageNo(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 11));
                        }
                    }


                    signaturesDto1.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto1.setHeight(20);
                    signaturesDto1.setWidth(60);
                    signaturesDto1.setxCoordinate(Arrays.asList(0, 0));
                    signaturesDto1.setyCoordinate(Arrays.asList(25, 25));
                    signaturesList.add(signaturesDto1);

                    SignaturesDto signaturesDto2 = new SignaturesDto();
                    signaturesDto2.setPageNo(List.of(3));
                    signaturesDto2.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto2.setHeight(20);
                    signaturesDto2.setWidth(60);
                    signaturesDto2.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto2.setyCoordinate(Arrays.asList(73, 73));
                    signaturesList.add(signaturesDto2);

                    SignaturesDto signaturesDto3 = new SignaturesDto();
                    signaturesDto3.setPageNo(List.of(4));
                    signaturesDto3.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto3.setHeight(20);
                    signaturesDto3.setWidth(60);
                    signaturesDto3.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto3.setyCoordinate(Arrays.asList(670, 670));
                    signaturesList.add(signaturesDto3);

                    SignaturesDto signaturesDto4 = new SignaturesDto();
                    signaturesDto4.setPageNo(List.of(4));
                    signaturesDto4.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto4.setHeight(20);
                    signaturesDto4.setWidth(60);
                    signaturesDto4.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto4.setyCoordinate(Arrays.asList(280, 280));
                    signaturesList.add(signaturesDto4);

                    SignaturesDto signaturesDto6 = new SignaturesDto();
                    signaturesDto6.setPageNo(List.of(5));
                    signaturesDto6.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto6.setHeight(20);
                    signaturesDto6.setWidth(60);
                    signaturesDto6.setxCoordinate(Arrays.asList(400 - 300, 400 - 300));
                    signaturesDto6.setyCoordinate(Arrays.asList(570, 570));
                    signaturesList.add(signaturesDto6);

                    SignaturesDto signaturesDto5 = new SignaturesDto();
                    signaturesDto5.setPageNo(List.of(12));
                    signaturesDto5.setSignaturePosition(Collections.singletonList("CUSTOMIZE"));
                    signaturesDto5.setHeight(20);
                    signaturesDto5.setWidth(60);
                    signaturesDto5.setxCoordinate(Arrays.asList(350, 350));
                    signaturesDto5.setyCoordinate(Arrays.asList(710 - 133, 710 - 133));
                    signaturesList.add(signaturesDto5);

                    signerDetail4.setSignatures(signaturesList);

                    signerDetailList.add(signerDetail4);
                }
                contractRequestDto.setSignerdetail(signerDetailList.subList(0, indexes.size()));
                contractRequestDto.setCustomerMailList(authEmail);
                contractRequestDto.setInitiationEmailSubject("Agreement Signing");
                Gson gson = new Gson();
                String request = gson.toJson(contractRequestDto);
                logger.info("contract request DTO -------------------- : {}", request);
                url = signzy360ContractBaseUrl + "contract/initiate";
                logger.info("url --------------- : {}", url);
                logger.info("NameAuthorizer--{}", List.of(authName));
                logger.info("emailId Authoruzed--{}", List.of(authEmail));
                logger.info("emailId Authoruzed--{}", List.of(signerDetail));

                HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
                out = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                logger.info("contract Response out -- : {}", out.getBody());

                SignzyContract360ResponseDto responseDto = objectMapper.readValue(out.getBody(), SignzyContract360ResponseDto.class);
                logger.info("contract responseDto -- : {}", responseDto);

                details.setToken(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignerId(responseDto.getSignerdetail().get(0).getSignerId());
                details.setSignatureType(responseDto.getSignerdetail().get(0).getSignatureType());
                details.setUrl(responseDto.getSignerdetail().get(0).getWorkflowUrl());
                details.setInputFilePdfUrl(pdfResponseDto.getFile().getDirectURL());
                details.setContractId(responseDto.getContractId());
                details.setResponse(false);
                AgreementDetails agreementDetails = agreementDetailsRepository.save(details);
                logger.info("agreement Details Contract Id-- : {}", agreementDetails.getContractId());

                return Pair.of(1, responseDto);
            }
            return Pair.of(0, "pdf url not generated");
        } catch (HttpClientErrorException httpClientErrorException) {
            String clientErrorResponse = httpClientErrorException.getResponseBodyAsString();
            logger.info("clientErrorResponse -- : {}", clientErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(clientErrorResponse);
            logger.info("httpClientErrorException  -- : {}", clientErrorResponse);

            return Pair.of(0, jsonObject);
        } catch (HttpServerErrorException httpServerErrorException) {
            String serverErrorResponse = httpServerErrorException.getResponseBodyAsString();
            logger.info("serverErrorResponse -- : {}", serverErrorResponse);
            jsonObject = (JSONObject) jsonParser.parse(serverErrorResponse);
            logger.info("httpServerErrorException -- : {}", jsonObject);
            httpServerErrorException.printStackTrace();


            return Pair.of(0, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception -- : {}", e.getMessage());

            return Pair.of(0, e.getMessage());
        }
    }

    @Override
    public Pair<SignzyExtractionResponse, Integer> extractPan(MultipartFile files, long leadInformation, long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException {
        logger.info("start uploading to internal server :");
        Gson gson = new Gson();
        ResponseEntity<String> out = null;
        LeadInformation lead = null;
        SubDocumentType subDocument = null;
        UploadedDocuments model = null;
        subDocument = documentService.getSubDocumentDetails(subDocumentType);
        //Lead check
        lead = documentService.checkLead(leadInformation);
        if (lead != null) {
            //Document type check
            subDocument = documentService.getSubDocumentDetails(subDocumentType);
            if (subDocument != null) {
                logger.info("file -- : {}", files.getOriginalFilename());
                FileDto fileDto = new FileDto();
                logger.info("file -- : {}", files.getOriginalFilename());
                // file name generate
                String newFileName = lead.getLeadId() + "_" + subDocument.getName();
                Map<String, String> response = fileStorageService.storeFile(files, newFileName);
                fileDto.setFileName(response.get("fileName"));
                fileDto.setMimeType(response.get("mimeType"));
                fileDto.setFileNameOriginal(response.get("fileNameOriginal"));
                if (response != null) {
                    logger.info("files stored successfully ");

                    //Storing lead and uploaded documents table
                    model = documentService.storeLeadDocuments(lead, fileDto, subDocument, typeOfDocument, latlng);
                }
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", extractionAccessToken);
        ExtractDTO panExtractDTO = new ExtractDTO();
        List<String> data = new ArrayList<>();
        assert model != null;
        data.add(signzyDownloadUrl + model.getFileName());
        panExtractDTO.setFiles(data.toArray(new String[0]));
        panExtractDTO.setType("businessPan");
        HttpEntity<ExtractDTO> httpEntity = new HttpEntity<>(panExtractDTO, headers);
        try {
            out = restTemplate.exchange(signzyPanUrl, HttpMethod.POST, httpEntity, String.class);
        }catch (HttpClientErrorException e){
            logger.info("Client Error Exception--{}:",e.getLocalizedMessage());
            return Pair.of(new SignzyExtractionResponse(),2);
        }catch (HttpServerErrorException ex){
            logger.info("server Error Exception--{}:",ex.getLocalizedMessage());
            return Pair.of(new SignzyExtractionResponse(),1);
        }
        if (out.getStatusCodeValue() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode object = mapper.readTree(out.getBody());
            SignzyExtractionResponse signzyExtractionResponse = new SignzyExtractionResponse();
            signzyExtractionResponse.setNumber(object.path("result").path("number").asText() != null ? object.path("result").path("number").asText() : null);
            signzyExtractionResponse.setName(object.path("result").path("name").asText() != null ? object.path("result").path("name").asText() : null);
            signzyExtractionResponse.setDob(object.path("result").path("dateOfIssue").asText() != null ? object.path("result").path("dateOfIssue").asText() : null);
            signzyExtractionResponse.setAddress(object.path("result").path("summary").path("address").asText() != null ?
                    object.path("result").path("summary").path("address").asText() : null);
            signzyExtractionResponse.setFatherName(object.path("result").path("fatherName").asText() != null ? object.path("result").path("fatherName").asText() : null);
            signzyExtractionResponse.setUploadedDocuments(model);
            signzyExtractionResponse.setSubDocumentId(subDocumentType);
            SignzyExtractionData extractData = signzyExtractionDataRepo.findByleadAndSubDocument(lead.getLeadId(), subDocumentType);
            if (extractData != null) {
                extractData.setNumber(signzyExtractionResponse.getNumber());
                extractData.setName(signzyExtractionResponse.getName());
                extractData.setDob(signzyExtractionResponse.getDob());
                extractData.setAddress(signzyExtractionResponse.getAddress());
                extractData.setFatherName(signzyExtractionResponse.getFatherName());
                signzyExtractionDataRepo.save(extractData);
            } else {
                SignzyExtractionData signzyExtractionData = new SignzyExtractionData();
                signzyExtractionData.setNumber(signzyExtractionResponse.getNumber());
                signzyExtractionData.setName(signzyExtractionResponse.getName());
                signzyExtractionData.setDob(signzyExtractionResponse.getDob());
                signzyExtractionData.setAddress(signzyExtractionResponse.getAddress());
                signzyExtractionData.setFatherName(signzyExtractionResponse.getFatherName());
                signzyExtractionData.setLeadId(lead.getLeadId());
                signzyExtractionData.setSubDocument(subDocumentType);
                signzyExtractionDataRepo.save(signzyExtractionData);
            }
            return Pair.of(signzyExtractionResponse, 0);
        }
        return Pair.of(new SignzyExtractionResponse(), 1);
    }

    @Override
    public Pair<SignzyExtractionResponse, Integer> extractGst(MultipartFile files, long leadInformation, long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException {
        logger.info("start uploading to internal server :");
        Gson gson = new Gson();
        ResponseEntity<String> out = null;
        LeadInformation lead = null;
        SubDocumentType subDocument = null;
        UploadedDocuments model = null;
        subDocument = documentService.getSubDocumentDetails(subDocumentType);

        //Lead check
        lead = documentService.checkLead(leadInformation);
        if (lead != null) {
            //Document type check
            subDocument = documentService.getSubDocumentDetails(subDocumentType);
            if (subDocument != null) {
                logger.info("file -- : {}", files.getOriginalFilename());
                FileDto fileDto = new FileDto();
                logger.info("file -- : {}", files.getOriginalFilename());
                // file name generate
                String newFileName = lead.getLeadId() + "_" + subDocument.getName();
                Map<String, String> response = fileStorageService.storeFile(files, newFileName);
                fileDto.setFileName(response.get("fileName"));
                fileDto.setMimeType(response.get("mimeType"));
                fileDto.setFileNameOriginal(response.get("fileNameOriginal"));
                if (response != null) {
                    logger.info("files stored successfully ");

                    //Storing lead and uploaded documents table
                    model = documentService.storeLeadDocuments(lead, fileDto, subDocument, typeOfDocument, latlng);
                }
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", extractionAccessToken);
        ExtractDTO panExtractDTO = new ExtractDTO();
        List<String> data = new ArrayList<>();
        assert model != null;
        data.add(signzyDownloadUrl + model.getFileName());
        panExtractDTO.setFiles(data.toArray(new String[0]));
        HttpEntity<ExtractDTO> httpEntity = new HttpEntity<>(panExtractDTO, headers);
        logger.info("signzy Data---{}", gson.toJson(panExtractDTO));
        try {
            out = restTemplate.exchange(signzyGstUrl, HttpMethod.POST, httpEntity, String.class);
        }catch (HttpClientErrorException e){
            logger.info("Client Error Exception--{}:", e.getLocalizedMessage());
            return Pair.of(new SignzyExtractionResponse(), 2);
        } catch (HttpServerErrorException ex) {
            logger.info("server Error Exception--{}:", ex.getLocalizedMessage());
            return Pair.of(new SignzyExtractionResponse(), 1);
        }
        if (out != null && out.getStatusCodeValue() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode object = mapper.readTree(out.getBody());
            SignzyExtractionResponse signzyExtractionResponse = new SignzyExtractionResponse();
            signzyExtractionResponse.setNumber(object.path("results").path("gstDetails").path("gstNumber").asText() != null ? object.path("results").path("gstDetails").path("gstNumber").asText() : null);
            signzyExtractionResponse.setName(object.path("results").path("gstDetails").path("legalName").asText() != null ? object.path("results").path("gstDetails").path("legalName").asText() : null);
            signzyExtractionResponse.setDbaName(object.path("results").path("gstDetails").path("tradeName").asText() != null ? object.path("results").path("gstDetails").path("tradeName").asText() : null);
            signzyExtractionResponse.setAddress(object.path("results").path("gstDetails").path("addressOfPrincipalPlaceOfBusiness").path("address").asText() != null ?
                    object.path("results").path("gstDetails").path("addressOfPrincipalPlaceOfBusiness").path("address").asText() : null);
            signzyExtractionResponse.setSubDocumentId(subDocumentType);
            signzyExtractionResponse.setUploadedDocuments(model);
            SignzyExtractionData extractData = signzyExtractionDataRepo.findByleadAndSubDocument(lead.getLeadId(), subDocumentType);
            if (extractData != null) {
                extractData.setNumber(signzyExtractionResponse.getNumber());
                extractData.setName(signzyExtractionResponse.getName());
                extractData.setDbaName(signzyExtractionResponse.getDbaName());
                extractData.setAddress(signzyExtractionResponse.getAddress());
                signzyExtractionDataRepo.save(extractData);
            }else {
                SignzyExtractionData signzyExtractionData = new SignzyExtractionData();
                signzyExtractionData.setNumber(signzyExtractionResponse.getNumber());
                signzyExtractionData.setName(signzyExtractionResponse.getName());
                signzyExtractionData.setDbaName(signzyExtractionResponse.getDbaName());
                signzyExtractionData.setAddress(signzyExtractionResponse.getAddress());
                signzyExtractionData.setLeadId(lead.getLeadId());
                signzyExtractionData.setSubDocument(subDocumentType);
                signzyExtractionDataRepo.save(signzyExtractionData);
            }
            return Pair.of(signzyExtractionResponse,0);
        }
        return Pair.of(new SignzyExtractionResponse(),1);
    }

    @Override
    public Pair<SignzyExtractionResponse,Integer> extractAdhaar(MultipartFile files, MultipartFile fileMasked, long leadInformation, long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException {
        logger.info("start uploading to internal server :");
        Gson gson = new Gson();
        ResponseEntity<String> out = null;
        LeadInformation lead = null;
        SubDocumentType subDocument = null;
        UploadedDocuments model = null;
        String unMaskedFileName=null;
        String MaskedFileName=null;
        subDocument = documentService.getSubDocumentDetails(subDocumentType);

        //Lead check
        lead = documentService.checkLead(leadInformation);
        if (lead != null) {
            //Document type check
            subDocument = documentService.getSubDocumentDetails(subDocumentType);
            if (subDocument != null) {
                logger.info("file -- : {}", files.getOriginalFilename());
                FileDto fileDto = new FileDto();
                logger.info("file -- : {}", files.getOriginalFilename());
                // file name generate
                String newFileName1 = lead.getLeadId() + "_" + subDocument.getName() + "UnMasked";
                Map<String, String> response = fileStorageService.storeFile(files, newFileName1);
                unMaskedFileName = response.get("fileName");
                logger.info("fileMasked  -- : {}", fileMasked.getOriginalFilename());
                logger.info("fileMasked -- : {}", fileMasked.getOriginalFilename());
                // file name generate
                String newFileName = lead.getLeadId() + "_" + subDocument.getName();
                Map<String, String> response1 = fileStorageService.storeFile(fileMasked, newFileName);
                if (response1 != null) {
                    fileDto.setFileName(response1.get("fileName"));
                    fileDto.setMimeType(response1.get("mimeType"));
                    fileDto.setFileNameOriginal(response1.get("fileNameOriginal"));
                    logger.info("files stored successfully ");
                    //Storing lead and uploaded documents table
                    model = documentService.storeLeadDocuments(lead, fileDto, subDocument, typeOfDocument, latlng);
                    MaskedFileName = response1.get("fileName");
                }
                logger.info("file Name Masked--{}", MaskedFileName);
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", extractionAccessToken);
        ExtractDTO extractDTO = new ExtractDTO();
        assert model != null;
        extractDTO.setFrontUrl(signzyDownloadUrl + unMaskedFileName);
//        extractDTO.setFrontUrl("https://uatapp2.bijlipay.co.in:8027/document/file/downloadFile/2014_AadhaarCardFront_1764846413491.png");
        HttpEntity<ExtractDTO> httpEntity = new HttpEntity<>(extractDTO, headers);
        logger.info("signzy Payload---{}", gson.toJson(extractDTO));
        try {
            out = restTemplate.exchange(signzyAdhaarUrl, HttpMethod.POST, httpEntity, String.class);
            documentService.deleteUnMaskedImage(unMaskedFileName);
        }catch (HttpClientErrorException e) {
            logger.info("Client Error Exception--{}:",e.getLocalizedMessage());
            documentService.deleteUnMaskedImage(unMaskedFileName);
            return Pair.of(new SignzyExtractionResponse(),2);
        }catch (HttpServerErrorException ex){
            logger.info("server Error Exception--{}:",ex.getLocalizedMessage());
            documentService.deleteUnMaskedImage(unMaskedFileName);
            return Pair.of(new SignzyExtractionResponse(),1);
        }

        if (out != null && out.getStatusCodeValue() == 200) {
            documentService.deleteUnMaskedImage(unMaskedFileName);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode object = mapper.readTree(out.getBody());
            SignzyExtractionResponse signzyExtractionResponse = new SignzyExtractionResponse();
            signzyExtractionResponse.setNumber(object.get("poi").asText() != null ? object.get("poi").asText() : null);
            signzyExtractionResponse.setName(object.get("name").asText() != null ? object.get("name").asText() : null);
            signzyExtractionResponse.setDob(object.get("dob").asText() != null ? object.get("dob").asText() : null);
            signzyExtractionResponse.setAddress(object.get("address").asText() != null ? object.get("address").asText() : null);
            signzyExtractionResponse.setFatherName(object.get("guardianName").asText() != null ? object.get("guardianName").asText() : null);
            signzyExtractionResponse.setSubDocumentId(subDocumentType);
            signzyExtractionResponse.setUploadedDocuments(model);
            SignzyExtractionData extractData = signzyExtractionDataRepo.findByleadAndSubDocument(lead.getLeadId(), subDocumentType);
            if (extractData != null) {
                extractData.setNumber(signzyExtractionResponse.getNumber());
                extractData.setName(signzyExtractionResponse.getName());
                extractData.setDob(signzyExtractionResponse.getDob());
                extractData.setAddress(signzyExtractionResponse.getAddress());
                extractData.setFatherName(signzyExtractionResponse.getFatherName());
                signzyExtractionDataRepo.save(extractData);
            } else {
                SignzyExtractionData signzyExtractionData = new SignzyExtractionData();
                signzyExtractionData.setNumber(signzyExtractionResponse.getNumber());
                signzyExtractionData.setName(signzyExtractionResponse.getName());
                signzyExtractionData.setDob(signzyExtractionResponse.getDob());
                signzyExtractionData.setAddress(signzyExtractionResponse.getAddress());
                signzyExtractionData.setFatherName(signzyExtractionResponse.getFatherName());
                signzyExtractionData.setLeadId(lead.getLeadId());
                signzyExtractionData.setSubDocument(subDocumentType);
                signzyExtractionDataRepo.save(signzyExtractionData);
            }
            logger.info("data Log--{}:", signzyExtractionResponse.toString());
            return Pair.of(signzyExtractionResponse, 0);
        }
        return Pair.of(new SignzyExtractionResponse(), 1);
    }



    @Override
    public Pair<SignzyExtractionResponse, Integer> extractDocument(MultipartFile files, long leadInformation, long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException {
        logger.info("start uploading to internal server :");
        Gson gson = new Gson();
        ResponseEntity<String> out = null;
        LeadInformation lead = null;
        SubDocumentType subDocument = null;
        UploadedDocuments model = null;
        subDocument = documentService.getSubDocumentDetails(subDocumentType);

        // Lead check
        lead = documentService.checkLead(leadInformation);
        if (lead != null) {
            // Document type check
            subDocument = documentService.getSubDocumentDetails(subDocumentType);
            if (subDocument != null) {
                logger.info("file -- : {}", files.getOriginalFilename());
                FileDto fileDto = new FileDto();
                // file name generate
                String newFileName = lead.getLeadId() + "_" + subDocument.getName();
                Map<String, String> response = fileStorageService.storeFile(files, newFileName);
                logger.info("storeFile response map: {}", response);
                logger.info("file -- : {}", files.getOriginalFilename());
                fileDto.setFileName(response.get("fileName"));
                fileDto.setMimeType(response.get("mimeType"));
                fileDto.setFileNameOriginal(response.get("fileNameOriginal"));
                if (response != null) {
                    logger.info("files stored successfully ");
                    model = documentService.storeLeadDocuments(lead, fileDto, subDocument, typeOfDocument, latlng);
                }
            }
        }

        // ─── Call /verifai/extract instead of Signzy ────────────────────────
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-DocExtract-Key", NewextractionAccessToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Build request body
//        assert model != null;
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("files", new String[]{signzyDownloadUrl + model.getFileName()});
//        requestBody.put("model", "sonnet");
//
//        logger.info("Verifai Extract Request -- files: {}", signzyDownloadUrl + model.getFileName());
//
//        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
        if (model == null) {
            logger.error("Document not stored -- leadId {} subDocumentType {}", leadInformation, subDocumentType);
            return Pair.of(new SignzyExtractionResponse(), 1);
        }

        String original = files.getOriginalFilename();


        byte[] fileBytes;
        try {
            fileBytes = files.getBytes();
        } catch (IOException e) {
            logger.error("Could not read uploaded file -- {}", e.getMessage());
            return Pair.of(new SignzyExtractionResponse(), 1);
        }
        logger.info("file bytes length -- {}", fileBytes.length);

        Resource fileResource = new ByteArrayResource(fileBytes) {
            @Override public String getFilename() { return original; }
        };

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.parseMediaType(
                files.getContentType() != null ? files.getContentType() : "application/octet-stream"));
        HttpEntity<Resource> filePart = new HttpEntity<>(fileResource, fileHeaders);

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", filePart);
        body.add("doctype", "extract");
        body.add("model", "sonnet");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-DocExtract-Key", NewextractionAccessToken);

        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        logger.info("Verifai Upload Request -- file: {} ({} bytes)", original, fileBytes.length);
        try {
            out = restTemplate.exchange(verifaiUploadUrl, HttpMethod.POST, httpEntity, String.class);
        } catch (HttpClientErrorException e) {
            logger.info("Client Error Exception--{}:", e.getLocalizedMessage());
            logger.error("Response Body: {}", e.getResponseBodyAsString());
            return Pair.of(new SignzyExtractionResponse(), 2);
        } catch (HttpServerErrorException ex) {
            logger.info("Server Error Exception--{}:", ex.getLocalizedMessage());
            logger.error("Response Body: {}", ex.getResponseBodyAsString());        // ← add this too
            return Pair.of(new SignzyExtractionResponse(), 1);
        } catch (ResourceAccessException rae) {
            logger.error("Connection/IO Error calling Verifai upload URL: {} -- {}", verifaiUploadUrl, rae.getMessage());
            return Pair.of(new SignzyExtractionResponse(), 1);
        } catch (Exception e) {
            logger.error("Unexpected error calling Verifai upload URL: {} -- {}", verifaiUploadUrl, e.getMessage(), e);
            return Pair.of(new SignzyExtractionResponse(), 1);
        }

        // ─── Parse response based on documentType ───────────────────────────
        if (out != null && out.getStatusCodeValue() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(out.getBody());

            String documentType = root.path("document_type").asText();
            logger.info("Verifai detected documentType -- {}", documentType);

            SignzyExtractionResponse signzyExtractionResponse = new SignzyExtractionResponse();

            switch (documentType) {
                case "pan":
                    JsonNode panResult = root.path("fields");
                    signzyExtractionResponse.setNumber(panResult.path("pan_number").asText(null));
                    signzyExtractionResponse.setName(panResult.path("full_name").asText(null));
                    signzyExtractionResponse.setDob(panResult.path("date_of_birth").asText(null));
                    signzyExtractionResponse.setFatherName(panResult.path("father_name").asText(null));
                    break;


                case "aadhaar":
                    JsonNode aadhaarFields = root.path("fields");
                    signzyExtractionResponse.setNumber(aadhaarFields.path("aadhaar_number").asText(null));
                    signzyExtractionResponse.setAddress(aadhaarFields.path("address").asText(null));
                    if (subDocumentType == 2) {
                        // back page prints the S/O line into full_name and carries no DOB
                        signzyExtractionResponse.setName(aadhaarFields.path("full_name").asText(null));
                        signzyExtractionResponse.setDob(aadhaarFields.path("date_of_birth").asText(null));
                        signzyExtractionResponse.setFatherName(aadhaarFields.path("father_name").asText(null));
                    }
                    break;

                case "gst_registration_certificate":
                    JsonNode gstDetails = root.path("fields");
                    signzyExtractionResponse.setNumber(gstDetails.path("registration_number").asText(null));
                    signzyExtractionResponse.setName(gstDetails.path("legal_name").asText(null));
                    signzyExtractionResponse.setDbaName(gstDetails.path("trade_name").asText(null));
                    logger.info("dba name check ---{}",(gstDetails.path("trade_name").asText(null)));
                    // the address arrives four different ways: under three different key names,
                    // sometimes as a string, sometimes as an object, sometimes flat in "fields"
                    JsonNode addrNode = gstDetails.path("address_of_principal_place_of_business");
                    if (addrNode.isMissingNode()) addrNode = gstDetails.path("address_of_principal_place");
                    if (addrNode.isMissingNode()) addrNode = gstDetails.path("address");
                    if (addrNode.isMissingNode()) addrNode = gstDetails;

                    if (addrNode.isObject()) {
                        StringBuilder sb = new StringBuilder();
                        for (String k : new String[]{"floor_no", "building_plot_number", "building_no_flat_no",
                                "premises_building_name", "name_of_premises_building", "road_street",
                                "nearby_landmark", "locality", "locality_sub_locality",
                                "city_town_village", "district", "state", "pin_code"}) {
                            String v = addrNode.path(k).asText(null);
                            if (v == null || v.trim().isEmpty()) continue;
                            // skip a part already present, e.g. locality "VRINDAVAN" and city "Vrindavan"
                            if (sb.toString().toUpperCase().contains(v.trim().toUpperCase())) continue;
                            if (sb.length() > 0) sb.append(", ");
                            sb.append(v.trim());
                        }
                        signzyExtractionResponse.setAddress(sb.length() > 0 ? sb.toString() : null);
                    } else {
                        signzyExtractionResponse.setAddress(addrNode.asText(null));
                    }

                    String gstIssue = gstDetails.path("date_of_issue_of_certificate").asText(null);
                    if (gstIssue == null) gstIssue = gstDetails.path("date_of_issue").asText(null);
                    signzyExtractionResponse.setDateOfIssue(gstIssue);
                    break;

                case "driving_license":
                    JsonNode getdl = root.path("fields");

                    String dlNumber = getdl.path("dl_number").asText(null);
                    signzyExtractionResponse.setNumber(dlNumber != null ? dlNumber.replace(" ", "") : null);

                    String dlDob = getdl.path("date_of_birth").asText(null);
                    signzyExtractionResponse.setDob(dlDob != null ? dlDob.replace('-', '/') : null);
                    signzyExtractionResponse.setAddress(getdl.path("address").asText(null));
                    if (subDocumentType == 4) {
                        signzyExtractionResponse.setName(getdl.path("full_name").asText(null));
                        signzyExtractionResponse.setDateOfIssue(getdl.path("date_of_issue").asText(null));
                        signzyExtractionResponse.setDateOfExpiry(getdl.path("valid_till").asText(null));
                        signzyExtractionResponse.setFatherName(getdl.path("father_husband_name").asText(null));
                    }
                    break;

                case "passport":
                    JsonNode genericResult = root.path("fields");

                    String passportName = genericResult.path("full_name").asText(null);
                    if (passportName == null) {
                        String given = genericResult.path("given_name").asText(null);
                        String surname = genericResult.path("surname").asText(null);
                        if (given != null && surname != null) passportName = given + " " + surname;
                        else if (given != null) passportName = given;
                        else passportName = surname;
                    }
                    signzyExtractionResponse.setName(passportName);

                    if (subDocumentType == 7) {
                        // last page carries the File No. that Signzy verification needs
                        signzyExtractionResponse.setNumber(genericResult.path("file_number").asText(null));
                    } else {
                        signzyExtractionResponse.setNumber(genericResult.path("passport_number").asText(null));
                    }

                    signzyExtractionResponse.setDob(genericResult.path("date_of_birth").asText(null));
                    signzyExtractionResponse.setAddress(genericResult.path("address").asText(null));
                    signzyExtractionResponse.setFatherName(genericResult.path("father_name").asText(null));
                    signzyExtractionResponse.setDateOfIssue(genericResult.path("date_of_issue").asText(null));
                    signzyExtractionResponse.setDateOfExpiry(genericResult.path("date_of_expiry").asText(null));
                    break;

                case "voter_id":
                    JsonNode voterResult = root.path("fields");

                    String voterNumber = voterResult.path("elector_id").asText(null);
                    if (voterNumber == null) voterNumber = voterResult.path("card_number").asText(null);
                    if (voterNumber == null) voterNumber = voterResult.path("epic_number").asText(null);
                    if (voterNumber == null) voterNumber = voterResult.path("voter_id").asText(null);
                    if (voterNumber == null) voterNumber = voterResult.path("voter_id_number").asText(null);
                    if (voterNumber == null) voterNumber = voterResult.path("identity_card_number").asText(null);
                    if (voterNumber == null) voterNumber = voterResult.path("number").asText(null);
                    signzyExtractionResponse.setNumber(voterNumber);

                    signzyExtractionResponse.setAddress(voterResult.path("address").asText(null));
                    signzyExtractionResponse.setDateOfIssue(voterResult.path("date_of_issue").asText(null));

                    if (subDocumentType == 8) {
                        signzyExtractionResponse.setName(voterResult.path("full_name").asText(null));

                        String voterFather = voterResult.path("father_name").asText(null);
                        if (voterFather == null) voterFather = voterResult.path("relation_name").asText(null);
                        if (voterFather == null) voterFather = voterResult.path("father_husband_name").asText(null);
                        signzyExtractionResponse.setFatherName(voterFather);
                    }
                    break;



                case "unknown":
                default:
                    logger.warn("Verifai could not identify document type -- routing to manual review");
                    return Pair.of(new SignzyExtractionResponse(), 2);
            }

            signzyExtractionResponse.setSubDocumentId(subDocumentType);
            signzyExtractionResponse.setUploadedDocuments(model);

            // ─── Save to DB ──────────────────────────────────────────────────
            SignzyExtractionData extractData = signzyExtractionDataRepo.findByleadAndSubDocument(lead.getLeadId(), subDocumentType);
            if (extractData != null) {
                extractData.setNumber(signzyExtractionResponse.getNumber());
                extractData.setName(signzyExtractionResponse.getName());
                extractData.setDob(signzyExtractionResponse.getDob());
                extractData.setAddress(signzyExtractionResponse.getAddress());
                extractData.setFatherName(signzyExtractionResponse.getFatherName());
                extractData.setDateOfIssue(signzyExtractionResponse.getDateOfIssue());
                extractData.setDateOfExpiry(signzyExtractionResponse.getDateOfExpiry());
                extractData.setDbaName(signzyExtractionResponse.getDbaName()==null?"null":signzyExtractionResponse.getDbaName());
                signzyExtractionDataRepo.save(extractData);
            } else {
                SignzyExtractionData signzyExtractionData = new SignzyExtractionData();
                signzyExtractionData.setNumber(signzyExtractionResponse.getNumber());
                signzyExtractionData.setName(signzyExtractionResponse.getName());
                signzyExtractionData.setDob(signzyExtractionResponse.getDob());
                signzyExtractionData.setAddress(signzyExtractionResponse.getAddress());
                signzyExtractionData.setFatherName(signzyExtractionResponse.getFatherName());
                signzyExtractionData.setDateOfIssue(signzyExtractionResponse.getDateOfIssue());
                signzyExtractionData.setDateOfExpiry(signzyExtractionResponse.getDateOfExpiry());
                signzyExtractionData.setLeadId(lead.getLeadId());
                signzyExtractionData.setSubDocument(subDocumentType);
                signzyExtractionData.setDbaName(signzyExtractionResponse.getDbaName()==null?"null":signzyExtractionResponse.getDbaName());
                signzyExtractionDataRepo.save(signzyExtractionData);
            }

            return Pair.of(signzyExtractionResponse, 0);
        }

        return Pair.of(new SignzyExtractionResponse(), 1);
    }

    @Override
    public Pair<SignzyExtractionResponse, Integer> extractAdhaarDocument(MultipartFile files, MultipartFile fileMasked, long leadInformation, long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException {
        logger.info("start uploading to internal server :");
        ResponseEntity<String> out = null;
        LeadInformation lead = null;
        SubDocumentType subDocument = null;
        UploadedDocuments model = null;
        String unMaskedFileName = null;
        subDocument = documentService.getSubDocumentDetails(subDocumentType);

        // Lead check
        lead = documentService.checkLead(leadInformation);
        if (lead != null) {
            // Document type check
            subDocument = documentService.getSubDocumentDetails(subDocumentType);
            if (subDocument != null) {
                logger.info("file -- : {}", files.getOriginalFilename());
                FileDto fileDto = new FileDto();

                // UNMASKED -- stored temporarily, sent to extraction, deleted again below
                String newFileName1 = lead.getLeadId() + "_" + subDocument.getName() + "UnMasked";
                Map<String, String> responseUnMasked = fileStorageService.storeFile(files, newFileName1);
                unMaskedFileName = responseUnMasked != null ? responseUnMasked.get("fileName") : null;
                logger.info("file Name UnMasked--{}", unMaskedFileName);

                // MASKED -- this is the document that is kept and shown in web and Android
                logger.info("fileMasked -- : {}", fileMasked.getOriginalFilename());
                String newFileName = lead.getLeadId() + "_" + subDocument.getName();
                Map<String, String> response = fileStorageService.storeFile(fileMasked, newFileName);
                logger.info("storeFile response map: {}", response);
                if (response != null) {
                    fileDto.setFileName(response.get("fileName"));
                    fileDto.setMimeType(response.get("mimeType"));
                    fileDto.setFileNameOriginal(response.get("fileNameOriginal"));
                    logger.info("files stored successfully ");
                    model = documentService.storeLeadDocuments(lead, fileDto, subDocument, typeOfDocument, latlng);
                }
            }
        }

        if (model == null) {
            logger.error("Document not stored -- leadId {} subDocumentType {}", leadInformation, subDocumentType);
            if (unMaskedFileName != null) documentService.deleteUnMaskedImage(unMaskedFileName);
            return Pair.of(new SignzyExtractionResponse(), 1);
        }

        // the UNMASKED image is what goes to extraction
        String original = files.getOriginalFilename();

        byte[] fileBytes;
        try {
            fileBytes = files.getBytes();
        } catch (IOException e) {
            logger.error("Could not read uploaded file -- {}", e.getMessage());
            if (unMaskedFileName != null) documentService.deleteUnMaskedImage(unMaskedFileName);
            return Pair.of(new SignzyExtractionResponse(), 1);
        }
        logger.info("file bytes length -- {}", fileBytes.length);

        Resource fileResource = new ByteArrayResource(fileBytes) {
            @Override public String getFilename() { return original; }
        };

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.parseMediaType(
                files.getContentType() != null ? files.getContentType() : "application/octet-stream"));
        HttpEntity<Resource> filePart = new HttpEntity<>(fileResource, fileHeaders);

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", filePart);
        body.add("model", "sonnet");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-DocExtract-Key", NewextractionAccessToken);

        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        logger.info("Extraction Request -- url: {} file: {} ({} bytes)", verifaiUploadUrl, original, fileBytes.length);

        try {
            out = restTemplate.exchange(verifaiUploadUrl, HttpMethod.POST, httpEntity, String.class);
            if (unMaskedFileName != null) documentService.deleteUnMaskedImage(unMaskedFileName);
        } catch (HttpClientErrorException e) {
            logger.info("Client Error Exception--{}:", e.getLocalizedMessage());
            logger.error("Response Body: {}", e.getResponseBodyAsString());
            if (unMaskedFileName != null) documentService.deleteUnMaskedImage(unMaskedFileName);
            return Pair.of(new SignzyExtractionResponse(), 2);
        } catch (HttpServerErrorException ex) {
            logger.info("Server Error Exception--{}:", ex.getLocalizedMessage());
            logger.error("Response Body: {}", ex.getResponseBodyAsString());
            if (unMaskedFileName != null) documentService.deleteUnMaskedImage(unMaskedFileName);
            return Pair.of(new SignzyExtractionResponse(), 1);
        } catch (ResourceAccessException rae) {
            logger.error("Connection/IO Error calling Verifai upload URL: {} -- {}", verifaiUploadUrl, rae.getMessage());
            if (unMaskedFileName != null) documentService.deleteUnMaskedImage(unMaskedFileName);
            return Pair.of(new SignzyExtractionResponse(), 1);
        } catch (Exception e) {
            logger.error("Unexpected error calling Verifai upload URL: {} -- {}", verifaiUploadUrl, e.getMessage(), e);
            if (unMaskedFileName != null) documentService.deleteUnMaskedImage(unMaskedFileName);
            return Pair.of(new SignzyExtractionResponse(), 1);
        }

        // ─── Parse response based on document_type ──────────────────────────
        if (out != null && out.getStatusCodeValue() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(out.getBody());

            String documentType = root.path("document_type").asText();
            logger.info("detected documentType -- {}", documentType);

            SignzyExtractionResponse signzyExtractionResponse = new SignzyExtractionResponse();

            switch (documentType) {
                case "aadhaar":
                    JsonNode aadhaarFields = root.path("fields");
                    signzyExtractionResponse.setNumber(aadhaarFields.path("aadhaar_number").asText(null));
                    signzyExtractionResponse.setAddress(aadhaarFields.path("address").asText(null));
                    if (subDocumentType == 2) {
                        // front page only -- the back page puts the S/O line into full_name and has no DOB
                        signzyExtractionResponse.setName(aadhaarFields.path("full_name").asText(null));
                        signzyExtractionResponse.setDob(aadhaarFields.path("date_of_birth").asText(null));
                        signzyExtractionResponse.setFatherName(aadhaarFields.path("father_name").asText(null));
                    }
                    break;

                case "unknown":
                default:
                    logger.warn("could not identify document type -- routing to manual review");
                    return Pair.of(new SignzyExtractionResponse(), 2);
            }

            signzyExtractionResponse.setSubDocumentId(subDocumentType);
            signzyExtractionResponse.setUploadedDocuments(model);

            // ─── Save to DB ──────────────────────────────────────────────────
            SignzyExtractionData extractData = signzyExtractionDataRepo.findByleadAndSubDocument(lead.getLeadId(), subDocumentType);
            if (extractData != null) {
                extractData.setNumber(signzyExtractionResponse.getNumber());
                extractData.setName(signzyExtractionResponse.getName());
                extractData.setDob(signzyExtractionResponse.getDob());
                extractData.setAddress(signzyExtractionResponse.getAddress());
                extractData.setFatherName(signzyExtractionResponse.getFatherName());
                signzyExtractionDataRepo.save(extractData);
            } else {
                SignzyExtractionData signzyExtractionData = new SignzyExtractionData();
                signzyExtractionData.setNumber(signzyExtractionResponse.getNumber());
                signzyExtractionData.setName(signzyExtractionResponse.getName());
                signzyExtractionData.setDob(signzyExtractionResponse.getDob());
                signzyExtractionData.setAddress(signzyExtractionResponse.getAddress());
                signzyExtractionData.setFatherName(signzyExtractionResponse.getFatherName());
                signzyExtractionData.setLeadId(lead.getLeadId());
                signzyExtractionData.setSubDocument(subDocumentType);
                signzyExtractionDataRepo.save(signzyExtractionData);
            }

            return Pair.of(signzyExtractionResponse, 0);
        }

        return Pair.of(new SignzyExtractionResponse(), 1);
    }

}
