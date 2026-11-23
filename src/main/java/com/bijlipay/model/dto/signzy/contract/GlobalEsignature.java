package com.bijlipay.model.dto.signzy.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalEsignature {
    private String previewRequired;
    private String enableUploadSignature;
    private String enableFontSignature;
    private String enableDrawSignature;
    private String enableSignaturePad;
    private String enableViewDocumentLink;
    private String isCustomized;
    private String ValidateAllPlaceholders;

    public String getPreviewRequired() {
        return previewRequired;
    }

    public void setPreviewRequired(String previewRequired) {
        this.previewRequired = previewRequired;
    }

    public String getEnableUploadSignature() {
        return enableUploadSignature;
    }

    public void setEnableUploadSignature(String enableUploadSignature) {
        this.enableUploadSignature = enableUploadSignature;
    }

    public String getEnableFontSignature() {
        return enableFontSignature;
    }

    public void setEnableFontSignature(String enableFontSignature) {
        this.enableFontSignature = enableFontSignature;
    }

    public String getEnableDrawSignature() {
        return enableDrawSignature;
    }

    public void setEnableDrawSignature(String enableDrawSignature) {
        this.enableDrawSignature = enableDrawSignature;
    }

    public String getEnableSignaturePad() {
        return enableSignaturePad;
    }

    public void setEnableSignaturePad(String enableSignaturePad) {
        this.enableSignaturePad = enableSignaturePad;
    }

    public String getEnableViewDocumentLink() {
        return enableViewDocumentLink;
    }

    public void setEnableViewDocumentLink(String enableViewDocumentLink) {
        this.enableViewDocumentLink = enableViewDocumentLink;
    }

    public String getIsCustomized() {
        return isCustomized;
    }

    public void setIsCustomized(String isCustomized) {
        this.isCustomized = isCustomized;
    }

    public String getValidateAllPlaceholders() {
        return ValidateAllPlaceholders;
    }

    public void setValidateAllPlaceholders(String validateAllPlaceholders) {
        ValidateAllPlaceholders = validateAllPlaceholders;
    }
}
