package com.bijlipay.model.dto.signzy.contract;

import java.util.List;

public class SignzyContractResponseDto {
    private String pdfurl;
    private List<SignerDetail> signerdetail;
    private String userReminderTime;
    private String redirectUrl;
    private String redirectTime;
    private List<String> customerMailList;
    private String id;
    private String customerId;
    private String multiPages;
//    private String pageNo;

    private List<Integer> pageNo;

    private String fileFormat;
    private String logoUrl;
    private String callbackUrl;
    private String signerCallbackUrl;
    private String iacceptSubject;
    private String iacceptMessage;
    private List<String> iacceptCCEmailIds;
    private List<String> iacceptNegativeTexts;
    private long contractTtl;
    private String emailPdfCustomNameFormat;
    private String isParallel;
    private String allowSignerGenderMatch;
    private String allowSignerYOBMatch;
    private String showTextBelowNormalEsign;
    private String callbackUrlAuthPresent;
    private String contractName;
    private String useCustomSmtpServer;
    private List<String> customerCallbackExtraParameters;
    private String contractExecuterName;
    private String initiationEmailSubject;
    private String isWhiteLabel;
    private String iacceptToEmailId;
    private String authMode;
    private String provider;
    private String locationCaptureMethod;
    private String pageCount;

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }

    public List<SignerDetail> getSignerdetail() {
        return signerdetail;
    }

    public void setSignerdetail(List<SignerDetail> signerdetail) {
        this.signerdetail = signerdetail;
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

    public List<String> getCustomerMailList() {
        return customerMailList;
    }

    public void setCustomerMailList(List<String> customerMailList) {
        this.customerMailList = customerMailList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMultiPages() {
        return multiPages;
    }

    public void setMultiPages(String multiPages) {
        this.multiPages = multiPages;
    }

//    public String getPageNo() {
//        return pageNo;
//    }
//
//    public void setPageNo(String pageNo) {
//        this.pageNo = pageNo;
//    }


    public List<Integer> getPageNo() {
        return pageNo;
    }

    public void setPageNo(List<Integer> pageNo) {
        this.pageNo = pageNo;
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

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getSignerCallbackUrl() {
        return signerCallbackUrl;
    }

    public void setSignerCallbackUrl(String signerCallbackUrl) {
        this.signerCallbackUrl = signerCallbackUrl;
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

    public long getContractTtl() {
        return contractTtl;
    }

    public void setContractTtl(long contractTtl) {
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

    public String getCallbackUrlAuthPresent() {
        return callbackUrlAuthPresent;
    }

    public void setCallbackUrlAuthPresent(String callbackUrlAuthPresent) {
        this.callbackUrlAuthPresent = callbackUrlAuthPresent;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getUseCustomSmtpServer() {
        return useCustomSmtpServer;
    }

    public void setUseCustomSmtpServer(String useCustomSmtpServer) {
        this.useCustomSmtpServer = useCustomSmtpServer;
    }

    public List<String> getCustomerCallbackExtraParameters() {
        return customerCallbackExtraParameters;
    }

    public void setCustomerCallbackExtraParameters(List<String> customerCallbackExtraParameters) {
        this.customerCallbackExtraParameters = customerCallbackExtraParameters;
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

    public String getIsWhiteLabel() {
        return isWhiteLabel;
    }

    public void setIsWhiteLabel(String isWhiteLabel) {
        this.isWhiteLabel = isWhiteLabel;
    }

    public String getIacceptToEmailId() {
        return iacceptToEmailId;
    }

    public void setIacceptToEmailId(String iacceptToEmailId) {
        this.iacceptToEmailId = iacceptToEmailId;
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

    public String getLocationCaptureMethod() {
        return locationCaptureMethod;
    }

    public void setLocationCaptureMethod(String locationCaptureMethod) {
        this.locationCaptureMethod = locationCaptureMethod;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }
}
