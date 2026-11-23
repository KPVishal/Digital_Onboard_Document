package com.bijlipay.model.dto;

import com.bijlipay.model.*;

import java.util.Date;
import java.util.List;

public class LeadDto {
    private String city;
    private String amountCollected;
    private String recurringFees;
    private String intlCc;
    private String branch;
    private String referenceNumber;
    private String contactAddress;

    private String contactNumber;
    private String superPremiumCc;
    private Plan plan;
    private String deviceCount;
    private boolean isActive;
    private String vasInstanceMapping;
//    private Device device;
    private String device;

    private String leadNumber;
    private String otpVerified;
    private String stdCc;
    private boolean posEnabled;
    private String paymentOption;
    private String debitLessthanAmount;
    private String setupFees;
    private String isBijlipaySwich;
    private String contactName;
    private String corpCc;
    private String planName;
//    private MerchantCategory merchantCategory;
    private String merchantCategory;
    private String premiumCc;
    private String debitGreaterthanAmount;
//    private LeadSource leadSource;
private String leadSource;

    private Region region;
    private int merchantRefCode;
    private String applicationNumber;
    private boolean reject;
//    private MerchantType merchantType;
    private String merchantType;

    private String leadCategory;
    private String email;
    private String marsReason;
    private String merchantName;
//    private MerchantCategoryCode mcc;
private String mcc;

    private boolean isQR;
    private boolean isSoundBox;
    private boolean isPos;
    private boolean icAcceptance;

    private double tipPercentage;
//    private SimProvider simProvider;
    private String simProvider;

    private int settlementMode;    // 1.Auto  2.Manual
    private boolean isOmcConvenienceFees;
    private int yearlyRevenue;        //  1. yearlyRevenue<20L  2. yearlyRevenue>20L
    private String legalName;
    private String dbaName;
    private int shop_ownership_status;       //1.Rented  2.Owned
    private String weekDayTimingStart;
    private String weekdayTimingEnd;
    private String weekendTimingStart;
    private String weekendTimingEnd;
    private double maxMonthlyCardVolume;
    private double avgMonthlyCardvolume;
    private Date memberSince;
    private int havePosMpos;
    private double debitMdr;
    private double creditMdr;
    private Date transactionDate;
    private String paymentContactNumber;
    private boolean isPaymentSuccess;
    private String ifscCode;
    private String bank;
    private String accountNumber;
    private String reason;
    private long pincode;
    private String state;
    private Date ownerDOB;
    private String ownerFirstName;
    private String ownerLastName;
    private String merchantStateName;
    private String onboardingVendor;
    private String settlementType;
    private String turnOverLastFinancialYear;
    private double avgBillAmount;
    private boolean creditCardBlock;
    private String mdrPlanName;
    private LeadSourceMCCAccountTypeMapping leadSourceMCCAccountTypeMapping;
    private String domesticDebitUpTo2000;
    private String domesticDebitAbove2000;
    private String standardOrClassic;
    private String premiumOrPlatinum;
    private String superPremiumOrSignature;
    private String commercialOrCorporate;
    private String internationalDebitCard;
    private String internationalCreditCard;
    private String onus;
    private String diners;
    private String cashAtPos;
    private String masterPass;
    private String mdrPlanCode;
    private String rentalPlanCode;
//    private MarsMdrCharges marsMdrCharges;
    private String marsMdrCharges;

//    private MarsRentalPlans marsRentalPlans;
    private String marsRentalPlans;


    private boolean isProfessionalLead;

    private String panNumber;

    private String gstNumber;

    private String natureOfBusiness;

    private String aggregator;

    private String merchantDistrictName;

    private String merchantSubDistrictName;

    private String merchantVillageName;

    private String merchantTier;
    private int categoryType;
    private String vpaName;

    private String rid;

    private String caseNumber;

