package com.bijlipay.model.dto.signzy.contract;

import com.bijlipay.model.dto.signzy.esign.SignaturesDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONPropertyIgnore;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignerDetail {
    private String signerName;
    private String signerEmail;
    private String signerUid;
    private String signerPincode;
    private String signerGender;
    private String signerDateOfBirth;
    private String signerUidLastFourDigits;
    private List<String> signatureOptions;
    private String[] additionalVerification;
    private String showId;
    private String idImage;
    private String preVerification;
    private String faceMatchThreshold;
    private String signerMobile;
    private String signerOtpMobile;
    private String isOptional;
    private String signaturePosition;
    private String xCoordinate;
    private String yCoordinate;
    private String height;
    private String width;
    private String tnc;
    private String location;
    private String reason;
    private String signerId;
    private String contractUrl;
    private List<SignaturesDto> signatures;


    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getSignerEmail() {
        return signerEmail;
    }

    public void setSignerEmail(String signerEmail) {
        this.signerEmail = signerEmail;
    }

    public String getSignerUid() {
        return signerUid;
    }

    public void setSignerUid(String signerUid) {
        this.signerUid = signerUid;
    }

    public String getSignerPincode() {
        return signerPincode;
    }

    public void setSignerPincode(String signerPincode) {
        this.signerPincode = signerPincode;
    }

    public String getSignerGender() {
        return signerGender;
    }

    public void setSignerGender(String signerGender) {
        this.signerGender = signerGender;
    }

    public String getSignerDateOfBirth() {
        return signerDateOfBirth;
    }

    public void setSignerDateOfBirth(String signerDateOfBirth) {
        this.signerDateOfBirth = signerDateOfBirth;
    }

    public String getSignerUidLastFourDigits() {
        return signerUidLastFourDigits;
    }

    public void setSignerUidLastFourDigits(String signerUidLastFourDigits) {
        this.signerUidLastFourDigits = signerUidLastFourDigits;
    }

    public List<String> getSignatureOptions() {
        return signatureOptions;
    }

    public void setSignatureOptions(List<String> signatureOptions) {
        this.signatureOptions = signatureOptions;
    }

    public String[] getAdditionalVerification() {
        return additionalVerification;
    }

    public void setAdditionalVerification(String[] additionalVerification) {
        this.additionalVerification = additionalVerification;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getPreVerification() {
        return preVerification;
    }

    public void setPreVerification(String preVerification) {
        this.preVerification = preVerification;
    }

    public String getFaceMatchThreshold() {
        return faceMatchThreshold;
    }

    public void setFaceMatchThreshold(String faceMatchThreshold) {
        this.faceMatchThreshold = faceMatchThreshold;
    }

    public String getSignerMobile() {
        return signerMobile;
    }

    public void setSignerMobile(String signerMobile) {
        this.signerMobile = signerMobile;
    }

    public String getSignerOtpMobile() {
        return signerOtpMobile;
    }

    public void setSignerOtpMobile(String signerOtpMobile) {
        this.signerOtpMobile = signerOtpMobile;
    }

    public String getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(String isOptional) {
        this.isOptional = isOptional;
    }

    public String getSignaturePosition() {
        return signaturePosition;
    }

    public void setSignaturePosition(String signaturePosition) {
        this.signaturePosition = signaturePosition;
    }

    public String getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(String xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public String getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<SignaturesDto> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignaturesDto> signatures) {
        this.signatures = signatures;
    }

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }
}
