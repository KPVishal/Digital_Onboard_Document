package com.bijlipay.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "mars_device_details")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class MarsDeviceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mars_device_id", nullable = false)
    private long id;
    @Column(name = "tid", nullable = true)
    private String tid;
    @Column(name = "mid", nullable = true)
    private String mid;
    //    @Column(name = "user_name", nullable = true)
//    private String userName;
//    @Column(name = "password", nullable = true)
//    private String password;
//    @ManyToOne(optional = true)
//    @JoinColumn(name = "regional_inventory_id", referencedColumnName = "regional_inventory_id")
//    private RegionalInventory regionalInventory;
//
//    @ManyToOne(optional = true)
//    @JoinColumn(name = "aggregator_regional_inventory_id",referencedColumnName = "agg_regional_id")
//    private AggregatorRegionalInventory aggregatorRegionalInventory;
    @JsonIgnore
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
//    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", referencedColumnName = "lead_id")
    private LeadInformation leadInformation;
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_to", referencedColumnName = "user_id")
    private User assignedTo;
    //    @ManyToOne(optional = true)
//    @JoinColumn(name = "recovered_by", referencedColumnName = "user_id")
//    private User recoveredBy;
//    @Column(name = "device_recovery_status", columnDefinition = "int(11) default 0")
//    private Integer deviceRecoveryStatus; //1- unpick, 2- picked,
    @Column(name = "email", nullable = true)
    private String email;
    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    private boolean isActive;
    @Column(name = "device_status", columnDefinition = "int(11) default 1")
    private Integer deviceStatus; //1-mars tid,mid generated, 2- assigned to implementation officer 3- scan and pick,
    @Column(name = "device_address", nullable = true)
    private String deviceAddress;
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
    @Column(name = "device_status_date", nullable = true)
    private Date deviceStatusDate;
    @ManyToOne(optional = true)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    private User createdBy;
    @ManyToOne(optional = true)
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id")
    private User updatedBy;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "application_number", nullable = true)
    private String applicationNumber;
    //    @Column(name = "implementation_form", nullable = true)
//    private String implementationForm;
//    @Column(name = "picture_of_shop", nullable = true)
//    private String pictureOfShop;
//    @Column(name = "cpv_form", nullable = true)
//    private String cpvForm;
//    @Column(name = "reason", nullable = true)
//    private String reason;
//    @Column(name = "implementatio_form_mimetype", nullable = true)
//    private String implementationFormMimeType;
//    @Column(name = "picture_of_shop_mimetype", nullable = true)
//    private String pictureOfShopMimeType;
//    @Column(name = "cpv_form_mimetype", nullable = true)
//    private String cpvFormMimeType;
//    @ManyToOne(optional = true)
//    @JoinColumn(name = "replaced_by", referencedColumnName = "user_id")
//    private User replacedBy;
//    @Column(name = "device_replacement_status", columnDefinition = "int(11) default 0")
//    private Integer devicereplacementStatus; //1- notreplaced, 2- replaced yettosubmit,3- submitted
//    @Column(name = "replacement_form", nullable = true)
//    private String replacementForm;
//    @Column(name = "replacement_form_mimetype", nullable = true)
//    private String replacementFormMimeType;
//    @ManyToOne(optional = true)
//    @JoinColumn(name = "replaced_regional_inventory_id", referencedColumnName = "regional_inventory_id")
//    private RegionalInventory replacedregionalInventory;
//    @ManyToOne(optional = true)
//    @JoinColumn(name = "replaced_aggregator_regional_inventory_id",referencedColumnName = "agg_regional_id")
//    private AggregatorRegionalInventory replacedAggregatorRegionalInventory;
    @Column(name = "impl_state_name",nullable = true)
    private String implStateName;
    @Column(name = "impl_district_name",nullable = true)
    private String implDistrictName;
    @Column(name="impl_sub_district_name",nullable = true)
    private String implSubDistrictName;
    @Column(name = "impl_village_name",nullable = true)
    private String implVillageName;
    @Column(name = "impl_tier",nullable = true)
    private String implTier;
    @Column(name = "impl_lat_lng",nullable = true)
    private String implLatLng;
    //    @Column(name = "is_sub_tid", columnDefinition = "tinyint(1) default 0")
//    private boolean isSubTid;
//    @Column(name = "merchant_reference_code")
//    private String merchantRefCode;
//    @Column(name = "tid_identifier", columnDefinition = "VARCHAR(255)")
//    private String tidIdentifier;
//    @Column(name = "institution_code" , columnDefinition = "int(11) default 0")
//    private int institutionCode;
//    @Column(name = "sub_tid_count")
//    private Integer subTidCount;
//    @Column(name = "base_tid", nullable = true)
//    private String baseTid;
//    @Column(name = "rrid", nullable = true)
//    private String rrId;
//    @Column(name = "scheme", nullable = true)
//    private String scheme;
//
//    @Column(name = "is_submitted_to_mars", columnDefinition = "tinyint(1) default 0")
//    private boolean isSubmittedToMars;
//
//    @Transient
//    private String subTidOfDbs;
//
    @Column(name = "sim",nullable = true)
    private String sim;
    //    @Column(name = "is_courier",columnDefinition = "tinyint(1) default 0")
