package com.bijlipay.config;

import org.springframework.beans.factory.annotation.Value;

public final class Constants {
    // Signzy Identity Object
    public static final String IDENTITY_TYPE_AADHAAR = "aadhaar";
    public static final String IDENTITY_TYPE_INDIVIDUAL_PAN ="individualPan";
    public static final String IDENTITY_TYPE_BUSINESS_PAN ="businessPan";
    public static final String IDENTITY_TYPE_PASSPORT ="passport";
    public static final String IDENTITY_TYPE_VOTER_ID ="voterid";
    public static final String IDENTITY_TYPE_DRIVING_LICENCE ="drivingLicence";

    // Signzy Fetch Types
    public static final String FETCH_TYPE_GST_IN ="GST Document";
    public static final String FETCH_TYPE_PAN ="Pan";
    public static final String FETCH_TYPE_DRIVING_LICENCE = "Driving License Front";
    public static final String FETCH_TYPE_PASSPORT = "Passport Front";
    public static final String FETCH_TYPE_VOTER_ID = "Voter ID Front";
    public static final String FETCH_TYPE_AADHAAR_CARD = "Aadhaar Card Front";

    // Signzy Esign Types
    public static final int NORMAL_ESIGN = 0;
    public static final int AADHAAR_ESIGN = 1;

    //Conditional values
    public static final String IS_SUCCESS = String.valueOf(true);
    public static final String IS_FAILED = String.valueOf(false);
    public static final String IS_ACTIVE = "ACTIVE";

    //Lead Document History Status
    public static final String DOCUMENT_UPLOADED = "Document Uploaded";
    public static final String DOCUMENT_VERIFICATION_SUCCESS ="Document Verification Success";
    public static final String DOCUMENT_VERIFICATION_FAILURE = "Document Verification Failed";
    public static final String UPLOADED_DOCUMENT_DELETED = "Uploaded Document deleted";
    public static final String DOCUMENT_REUPLOADED = "Document Reuploaded";
    public static final String DOCUMENT_VERIFICATION_IN_PROCESS = "Document Verification In Process";

    //Lead Document status
    public static final int DOCUMENT_VERIFICATION_PENDING = 0;
    public static final int SIGNZY_VERIFICATION_SUCCESS = 1;
    public static final int SIGNZY_VERIFICATION_FAILED = 2;
    public static final int OPS_VERIFICATION_PENDING = 3;
    public static final int OPS_VERIFICATION_SUCCESS = 4;
    public static final int OPS_VERIFICATION_FAILED = 5;
    public static final int SIGNZY_VERIFICATION_PENDING = 6;

    public static final int MCC_VERIFICATION_PENDING = 7;

    public static final int MCC_VERIFICATION_APPROVE = 8;

    public static final int MCC_VERIFICATION_REJECT = 9;

    public static final int ICA_VERIFICATION_PENDING = 10;

    public static final int ICA_VERIFICATION_APPROVE = 11;

    public static final int ICA_VERIFICATION_REJECT = 12;

    public static final int MCC_DOCUMENT_RE_UPLOAD = 13;





    public static final String PENNY_DROP_AMOUNT_MATCHED_SUCCESS = "true";
    public static final String PENNY_DROP_AMOUNT_MATCHED_FAILURE = "false";

    // Agreement Signing Path
    public static final String AGREEMENT_DOCUMENT_PATH ="/AgreementDocument/";


    //Lead status
    public static final long PENDING_MERCHANT_DETAILS = 1;
    public static final long PENDING_DOCUMENT_UPLOAD =2;
    public static final long PENDING_MDR_AND_RENTAL_PLAN =3;
    public static final long PENDING_BIZ_DETAILS =4;
    public static final long PENDING_PAYMENT_DETAILS=5;
    public static final long PENDING_AGREEMENT_SIGNING=7;
    public static final long AGREEMENT_SIGNING_COMPLETED=8;
    public static final long AGREEMENT_SIGNING_FAILED =11;
    public static final long LEAD_REJECTED_BY_SO_OR_SAT = 6;
    public static final long LEAD_SUBMITTED_TID_PENDING=17;
    public static final long DOCUMENT_REJECTED_BY_OPS=20;
    public static final long QR_AGREEMENT_PENDING=25;
    public static final long Mcc_Document_Rejected_By_Ops=31;
    public static final long Pending_MCC_documents_resubmission=30;
    public static final long PENDING_MCC_APPROVAL=29;