    private String zone;
    private String storeAddress;
    private String merchantPan;
    private String merchantDob;
    private int institutionCode;
    private String marsRegion;
    private String constitution;
    private String establishYear;
    private String registerNumber;
    private String maximumMonthlyUsage;
    private String merchantTypeCode;
    private String advanceRentMode;
    private String deviceOwnedBy;
    private String devicePrice;
    private String emiTenure;
    private String emiStartDate;
    private int gracePeriod;
    private String installationFee;
    private int noOfMonthRentPaidInAdvance;
    private String rentalMode;
    private String rentalType;
    private String totalEmiAmount;
    private String terminalModeCode;
    private String terminalType;
    private int bqrEnabled;
    private String cashAtPosEnabled;
    private int linkpaymentFlag;
    private String matmAllowed;
    private String preAuth;
    private int saleFlag;
    private String settlement;
    private String tipEnabled;
    private int upichargeslipEnabled;
    private int upiFlag;
    private String accountType;
    private String marsDevice;

    private List<String> vpa;

    private boolean posQr;

    private String language;
    private Long soundId;

    private boolean isSQR;
    private String upiCreditAbove2000;
    private String upiCreditUpto2000;
    private String upiAbove2000;
    private String upiUpto2000;
    private String amexDomestic;
    private String amexInternational;

    private int noOfAuthorizer;

    private int noOfStaff;

    private String bussinessInLocation;

    private int stockverificationConfirmation;

    private String remarks;
    private String nameOfAuthorizer;
    private String emailIdForAuthorizer;
    private String phoneForAuthorizer;

    private String addressForAuthorizer;

    private String panNumberForAuthorizer;

    private String isAuthorizedForAuthorizer;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(String amountCollected) {
        this.amountCollected = amountCollected;
    }

    public String getRecurringFees() {
        return recurringFees;
    }

    public void setRecurringFees(String recurringFees) {
        this.recurringFees = recurringFees;
    }

    public String getIntlCc() {
        return intlCc;
    }

