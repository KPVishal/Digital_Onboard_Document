package com.bijlipay.model.dto.signzy.fetch;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignzyFetchResultDto {

    private String name;
    private String dob;

    //PAN
    private String number;
    private String typeOfHolder;
    private String status;
    private String isIndividual;
    private String isValid;
    private String firstName;
    private String middleName;
    private String lastName;
    private String title;
    private String panStatus;
    private String panStatusCode;
    private String aadhaarSeedingStatus;
    private String aadhaarSeedingStatusCode;
    private String lastUpdatedOn;
    //PASS
    private String fileNumber;
    private String givenName;
    private String surname;
    private String typeOfApplication;
    private String applicationReceivedOnDate;
    //Voter ID
    private String district;
    private String state;
    private String fatherName;
    private String gender;
    //Driving License
    private String dlNumber;
    private List<BadgeDetailsDto> badgeDetails;
    private DlValidityDto dlValidity;
    private DetailsOfDrivingLicenceDto detailsOfDrivingLicence;
    //GST
    private GSTNDetailedDto gstnDetailed;
    private List<GSTNRecordsDto> gstnRecords;
    private String gstin;
    //Aadhaar
    private String url;
    private String requestId;
    private UserDetailsDto userDetails;
    private List<FilesDto> files;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTypeOfHolder() {
        return typeOfHolder;
    }

    public void setTypeOfHolder(String typeOfHolder) {
        this.typeOfHolder = typeOfHolder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsIndividual() {
        return isIndividual;
    }

    public void setIsIndividual(String isIndividual) {
        this.isIndividual = isIndividual;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPanStatus() {
        return panStatus;
    }

    public void setPanStatus(String panStatus) {
        this.panStatus = panStatus;
    }

    public String getPanStatusCode() {
        return panStatusCode;
    }

    public void setPanStatusCode(String panStatusCode) {
        this.panStatusCode = panStatusCode;
    }

    public String getAadhaarSeedingStatus() {
        return aadhaarSeedingStatus;
    }

    public void setAadhaarSeedingStatus(String aadhaarSeedingStatus) {
        this.aadhaarSeedingStatus = aadhaarSeedingStatus;
    }

    public String getAadhaarSeedingStatusCode() {
        return aadhaarSeedingStatusCode;
    }

    public void setAadhaarSeedingStatusCode(String aadhaarSeedingStatusCode) {
        this.aadhaarSeedingStatusCode = aadhaarSeedingStatusCode;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTypeOfApplication() {
        return typeOfApplication;
    }

    public void setTypeOfApplication(String typeOfApplication) {
        this.typeOfApplication = typeOfApplication;
    }

    public String getApplicationReceivedOnDate() {
        return applicationReceivedOnDate;
    }

    public void setApplicationReceivedOnDate(String applicationReceivedOnDate) {
        this.applicationReceivedOnDate = applicationReceivedOnDate;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDlNumber() {
        return dlNumber;
    }

    public void setDlNumber(String dlNumber) {
        this.dlNumber = dlNumber;
    }

    public List<BadgeDetailsDto> getBadgeDetails() {
        return badgeDetails;
    }

    public void setBadgeDetails(List<BadgeDetailsDto> badgeDetails) {
        this.badgeDetails = badgeDetails;
    }

    public DlValidityDto getDlValidity() {
        return dlValidity;
    }

    public void setDlValidity(DlValidityDto dlValidity) {
        this.dlValidity = dlValidity;
    }

    public DetailsOfDrivingLicenceDto getDetailsOfDrivingLicence() {
        return detailsOfDrivingLicence;
    }

    public void setDetailsOfDrivingLicence(DetailsOfDrivingLicenceDto detailsOfDrivingLicence) {
        this.detailsOfDrivingLicence = detailsOfDrivingLicence;
    }

    public GSTNDetailedDto getGstnDetailed() {
        return gstnDetailed;
    }

    public void setGstnDetailed(GSTNDetailedDto gstnDetailed) {
        this.gstnDetailed = gstnDetailed;
    }

    public List<GSTNRecordsDto> getGstnRecords() {
        return gstnRecords;
    }

    public void setGstnRecords(List<GSTNRecordsDto> gstnRecords) {
        this.gstnRecords = gstnRecords;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public UserDetailsDto getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsDto userDetails) {
        this.userDetails = userDetails;
    }

    public List<FilesDto> getFiles() {
        return files;
    }

    public void setFiles(List<FilesDto> files) {
        this.files = files;
    }
}
