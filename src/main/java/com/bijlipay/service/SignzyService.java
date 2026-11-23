package com.bijlipay.service;

import com.bijlipay.model.AgreementDetails;
import com.bijlipay.model.LeadInformation;
import com.bijlipay.model.UploadedDocuments;
import com.bijlipay.model.dto.SignzyExtractionResponse;
import com.bijlipay.model.dto.SignzyLoginResponseDto;
import com.bijlipay.model.dto.signzy.digilocker.AadhaarResponseDTO;
import com.bijlipay.model.dto.signzy.digilocker.DigilockerResponseDTO;
import com.bijlipay.model.dto.signzy.esign.SignzyAadhaarEsignRequestDto;
import com.bijlipay.model.dto.signzy.extraction.SignzyExtractionRequestDto;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchRequestDto;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchResponseDto;
import com.bijlipay.model.dto.signzy.fetch.SignzyFetchResultDto;
import com.bijlipay.model.dto.signzy.identity.SignzyIdentityRequestDto;
import com.bijlipay.model.dto.signzy.identity.SignzyIdentityResponseDto;
import com.bijlipay.model.dto.signzy.pennydrop.AccountTransferRequestDto;
import com.bijlipay.model.dto.signzy.pennydrop.AccountTransferResponseDto;
import com.bijlipay.model.dto.signzy.pennydrop.VerifyTransferRequestDto;
import com.bijlipay.model.dto.signzy.verification.SignzyVerificationRequestDto;
import com.bijlipay.model.dto.signzy.verification.SignzyVerificationResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.parser.ParseException;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SignzyService {

    SignzyLoginResponseDto generateAccessToken();

    ResponseEntity<Object> deActivateAccessToken(String id) throws HttpClientErrorException;

    Pair<Integer,SignzyIdentityResponseDto> signzyIdentification(SignzyIdentityRequestDto signzyIdentityRequestDto) throws JsonProcessingException, ParseException;

    Pair<Integer,Object> signzyExtraction(SignzyExtractionRequestDto signzyExtractionRequestDto , List<UploadedDocuments> uploadedDocuments) throws ParseException;

    Pair<Integer, SignzyVerificationResponseDto> signzyVerification(SignzyVerificationRequestDto signzyVerificationRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException, JsonProcessingException;

    Pair<Integer, SignzyFetchResponseDto> signzyFetch(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException, IOException;

//    Pair<Integer, Object> signzyAadhaarEsign(String eSignName , MultipartFile multipartFile) throws JsonProcessingException, ParseException;

    Pair<Integer, Object> signzyEsign(int action , String eSignName , MultipartFile multipartFile, AgreementDetails agreementDetails, int pageCount) throws JsonProcessingException, ParseException;

    void sendSmsToMerchantForAgreementSign(AgreementDetails details);

    SignzyLoginResponseDto generateEsignAccessToken();

    ResponseEntity<Object> deActivateEsignAccessToken(String id) throws HttpClientErrorException;

    Pair<Integer, Object> signzyAccountTransfer(LeadInformation leadInformation , AccountTransferRequestDto accountTransferRequestDto) throws JsonProcessingException, ParseException;

    Pair<Integer, SignzyFetchResponseDto> digiLocker(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException, JsonProcessingException;

    Pair<Integer, DigilockerResponseDTO> digiLockerNew(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws ParseException, JsonProcessingException;

    Pair<Integer, SignzyFetchResponseDto> getAadhaarDetails(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws JsonProcessingException, ParseException;

    Pair<Integer, AadhaarResponseDTO> getAadhaarDetailsNew(SignzyFetchRequestDto signzyFetchRequestDto, List<UploadedDocuments> uploadedDocuments) throws JsonProcessingException, ParseException;

    Pair<Integer, Object> signzyContract(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException;

    Pair<Integer, Object> signzyAggContract(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException;

    ResponseEntity<Object> deActivateContractAccessToken(String id);

    SignzyLoginResponseDto generateContractAccessToken();

    Pair<Integer, Object> signzy360AggContract(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException, JsonProcessingException;

    Pair<Integer, Object> signzy360AggContract1(int action, MultipartFile multipartFile, LeadInformation li, AgreementDetails details, int pageCount) throws ParseException;

    Pair<Integer, Object> signzy360AggContractDoQR(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException;

    Pair<Integer, Object> signzy360AggContractDoPOS(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException;

    Pair<Integer, Object> signzy360AggContractUtil(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException;

    Pair<Integer, Object> signzy360AggContractDo(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException;

    Pair<Integer, Object> signzy360AggContractDoPOSQRSB(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException;

    Pair<String, Integer> pullContract(String contractId) throws IOException;

    Pair<Integer, Object> signzy360AggContractMultiSign(int action, MultipartFile multipartFile, LeadInformation leadInformation, AgreementDetails details, int pageCount) throws ParseException, JsonProcessingException;


    Pair<SignzyExtractionResponse,Integer> extractPan(MultipartFile files, long leadInformation, long subDocumentType,String typeOfDocument, Optional<String> latlng) throws JsonProcessingException;

    Pair<SignzyExtractionResponse,Integer> extractGst(MultipartFile files, long leadInformation, long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException;

    Pair<SignzyExtractionResponse,Integer> extractAdhaar(MultipartFile files, MultipartFile fileMasked, long leadInformation, long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException;

    Pair<SignzyExtractionResponse,Integer> extractDocument(MultipartFile files, long leadInformation,long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException;

    Pair<SignzyExtractionResponse,Integer> extractAdhaarDocument(MultipartFile files, MultipartFile fileMasked, long leadInformation, long subDocumentType, String typeOfDocument, Optional<String> latlng) throws JsonProcessingException;


//    Pair<Integer, Object> verifyTransfer(LeadInformation leadInformation, VerifyTransferRequestDto verifyTransferRequestDto) throws JsonProcessingException, ParseException;
}