    public static final long PENDING_MCC_DOCUMENTS_RESUBMISSION=30;

    public static final long MCC_DOCUMENT_REJECTED_BY_OPS=31;

    public static final long PENDING_ICA_APPROVAL=32;

    public static final long PENDING_MCC_DOCUMENTS_RESUBMISSION_APPROVAL_OPS=33;


    public static final long LEAD_PENDING_OSV=33;
    public static final long PENDING_DEVICE_DETAILS=40;




    public static final String AGREEMENT_SIGNATURE_TYPE_NORMAL_ESIGN="esign";
    public static final String AGREEMENT_SIGNATURE_TYPE_AADHAAR_ESIGN="aadhaaresign";

    //Lead Document cancel action
    public static final int LEAD_DOCUMENT_APPROVAL = 1;
    public static final int LEAD_DOCUMENT_REJECT = 2;
    public static final int MCC_LEAD_DOCUMENT_APPROVAL = 3;
    public static final int MCC_LEAD_DOCUMENT_REJECT = 4;

    //Lead sub status
    public static final int LEAD_APPROVED_BY_NATIONAL_HEAD = 1;
    public static final int LEAD_PENDING_WITH_NATIONAL_HEAD = 2;
    public static final int LEAD_REJECTED_BY_NATIONAL_LEAD = 3;

    // Mcc Category Indicator
    public static final int MCC_CATEGORY_INDICATOR_BASIC = 0;
    public static final int MCC_CATEGORY_INDICATOR_MCC = 1;
    public static final int MCC_CATEGORY_INDICATOR_ALL_MCC = 2;
    public static final int MCC_CATEGORY_INDICATOR_IMPLEMENT = 3;

    public static final int MCC_CATEGORY_INDICATOR_BANK_APPROVAL = 5;


    public static final String LOGO_IMAGE = "uploadDocumentDir/Logo.png";

//    public static final String LOGO_IMAGE = "C:\\MuthupandiFiles\\";


    public static final String SO_SIGN_IMAGE = "uploadDocumentDir/";

//    public static final String SO_SIGN_IMAGE = "C:\\MuthupandiFiles\\";



//       public static final String LOGO_IMAGE = "src/main/resources/images/Logo.png";

    //Document Category
    public static final long DOCUMENT_CATEGORY_PROOF_OF_IDENTITY =1;
    public static final long DOCUMENT_CATEGORY_PROOF_OF_ADDRESS=2;
    public static final long DOCUMENT_CATEGORY_PROOF_OF_BUSINESS=3;
    public static final long DOCUMENT_CATEGORY_PROOF_OF_BUSINESS_CATEGORY=4;
    //PUSH NOTIFICATION

    public static final String  PUSH_NOTIFICATION_TO_DOCUMENT_APPROVED = "OPSDocumentApproved";
    public static final String  PUSH_NOTIFICATION_TO_DOCUMENT_REJECTED = "OPSDocumentRejected";
    public static final String  PUSH_NOTIFICATION_TO_NH_APPROVAL_PENDING ="SONHApprovalPending";

    public static final String AGREEMENT_SIGNATURE_POSITION_CUSTOMIZE ="CUSTOMIZE";
    public static final String  PUSH_NOTIFICATION_TO_PENDING_MERCHANT_DETAILS_PENDING ="RedirectToMerchantDetails";
    public static final String  PUSH_NOTIFICATION_TO_MCC_REJECTED ="MccRejected";

    //Signed file path
    public static final String AGREEMENT_SIGNED_DOCUMENT_PATH ="uploadDocumentDir/";

    public static final String CHECKBOX_UNSELECTED_IMAGE = "uploadDocumentDir/CheckBox_Unselected.png";
    public static final String CHECKBOX_SELECTED_IMAGE = "uploadDocumentDir/CheckBox_Selected.png";

    public static final String SIGNED_IMAGE ="uploadDocumentDir/Signed_Image.png";
    public static final String SIGNED_IMAGE_WOWPE ="uploadDocumentDir/WowpeSign.png";

    //Document Path
    public static final String IMAGE_PATH ="uploadDocumentDir/";

