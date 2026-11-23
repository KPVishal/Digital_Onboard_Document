package com.bijlipay.model;

import com.bijlipay.model.dto.ImplementDetailsDto;
import com.bijlipay.model.dto.MccDocumentDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


@Entity
@Table(name = "lead_information")
public class LeadInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_id", nullable = false)
    private long leadId;

    @Column(name = "merchant_name", nullable = true)
    private String merchantName;
    @Column(name = "contact_name",nullable = true)
    private String contactName;

    @Column(name = "contact_email", nullable = true)
    private String contactEmail;

    @Column(name = "contact_address", nullable = true)
    private String contactAddress;

    @Column(name = "contact_number", nullable = true)
    private String contactNumber;

    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_source", referencedColumnName = "lead_source_id")
    private LeadSource leadSource;

    @Column(name = "sole_id")
    private String soleId;

    @Column(name = "employee_id")
    private String employeeId;
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_status",referencedColumnName = "lead_status_id")
    private LeadStatus leadStatus;
    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_sub_status",referencedColumnName = "lead_sub_status_id")
    private LeadSubStatus leadSubStatus;
    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(optional = true)
    @JoinColumn(name="lead_cancel_reason",referencedColumnName ="reason_id")
    private LeadCancellationReason leadCancellationReason;

    @Column(name="lead_cancel_remarks",nullable = true)
    private String leadCancelRemarks;

    @Column(name="otp",nullable = true)
    private String otp;

    @ManyToOne(optional = true)
    @JoinColumn(name = "merchant_type_id", referencedColumnName = "merchant_type_id")
    private MerchantType merchantType;

    @ManyToOne(optional = true)
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    private Device device;
    @Column(name = "device_count", nullable = true)
    private Integer deviceCount;

    @Column(name = "qr_count", nullable = true)
    private Integer qrCount;
    @Column(name = "is_qr")
    private boolean isQR;
    @Column(name = "is_sound_box")
    private boolean isSoundBox;
    @Column(name = "is_pos")
    private boolean isPos;
    @Column(name = "vas_instance_mapping")
    private String vasInstanceMapping;
    @ManyToOne(optional = true)
    @JoinColumn(name = "mcc_id", referencedColumnName = "mcc_id")
    private MerchantCategoryCode mcc;

    @ManyToOne(optional = true)
    @JoinColumn(name = "merchant_category", referencedColumnName = "merchant_category_id")
    private MerchantCategory merchantCategory;
    @ManyToOne(optional = true)
    @JoinColumn(name = "plan_id", referencedColumnName = "plan_id")
    private Plan plan;
    @ManyToOne(optional = true)
    @JoinColumn(name = "mdr_id", referencedColumnName = "mdr_plan_id")
    private MDRCharge mdrCharge;
    @Column(name = "is_mdr_subvention_requested")
    private boolean isMDRSubventionRequested;
    @Column(name = "is_rental_subvention_requested")
    private boolean isRentalSubventionRequested;
    @Column(name = "recurring_fee_applied_amount")
    private double recurringFeeAppliedAmount;
    @Column(name = "set_up_fee_applied_amount")
    private double setUpFeeAppliedAmount;
    @Column(name = "mdr_approved_by_NH")
    private boolean mdrApprovedByNH;
    @Column(name = "rental_approved_by_NH")
    private boolean rentalApprovedByNH;
    @Column(name = "mdr_rejected_reason")
    private String mdrRejectedReason;
    @Column(name = "rental_rejected_reason")
    private String rental_rejected_reason;
    @Column(name = "corp_cc")
    private double corpCC;
    @Column(name = "debit_lessthan_amount")
    private double debitLessThanAmount;
    @Column(name = "debit_greaterthan_amount")
    private double debitGreaterThanAmount;
    @Column(name = "intl_cc")
    private double intlCC;
    @Column(name = "super_premium_cc")
    private double superPremiumCC;
    @Column(name = "std_cc")
    private double stdCC;
    @Column(name = "premium_cc")
    private double premiumCC;
    @Column(name = "mdr_plan_name")
    private String mdrPlanName;
    @Column(name = "recurring_fees")
    private double recurringFees;
    @Column(name = "setup_fees")
    private double setUpFees;
    @Column(name = "ic_acceptance")
    private boolean icAcceptance;
    @Column(name = "credit_card_block")
    private boolean creditCardBlock;
    @Column(name = "tip_percentage")
    private double tipPercentage;
    @ManyToOne(optional = true)
    @JoinColumn(name = "sim_provider_id", referencedColumnName = "sim_provider_id")
    private SimProvider simProvider;
    @Column(name = "settlement_mode")
    private int settlementMode;    // 1.Auto  2.Manual
    @Column(name = "is_omc_convenience_fee")
    private boolean isOmcConvenienceFees;
    @Column(name = "yearly_revenue")
    private int yearlyRevenue;        //  1. yearlyRevenue<20L  2. yearlyRevenue>20L
    @Column(name = "legal_name")
    private String legalName;
    @Column(name = "dba_name")
    private String dbaName;
    @Column(name = "shop_ownership_status")
    private int shop_ownership_status;       //1.Rented  2.Owned
    @Column(name = "weekday_timing_start")
    private String weekDayTimingStart;
    @Column(name = "weekday_timing_end")
    private String weekdayTimingEnd;
    @Column(name = "weekend_timing_start")
    private String weekendTimingStart;
    @Column(name = "weekend_timing_end")
    private String weekendTimingEnd;
    @Column(name = "max_monthly_card_volume")
    private double maxMonthlyCardVolume;
    @Column(name = "avg_monthly_card_volume")
    private double avgMonthlyCardvolume;
    @Column(name = "turnover_last_financial_year")
    private String turnOverLastFinancialYear;
    @Column(name = "avg_bill_amount")
    private double avgBillAmount;
    @Column(name = "member_since")
    private Date memberSince;
