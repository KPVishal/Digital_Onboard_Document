package com.bijlipay.model.dto.signzy.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.actuate.trace.http.Include;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignzyContractRequestDto {
    private String pdfurl;
    private String multiPages;
    private List<Integer> pageNo;
    private String authMode;
    private String provider;
    private List<SignerDetail> signerdetail;
    private List<String> customerMailList;
    private String fileFormat;
    private String logoUrl;
    private String contractName;
    private String contractExecuterName;
    private String initiationEmailSubject;
    private String callbackUrl;
    private String callbackUrlAuthPresent;
    private String callbackUrlAuthType;
    private CallbackUrlAuthCredentials callbackUrlAuthCredentials;
    private List<String> customerCallbackExtraParameters;
    private String signerCallbackUrl;
    private String signerCallbackUrlAuthPresent;
    private String signerCallbackUrlAuthType;
    private CallbackUrlAuthCredentials signerCallbackUrlAuthCredentials;
    private String iacceptSubject;
    private String iacceptMessage;
    private List<String> iacceptCCEmailIds;
    private List<String> iacceptNegativeTexts;
    private String userReminderTime;
    private String redirectUrl;
    private String redirectTime;
    private String contractTtl;
    private String emailPdfCustomNameFormat;
    private String isParallel;
    private String nameMatchThreshold;
    private String allowSignerGenderMatch;
    private String allowSignerYOBMatch;
    private String showTextBelowNormalEsign;
    private EStampDto estamp;
    private GlobalEsignature globalesignature;

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }

    public String getMultiPages() {
        return multiPages;
    }

    public void setMultiPages(String multiPages) {
        this.multiPages = multiPages;
    }

    public List<Integer> getPageNo() {
        return pageNo;
    }

    public void setPageNo(List<Integer> pageNo) {
        this.pageNo = pageNo;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public List<SignerDetail> getSignerdetail() {
        return signerdetail;
    }

    public void setSignerdetail(List<SignerDetail> signerdetail) {
        this.signerdetail = signerdetail;
    }

    public List<String> getCustomerMailList() {
        return customerMailList;
    }

    public void setCustomerMailList(List<String> customerMailList) {
        this.customerMailList = customerMailList;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractExecuterName() {
        return contractExecuterName;
    }

    public void setContractExecuterName(String contractExecuterName) {
        this.contractExecuterName = contractExecuterName;
    }

    public String getInitiationEmailSubject() {
        return initiationEmailSubject;
    }

    public void setInitiationEmailSubject(String initiationEmailSubject) {
        this.initiationEmailSubject = initiationEmailSubject;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackUrlAuthPresent() {
        return callbackUrlAuthPresent;
    }

    public void setCallbackUrlAuthPresent(String callbackUrlAuthPresent) {
        this.callbackUrlAuthPresent = callbackUrlAuthPresent;
    }

    public String getCallbackUrlAuthType() {
        return callbackUrlAuthType;
    }

    public void setCallbackUrlAuthType(String callbackUrlAuthType) {
        this.callbackUrlAuthType = callbackUrlAuthType;
    }

    public CallbackUrlAuthCredentials getCallbackUrlAuthCredentials() {
        return callbackUrlAuthCredentials;
    }

    public void setCallbackUrlAuthCredentials(CallbackUrlAuthCredentials callbackUrlAuthCredentials) {
        this.callbackUrlAuthCredentials = callbackUrlAuthCredentials;
    }

    public List<String> getCustomerCallbackExtraParameters() {
        return customerCallbackExtraParameters;
    }

    public void setCustomerCallbackExtraParameters(List<String> customerCallbackExtraParameters) {
        this.customerCallbackExtraParameters = customerCallbackExtraParameters;
    }

    public String getSignerCallbackUrl() {
        return signerCallbackUrl;
    }

    public void setSignerCallbackUrl(String signerCallbackUrl) {
        this.signerCallbackUrl = signerCallbackUrl;
    }

    public String getSignerCallbackUrlAuthPresent() {
        return signerCallbackUrlAuthPresent;
    }

    public void setSignerCallbackUrlAuthPresent(String signerCallbackUrlAuthPresent) {
        this.signerCallbackUrlAuthPresent = signerCallbackUrlAuthPresent;
    }

    public String getSignerCallbackUrlAuthType() {
        return signerCallbackUrlAuthType;
    }

    public void setSignerCallbackUrlAuthType(String signerCallbackUrlAuthType) {
        this.signerCallbackUrlAuthType = signerCallbackUrlAuthType;
    }

    public CallbackUrlAuthCredentials getSignerCallbackUrlAuthCredentials() {
        return signerCallbackUrlAuthCredentials;
    }

    public void setSignerCallbackUrlAuthCredentials(CallbackUrlAuthCredentials signerCallbackUrlAuthCredentials) {
        this.signerCallbackUrlAuthCredentials = signerCallbackUrlAuthCredentials;
    }

    public String getIacceptSubject() {
        return iacceptSubject;
    }

    public void setIacceptSubject(String iacceptSubject) {
        this.iacceptSubject = iacceptSubject;
    }

    public String getIacceptMessage() {
        return iacceptMessage;
    }

    public void setIacceptMessage(String iacceptMessage) {
        this.iacceptMessage = iacceptMessage;
    }

    public List<String> getIacceptCCEmailIds() {
        return iacceptCCEmailIds;
    }

    public void setIacceptCCEmailIds(List<String> iacceptCCEmailIds) {
        this.iacceptCCEmailIds = iacceptCCEmailIds;
    }

    public List<String> getIacceptNegativeTexts() {
        return iacceptNegativeTexts;
    }

    public void setIacceptNegativeTexts(List<String> iacceptNegativeTexts) {
        this.iacceptNegativeTexts = iacceptNegativeTexts;
    }

    public String getUserReminderTime() {
        return userReminderTime;
    }

    public void setUserReminderTime(String userReminderTime) {
        this.userReminderTime = userReminderTime;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectTime() {
        return redirectTime;
    }

    public void setRedirectTime(String redirectTime) {
        this.redirectTime = redirectTime;
    }

    public String getContractTtl() {
        return contractTtl;
    }

    public void setContractTtl(String contractTtl) {
        this.contractTtl = contractTtl;
    }

    public String getEmailPdfCustomNameFormat() {
        return emailPdfCustomNameFormat;
    }

    public void setEmailPdfCustomNameFormat(String emailPdfCustomNameFormat) {
        this.emailPdfCustomNameFormat = emailPdfCustomNameFormat;
    }

    public String getIsParallel() {
        return isParallel;
    }

    public void setIsParallel(String isParallel) {
        this.isParallel = isParallel;
    }

    public String getNameMatchThreshold() {
        return nameMatchThreshold;
    }

    public void setNameMatchThreshold(String nameMatchThreshold) {
        this.nameMatchThreshold = nameMatchThreshold;
    }

    public String getAllowSignerGenderMatch() {
        return allowSignerGenderMatch;
    }

    public void setAllowSignerGenderMatch(String allowSignerGenderMatch) {
        this.allowSignerGenderMatch = allowSignerGenderMatch;
    }

    public String getAllowSignerYOBMatch() {
        return allowSignerYOBMatch;
    }

    public void setAllowSignerYOBMatch(String allowSignerYOBMatch) {
        this.allowSignerYOBMatch = allowSignerYOBMatch;
    }

    public String getShowTextBelowNormalEsign() {
        return showTextBelowNormalEsign;
    }

    public void setShowTextBelowNormalEsign(String showTextBelowNormalEsign) {
        this.showTextBelowNormalEsign = showTextBelowNormalEsign;
    }

    public EStampDto getEstamp() {
        return estamp;
    }

    public void setEstamp(EStampDto estamp) {
        this.estamp = estamp;
    }

    public GlobalEsignature getGlobalesignature() {
        return globalesignature;
    }

    public void setGlobalesignature(GlobalEsignature globalesignature) {
        this.globalesignature = globalesignature;
    }
}