//    private boolean isCourier;
    @Column(name = "schedule_date",nullable = true)
    private Date scheduleDate;
    @Column(name = "re_schedule_date",nullable = true)
    private Date reScheduleDate;
    @Column(name = "reschdule_remarks",nullable = true)
    private String rescheduleRemarks;
    @Column(name = "reschdule_reason",nullable = true)
    private String rescheduleReason;
    @Column(name = "terminal_sno" , nullable = true)
    private String terminalSNo;
    @Column(name = "is_document_collected",columnDefinition = "tinyint(1) default 0")
    private boolean isDocumentCollected;
//    @Column(name = "pod_number_courier",nullable = true)
//    private String podNumberCourier;
//    @Column(name = "replacement_shop_picture", nullable = true)
//    private String replacementShopPicture;
//    @Column(name = "replacement_shop_picture_mimetype", nullable = true)
//    private String replacementShopPictureMimeType;

    //    @Column(name = "is_upi_enabled", columnDefinition = "tinyint(1) default 0")
//    private boolean isUpiEnabled;
//    @Column(name = "base_mid", nullable = true)
//    private String baseMid;
//
//    @Column(name = "aggregator_id", nullable = true)
//    private String aggregatorId;
//    @Column(name = "vpa", nullable = true)
//    private String vpa;
//    @Column(name = "merchant_ref_id",nullable = true)
//    private String merchantRefId;
//
//    @Column(name = "partner_ref_id",nullable = true)
//    private String partnerRefId;
//
//    @Column(name = "customer_ref_id",nullable = true)
//    private String customerRefId;
//
    @Column(name = "merchant_name",nullable = true)
    private String merchantName;
    @Column(name = "sqr_vpa", nullable = true)
    private String sqrVpa;
//
//    @Column(name = "onboard_cancel_remarks",nullable = true)
//    private String onboardCancelRemarks;

    @Transient
    private List<ScheduleLogs> scheduleLogs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public LeadInformation getLeadInformation() {
        return leadInformation;
    }

    public void setLeadInformation(LeadInformation leadInformation) {
        this.leadInformation = leadInformation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

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

    public Date getDeviceStatusDate() {
        return deviceStatusDate;
    }

    public void setDeviceStatusDate(Date deviceStatusDate) {
        this.deviceStatusDate = deviceStatusDate;
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

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getImplStateName() {
        return implStateName;
    }

    public void setImplStateName(String implStateName) {
        this.implStateName = implStateName;
    }

    public String getImplDistrictName() {
        return implDistrictName;
    }

    public void setImplDistrictName(String implDistrictName) {
        this.implDistrictName = implDistrictName;
    }

    public String getImplSubDistrictName() {
        return implSubDistrictName;
    }

    public void setImplSubDistrictName(String implSubDistrictName) {
        this.implSubDistrictName = implSubDistrictName;
    }

    public String getImplVillageName() {
        return implVillageName;
    }

    public void setImplVillageName(String implVillageName) {
        this.implVillageName = implVillageName;
    }

    public String getImplTier() {
        return implTier;
    }

    public void setImplTier(String implTier) {
        this.implTier = implTier;
    }

    public String getImplLatLng() {
        return implLatLng;
    }

    public void setImplLatLng(String implLatLng) {
        this.implLatLng = implLatLng;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getRescheduleRemarks() {
        return rescheduleRemarks;
    }

    public void setRescheduleRemarks(String rescheduleRemarks) {
        this.rescheduleRemarks = rescheduleRemarks;
    }

    public String getRescheduleReason() {
        return rescheduleReason;
    }

    public void setRescheduleReason(String rescheduleReason) {
        this.rescheduleReason = rescheduleReason;
    }

    public String getTerminalSNo() {
        return terminalSNo;
    }

    public void setTerminalSNo(String terminalSNo) {
        this.terminalSNo = terminalSNo;
    }

    public Date getReScheduleDate() {
        return reScheduleDate;
    }

    public void setReScheduleDate(Date reScheduleDate) {
        this.reScheduleDate = reScheduleDate;
    }

    public boolean isDocumentCollected() {
        return isDocumentCollected;
    }

    public void setDocumentCollected(boolean documentCollected) {
        isDocumentCollected = documentCollected;
    }

    public List<ScheduleLogs> getScheduleLogs() {
        return scheduleLogs;
    }

    public void setScheduleLogs(List<ScheduleLogs> scheduleLogs) {
        this.scheduleLogs = scheduleLogs;
    }

    public String getSqrVpa() {
        return sqrVpa;
    }

    public void setSqrVpa(String sqrVpa) {
        this.sqrVpa = sqrVpa;
    }
}