//    @Column(name = "have_pos_mpos")
//    private boolean havePosMpos;
    @Column(name = "have_pos_mpos")
    private int havePosMpos;

    @Column(name = "pos_status",columnDefinition = "tinyint(1) default 0")
    private int posStatus;    // 1-yes  2-no
    @Column(name = "debit_mdr")
    private double debitMdr;
    @Column(name = "credit_mdr")
    private double creditMdr;
    @Column(name = "amount_collected")
    private double amountCollected;
    @Column(name = "transaction_date")
    private Date transactionDate;
    @Column(name = "payment_option")
    private int paymentOption;        //1.QR  2.SMS
    @Column(name = "payment_contact_number")
    private String paymentContactNumber;
    @Column(name = "is_payment_success")
    private boolean isPaymentSuccess;
    @Column(name = "ifsc_code")
    private String ifscCode;
    @Column(name = "bank")
    private String bank;
    @Column(name = "branch")
    private String branch;
    @ManyToOne(optional = true)
    @JoinColumn(name = "account_type", referencedColumnName = "lead_source_mcc_account_type_id")
    private LeadSourceMCCAccountTypeMapping leadSourceMCCAccountTypeMapping;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "reference_number",nullable = true)
    private String referenceNumber;
    @Column(name = "merchant_details_complete_date", nullable = true)
    private Date merchantDetailsCompleteDate;
    @Column(name = "document_details_complete_date", nullable = true)
    private Date documentDetailsCompleteDate;
    @Column(name = "mdr_details_complete_date", nullable = true)
    private Date mdrDetailsCompleteDate;
    @Column(name = "biz_details_complete_date", nullable = true)
    private Date bizDetailsCompleteDate;
    @Column(name = "payment_details_complete_date", nullable = true)
    private Date paymentDetailsCompleteDate;

    @Column(name = "osv_details_completed_date",nullable = true)
    private Date osvDetailsCompletedDate;
    @Column(name = "otp_verified", nullable = true)
    private boolean otpVerified;
    @Column(name = "is_merchant_details_saved")
    private boolean isMerchantDetailsSaved;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_to",referencedColumnName = "user_id")
    private User assignedTo;
    @ManyToOne(optional = true)
    @JoinColumn(name = "created_by",referencedColumnName = "user_id")
    private User createdBy;
    @ManyToOne(optional = true)
    @JoinColumn(name = "updated_by",referencedColumnName = "user_id")
    private User updatedBy;
    @Column(name = "submitted_to_mars_date")
    private Date submittedToMarsDate;
    @Column(name = "lead_category")
    private int leadCategory;
    @Column(name = "reason")
    private String reason;
    @Column(name = "lead_number")
    private String leadNumber;
    @Column(name = "application_number")
    private String applicationNumber;
    @Column(name = "merchant_ref_code")
    private String merchantRefCode;
    @Column(name = "mars_reason")
    private String marsReason;
    @ManyToOne(optional = true)
    @JoinColumn(name = "region",referencedColumnName = "region_id")
    private Region region;
    @Column(name = "reject",columnDefinition = "tinyint(1) default 0")
    private boolean reject;
    @Column(name = "is_bijlipay_switch", columnDefinition = "tinyint(1) default 0")
    private boolean isBijlipaySwitch;
    @Column(name = "is_penny_drop_verified", columnDefinition = "tinyint(1) default 0")
    private boolean isPennyDropVerified;

    @Column(name = "applied_debit_lessthan_amount",nullable = true)
    private double appliedDebitLessthanAmount;
    @Column(name = "applied_debit_greaterthan_amount",nullable = true)
    private double appliedDebitGreaterthanAmount;
    @Column(name = "applied_std_cc",nullable = true)
    private double appliedStdCC;
    @Column(name = "applied_premium_cc",nullable = true)
    private double appliedPremiumCC;
    @Column(name = "applied_corp_cc",nullable = true)
    private double appliedCorpCC;
    @Column(name = "applied_intl_cc",nullable = true)
    private double appliedIntlCC;
    @Column(name = "applied_super_premium_cc", nullable = true)
    private double appliedSuperPremiumCC;

    @Column(name = "visit_date_and_time",nullable = true)
    private Date visitDateAndTime;

    @Column(name = "follow_up_date_and_time",nullable = true)
    private Date followUpDateAndTime;
    @Column(name = "follow_up_remarks",nullable = true)
    private String followUpRemarks;
    @Column(name = "cms_lead_status", columnDefinition = "int(11) default 0")
    private int cmsLeadStatus;
    @Column(name = "verified_cms_pricing_status", columnDefinition = "int(11) default 0")
    private Integer verifiedCmsPricingStatus;// 1-success, 2-rsm pending, 3-rsm reject, 4-nh pending, 5- nh reject ,6-finance pending, 7-finance reject
    @Column(name = "verified_Cms_finance_status", columnDefinition = "int(11) default 0")
    private Integer verifiedCmsFinanceStatus; // 1-success, 2-pending, 3-reject

    @Column(name = "latitude", nullable = true)
    private Double latitude;

    @Column(name = "longitude", nullable = true)
    private Double longitude;

    @Column(name = "pincode", nullable = true)
    private long pincode;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "state_name", nullable = true)
    private String state;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "Asia/Kolkata")
    @Column(name = "ownerDOB", nullable = true)
    private Date ownerDOB;

    @Column(name = "ownerFirstName", nullable = true)
    private String ownerFirstName;

    @Column(name = "ownerLastName", nullable = true)
    private String ownerLastName;

    @Column(name="merchant_state_name",nullable = true)
    private String merchantStateName;

    @Column(name="merchant_district_name",nullable = true)
    private String merchantDistrictName;

    @Column(name="merchant_sub_district_name",nullable = true)
    private String merchantSubDistrictName;

    @Column(name="merchant_village_name",nullable = true)
    private String merchantVillageName;

    @Column(name="merchant_tier",nullable = true)
    private String merchantTier;

    @Column(name = "merchant_lat_lng",nullable = true)
    private String merchantLatLng;

    @Column(name = "total_tids",nullable = true)
    private String totalTids;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_to_nh",referencedColumnName = "user_id")
    private User assignedToNH;

    @OneToMany(mappedBy = "leadInformation")
    private List<LeadVerificationStatus> leadVerificationStatus;

    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_approved_by", referencedColumnName = "user_id")
    private User leadApprovedBy;

    @ManyToOne(optional = true)
    @JoinColumn(name = "lead_reject_by", referencedColumnName = "user_id")
    private User leadRejectBy;

    @Column(name = "lead_approved_date", nullable = true)
    private Date leadApprovedDate;

    @Column(name = "lead_rejected_date", nullable = true)
    private Date leadRejectedDate;

    @Column(name = "ops_rejected_remarks", nullable = true)
    private String opsRejectedRemarks;

    @Column(name = "reject_lead_date", nullable = true)
    private Date rejectLeadDate;

    @ManyToOne(optional = true)
    @JoinColumn(name = "reject_lead_by", referencedColumnName = "user_id")
    private User rejectLeadBy;

    @Column(name = "switch_reject_date", nullable = true)
    private Date switchRejectDate;

    @Column(name = "onboarding_vendor", nullable = true)
    private String onboardingVendor;

    @Column(name = "settlement_type",nullable = true)
    private int settlementType;

    @Column(name = "mdr_code")
    private String mdrPlancode;
    @Column(name = "domestic_debit_upto_2000",columnDefinition = "JSON",nullable = true)
    private String domesticDebitUpTo2000;
    @Column(name = "domestic_debit_above_2000",columnDefinition = "JSON",nullable = true)
    private String domesticDebitAbove2000;
    @Column(name = "standard_or_classic",columnDefinition = "JSON",nullable = true)
    private String standardOrClassic;
    @Column(name = "premium_or_platinum", columnDefinition = "JSON",nullable = true)
    private String premiumOrPlatinum;
    @Column(name = "super_premium_or_signature", columnDefinition = "JSON",nullable = true)
    private String superPremiumOrSignature;
    @Column(name = "commercial_or_corporate",columnDefinition = "JSON",nullable = true)
    private String commercialOrCorporate;
    @Column(name = "international_debit_card",columnDefinition = "JSON",nullable = true)
    private String internationalDebitCard;
    @Column(name = "international_credit_card",columnDefinition = "JSON",nullable = true)
    private String internationalCreditCard;
    @Column(name = "onus",columnDefinition = "JSON",nullable = true)
    private String onus;
    @Column(name = "diners",columnDefinition = "JSON",nullable = true)
    private String diners;
    @Column(name = "cashAt_pos",columnDefinition = "JSON",nullable = true)
    private String cashAtPos;
    @Column(name = "master_pass",columnDefinition = "JSON",nullable = true)
    private String masterPass;

    @JoinColumn(name = "mars_mdr_charges",referencedColumnName = "id",nullable = true)
    @ManyToOne(optional = true)
    private MarsMdrCharges marsMdrCharges;

    @JoinColumn(name = "mars_rental_plan",referencedColumnName = "id",nullable = true)
    @ManyToOne(optional = true)
    private MarsRentalPlans marsRentalPlans;

    @Column(name = "rental_code",nullable = true)
    private String rentalPlanCode;

    @Column(name = "is_professional_lead", columnDefinition = "tinyint(1) default 0")
    private boolean isProfessionalLead;

    @Column(name = "pan_number", nullable = true)
    private String panNumber;

    @Column(name = "gst_number", nullable = true)
    private String gstNumber;

    @Column(name = "nature_of_business", nullable = true)
    private String natureOfBusiness;

    @Column(name = "aggregator",nullable = true)
    private String aggregator;

    @Column(name = "category_type", nullable = true)
    private int categoryType;// 1 - small merchant 2 - other merchant

    @Column(name = "is_agreement_success",columnDefinition = "tinyint(1) default 0")
    private boolean isAgreementSuccess;

    @Column(name = "is_aggregator",columnDefinition = "tinyint(1) default 0")
    private boolean isAggregator;

    @Column(name = "is_manual_agreement",columnDefinition = "tinyint(1) default 0")
    private boolean isManualAgreement;

    @Column(name = "vpa_name",columnDefinition = "TEXT",nullable = true)
    private String vpaName;

    @ManyToOne(optional = true)
    @JoinColumn(name = "sqr_device_id", referencedColumnName = "device_id")
    private Device sqrDevice;
    @Column(name = "is_pos_enable")
    private boolean posFlag;

    @Column(name = "rid", nullable = true)
    private String rid;

    @Column(name = "case_number", nullable = true)
    private String caseNumber;

    @Column(name = "is_mars_flag",columnDefinition = "int(11) default 0")
    private int isMarsApiTriggered;   //0 - mars api not triggered , 1 - mars api triggered , 2- Tid created

    @Column(name = "zone", nullable = true)
    private String zone;

    @Column(name = "is_manual_approved",columnDefinition = "tinyint(1) default 0")
    private boolean isManualApproved;

    @Column(name = "store_address", nullable = true,columnDefinition= "VARCHAR(200)")
    private String storeAddress;

    @Column(name = "merchant_pan", nullable = true,columnDefinition= "VARCHAR(20)")
    private String merchantPan;

    @Column(name = "merchant_dob", nullable = true)
    private String merchantDob;

    @Column(name = "institution_code", nullable = true, columnDefinition = "int(1) default 0")
    private int institutionCode;


    @Column(name = "mars_region", nullable = true,columnDefinition= "VARCHAR(10)")
    private String marsRegion;

    @Column(name = "constitution", nullable = true,columnDefinition= "VARCHAR(10)")
    private String constitution;

    @Column(name = "establish_year", nullable = true,columnDefinition= "VARCHAR(20)")
    private String establishYear;

    @Column(name = "register_number", nullable = true,columnDefinition= "VARCHAR(20)")
    private String registerNumber;

    @Column(name = "maximum_monthly_usage", nullable = true,columnDefinition= "VARCHAR(30)")
    private String maximumMonthlyUsage;

    @Column(name = "merchant_type_code", nullable = true,columnDefinition= "VARCHAR(20)")
    private String merchantTypeCode;

    @Column(name = "advance_rent_mode", nullable = true,columnDefinition= "VARCHAR(20)")
    private String advanceRentMode;

    @Column(name = "device_owned_by", nullable = true,columnDefinition= "VARCHAR(10)")
    private String deviceOwnedBy;

    @Column(name = "device_price", nullable = true,columnDefinition= "VARCHAR(20)")
    private String devicePrice;

    @Column(name = "emi_tenure", nullable = true,columnDefinition= "VARCHAR(10)")
    private String emiTenure;

    @Column(name = "emi_start_date", nullable = true,columnDefinition= "VARCHAR(20)")
    private String emiStartDate;

    @Column(name = "grace_period", columnDefinition = "int(1) default 0", nullable = true)
    private int gracePeriod;

    @Column(name = "installation_fee", nullable = true,columnDefinition= "VARCHAR(10)")
    private String installationFee;

    @Column(name = "no_of_month_rent_paid_in_advance",columnDefinition = "int(1) default 0",nullable = true)
    private int noOfMonthRentPaidInAdvance;

    @Column(name = "rental_mode", nullable = true,columnDefinition= "VARCHAR(20)")
    private String rentalMode;

    @Column(name = "rental_type", nullable = true,columnDefinition= "VARCHAR(10)")
    private String rentalType;

    @Column(name = "total_emi_amount", nullable = true,columnDefinition= "VARCHAR(10)")
    private String totalEmiAmount;

    @Column(name = "terminal_mode_code", nullable = true,columnDefinition= "VARCHAR(10)")
    private String terminalModeCode;

    @Column(name = "terminal_type", nullable = true,columnDefinition= "VARCHAR(10)")
    private String terminalType;

    @Column(name = "bqr_enabled", nullable = true,columnDefinition = "int(1) default 0")
    private int bqrEnabled;

    @Column(name = "cash_at_pos_enabled", nullable = true,columnDefinition= "VARCHAR(10)")
    private String cashAtPosEnabled;

    @Column(name = "linkpayment_flag", nullable = true,columnDefinition = "int(1) default 0")
    private int linkpaymentFlag;

    @Column(name = "matm_allowed", nullable = true,columnDefinition= "VARCHAR(10)")
    private String matmAllowed;

    @Column(name = "pre_auth", nullable = true,columnDefinition= "VARCHAR(10)")
    private String preAuth;

    @Column(name = "sale_flag", nullable = true,columnDefinition = "int(1) default 0")
    private int saleFlag;

    @Column(name = "settlement", nullable = true,columnDefinition= "VARCHAR(10)")
    private String settlement;

    @Column(name = "tip_enabled", nullable = true,columnDefinition= "VARCHAR(10)")
    private String tipEnabled;

    @Column(name = "upichargeslip_enabled", nullable = true,columnDefinition = "int(1) default 0")
    private int upichargeslipEnabled;

    @Column(name = "upi_flag", nullable = true,columnDefinition = "int(1) default 0")
    private int upiFlag;

    @Column(name = "account", nullable = true,columnDefinition= "VARCHAR(20)")
    private String accountType;

    @Column(name = "mars_device", nullable = true,columnDefinition= "VARCHAR(10)")
    private String marsDevice;
    @Column(name="pos_Qr",columnDefinition = "tinyint(1) default 0")
    private boolean posQr;

    @ManyToOne(optional = true)
    @JoinColumn(name = "language",referencedColumnName = "id", nullable = true)
    private LanguageSoundbox languageSoundbox;

    @Column(name = "is_sqr",columnDefinition = "tinyint(1) default 0")
    private boolean isSQR;

    @Column(name = "upi_Credit_Above_2000",columnDefinition = "JSON")
    private String upiCreditAbove2000;
    @Column(name = "upi_Credit_Upto_2000",columnDefinition = "JSON")
    private String upiCreditUpto2000;
    @Column(name = "upi_Above_2000",columnDefinition = "JSON")
    private String upiAbove2000;
    @Column(name = "upi_Upto_2000",columnDefinition = "JSON")
    private String upiUpto2000;

    @Column(name = "mcc_approval",columnDefinition = "tinyint(1) default 0")
    private boolean mccApproval;

    @Column(name = "ica_approval",columnDefinition = "tinyint(1) default 0")
    private boolean icaApproval;

    @Column(name="mcc_reason",nullable = true,columnDefinition= "VARCHAR(30)")
    private String mccReason;

    @Column(name = "osv_otp_verified",columnDefinition = "tinyint(1) default 0")
    private boolean osvOtpVerified;

    @Column(name = "ica_enable",columnDefinition = "int(1) default 0")
    private int icaEnabled;

    @Transient
    private List<UploadedDocuments> uploadedDocuments;

    @Transient
    private List<UploadedDocuments> rejectedDocuments;

    @Transient
    private ImplementDetailsDto implementDetailsDto;

    @Transient
    private List<SubventionLogs> subventionLogs;
    @ManyToOne(optional = true)
    @JoinColumn(name = "ops_mcc", referencedColumnName = "mcc_id")
    private MerchantCategoryCode opsMcc;

    @Column(name = "document_status",columnDefinition = "int(11) default 0") //1--->resubmit  2--->reject 3---->resubmit  4--->reject
    private int documentStatus;
    @Column(name = "mcc_approved_at", nullable = true)
    private Date mccApproved;

    @Column(name = "ica_status", nullable = true)
    private Boolean  icaStatus;    // 1- reject 0-update
    @Transient
    private MccDocumentDto mccDocuments;
    @Column(name = "is_qr_enable", columnDefinition = "tinyint(1) default 0")
    private boolean qrFlag;
    @Column(name = "sqr_recurring_fees", columnDefinition="Decimal(10,2) default '0.00'")
    private double sqrRecurringFees;
    @Column(name = "sqr_setup_fees", columnDefinition="Decimal(10,2) default '0.00'")
    private double sqrSetUpFees;
    @ManyToOne(optional = true)
    @JoinColumn(name = "sqr_mars_mdr_charges", referencedColumnName = "id")
    private MarsMdrCharges sqrMarsMdrCharge;

    @Column(name = "no_of_staff",columnDefinition = "int(11) default 0")
    private int noOfStaff;

    @Column(name = "bussiness_in_location", nullable = true)
    private String bussinessInLocation;

    @Column(name = "alternate_mobile_no", nullable = true)
    private String alternateMobileNo;

    @Column(name = "residential_address", nullable = true)
    private String residentialAddress;

    @JoinColumn(name = "sticker_mars_rental_plan",referencedColumnName = "id",nullable = true)
    @ManyToOne(optional = true)
    private MarsRentalPlans stickerMarsRentalPlans;

    @JoinColumn(name = "sticker_mars_mdr_charges",referencedColumnName = "id",nullable = true)
    @ManyToOne(optional = true)
    private MarsMdrCharges stickerMarsMdrCharges;

    @ManyToOne(optional = true)
    @JoinColumn(name = "sticker_device_id", referencedColumnName = "device_id")
    private Device stickerDevice;

    @Column(name = "soundBox_count",columnDefinition = "int(11) default 0")
    private int soundBoxCount;

    @Column(name = "sticker_setup_fees", columnDefinition="Decimal(10,2) default '0.00'")
    private double stickerSetUpFees;

    @Column(name = "amex_Domestic ",columnDefinition = "JSON")
    private String amexDomestic;
    @Column(name = "amex_International",columnDefinition = "JSON")
    private String amexInternational;

    @Column(name = "agg_id",columnDefinition = "int(11) default 0")
    private int aggId;

    @Column(name = "merchant_ref_no",columnDefinition ="int(11) default 0")
    private int merchantRefNo;
    @Column(name = "no_of_authorizer", columnDefinition = "int(1) default 0")
    private int noOfAuthorizer;
    @Column(name = "stock_verification_confirmation", columnDefinition = "int(11) default 0")
    private int stockverificationConfirmation;

    @Column(name = "remarks", nullable = true)
    private String remarks;

    @Column(name = "pull_triggered",columnDefinition = "tinyint(1) default 0")
    private boolean pullTriggered;

    @Column(name = "name_of_authorizer",columnDefinition = "TEXT")
    private String nameOfAuthorizer;
    @Column(name = "email_for_authorizer",columnDefinition = "TEXT")
    private String emailIdForAuthorizer;
    @Column(name = "phone_for_authorizer", nullable = true,columnDefinition= "VARCHAR(100)")
    private String phoneForAuthorizer;
    @Column(name = "address_for_authorizer",columnDefinition = "TEXT")
    private String addressForAuthorizer;
    @Column(name = "panNumber_for_authorizer", nullable = true,columnDefinition= "VARCHAR(100)")
    private String panNumberForAuthorizer;
    @Column(name = "is_authorized_for_authorizer", nullable = true,columnDefinition= "VARCHAR(50)")
    private String isAuthorizedForAuthorizer;
    @Column(name = "without_gst",columnDefinition = "tinyint(1) default 0")
    private boolean withOutGst;

    @Column(name = "additional_document_name", nullable = true,columnDefinition= "VARCHAR(30)")
    private String additionalDocumentName;
    @Column(name = "additional_document_remark", nullable = true,columnDefinition= "VARCHAR(30)")
    private String additionalDocumentRemark;

    @Column(name = "is_digital",columnDefinition = "tinyint(1) default 0")
    private boolean isDigital;
    @Column(name = "gee_nee_pos",columnDefinition = "tinyint(1) default 0")
    private boolean geeNeePos;

    @Column(name = "offline_payment_type", columnDefinition = "int(1) default 0")
    private int offlinePaymentType;

    @Transient
    private RazorPayQRStatus razorPayQRStatus;

    @JoinColumn(name = "sqr_mars_rental_plan",referencedColumnName = "id",nullable = true)
    @ManyToOne(optional = true)
    private MarsRentalPlans sqrMarsRentalPlans;



    public LeadInformation(){

    }

    public LeadInformation(long leadId, String merchantName, String contactName, String contactEmail, String contactAddress, String contactNumber, LeadSource leadSource, String soleId, String employeeId, LeadStatus leadStatus, LeadSubStatus leadSubStatus, boolean isActive, LeadCancellationReason leadCancellationReason, String leadCancelRemarks, String otp, MerchantType merchantType, Device device, Integer deviceCount, boolean isQR, boolean isSoundBox, boolean isPos, String vasInstanceMapping, MerchantCategoryCode mcc, Plan plan, MDRCharge mdrCharge, boolean isMDRSubventionRequested, boolean isRentalSubventionRequested, double recurringFeeAppliedAmount, double setUpFeeAppliedAmount, boolean mdrApprovedByNH, boolean rentalApprovedByNH, String mdrRejectedReason, String rental_rejected_reason, double corpCC, double debitLessThanAmount, double debitGreaterThanAmount, double intlCC, double superPremiumCC, double stdCC, double premiumCC, String mdrPlanName, double recurringFees, double setUpFees, boolean icAcceptance, boolean creditCardBlock, double tipPercentage, SimProvider simProvider, int settlementMode, boolean isOmcConvenienceFees, int yearlyRevenue, String legalName, String dbaName, int shop_ownership_status, String weekDayTimingStart, String weekdayTimingEnd, String weekendTimingStart, String weekendTimingEnd, double maxMonthlyCardVolume, double avgMonthlyCardvolume, String turnOverLastFinancialYear, double avgBillAmount, Date memberSince, int havePosMpos, double debitMdr, double creditMdr, double amountCollected, Date transactionDate, int paymentOption, String paymentContactNumber, boolean isPaymentSuccess, String ifscCode, String bank, String branch, LeadSourceMCCAccountTypeMapping leadSourceMCCAccountTypeMapping, String accountNumber, String referenceNumber, Date merchantDetailsCompleteDate, Date documentDetailsCompleteDate, Date mdrDetailsCompleteDate, Date bizDetailsCompleteDate, Date paymentDetailsCompleteDate, boolean otpVerified, boolean isMerchantDetailsSaved, Date createdAt, Date updatedAt,double appliedDebitLessthanAmount,double appliedDebitGreaterthanAmount,double appliedStdCC,double appliedPremiumCC, double appliedCorpCC,double appliedIntlCC, double appliedSuperPremiumCC,Date visitDateAndTime, Date followUpDateAndTime,String followUpRemarks,boolean mccApproval,boolean icaApproval,String mccReason,Date osvDetailsCompletedDate,boolean osvOtpVerified,int icaEnabled,int posStatus) {
        this.leadId = leadId;
        this.merchantName = merchantName;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactAddress = contactAddress;
        this.contactNumber = contactNumber;
        this.leadSource = leadSource;
        this.soleId = soleId;
        this.employeeId = employeeId;
        this.leadStatus = leadStatus;
        this.leadSubStatus = leadSubStatus;
        this.isActive = isActive;
        this.leadCancellationReason = leadCancellationReason;
        this.leadCancelRemarks = leadCancelRemarks;
        this.otp = otp;
        this.merchantType = merchantType;
        this.device = device;
        this.deviceCount = deviceCount;
        this.isQR = isQR;
        this.isSoundBox = isSoundBox;
        this.isPos = isPos;
        this.vasInstanceMapping = vasInstanceMapping;
        this.mcc = mcc;
        this.plan = plan;
        this.mdrCharge = mdrCharge;
        this.isMDRSubventionRequested = isMDRSubventionRequested;
        this.isRentalSubventionRequested = isRentalSubventionRequested;
        this.recurringFeeAppliedAmount = recurringFeeAppliedAmount;
        this.setUpFeeAppliedAmount = setUpFeeAppliedAmount;
        this.mdrApprovedByNH = mdrApprovedByNH;
        this.rentalApprovedByNH = rentalApprovedByNH;
        this.mdrRejectedReason = mdrRejectedReason;
        this.rental_rejected_reason = rental_rejected_reason;
        this.corpCC = corpCC;
        this.debitLessThanAmount = debitLessThanAmount;
        this.debitGreaterThanAmount = debitGreaterThanAmount;
        this.intlCC = intlCC;
        this.superPremiumCC = superPremiumCC;
        this.stdCC = stdCC;
        this.premiumCC = premiumCC;
        this.mdrPlanName = mdrPlanName;
        this.recurringFees = recurringFees;
        this.setUpFees = setUpFees;
        this.icAcceptance = icAcceptance;
        this.creditCardBlock = creditCardBlock;
        this.tipPercentage = tipPercentage;
        this.simProvider = simProvider;
        this.settlementMode = settlementMode;
        this.isOmcConvenienceFees = isOmcConvenienceFees;
        this.yearlyRevenue = yearlyRevenue;
        this.legalName = legalName;
        this.dbaName = dbaName;
        this.shop_ownership_status = shop_ownership_status;
        this.weekDayTimingStart = weekDayTimingStart;
        this.weekdayTimingEnd = weekdayTimingEnd;
        this.weekendTimingStart = weekendTimingStart;
        this.weekendTimingEnd = weekendTimingEnd;
        this.maxMonthlyCardVolume = maxMonthlyCardVolume;
        this.avgMonthlyCardvolume = avgMonthlyCardvolume;
        this.turnOverLastFinancialYear = turnOverLastFinancialYear;
        this.avgBillAmount = avgBillAmount;
        this.memberSince = memberSince;
        this.havePosMpos = havePosMpos;
        this.debitMdr = debitMdr;
        this.creditMdr = creditMdr;
        this.amountCollected = amountCollected;
        this.transactionDate = transactionDate;
        this.paymentOption = paymentOption;
        this.paymentContactNumber = paymentContactNumber;
        this.isPaymentSuccess = isPaymentSuccess;
        this.ifscCode = ifscCode;
        this.bank = bank;
        this.branch = branch;
        this.leadSourceMCCAccountTypeMapping = leadSourceMCCAccountTypeMapping;
        this.accountNumber = accountNumber;
        this.referenceNumber = referenceNumber;
        this.merchantDetailsCompleteDate = merchantDetailsCompleteDate;
        this.documentDetailsCompleteDate = documentDetailsCompleteDate;
        this.mdrDetailsCompleteDate = mdrDetailsCompleteDate;
        this.bizDetailsCompleteDate = bizDetailsCompleteDate;
        this.paymentDetailsCompleteDate = paymentDetailsCompleteDate;
        this.otpVerified = otpVerified;
        this.isMerchantDetailsSaved = isMerchantDetailsSaved;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.appliedDebitLessthanAmount =appliedDebitLessthanAmount;
        this.appliedDebitGreaterthanAmount = appliedDebitGreaterthanAmount;
        this.appliedStdCC = appliedStdCC;
        this.appliedPremiumCC = appliedPremiumCC;
        this.appliedCorpCC = appliedCorpCC;
        this.appliedIntlCC = appliedIntlCC;
        this.appliedSuperPremiumCC = appliedSuperPremiumCC;
        this.visitDateAndTime = visitDateAndTime;
        this.followUpDateAndTime = followUpDateAndTime;
        this.followUpRemarks = followUpRemarks;
        this.mccApproval =mccApproval;
        this.icaApproval =icaApproval;
        this.mccReason =mccReason;
        this.osvDetailsCompletedDate=osvDetailsCompletedDate;
        this.osvOtpVerified=osvOtpVerified;
        this.icaEnabled=icaEnabled;
        this.posStatus=posStatus;
        this.aggId=aggId;
    }

    public long getLeadId() {
        return leadId;
    }

    public void setLeadId(long leadId) {
        this.leadId = leadId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
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

    public LeadSource getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(LeadSource leadSource) {
        this.leadSource = leadSource;
    }

    public String getSoleId() {
        return soleId;
    }

    public void setSoleId(String soleId) {
        this.soleId = soleId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LeadStatus getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(LeadStatus leadStatus) {
        this.leadStatus = leadStatus;
    }

    public LeadSubStatus getLeadSubStatus() {
        return leadSubStatus;
    }

    public void setLeadSubStatus(LeadSubStatus leadSubStatus) {
        this.leadSubStatus = leadSubStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public MerchantType getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(MerchantType merchantType) {
        this.merchantType = merchantType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

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

    public boolean isPos() {
        return isPos;
    }

    public void setPos(boolean pos) {
        isPos = pos;
    }

    public String getVasInstanceMapping() {
        return vasInstanceMapping;
    }

    public void setVasInstanceMapping(String vasInstanceMapping) {
        this.vasInstanceMapping = vasInstanceMapping;
    }

    public MerchantCategoryCode getMcc() {
        return mcc;
    }

    public void setMcc(MerchantCategoryCode mcc) {
        this.mcc = mcc;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public MDRCharge getMdrCharge() {
        return mdrCharge;
    }

    public void setMdrCharge(MDRCharge mdrCharge) {
        this.mdrCharge = mdrCharge;
    }

    public boolean isMDRSubventionRequested() {
        return isMDRSubventionRequested;
    }

    public void setMDRSubventionRequested(boolean MDRSubventionRequested) {
        isMDRSubventionRequested = MDRSubventionRequested;
    }

    public boolean isRentalSubventionRequested() {
        return isRentalSubventionRequested;
    }

    public void setRentalSubventionRequested(boolean rentalSubventionRequested) {
        isRentalSubventionRequested = rentalSubventionRequested;
    }

    public double getRecurringFeeAppliedAmount() {
        return recurringFeeAppliedAmount;
    }

    public void setRecurringFeeAppliedAmount(double recurringFeeAppliedAmount) {
        this.recurringFeeAppliedAmount = recurringFeeAppliedAmount;
    }

    public double getSetUpFeeAppliedAmount() {
        return setUpFeeAppliedAmount;
    }

    public void setSetUpFeeAppliedAmount(double setUpFeeAppliedAmount) {
        this.setUpFeeAppliedAmount = setUpFeeAppliedAmount;
    }

    public boolean isMdrApprovedByNH() {
        return mdrApprovedByNH;
    }

    public void setMdrApprovedByNH(boolean mdrApprovedByNH) {
        this.mdrApprovedByNH = mdrApprovedByNH;
    }

    public boolean isRentalApprovedByNH() {
        return rentalApprovedByNH;
    }

    public void setRentalApprovedByNH(boolean rentalApprovedByNH) {
        this.rentalApprovedByNH = rentalApprovedByNH;
    }

    public String getMdrRejectedReason() {
        return mdrRejectedReason;
    }

    public void setMdrRejectedReason(String mdrRejectedReason) {
        this.mdrRejectedReason = mdrRejectedReason;
    }

    public String getRental_rejected_reason() {
        return rental_rejected_reason;
    }

    public void setRental_rejected_reason(String rental_rejected_reason) {
        this.rental_rejected_reason = rental_rejected_reason;
    }

    public double getCorpCC() {
        return corpCC;
    }

    public void setCorpCC(double corpCC) {
        this.corpCC = corpCC;
    }

    public double getDebitLessThanAmount() {
        return debitLessThanAmount;
    }

    public void setDebitLessThanAmount(double debitLessThanAmount) {
        this.debitLessThanAmount = debitLessThanAmount;
    }

    public double getDebitGreaterThanAmount() {
        return debitGreaterThanAmount;
    }

    public void setDebitGreaterThanAmount(double debitGreaterThanAmount) {
        this.debitGreaterThanAmount = debitGreaterThanAmount;
    }

    public double getIntlCC() {
        return intlCC;
    }

    public void setIntlCC(double intlCC) {
        this.intlCC = intlCC;
    }

    public double getSuperPremiumCC() {
        return superPremiumCC;
    }

    public void setSuperPremiumCC(double superPremiumCC) {
        this.superPremiumCC = superPremiumCC;
    }

    public double getStdCC() {
        return stdCC;
    }

    public void setStdCC(double stdCC) {
        this.stdCC = stdCC;
    }

    public double getPremiumCC() {
        return premiumCC;
    }

    public void setPremiumCC(double premiumCC) {
        this.premiumCC = premiumCC;
    }

    public String getMdrPlanName() {
        return mdrPlanName;
    }

    public void setMdrPlanName(String mdrPlanName) {
        this.mdrPlanName = mdrPlanName;
    }

    public double getRecurringFees() {
        return recurringFees;
    }

    public void setRecurringFees(double recurringFees) {
        this.recurringFees = recurringFees;
    }

    public double getSetUpFees() {
        return setUpFees;
    }

    public void setSetUpFees(double setUpFees) {
        this.setUpFees = setUpFees;
    }

    public boolean isIcAcceptance() {
        return icAcceptance;
    }

    public void setIcAcceptance(boolean icAcceptance) {
        this.icAcceptance = icAcceptance;
    }

    public boolean isCreditCardBlock() {
        return creditCardBlock;
    }

    public void setCreditCardBlock(boolean creditCardBlock) {
        this.creditCardBlock = creditCardBlock;
    }

    public double getTipPercentage() {
        return tipPercentage;
    }

    public void setTipPercentage(double tipPercentage) {
        this.tipPercentage = tipPercentage;
    }

    public SimProvider getSimProvider() {
        return simProvider;
    }

    public void setSimProvider(SimProvider simProvider) {
        this.simProvider = simProvider;
    }

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

    public Date getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(Date memberSince) {
        this.memberSince = memberSince;
    }

//    public boolean isHavePosMpos() {
//        return havePosMpos;
//    }
//
//    public void setHavePosMpos(boolean havePosMpos) {
//        this.havePosMpos = havePosMpos;
//    }


    public boolean isPosFlag() {
        return posFlag;
    }

    public void setPosFlag(boolean posFlag) {
        this.posFlag = posFlag;
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

    public double getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(double amountCollected) {
        this.amountCollected = amountCollected;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(int paymentOption) {
        this.paymentOption = paymentOption;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public LeadSourceMCCAccountTypeMapping getLeadSourceMCCAccountTypeMapping() {
        return leadSourceMCCAccountTypeMapping;
    }

    public void setLeadSourceMCCAccountTypeMapping(LeadSourceMCCAccountTypeMapping leadSourceMCCAccountTypeMapping) {
        this.leadSourceMCCAccountTypeMapping = leadSourceMCCAccountTypeMapping;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LeadCancellationReason getLeadCancellationReason() {
        return leadCancellationReason;
    }

    public void setLeadCancellationReason(LeadCancellationReason leadCancellationReason) {
        this.leadCancellationReason = leadCancellationReason;
    }

    public String getLeadCancelRemarks() {
        return leadCancelRemarks;
    }

    public void setLeadCancelRemarks(String leadCancelRemarks) {
        this.leadCancelRemarks = leadCancelRemarks;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getMerchantDetailsCompleteDate() {
        return merchantDetailsCompleteDate;
    }

    public void setMerchantDetailsCompleteDate(Date merchantDetailsCompleteDate) {
        this.merchantDetailsCompleteDate = merchantDetailsCompleteDate;
    }

    public Date getDocumentDetailsCompleteDate() {
        return documentDetailsCompleteDate;
    }

    public void setDocumentDetailsCompleteDate(Date documentDetailsCompleteDate) {
        this.documentDetailsCompleteDate = documentDetailsCompleteDate;
    }

    public Date getMdrDetailsCompleteDate() {
        return mdrDetailsCompleteDate;
    }

    public void setMdrDetailsCompleteDate(Date mdrDetailsCompleteDate) {
        this.mdrDetailsCompleteDate = mdrDetailsCompleteDate;
    }

    public Date getBizDetailsCompleteDate() {
        return bizDetailsCompleteDate;
    }

    public void setBizDetailsCompleteDate(Date bizDetailsCompleteDate) {
        this.bizDetailsCompleteDate = bizDetailsCompleteDate;
    }

    public Date getPaymentDetailsCompleteDate() {
        return paymentDetailsCompleteDate;
    }

    public void setPaymentDetailsCompleteDate(Date paymentDetailsCompleteDate) {
        this.paymentDetailsCompleteDate = paymentDetailsCompleteDate;
    }

    public boolean isMerchantDetailsSaved() {
        return isMerchantDetailsSaved;
    }

    public void setMerchantDetailsSaved(boolean merchantDetailsSaved) {
        isMerchantDetailsSaved = merchantDetailsSaved;
    }

    public boolean isOtpVerified() {
        return otpVerified;
    }

    public void setOtpVerified(boolean otpVerified) {
        this.otpVerified = otpVerified;
    }

    public List<UploadedDocuments> getUploadedDocuments() {
        return uploadedDocuments;
    }

    public void setUploadedDocuments(List<UploadedDocuments> uploadedDocuments) {
        this.uploadedDocuments = uploadedDocuments;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getSubmittedToMarsDate() {
        return submittedToMarsDate;
    }

    public void setSubmittedToMarsDate(Date submittedToMarsDate) {
        this.submittedToMarsDate = submittedToMarsDate;
    }

    public int getLeadCategory() {
        return leadCategory;
    }

    public void setLeadCategory(int leadCategory) {
        this.leadCategory = leadCategory;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getMerchantRefCode() {
        return merchantRefCode;
    }

    public void setMerchantRefCode(String merchantRefCode) {
        this.merchantRefCode = merchantRefCode;
    }

    public String getMarsReason() {
        return marsReason;
    }

    public void setMarsReason(String marsReason) {
        this.marsReason = marsReason;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public boolean isBijlipaySwitch() {
        return isBijlipaySwitch;
    }

    public void setBijlipaySwitch(boolean bijlipaySwitch) {
        isBijlipaySwitch = bijlipaySwitch;
    }

    public boolean isPennyDropVerified() {
        return isPennyDropVerified;
    }

    public void setPennyDropVerified(boolean pennyDropVerified) {
        isPennyDropVerified = pennyDropVerified;
    }



    public double getAppliedDebitLessthanAmount() {
        return appliedDebitLessthanAmount;
    }

    public void setAppliedDebitLessthanAmount(double appliedDebitLessthanAmount) {
        this.appliedDebitLessthanAmount = appliedDebitLessthanAmount;
    }

    public double getAppliedDebitGreaterthanAmount() {
        return appliedDebitGreaterthanAmount;
    }

    public void setAppliedDebitGreaterthanAmount(double appliedDebitGreaterthanAmount) {
        this.appliedDebitGreaterthanAmount = appliedDebitGreaterthanAmount;
    }

    public double getAppliedStdCC() {
        return appliedStdCC;
    }

    public void setAppliedStdCC(double appliedStdCC) {
        this.appliedStdCC = appliedStdCC;
    }

    public double getAppliedPremiumCC() {
        return appliedPremiumCC;
    }

    public void setAppliedPremiumCC(double appliedPremiumCC) {
        this.appliedPremiumCC = appliedPremiumCC;
    }

    public double getAppliedCorpCC() {
        return appliedCorpCC;
    }

    public void setAppliedCorpCC(double appliedCorpCC) {
        this.appliedCorpCC = appliedCorpCC;
    }

    public double getAppliedIntlCC() {
        return appliedIntlCC;
    }

    public void setAppliedIntlCC(double appliedIntlCC) {
        this.appliedIntlCC = appliedIntlCC;
    }

    public double getAppliedSuperPremiumCC() {
        return appliedSuperPremiumCC;
    }

    public void setAppliedSuperPremiumCC(double appliedSuperPremiumCC) {
        this.appliedSuperPremiumCC = appliedSuperPremiumCC;
    }

    public Date getVisitDateAndTime() {
        return visitDateAndTime;
    }

    public void setVisitDateAndTime(Date visitDateAndTime) {
        this.visitDateAndTime = visitDateAndTime;
    }

    public Date getFollowUpDateAndTime() {
        return followUpDateAndTime;
    }

    public void setFollowUpDateAndTime(Date followUpDateAndTime) {
        this.followUpDateAndTime = followUpDateAndTime;
    }

    public String getFollowUpRemarks() {
        return followUpRemarks;
    }

    public void setFollowUpRemarks(String followUpRemarks) {
        this.followUpRemarks = followUpRemarks;
    }

    public MerchantCategory getMerchantCategory() {
        return merchantCategory;
    }

    public void setMerchantCategory(MerchantCategory merchantCategory) {
        this.merchantCategory = merchantCategory;
    }


    public int getCmsLeadStatus() {
        return cmsLeadStatus;
    }

    public void setCmsLeadStatus(int cmsLeadStatus) {
        this.cmsLeadStatus = cmsLeadStatus;
    }

    public Integer getVerifiedCmsPricingStatus() {
        return verifiedCmsPricingStatus;
    }

    public void setVerifiedCmsPricingStatus(Integer verifiedCmsPricingStatus) {
        this.verifiedCmsPricingStatus = verifiedCmsPricingStatus;
    }

    public Integer getVerifiedCmsFinanceStatus() {
        return verifiedCmsFinanceStatus;
    }

    public void setVerifiedCmsFinanceStatus(Integer verifiedCmsFinanceStatus) {
        this.verifiedCmsFinanceStatus = verifiedCmsFinanceStatus;
    }

//    public List<LeadVerificationStatus> getLeadVerificationStatus() {
//        return leadVerificationStatus;
//    }
//
//    public void setLeadVerificationStatus(List<LeadVerificationStatus> leadVerificationStatus) {
//        this.leadVerificationStatus = leadVerificationStatus;
//    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getMerchantLatLng() {
        return merchantLatLng;
    }

    public void setMerchantLatLng(String merchantLatLng) {
        this.merchantLatLng = merchantLatLng;
    }

    public String getTotalTids() {
        return totalTids;
    }

    public void setTotalTids(String totalTids) {
        this.totalTids = totalTids;
    }

    public User getAssignedToNH() {
        return assignedToNH;
    }

    public void setAssignedToNH(User assignedToNH) {
        this.assignedToNH = assignedToNH;
    }

    public String getOnboardingVendor() {
        return onboardingVendor;
    }

    public void setOnboardingVendor(String onboardingVendor) {
        this.onboardingVendor = onboardingVendor;
    }

    public int getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(int settlementType) {
        this.settlementType = settlementType;
    }

    public User getLeadApprovedBy() {
        return leadApprovedBy;
    }

    public void setLeadApprovedBy(User leadApprovedBy) {
        this.leadApprovedBy = leadApprovedBy;
    }

    public User getLeadRejectBy() {
        return leadRejectBy;
    }

    public void setLeadRejectBy(User leadRejectBy) {
        this.leadRejectBy = leadRejectBy;
    }

    public Date getLeadApprovedDate() {
        return leadApprovedDate;
    }

    public void setLeadApprovedDate(Date leadApprovedDate) {
        this.leadApprovedDate = leadApprovedDate;
    }

    public Date getLeadRejectedDate() {
        return leadRejectedDate;
    }

    public void setLeadRejectedDate(Date leadRejectedDate) {
        this.leadRejectedDate = leadRejectedDate;
    }

    public String getMdrPlancode() {
        return mdrPlancode;
    }

    public void setMdrPlancode(String mdrPlancode) {
        this.mdrPlancode = mdrPlancode;
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

    public MarsMdrCharges getMarsMdrCharges() {
        return marsMdrCharges;
    }

    public void setMarsMdrCharges(MarsMdrCharges marsMdrCharges) {
        this.marsMdrCharges = marsMdrCharges;
    }

    public MarsRentalPlans getMarsRentalPlans() {
        return marsRentalPlans;
    }

    public void setMarsRentalPlans(MarsRentalPlans marsRentalPlans) {
        this.marsRentalPlans = marsRentalPlans;
    }

    public String getRentalPlanCode() {
        return rentalPlanCode;
    }

    public void setRentalPlanCode(String rentalPlanCode) {
        this.rentalPlanCode = rentalPlanCode;
    }

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

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public boolean isAgreementSuccess() {
        return isAgreementSuccess;
    }

    public void setAgreementSuccess(boolean agreementSuccess) {
        isAgreementSuccess = agreementSuccess;
    }

    public boolean isAggregator() {
        return isAggregator;
    }

    public void setAggregator(boolean aggregator) {
        isAggregator = aggregator;
    }

    public boolean isManualAgreement() {
        return isManualAgreement;
    }

    public void setManualAgreement(boolean manualAgreement) {
        isManualAgreement = manualAgreement;
    }

    public String getVpaName() {
        return vpaName;
    }

    public void setVpaName(String vpaName) {
        this.vpaName = vpaName;
    }

    public Device getSqrDevice() {
        return sqrDevice;
    }

    public void setSqrDevice(Device sqrDevice) {
        this.sqrDevice = sqrDevice;
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

    public int getIsMarsApiTriggered() {
        return isMarsApiTriggered;
    }

    public void setIsMarsApiTriggered(int isMarsApiTriggered) {
        this.isMarsApiTriggered = isMarsApiTriggered;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isManualApproved() {
        return isManualApproved;
    }

    public void setManualApproved(boolean manualApproved) {
        isManualApproved = manualApproved;
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
        this.upichargeslipEnabled = upichargeslipEnabled;
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

    public void setMarsDevice(String marsDevice) {
        this.marsDevice = marsDevice;
    }


    public boolean getPosQr() {
        return posQr;
    }

    public void setPosQr(boolean posQr) {
        this.posQr = posQr;
    }

    public LanguageSoundbox getLanguageSoundbox() {
        return languageSoundbox;
    }

    public void setLanguageSoundbox(LanguageSoundbox languageSoundbox) {
        this.languageSoundbox = languageSoundbox;
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

    public List<LeadVerificationStatus> getLeadVerificationStatus() {
        return leadVerificationStatus;
    }

    public void setLeadVerificationStatus(List<LeadVerificationStatus> leadVerificationStatus) {
        this.leadVerificationStatus = leadVerificationStatus;
    }

    public String getOpsRejectedRemarks() {
        return opsRejectedRemarks;
    }

    public void setOpsRejectedRemarks(String opsRejectedRemarks) {
        this.opsRejectedRemarks = opsRejectedRemarks;
    }

    public Date getRejectLeadDate() {
        return rejectLeadDate;
    }

    public void setRejectLeadDate(Date rejectLeadDate) {
        this.rejectLeadDate = rejectLeadDate;
    }

    public User getRejectLeadBy() {
        return rejectLeadBy;
    }

    public void setRejectLeadBy(User rejectLeadBy) {
        this.rejectLeadBy = rejectLeadBy;
    }

    public Date getSwitchRejectDate() {
        return switchRejectDate;
    }

    public void setSwitchRejectDate(Date switchRejectDate) {
        this.switchRejectDate = switchRejectDate;
    }

    public boolean isPosQr() {
        return posQr;
    }

    public boolean isMccApproval() {
        return mccApproval;
    }

    public void setMccApproval(boolean mccApproval) {
        this.mccApproval = mccApproval;
    }

    public boolean isIcaApproval() {
        return icaApproval;
    }

    public void setIcaApproval(boolean icaApproval) {
        this.icaApproval = icaApproval;
    }

    public String getMccReason() {
        return mccReason;
    }

    public void setMccReason(String mccReason) {
        this.mccReason = mccReason;
    }

    public List<UploadedDocuments> getRejectedDocuments() {
        return rejectedDocuments;
    }

    public void setRejectedDocuments(List<UploadedDocuments> rejectedDocuments) {
        this.rejectedDocuments = rejectedDocuments;
    }

    public ImplementDetailsDto getImplementDetailsDto() {
        return implementDetailsDto;
    }

    public void setImplementDetailsDto(ImplementDetailsDto implementDetailsDto) {
        this.implementDetailsDto = implementDetailsDto;
    }

    public List<SubventionLogs> getSubventionLogs() {
        return subventionLogs;
    }

    public void setSubventionLogs(List<SubventionLogs> subventionLogs) {
        this.subventionLogs = subventionLogs;
    }
    public MccDocumentDto getMccDocuments() {
        return mccDocuments;
    }

    public MerchantCategoryCode getOpsMcc() {
        return opsMcc;
    }

    public void setOpsMcc(MerchantCategoryCode opsMcc) {
        this.opsMcc = opsMcc;
    }

    public Date getMccApproved() {
        return mccApproved;
    }

    public void setMccApproved(Date mccApproved) {
        this.mccApproved = mccApproved;
    }

    public int isDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(int documentStatus) {
        this.documentStatus = documentStatus;
    }

    public int getDocumentStatus() {
        return documentStatus;
    }

    public Boolean getIcaStatus() {
        return icaStatus;
    }

    public void setIcaStatus(Boolean icaStatus) {
        this.icaStatus = icaStatus;
    }
      public void setMccDocuments(MccDocumentDto mccDocuments) {
        this.mccDocuments = mccDocuments;
    }

    public Date getOsvDetailsCompletedDate() {
        return osvDetailsCompletedDate;
    }

    public void setOsvDetailsCompletedDate(Date osvDetailsCompletedDate) {
        this.osvDetailsCompletedDate = osvDetailsCompletedDate;
    }

    public int getPosStatus() {
        return posStatus;
    }

    public void setPosStatus(int posStatus) {
        this.posStatus = posStatus;
    }

    public boolean isOsvOtpVerified() {
        return osvOtpVerified;
    }

    public void setOsvOtpVerified(boolean osvOtpVerified) {
        this.osvOtpVerified = osvOtpVerified;
    }

    public int getIcaEnabled() {
        return icaEnabled;
    }

    public void setIcaEnable(int icaEnable) {
        this.icaEnabled = icaEnabled;
    }

    public Integer getQrCount() {
        return qrCount;
    }

    public void setQrCount(Integer qrCount) {
        this.qrCount = qrCount;
    }

    public void setIcaEnabled(int icaEnabled) {
        this.icaEnabled = icaEnabled;
    }

    public boolean isQrFlag() {
        return qrFlag;
    }

    public void setQrFlag(boolean qrFlag) {
        this.qrFlag = qrFlag;
    }

    public double getSqrRecurringFees() {
        return sqrRecurringFees;
    }

    public void setSqrRecurringFees(double sqrRecurringFees) {
        this.sqrRecurringFees = sqrRecurringFees;
    }

    public double getSqrSetUpFees() {
        return sqrSetUpFees;
    }

    public void setSqrSetUpFees(double sqrSetUpFees) {
        this.sqrSetUpFees = sqrSetUpFees;
    }

    public MarsMdrCharges getSqrMarsMdrCharge() {
        return sqrMarsMdrCharge;
    }

    public void setSqrMarsMdrCharge(MarsMdrCharges sqrMarsMdrCharge) {
        this.sqrMarsMdrCharge = sqrMarsMdrCharge;
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

    public String getAlternateMobileNo() {
        return alternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        this.alternateMobileNo = alternateMobileNo;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public MarsRentalPlans getStickerMarsRentalPlans() {
        return stickerMarsRentalPlans;
    }

    public void setStickerMarsRentalPlans(MarsRentalPlans stickerMarsRentalPlans) {
        this.stickerMarsRentalPlans = stickerMarsRentalPlans;
    }

    public MarsMdrCharges getStickerMarsMdrCharges() {
        return stickerMarsMdrCharges;
    }

    public void setStickerMarsMdrCharges(MarsMdrCharges stickerMarsMdrCharges) {
        this.stickerMarsMdrCharges = stickerMarsMdrCharges;
    }

    public Device getStickerDevice() {
        return stickerDevice;
    }

    public void setStickerDevice(Device stickerDevice) {
        this.stickerDevice = stickerDevice;
    }

    public int getSoundBoxCount() {
        return soundBoxCount;
    }

    public void setSoundBoxCount(int soundBoxCount) {
        this.soundBoxCount = soundBoxCount;
    }

    public double getStickerSetUpFees() {
        return stickerSetUpFees;
    }

    public void setStickerSetUpFees(double stickerSetUpFees) {
        this.stickerSetUpFees = stickerSetUpFees;
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

    public int getAggId() {
        return aggId;
    }

    public void setAggId(int aggId) {
        this.aggId = aggId;
    }

    public int getMerchantRefNo() {
        return merchantRefNo;
    }

    public void setMerchantRefNo(int merchantRefNo) {
        this.merchantRefNo = merchantRefNo;
    }

    public int getNoOfAuthorizer() {
        return noOfAuthorizer;
    }

    public void setNoOfAuthorizer(int noOfAuthorizer) {
        this.noOfAuthorizer = noOfAuthorizer;
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

    public boolean isPullTriggered() {
        return pullTriggered;
    }

    public void setPullTriggered(boolean pullTriggered) {
        this.pullTriggered = pullTriggered;
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

    public boolean isWithOutGst() {
        return withOutGst;
    }

    public void setWithOutGst(boolean withOutGst) {
        this.withOutGst = withOutGst;
    }

    public String getAdditionalDocumentName() {
        return additionalDocumentName;
    }

    public void setAdditionalDocumentName(String additionalDocumentName) {
        this.additionalDocumentName = additionalDocumentName;
    }

    public String getAdditionalDocumentRemark() {
        return additionalDocumentRemark;
    }

    public void setAdditionalDocumentRemark(String additionalDocumentRemark) {
        this.additionalDocumentRemark = additionalDocumentRemark;
    }

    public boolean isDigital() {
        return isDigital;
    }

    public void setDigital(boolean digital) {
        isDigital = digital;
    }

    public boolean isGeeNeePos() {
        return geeNeePos;
    }

    public void setGeeNeePos(boolean geeNeePos) {
        this.geeNeePos = geeNeePos;
    }

    public int getOfflinePaymentType() {
        return offlinePaymentType;
    }

    public void setOfflinePaymentType(int offlinePaymentType) {
        this.offlinePaymentType = offlinePaymentType;
    }

    public RazorPayQRStatus getRazorPayQRStatus() {
        return razorPayQRStatus;
    }

    public void setRazorPayQRStatus(RazorPayQRStatus razorPayQRStatus) {
        this.razorPayQRStatus = razorPayQRStatus;
    }

    public MarsRentalPlans getSqrMarsRentalPlans() {
        return sqrMarsRentalPlans;
    }

    public void setSqrMarsRentalPlans(MarsRentalPlans sqrMarsRentalPlans) {
        this.sqrMarsRentalPlans = sqrMarsRentalPlans;
    }

    @Override
    public String toString() {
        return "LeadInformation{" +
                "leadId=" + leadId +
                ", merchantName='" + merchantName + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", contactAddress='" + contactAddress + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", leadSource=" + leadSource +
                ", soleId='" + soleId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", leadStatus=" + leadStatus +
                ", leadSubStatus=" + leadSubStatus +
                ", isActive=" + isActive +
                ", leadCancellationReason=" + leadCancellationReason +
                ", leadCancelRemarks='" + leadCancelRemarks + '\'' +
                ", otp='" + otp + '\'' +
                ", merchantType=" + merchantType +
                ", device=" + device +
                ", deviceCount=" + deviceCount +
                ", qrCount=" + qrCount +
                ", isQR=" + isQR +
                ", isSoundBox=" + isSoundBox +
                ", isPos=" + isPos +
                ", vasInstanceMapping='" + vasInstanceMapping + '\'' +
                ", mcc=" + mcc +
                ", merchantCategory=" + merchantCategory +
                ", plan=" + plan +
                ", mdrCharge=" + mdrCharge +
                ", isMDRSubventionRequested=" + isMDRSubventionRequested +
                ", isRentalSubventionRequested=" + isRentalSubventionRequested +
                ", recurringFeeAppliedAmount=" + recurringFeeAppliedAmount +
                ", setUpFeeAppliedAmount=" + setUpFeeAppliedAmount +
                ", mdrApprovedByNH=" + mdrApprovedByNH +
                ", rentalApprovedByNH=" + rentalApprovedByNH +
                ", mdrRejectedReason='" + mdrRejectedReason + '\'' +
                ", rental_rejected_reason='" + rental_rejected_reason + '\'' +
                ", corpCC=" + corpCC +
                ", debitLessThanAmount=" + debitLessThanAmount +
                ", debitGreaterThanAmount=" + debitGreaterThanAmount +
                ", intlCC=" + intlCC +
                ", superPremiumCC=" + superPremiumCC +
                ", stdCC=" + stdCC +
                ", premiumCC=" + premiumCC +
                ", mdrPlanName='" + mdrPlanName + '\'' +
                ", recurringFees=" + recurringFees +
                ", setUpFees=" + setUpFees +
                ", icAcceptance=" + icAcceptance +
                ", creditCardBlock=" + creditCardBlock +
                ", tipPercentage=" + tipPercentage +
                ", simProvider=" + simProvider +
                ", settlementMode=" + settlementMode +
                ", isOmcConvenienceFees=" + isOmcConvenienceFees +
                ", yearlyRevenue=" + yearlyRevenue +
                ", legalName='" + legalName + '\'' +
                ", dbaName='" + dbaName + '\'' +
                ", shop_ownership_status=" + shop_ownership_status +
                ", weekDayTimingStart='" + weekDayTimingStart + '\'' +
                ", weekdayTimingEnd='" + weekdayTimingEnd + '\'' +
                ", weekendTimingStart='" + weekendTimingStart + '\'' +
                ", weekendTimingEnd='" + weekendTimingEnd + '\'' +
                ", maxMonthlyCardVolume=" + maxMonthlyCardVolume +
                ", avgMonthlyCardvolume=" + avgMonthlyCardvolume +
                ", turnOverLastFinancialYear='" + turnOverLastFinancialYear + '\'' +
                ", avgBillAmount=" + avgBillAmount +
                ", memberSince=" + memberSince +
                ", havePosMpos=" + havePosMpos +
                ", posStatus=" + posStatus +
                ", debitMdr=" + debitMdr +
                ", creditMdr=" + creditMdr +
                ", amountCollected=" + amountCollected +
                ", transactionDate=" + transactionDate +
                ", paymentOption=" + paymentOption +
                ", paymentContactNumber='" + paymentContactNumber + '\'' +
                ", isPaymentSuccess=" + isPaymentSuccess +
                ", ifscCode='" + ifscCode + '\'' +
                ", bank='" + bank + '\'' +
                ", branch='" + branch + '\'' +
                ", leadSourceMCCAccountTypeMapping=" + leadSourceMCCAccountTypeMapping +
                ", accountNumber='" + accountNumber + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", merchantDetailsCompleteDate=" + merchantDetailsCompleteDate +
                ", documentDetailsCompleteDate=" + documentDetailsCompleteDate +
                ", mdrDetailsCompleteDate=" + mdrDetailsCompleteDate +
                ", bizDetailsCompleteDate=" + bizDetailsCompleteDate +
                ", paymentDetailsCompleteDate=" + paymentDetailsCompleteDate +
                ", osvDetailsCompletedDate=" + osvDetailsCompletedDate +
                ", otpVerified=" + otpVerified +
                ", isMerchantDetailsSaved=" + isMerchantDetailsSaved +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", assignedTo=" + assignedTo +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", submittedToMarsDate=" + submittedToMarsDate +
                ", leadCategory=" + leadCategory +
                ", reason='" + reason + '\'' +
                ", leadNumber='" + leadNumber + '\'' +
                ", applicationNumber='" + applicationNumber + '\'' +
                ", merchantRefCode='" + merchantRefCode + '\'' +
                ", marsReason='" + marsReason + '\'' +
                ", region=" + region +
                ", reject=" + reject +
                ", isBijlipaySwitch=" + isBijlipaySwitch +
                ", isPennyDropVerified=" + isPennyDropVerified +
                ", appliedDebitLessthanAmount=" + appliedDebitLessthanAmount +
                ", appliedDebitGreaterthanAmount=" + appliedDebitGreaterthanAmount +
                ", appliedStdCC=" + appliedStdCC +
                ", appliedPremiumCC=" + appliedPremiumCC +
                ", appliedCorpCC=" + appliedCorpCC +
                ", appliedIntlCC=" + appliedIntlCC +
                ", appliedSuperPremiumCC=" + appliedSuperPremiumCC +
                ", visitDateAndTime=" + visitDateAndTime +
                ", followUpDateAndTime=" + followUpDateAndTime +
                ", followUpRemarks='" + followUpRemarks + '\'' +
                ", cmsLeadStatus=" + cmsLeadStatus +
                ", verifiedCmsPricingStatus=" + verifiedCmsPricingStatus +
                ", verifiedCmsFinanceStatus=" + verifiedCmsFinanceStatus +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", pincode=" + pincode +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", ownerDOB=" + ownerDOB +
                ", ownerFirstName='" + ownerFirstName + '\'' +
                ", ownerLastName='" + ownerLastName + '\'' +
                ", merchantStateName='" + merchantStateName + '\'' +
                ", merchantDistrictName='" + merchantDistrictName + '\'' +
                ", merchantSubDistrictName='" + merchantSubDistrictName + '\'' +
                ", merchantVillageName='" + merchantVillageName + '\'' +
                ", merchantTier='" + merchantTier + '\'' +
                ", merchantLatLng='" + merchantLatLng + '\'' +
                ", totalTids='" + totalTids + '\'' +
                ", assignedToNH=" + assignedToNH +
                ", leadVerificationStatus=" + leadVerificationStatus +
                ", leadApprovedBy=" + leadApprovedBy +
                ", leadRejectBy=" + leadRejectBy +
                ", leadApprovedDate=" + leadApprovedDate +
                ", leadRejectedDate=" + leadRejectedDate +
                ", opsRejectedRemarks='" + opsRejectedRemarks + '\'' +
                ", rejectLeadDate=" + rejectLeadDate +
                ", rejectLeadBy=" + rejectLeadBy +
                ", switchRejectDate=" + switchRejectDate +
                ", onboardingVendor='" + onboardingVendor + '\'' +
                ", settlementType=" + settlementType +
                ", mdrPlancode='" + mdrPlancode + '\'' +
                ", domesticDebitUpTo2000='" + domesticDebitUpTo2000 + '\'' +
                ", domesticDebitAbove2000='" + domesticDebitAbove2000 + '\'' +
                ", standardOrClassic='" + standardOrClassic + '\'' +
                ", premiumOrPlatinum='" + premiumOrPlatinum + '\'' +
                ", superPremiumOrSignature='" + superPremiumOrSignature + '\'' +
                ", commercialOrCorporate='" + commercialOrCorporate + '\'' +
                ", internationalDebitCard='" + internationalDebitCard + '\'' +
                ", internationalCreditCard='" + internationalCreditCard + '\'' +
                ", onus='" + onus + '\'' +
                ", diners='" + diners + '\'' +
                ", cashAtPos='" + cashAtPos + '\'' +
                ", masterPass='" + masterPass + '\'' +
                ", marsMdrCharges=" + marsMdrCharges +
                ", marsRentalPlans=" + marsRentalPlans +
                ", rentalPlanCode='" + rentalPlanCode + '\'' +
                ", isProfessionalLead=" + isProfessionalLead +
                ", panNumber='" + panNumber + '\'' +
                ", gstNumber='" + gstNumber + '\'' +
                ", natureOfBusiness='" + natureOfBusiness + '\'' +
                ", aggregator='" + aggregator + '\'' +
                ", categoryType=" + categoryType +
                ", isAgreementSuccess=" + isAgreementSuccess +
                ", isAggregator=" + isAggregator +
                ", isManualAgreement=" + isManualAgreement +
                ", vpaName='" + vpaName + '\'' +
                ", sqrDevice=" + sqrDevice +
                ", posFlag=" + posFlag +
                ", rid='" + rid + '\'' +
                ", caseNumber='" + caseNumber + '\'' +
                ", isMarsApiTriggered=" + isMarsApiTriggered +
                ", zone='" + zone + '\'' +
                ", isManualApproved=" + isManualApproved +
                ", storeAddress='" + storeAddress + '\'' +
                ", merchantPan='" + merchantPan + '\'' +
                ", merchantDob='" + merchantDob + '\'' +
                ", institutionCode=" + institutionCode +
                ", marsRegion='" + marsRegion + '\'' +
                ", constitution='" + constitution + '\'' +
                ", establishYear='" + establishYear + '\'' +
                ", registerNumber='" + registerNumber + '\'' +
                ", maximumMonthlyUsage='" + maximumMonthlyUsage + '\'' +
                ", merchantTypeCode='" + merchantTypeCode + '\'' +
                ", advanceRentMode='" + advanceRentMode + '\'' +
                ", deviceOwnedBy='" + deviceOwnedBy + '\'' +
                ", devicePrice='" + devicePrice + '\'' +
                ", emiTenure='" + emiTenure + '\'' +
                ", emiStartDate='" + emiStartDate + '\'' +
                ", gracePeriod=" + gracePeriod +
                ", installationFee='" + installationFee + '\'' +
                ", noOfMonthRentPaidInAdvance=" + noOfMonthRentPaidInAdvance +
                ", rentalMode='" + rentalMode + '\'' +
                ", rentalType='" + rentalType + '\'' +
                ", totalEmiAmount='" + totalEmiAmount + '\'' +
                ", terminalModeCode='" + terminalModeCode + '\'' +
                ", terminalType='" + terminalType + '\'' +
                ", bqrEnabled=" + bqrEnabled +
                ", cashAtPosEnabled='" + cashAtPosEnabled + '\'' +
                ", linkpaymentFlag=" + linkpaymentFlag +
                ", matmAllowed='" + matmAllowed + '\'' +
                ", preAuth='" + preAuth + '\'' +
                ", saleFlag=" + saleFlag +
                ", settlement='" + settlement + '\'' +
                ", tipEnabled='" + tipEnabled + '\'' +
                ", upichargeslipEnabled=" + upichargeslipEnabled +
                ", upiFlag=" + upiFlag +
                ", accountType='" + accountType + '\'' +
                ", marsDevice='" + marsDevice + '\'' +
                ", posQr=" + posQr +
                ", languageSoundbox=" + languageSoundbox +
                ", isSQR=" + isSQR +
                ", upiCreditAbove2000='" + upiCreditAbove2000 + '\'' +
                ", upiCreditUpto2000='" + upiCreditUpto2000 + '\'' +
                ", upiAbove2000='" + upiAbove2000 + '\'' +
                ", upiUpto2000='" + upiUpto2000 + '\'' +
                ", mccApproval=" + mccApproval +
                ", icaApproval=" + icaApproval +
                ", mccReason='" + mccReason + '\'' +
                ", osvOtpVerified=" + osvOtpVerified +
                ", icaEnabled=" + icaEnabled +
                ", uploadedDocuments=" + uploadedDocuments +
                ", rejectedDocuments=" + rejectedDocuments +
                ", implementDetailsDto=" + implementDetailsDto +
                ", subventionLogs=" + subventionLogs +
                ", opsMcc=" + opsMcc +
                ", documentStatus=" + documentStatus +
                ", mccApproved=" + mccApproved +
                ", icaStatus=" + icaStatus +
                ", mccDocuments=" + mccDocuments +
                ", qrFlag=" + qrFlag +
                ", sqrRecurringFees=" + sqrRecurringFees +
                ", sqrSetUpFees=" + sqrSetUpFees +
                ", sqrMarsMdrCharge=" + sqrMarsMdrCharge +
                ", noOfStaff=" + noOfStaff +
                ", bussinessInLocation='" + bussinessInLocation + '\'' +
                ", alternateMobileNo='" + alternateMobileNo + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                ", stickerMarsRentalPlans=" + stickerMarsRentalPlans +
                ", stickerMarsMdrCharges=" + stickerMarsMdrCharges +
                ", stickerDevice=" + stickerDevice +
                ", soundBoxCount=" + soundBoxCount +
                ", stickerSetUpFees=" + stickerSetUpFees +
                ", amexDomestic='" + amexDomestic + '\'' +
                ", amexInternational='" + amexInternational + '\'' +
                ", aggId=" + aggId +
                ", merchantRefNo=" + merchantRefNo +
                ", noOfAuthorizer=" + noOfAuthorizer +
                ", stockverificationConfirmation=" + stockverificationConfirmation +
                ", remarks='" + remarks + '\'' +
                ", pullTriggered=" + pullTriggered +
                ", nameOfAuthorizer='" + nameOfAuthorizer + '\'' +
                ", emailIdForAuthorizer='" + emailIdForAuthorizer + '\'' +
                ", phoneForAuthorizer='" + phoneForAuthorizer + '\'' +
                ", addressForAuthorizer='" + addressForAuthorizer + '\'' +
                ", panNumberForAuthorizer='" + panNumberForAuthorizer + '\'' +
                ", isAuthorizedForAuthorizer='" + isAuthorizedForAuthorizer + '\'' +
                '}';
    }

}