    // YEARLY REVENUE
    public static final int YEARLY_REVENUE_LESS_THAN_20L =1;    //  1. yearlyRevenue<20L
    public static final int YEARLY_REVENUE_GREATER_THAN_20L =2;  // 2. yearlyRevenue>20L


    //Document mandatory
    public static final int ANYONE_DOCUMENT = 1;
    public static final int ALL_DOCUMENT = 2;
    public static final int MANDATORY_DOCUMENT =3;

    public static final String professional_lead = "Mandatory Documents (for professionals)";

    public static final String LEAD_SOURCE_PET_POOJA = "LS_PET_POOJA";

    public static final String LEAD_SOURCE_FIDY_PAY = "LS_FidyPay";
    public static final String LEAD_SOURCE_VIYONA_FINTECH = "LS_Viyona_Fintech";

    public static final String LEAD_SOURCE_KVB = "LS_KARUR VYSYA BANK";
    public static final String LEAD_SOURCE_WOWPE = "LS_Wowpe";


    //Mars Api Actions
    public static final int MARS_API_NOT_TRIGGERED =0;
    public static final int MARS_API_TRIGGERED=1;
    public static final int MARS_RESPONSE_TID_CREATED=2;

    //Sqr Lead Status
    public static final String Qr_Pending="SQR_PENDING";


    //Approved

    public static final long LEAD_COMPLETED=13;
    public static final long SQR_ONBOARDING_FAILED=24;
    public static final int MARS_DEVICE_STATUS_TID_GENERATED=1;


    public static final String SUB_DOCUMENT_TYPE_GST="GST Document";
    public static final String SUB_DOCUMENT_TYPE_SHOP_FRONT="Shop Front";
    public static final String SUB_DOCUMENT_TYPE_SHOP_BACK="Shop Back";

    public static final String SUB_DOCUMENT_TYPE_PAN="Pan Card";
    public static final String SUB_DOCUMENT_TYPE_AADHAR="Aadhaar Card";
    public static final String SUB_DOCUMENT_TYPE_DRIVING_LICENCE="Driving License";
    public static final String SUB_DOCUMENT_TYPE_VOTER="Voter ID";
    public static final String SUB_DOCUMENT_TYPE_SO_PICTURE="SO Picture";
    public static final String SUB_DOCUMENT_TYPE_SO_SIGN="SO Signature";
    public static final String SUB_DOCUMENT_TYPE_MERCHANT_PICTURE="Merchant Picture";


    public static final int MCC_CATEGORY_INDICATOR_OSV = 6;

    public static final String FCM_FILE_PATH ="C:\\Pictures\\digitalonboardsoapp-firebase-adminsdk-uiwmj-ce9108343e.json";


    //DocumentCatagory
    public static final long  PROOF_OF_BUSSINESS_CATAGORY=4;
    public static final long PROOF_OF_BUSSINESS_CATAGORY_OTHERS=11;
    public static final long AUTH_SIGN=12;
    public static final long AUTH_SIGN1=12;
    public static final long AUTH_SIGN2=13;
    public static final long AUTH_SIGN3=14;
    public static final long AUTH_SIGN4=15;
    public static final long AUTH_SIGN5=16;

    public static final long PROOF_OF_BUSSINESS_CATAGORY_OTHER=6;
    public static final long ADDITIONAL_DOCUMENT=18;
    public static final long ADDITIONAL_DOCUMENT_BY_OPS=19;

    public static final long OFFLINE_PAYMENT_DOCUMENT=20;

    public static final long Authorized_Signatories=7;
    public static final long Authorized_Signatories1=7;
    public static final long Authorized_Signatories2=8;
    public static final long Authorized_Signatories3=9;
    public static final long Authorized_Signatories4=10;
    public static final long Authorized_Signatories5=11;

    //Implementation Form
    public static final String IMPLEMENTATION_DOCUMENT_PATH ="uploadDocumentDir/";

    // offline payment

    public static final int NOCHEQUENONEFT=0;
    public static final int CHEQUEONLY=1;

    public static final int NEFTONLY=2;

    // Payment Type

    public static final int QR_PAYMENT=1;
    public static final int SMS_PAYMENT=2;
    public static final int OFFLINE_PAYMENT=3;
    public static final int NO_PAYMENT_SELECTED=0;


}
