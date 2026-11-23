package com.bijlipay.model.dto.signzy.fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GSTNDetailedDto {
    private String constitutionOfBusiness;
    private String legalNameOfBusiness;
    private String tradeNameOfBusiness;
    private String centreJurisdiction;
    private String stateJurisdiction;
    private String registrationDate;
    private String taxPayerDate;
    private String taxPayerType;
    private String gstinStatus;
    private String cancellationDate;
    @JsonProperty("e-invoicingStatus")
    private String eInvoicingStatus;
    private List<String> natureOfBusinessActivities;
    private String principalPlaceAddress;
    private String principalPlaceLatitude;
    private String principalPlaceLongitude;
    private String principalPlaceBuildingNameFromGST;
    private String principalPlaceBuildingNoFromGST;
    private String principalPlaceFlatNo;
    private String principalPlaceStreet;
    private String principalPlaceLocality;
    private String principalPlaceCity;
    private String principalPlaceDistrict;
    private String principalPlaceState;
    private String principalPlacePincode;
    private String additionalPlaceAddress;
    private String additionalPlaceLatitude;
    private String additionalPlaceLongitude;
    private String additionalPlaceBuildingNameFromGST;
    private String additionalPlaceBuildingNoFromGST;
    private String additionalPlaceFlatNo;
    private String additionalPlaceStreet;
    private String additionalPlaceLocality;
    private String additionalPlaceCity;
    private String additionalPlaceDistrict;
    private String additionalPlaceState;
    private String additionalPlacePincode;

    private GSTPrincipalPlaceSplitAddress principalPlaceSplitAddress;
    private GSTAdditionalPlaceSplitAddress additionalPlaceSplitAddress;
    private List<GSTAdditionalAddressArray> additionalAddressArray;
    private String lastUpdatedDate;

    public String getConstitutionOfBusiness() {
        return constitutionOfBusiness;
    }

    public void setConstitutionOfBusiness(String constitutionOfBusiness) {
        this.constitutionOfBusiness = constitutionOfBusiness;
    }

    public String getLegalNameOfBusiness() {
        return legalNameOfBusiness;
    }

    public void setLegalNameOfBusiness(String legalNameOfBusiness) {
        this.legalNameOfBusiness = legalNameOfBusiness;
    }

    public String getTradeNameOfBusiness() {
        return tradeNameOfBusiness;
    }

    public void setTradeNameOfBusiness(String tradeNameOfBusiness) {
        this.tradeNameOfBusiness = tradeNameOfBusiness;
    }

    public String getCentreJurisdiction() {
        return centreJurisdiction;
    }

    public void setCentreJurisdiction(String centreJurisdiction) {
        this.centreJurisdiction = centreJurisdiction;
    }

    public String getStateJurisdiction() {
        return stateJurisdiction;
    }

    public void setStateJurisdiction(String stateJurisdiction) {
        this.stateJurisdiction = stateJurisdiction;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getTaxPayerDate() {
        return taxPayerDate;
    }

    public void setTaxPayerDate(String taxPayerDate) {
        this.taxPayerDate = taxPayerDate;
    }

    public String getTaxPayerType() {
        return taxPayerType;
    }

    public void setTaxPayerType(String taxPayerType) {
        this.taxPayerType = taxPayerType;
    }

    public String getGstinStatus() {
        return gstinStatus;
    }

    public void setGstinStatus(String gstinStatus) {
        this.gstinStatus = gstinStatus;
    }

    public String getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(String cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String geteInvoicingStatus() {
        return eInvoicingStatus;
    }

    public void seteInvoicingStatus(String eInvoicingStatus) {
        this.eInvoicingStatus = eInvoicingStatus;
    }

    public List<String> getNatureOfBusinessActivities() {
        return natureOfBusinessActivities;
    }

    public void setNatureOfBusinessActivities(List<String> natureOfBusinessActivities) {
        this.natureOfBusinessActivities = natureOfBusinessActivities;
    }

    public String getPrincipalPlaceAddress() {
        return principalPlaceAddress;
    }

    public void setPrincipalPlaceAddress(String principalPlaceAddress) {
        this.principalPlaceAddress = principalPlaceAddress;
    }

    public String getPrincipalPlaceLatitude() {
        return principalPlaceLatitude;
    }

    public void setPrincipalPlaceLatitude(String principalPlaceLatitude) {
        this.principalPlaceLatitude = principalPlaceLatitude;
    }

    public String getPrincipalPlaceLongitude() {
        return principalPlaceLongitude;
    }

    public void setPrincipalPlaceLongitude(String principalPlaceLongitude) {
        this.principalPlaceLongitude = principalPlaceLongitude;
    }

    public String getPrincipalPlaceBuildingNameFromGST() {
        return principalPlaceBuildingNameFromGST;
    }

    public void setPrincipalPlaceBuildingNameFromGST(String principalPlaceBuildingNameFromGST) {
        this.principalPlaceBuildingNameFromGST = principalPlaceBuildingNameFromGST;
    }

    public String getPrincipalPlaceBuildingNoFromGST() {
        return principalPlaceBuildingNoFromGST;
    }

    public void setPrincipalPlaceBuildingNoFromGST(String principalPlaceBuildingNoFromGST) {
        this.principalPlaceBuildingNoFromGST = principalPlaceBuildingNoFromGST;
    }

    public String getPrincipalPlaceFlatNo() {
        return principalPlaceFlatNo;
    }

    public void setPrincipalPlaceFlatNo(String principalPlaceFlatNo) {
        this.principalPlaceFlatNo = principalPlaceFlatNo;
    }

    public String getPrincipalPlaceStreet() {
        return principalPlaceStreet;
    }

    public void setPrincipalPlaceStreet(String principalPlaceStreet) {
        this.principalPlaceStreet = principalPlaceStreet;
    }

    public String getPrincipalPlaceLocality() {
        return principalPlaceLocality;
    }

    public void setPrincipalPlaceLocality(String principalPlaceLocality) {
        this.principalPlaceLocality = principalPlaceLocality;
    }

    public String getPrincipalPlaceCity() {
        return principalPlaceCity;
    }

    public void setPrincipalPlaceCity(String principalPlaceCity) {
        this.principalPlaceCity = principalPlaceCity;
    }

    public String getPrincipalPlaceDistrict() {
        return principalPlaceDistrict;
    }

    public void setPrincipalPlaceDistrict(String principalPlaceDistrict) {
        this.principalPlaceDistrict = principalPlaceDistrict;
    }

    public String getPrincipalPlaceState() {
        return principalPlaceState;
    }

    public void setPrincipalPlaceState(String principalPlaceState) {
        this.principalPlaceState = principalPlaceState;
    }

    public String getPrincipalPlacePincode() {
        return principalPlacePincode;
    }

    public void setPrincipalPlacePincode(String principalPlacePincode) {
        this.principalPlacePincode = principalPlacePincode;
    }

    public String getAdditionalPlaceAddress() {
        return additionalPlaceAddress;
    }

    public void setAdditionalPlaceAddress(String additionalPlaceAddress) {
        this.additionalPlaceAddress = additionalPlaceAddress;
    }

    public String getAdditionalPlaceLatitude() {
        return additionalPlaceLatitude;
    }

    public void setAdditionalPlaceLatitude(String additionalPlaceLatitude) {
        this.additionalPlaceLatitude = additionalPlaceLatitude;
    }

    public String getAdditionalPlaceLongitude() {
        return additionalPlaceLongitude;
    }

    public void setAdditionalPlaceLongitude(String additionalPlaceLongitude) {
        this.additionalPlaceLongitude = additionalPlaceLongitude;
    }

    public String getAdditionalPlaceBuildingNameFromGST() {
        return additionalPlaceBuildingNameFromGST;
    }

    public void setAdditionalPlaceBuildingNameFromGST(String additionalPlaceBuildingNameFromGST) {
        this.additionalPlaceBuildingNameFromGST = additionalPlaceBuildingNameFromGST;
    }

    public String getAdditionalPlaceBuildingNoFromGST() {
        return additionalPlaceBuildingNoFromGST;
    }

    public void setAdditionalPlaceBuildingNoFromGST(String additionalPlaceBuildingNoFromGST) {
        this.additionalPlaceBuildingNoFromGST = additionalPlaceBuildingNoFromGST;
    }

    public String getAdditionalPlaceFlatNo() {
        return additionalPlaceFlatNo;
    }

    public void setAdditionalPlaceFlatNo(String additionalPlaceFlatNo) {
        this.additionalPlaceFlatNo = additionalPlaceFlatNo;
    }

    public String getAdditionalPlaceStreet() {
        return additionalPlaceStreet;
    }

    public void setAdditionalPlaceStreet(String additionalPlaceStreet) {
        this.additionalPlaceStreet = additionalPlaceStreet;
    }

    public String getAdditionalPlaceLocality() {
        return additionalPlaceLocality;
    }

    public void setAdditionalPlaceLocality(String additionalPlaceLocality) {
        this.additionalPlaceLocality = additionalPlaceLocality;
    }

    public String getAdditionalPlaceCity() {
        return additionalPlaceCity;
    }

    public void setAdditionalPlaceCity(String additionalPlaceCity) {
        this.additionalPlaceCity = additionalPlaceCity;
    }

    public String getAdditionalPlaceDistrict() {
        return additionalPlaceDistrict;
    }

    public void setAdditionalPlaceDistrict(String additionalPlaceDistrict) {
        this.additionalPlaceDistrict = additionalPlaceDistrict;
    }

    public String getAdditionalPlaceState() {
        return additionalPlaceState;
    }

    public void setAdditionalPlaceState(String additionalPlaceState) {
        this.additionalPlaceState = additionalPlaceState;
    }

    public String getAdditionalPlacePincode() {
        return additionalPlacePincode;
    }

    public void setAdditionalPlacePincode(String additionalPlacePincode) {
        this.additionalPlacePincode = additionalPlacePincode;
    }



    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public GSTPrincipalPlaceSplitAddress getPrincipalPlaceSplitAddress() {
        return principalPlaceSplitAddress;
    }

    public void setPrincipalPlaceSplitAddress(GSTPrincipalPlaceSplitAddress principalPlaceSplitAddress) {
        this.principalPlaceSplitAddress = principalPlaceSplitAddress;
    }

    public GSTAdditionalPlaceSplitAddress getAdditionalPlaceSplitAddress() {
        return additionalPlaceSplitAddress;
    }

    public void setAdditionalPlaceSplitAddress(GSTAdditionalPlaceSplitAddress additionalPlaceSplitAddress) {
        this.additionalPlaceSplitAddress = additionalPlaceSplitAddress;
    }

    public List<GSTAdditionalAddressArray> getAdditionalAddressArray() {
        return additionalAddressArray;
    }

    public void setAdditionalAddressArray(List<GSTAdditionalAddressArray> additionalAddressArray) {
        this.additionalAddressArray = additionalAddressArray;
    }
}