    public void setIntlCc(String intlCc) {
        this.intlCc = intlCc;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSuperPremiumCc() {
        return superPremiumCc;
    }

    public void setSuperPremiumCc(String superPremiumCc) {
        this.superPremiumCc = superPremiumCc;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(String deviceCount) {
        this.deviceCount = deviceCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getVasInstanceMapping() {
        return vasInstanceMapping;
    }

    public void setVasInstanceMapping(String vasInstanceMapping) {
        this.vasInstanceMapping = vasInstanceMapping;
    }

//    public Device getDevice() {
//        return device;
//    }
//
//    public void setDevice(Device device) {
//        this.device = device;
//    }

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public String getOtpVerified() {
        return otpVerified;
    }

    public void setOtpVerified(String otpVerified) {
        this.otpVerified = otpVerified;
    }

    public String getStdCc() {
        return stdCc;
    }

    public void setStdCc(String stdCc) {
        this.stdCc = stdCc;
    }

    public boolean isPosEnabled() {
        return posEnabled;
    }

    public void setPosEnabled(boolean posEnabled) {
        this.posEnabled = posEnabled;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getDebitLessthanAmount() {
        return debitLessthanAmount;
    }

    public void setDebitLessthanAmount(String debitLessthanAmount) {
        this.debitLessthanAmount = debitLessthanAmount;
    }

    public String getSetupFees() {
        return setupFees;
    }

    public void setSetupFees(String setupFees) {
        this.setupFees = setupFees;
    }

    public String getIsBijlipaySwich() {
        return isBijlipaySwich;
    }

    public void setIsBijlipaySwich(String isBijlipaySwich) {
        this.isBijlipaySwich = isBijlipaySwich;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCorpCc() {
        return corpCc;
    }

    public void setCorpCc(String corpCc) {
        this.corpCc = corpCc;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
//    public MerchantCategory getMerchantCategory() {
//        return merchantCategory;
//    }
//
//    public void setMerchantCategory(MerchantCategory merchantCategory) {
//        this.merchantCategory = merchantCategory;
//    }

    public String getPremiumCc() {
        return premiumCc;
    }

    public void setPremiumCc(String premiumCc) {
        this.premiumCc = premiumCc;
    }

    public String getDebitGreaterthanAmount() {
        return debitGreaterthanAmount;
    }

    public void setDebitGreaterthanAmount(String debitGreaterthanAmount) {
        this.debitGreaterthanAmount = debitGreaterthanAmount;
    }
//    public LeadSource getLeadSource() {
//        return leadSource;
//    }
//
//    public void setLeadSource(LeadSource leadSource) {
//        this.leadSource = leadSource;
//    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getMerchantRefCode() {
        return merchantRefCode;
    }

    public void setMerchantRefCode(int merchantRefCode) {
        this.merchantRefCode = merchantRefCode;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

//    public MerchantType getMerchantType() {
//        return merchantType;
//    }
//
//    public void setMerchantType(MerchantType merchantType) {
//        this.merchantType = merchantType;
//    }

    public String getLeadCategory() {
        return leadCategory;
    }

    public void setLeadCategory(String leadCategory) {
        this.leadCategory = leadCategory;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMarsReason() {
        return marsReason;
    }

    public void setMarsReason(String marsReason) {
        this.marsReason = marsReason;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

//    public MerchantCategoryCode getMcc() {
//        return mcc;
//    }
//
//    public void setMcc(MerchantCategoryCode mcc) {
//        this.mcc = mcc;
//    }

    public boolean isQR() {
        return isQR;
    }

    public void setQR(boolean QR) {
        isQR = QR;
    }

    public boolean isSoundBox() {
        return isSoundBox;
    }

    public void setSoundBox(boolean soundBox) {
        isSoundBox = soundBox;
    }

    public boolean isIcAcceptance() {
        return icAcceptance;
    }

    public void setIcAcceptance(boolean icAcceptance) {
        this.icAcceptance = icAcceptance;
    }

    public double getTipPercentage() {
        return tipPercentage;
    }

    public void setTipPercentage(double tipPercentage) {
        this.tipPercentage = tipPercentage;
    }

//    public SimProvider getSimProvider() {
//        return simProvider;
//    }
//
//    public void setSimProvider(SimProvider simProvider) {
//        this.simProvider = simProvider;
//    }

    public int getSettlementMode() {
        return settlementMode;
    }

    public void setSettlementMode(int settlementMode) {
        this.settlementMode = settlementMode;
    }

    public boolean isOmcConvenienceFees() {
        return isOmcConvenienceFees;
    }

    public void setOmcConvenienceFees(boolean omcConvenienceFees) {
        isOmcConvenienceFees = omcConvenienceFees;
    }

    public int getYearlyRevenue() {
        return yearlyRevenue;
    }

    public void setYearlyRevenue(int yearlyRevenue) {
        this.yearlyRevenue = yearlyRevenue;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getDbaName() {
        return dbaName;
    }

    public void setDbaName(String dbaName) {
        this.dbaName = dbaName;
    }

    public int getShop_ownership_status() {
        return shop_ownership_status;
    }

    public void setShop_ownership_status(int shop_ownership_status) {
        this.shop_ownership_status = shop_ownership_status;
    }

    public String getWeekDayTimingStart() {
        return weekDayTimingStart;
    }

    public void setWeekDayTimingStart(String weekDayTimingStart) {
        this.weekDayTimingStart = weekDayTimingStart;
    }

    public String getWeekdayTimingEnd() {
        return weekdayTimingEnd;
    }

    public void setWeekdayTimingEnd(String weekdayTimingEnd) {
        this.weekdayTimingEnd = weekdayTimingEnd;
    }

    public String getWeekendTimingStart() {
        return weekendTimingStart;
    }

    public void setWeekendTimingStart(String weekendTimingStart) {
        this.weekendTimingStart = weekendTimingStart;
    }

    public String getWeekendTimingEnd() {
        return weekendTimingEnd;
    }

    public void setWeekendTimingEnd(String weekendTimingEnd) {
        this.weekendTimingEnd = weekendTimingEnd;
    }

    public double getMaxMonthlyCardVolume() {
        return maxMonthlyCardVolume;
    }

    public void setMaxMonthlyCardVolume(double maxMonthlyCardVolume) {
        this.maxMonthlyCardVolume = maxMonthlyCardVolume;
    }

    public double getAvgMonthlyCardvolume() {
        return avgMonthlyCardvolume;
    }

    public void setAvgMonthlyCardvolume(double avgMonthlyCardvolume) {
        this.avgMonthlyCardvolume = avgMonthlyCardvolume;
    }

    public Date getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(Date memberSince) {
        this.memberSince = memberSince;
    }

    public int getHavePosMpos() {
        return havePosMpos;
    }

    public void setHavePosMpos(int havePosMpos) {
        this.havePosMpos = havePosMpos;
    }

    public double getDebitMdr() {
        return debitMdr;
    }

    public void setDebitMdr(double debitMdr) {
        this.debitMdr = debitMdr;
    }

    public double getCreditMdr() {
        return creditMdr;
    }

    public void setCreditMdr(double creditMdr) {
        this.creditMdr = creditMdr;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPaymentContactNumber() {
        return paymentContactNumber;
    }

    public void setPaymentContactNumber(String paymentContactNumber) {
        this.paymentContactNumber = paymentContactNumber;
    }

    public boolean isPaymentSuccess() {
        return isPaymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        isPaymentSuccess = paymentSuccess;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getOwnerDOB() {
        return ownerDOB;
    }

    public void setOwnerDOB(Date ownerDOB) {
        this.ownerDOB = ownerDOB;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getMerchantStateName() {
        return merchantStateName;
    }

    public void setMerchantStateName(String merchantStateName) {
        this.merchantStateName = merchantStateName;
    }

    public String getOnboardingVendor() {
        return onboardingVendor;
    }

    public void setOnboardingVendor(String onboardingVendor) {
        this.onboardingVendor = onboardingVendor;
    }

    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    public String getTurnOverLastFinancialYear() {
        return turnOverLastFinancialYear;
    }

    public void setTurnOverLastFinancialYear(String turnOverLastFinancialYear) {
        this.turnOverLastFinancialYear = turnOverLastFinancialYear;
    }

    public double getAvgBillAmount() {
        return avgBillAmount;
    }

    public void setAvgBillAmount(double avgBillAmount) {
        this.avgBillAmount = avgBillAmount;
    }

    public boolean isCreditCardBlock() {
        return creditCardBlock;
    }

    public void setCreditCardBlock(boolean creditCardBlock) {
        this.creditCardBlock = creditCardBlock;
    }

    public boolean isPos() {
        return isPos;
    }

    public void setPos(boolean pos) {
        isPos = pos;
    }

    public String getMdrPlanName() {
        return mdrPlanName;
    }

    public void setMdrPlanName(String mdrPlanName) {
        this.mdrPlanName = mdrPlanName;
    }

    public LeadSourceMCCAccountTypeMapping getLeadSourceMCCAccountTypeMapping() {
        return leadSourceMCCAccountTypeMapping;
    }

    public void setLeadSourceMCCAccountTypeMapping(LeadSourceMCCAccountTypeMapping leadSourceMCCAccountTypeMapping) {
        this.leadSourceMCCAccountTypeMapping = leadSourceMCCAccountTypeMapping;
    }

    public String getDomesticDebitUpTo2000() {
        return domesticDebitUpTo2000;
    }

    public void setDomesticDebitUpTo2000(String domesticDebitUpTo2000) {
        this.domesticDebitUpTo2000 = domesticDebitUpTo2000;
    }

    public String getDomesticDebitAbove2000() {
        return domesticDebitAbove2000;
    }

    public void setDomesticDebitAbove2000(String domesticDebitAbove2000) {
        this.domesticDebitAbove2000 = domesticDebitAbove2000;
    }

    public String getStandardOrClassic() {
        return standardOrClassic;
    }

    public void setStandardOrClassic(String standardOrClassic) {
        this.standardOrClassic = standardOrClassic;
    }

    public String getPremiumOrPlatinum() {
        return premiumOrPlatinum;
    }

    public void setPremiumOrPlatinum(String premiumOrPlatinum) {
        this.premiumOrPlatinum = premiumOrPlatinum;
    }

    public String getSuperPremiumOrSignature() {
        return superPremiumOrSignature;
    }

    public void setSuperPremiumOrSignature(String superPremiumOrSignature) {
        this.superPremiumOrSignature = superPremiumOrSignature;
    }

    public String getCommercialOrCorporate() {
        return commercialOrCorporate;
    }

    public void setCommercialOrCorporate(String commercialOrCorporate) {
        this.commercialOrCorporate = commercialOrCorporate;
    }

    public String getInternationalDebitCard() {
        return internationalDebitCard;
    }

    public void setInternationalDebitCard(String internationalDebitCard) {
        this.internationalDebitCard = internationalDebitCard;
    }

    public String getInternationalCreditCard() {
        return internationalCreditCard;
    }

    public void setInternationalCreditCard(String internationalCreditCard) {
        this.internationalCreditCard = internationalCreditCard;
    }

    public String getOnus() {
        return onus;
    }

    public void setOnus(String onus) {
        this.onus = onus;
    }

    public String getDiners() {
        return diners;
    }

    public void setDiners(String diners) {
        this.diners = diners;
    }

    public String getCashAtPos() {
        return cashAtPos;
    }

    public void setCashAtPos(String cashAtPos) {
        this.cashAtPos = cashAtPos;
    }

    public String getMasterPass() {
        return masterPass;
    }

    public void setMasterPass(String masterPass) {
        this.masterPass = masterPass;
    }

    public String getMdrPlanCode() {
        return mdrPlanCode;
    }

    public void setMdrPlanCode(String mdrPlanCode) {
        this.mdrPlanCode = mdrPlanCode;
    }

    public String getRentalPlanCode() {
        return rentalPlanCode;
    }

    public void setRentalPlanCode(String rentalPlanCode) {
        this.rentalPlanCode = rentalPlanCode;
    }

//    public MarsMdrCharges getMarsMdrCharges() {
//        return marsMdrCharges;
//    }
//
//    public void setMarsMdrCharges(MarsMdrCharges marsMdrCharges) {
//        this.marsMdrCharges = marsMdrCharges;
//    }
//
//    public MarsRentalPlans getMarsRentalPlans() {
//        return marsRentalPlans;
//    }
//
//    public void setMarsRentalPlans(MarsRentalPlans marsRentalPlans) {
//        this.marsRentalPlans = marsRentalPlans;
//    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public boolean isProfessionalLead() {
        return isProfessionalLead;
    }

    public void setProfessionalLead(boolean professionalLead) {
        isProfessionalLead = professionalLead;
    }

    public String getAggregator() {
        return aggregator;
    }

    public void setAggregator(String aggregator) {
        this.aggregator = aggregator;
    }

    public String getMerchantDistrictName() {
        return merchantDistrictName;
    }

    public void setMerchantDistrictName(String merchantDistrictName) {
        this.merchantDistrictName = merchantDistrictName;
    }

    public String getMerchantSubDistrictName() {
        return merchantSubDistrictName;
    }

    public void setMerchantSubDistrictName(String merchantSubDistrictName) {
        this.merchantSubDistrictName = merchantSubDistrictName;
    }

    public String getMerchantVillageName() {
        return merchantVillageName;
    }

    public void setMerchantVillageName(String merchantVillageName) {
        this.merchantVillageName = merchantVillageName;
    }

    public String getMerchantTier() {
        return merchantTier;
    }

    public void setMerchantTier(String merchantTier) {
        this.merchantTier = merchantTier;
    }

    //new changes

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getMerchantCategory() {
        return merchantCategory;
    }

    public void setMerchantCategory(String merchantCategory) {
        this.merchantCategory = merchantCategory;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getSimProvider() {
        return simProvider;
    }

    public void setSimProvider(String simProvider) {
        this.simProvider = simProvider;
    }

    public String getMarsMdrCharges() {
        return marsMdrCharges;
    }

    public void setMarsMdrCharges(String marsMdrCharges) {
        this.marsMdrCharges = marsMdrCharges;
    }

    public String getMarsRentalPlans() {
        return marsRentalPlans;
    }

    public void setMarsRentalPlans(String marsRentalPlans) {
        this.marsRentalPlans = marsRentalPlans;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public String getVpaName() {
        return vpaName;
    }

    public void setVpaName(String vpaName) {
        this.vpaName = vpaName;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getMerchantPan() {
        return merchantPan;
    }

    public void setMerchantPan(String merchantPan) {
        this.merchantPan = merchantPan;
    }

    public String getMerchantDob() {
        return merchantDob;
    }

    public void setMerchantDob(String merchantDob) {
        this.merchantDob = merchantDob;
    }

    public int getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(int institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getMarsRegion() {
        return marsRegion;
    }

    public void setMarsRegion(String marsRegion) {
        this.marsRegion = marsRegion;
    }

    public String getConstitution() {
        return constitution;
    }

    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    public String getEstablishYear() {
        return establishYear;
    }

    public void setEstablishYear(String establishYear) {
        this.establishYear = establishYear;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getMaximumMonthlyUsage() {
        return maximumMonthlyUsage;
    }

    public void setMaximumMonthlyUsage(String maximumMonthlyUsage) {
        this.maximumMonthlyUsage = maximumMonthlyUsage;
    }

    public String getMerchantTypeCode() {
        return merchantTypeCode;
    }

    public void setMerchantTypeCode(String merchantTypeCode) {
        this.merchantTypeCode = merchantTypeCode;
    }

    public String getAdvanceRentMode() {
        return advanceRentMode;
    }

    public void setAdvanceRentMode(String advanceRentMode) {
        this.advanceRentMode = advanceRentMode;
    }

    public String getDeviceOwnedBy() {
        return deviceOwnedBy;
    }

    public void setDeviceOwnedBy(String deviceOwnedBy) {
        this.deviceOwnedBy = deviceOwnedBy;
    }

    public String getDevicePrice() {
        return devicePrice;
    }

    public void setDevicePrice(String devicePrice) {
        this.devicePrice = devicePrice;
    }

    public String getEmiTenure() {
        return emiTenure;
    }

    public void setEmiTenure(String emiTenure) {
        this.emiTenure = emiTenure;
    }

    public String getEmiStartDate() {
        return emiStartDate;
    }

    public void setEmiStartDate(String emiStartDate) {
        this.emiStartDate = emiStartDate;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public String getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(String installationFee) {
        this.installationFee = installationFee;
    }

    public int getNoOfMonthRentPaidInAdvance() {
        return noOfMonthRentPaidInAdvance;
    }

    public void setNoOfMonthRentPaidInAdvance(int noOfMonthRentPaidInAdvance) {
        this.noOfMonthRentPaidInAdvance = noOfMonthRentPaidInAdvance;
    }

    public String getRentalMode() {
        return rentalMode;
    }

    public void setRentalMode(String rentalMode) {
        this.rentalMode = rentalMode;
    }

    public String getRentalType() {
        return rentalType;
    }

    public void setRentalType(String rentalType) {
        this.rentalType = rentalType;
    }

    public String getTotalEmiAmount() {
        return totalEmiAmount;
    }

    public void setTotalEmiAmount(String totalEmiAmount) {
        this.totalEmiAmount = totalEmiAmount;
    }

    public String getTerminalModeCode() {
        return terminalModeCode;
    }

    public void setTerminalModeCode(String terminalModeCode) {
        this.terminalModeCode = terminalModeCode;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public int getBqrEnabled() {
        return bqrEnabled;
    }

    public void setBqrEnabled(int bqrEnabled) {
        this.bqrEnabled = bqrEnabled;
    }

    public String getCashAtPosEnabled() {
        return cashAtPosEnabled;
    }

    public void setCashAtPosEnabled(String cashAtPosEnabled) {
        this.cashAtPosEnabled = cashAtPosEnabled;
    }

    public int getLinkpaymentFlag() {
        return linkpaymentFlag;
    }

    public void setLinkpaymentFlag(int linkpaymentFlag) {
        this.linkpaymentFlag = linkpaymentFlag;
    }

    public String getMatmAllowed() {
        return matmAllowed;
    }

    public void setMatmAllowed(String matmAllowed) {
        this.matmAllowed = matmAllowed;
    }

    public String getPreAuth() {
        return preAuth;
    }

    public void setPreAuth(String preAuth) {
        this.preAuth = preAuth;
    }

    public int getSaleFlag() {
        return saleFlag;
    }

    public void setSaleFlag(int saleFlag) {
        this.saleFlag = saleFlag;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getTipEnabled() {
        return tipEnabled;
    }

    public void setTipEnabled(String tipEnabled) {
        this.tipEnabled = tipEnabled;
    }

    public int getUpichargeslipEnabled() {
        return upichargeslipEnabled;
    }

    public void setUpichargeslipEnabled(int upichargeslipEnabled) {
        this.upichargeslipEnabled =upichargeslipEnabled;
    }

    public int getUpiFlag() {
        return upiFlag;
    }

    public void setUpiFlag(int upiFlag) {
        this.upiFlag = upiFlag;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getMarsDevice() {
        return marsDevice;
    }

    public List<String> getVpa() {
        return vpa;
    }

    public void setVpa(List<String> vpa) {
        this.vpa = vpa;
    }

    public void setMarsDevice(String marsDevice) {
        this.marsDevice = marsDevice;
    }

    public boolean isPosQr() {
        return posQr;
    }

    public void setPosQr(boolean posQr) {
        this.posQr = posQr;
    }
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getSoundId() {
        return soundId;
    }

    public void setSoundId(Long soundId) {
        this.soundId = soundId;
    }

    public boolean isSQR() {
        return isSQR;
    }

    public void setSQR(boolean SQR) {
        isSQR = SQR;
    }

    public String getUpiCreditAbove2000() {
        return upiCreditAbove2000;
    }

    public void setUpiCreditAbove2000(String upiCreditAbove2000) {
        this.upiCreditAbove2000 = upiCreditAbove2000;
    }

    public String getUpiCreditUpto2000() {
        return upiCreditUpto2000;
    }

    public void setUpiCreditUpto2000(String upiCreditUpto2000) {
        this.upiCreditUpto2000 = upiCreditUpto2000;
    }

    public String getUpiAbove2000() {
        return upiAbove2000;
    }

    public void setUpiAbove2000(String upiAbove2000) {
        this.upiAbove2000 = upiAbove2000;
    }

    public String getUpiUpto2000() {
        return upiUpto2000;
    }

    public void setUpiUpto2000(String upiUpto2000) {
        this.upiUpto2000 = upiUpto2000;
    }

    public String getAmexDomestic() {
        return amexDomestic;
    }

    public void setAmexDomestic(String amexDomestic) {
        this.amexDomestic = amexDomestic;
    }

    public String getAmexInternational() {
        return amexInternational;
    }

    public void setAmexInternational(String amexInternational) {
        this.amexInternational = amexInternational;
    }

    public int getNoOfAuthorizer() {
        return noOfAuthorizer;
    }

    public void setNoOfAuthorizer(int noOfAuthorizer) {
        this.noOfAuthorizer = noOfAuthorizer;
    }

    public int getNoOfStaff() {
        return noOfStaff;
    }

    public void setNoOfStaff(int noOfStaff) {
        this.noOfStaff = noOfStaff;
    }

    public String getBussinessInLocation() {
        return bussinessInLocation;
    }

    public void setBussinessInLocation(String bussinessInLocation) {
        this.bussinessInLocation = bussinessInLocation;
    }

    public int getStockverificationConfirmation() {
        return stockverificationConfirmation;
    }

    public void setStockverificationConfirmation(int stockverificationConfirmation) {
        this.stockverificationConfirmation = stockverificationConfirmation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNameOfAuthorizer() {
        return nameOfAuthorizer;
    }

    public void setNameOfAuthorizer(String nameOfAuthorizer) {
        this.nameOfAuthorizer = nameOfAuthorizer;
    }

    public String getEmailIdForAuthorizer() {
        return emailIdForAuthorizer;
    }

    public void setEmailIdForAuthorizer(String emailIdForAuthorizer) {
        this.emailIdForAuthorizer = emailIdForAuthorizer;
    }

    public String getPhoneForAuthorizer() {
        return phoneForAuthorizer;
    }

    public void setPhoneForAuthorizer(String phoneForAuthorizer) {
        this.phoneForAuthorizer = phoneForAuthorizer;
    }

    public String getAddressForAuthorizer() {
        return addressForAuthorizer;
    }

    public void setAddressForAuthorizer(String addressForAuthorizer) {
        this.addressForAuthorizer = addressForAuthorizer;
    }

    public String getPanNumberForAuthorizer() {
        return panNumberForAuthorizer;
    }

    public void setPanNumberForAuthorizer(String panNumberForAuthorizer) {
        this.panNumberForAuthorizer = panNumberForAuthorizer;
    }

    public String getIsAuthorizedForAuthorizer() {
        return isAuthorizedForAuthorizer;
    }

    public void setIsAuthorizedForAuthorizer(String isAuthorizedForAuthorizer) {
        this.isAuthorizedForAuthorizer = isAuthorizedForAuthorizer;
    }
}
