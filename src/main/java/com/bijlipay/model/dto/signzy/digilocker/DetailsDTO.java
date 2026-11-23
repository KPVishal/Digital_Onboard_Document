package com.bijlipay.model.dto.signzy.digilocker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailsDTO {
    @JsonProperty("version")
    private int version;

    @JsonProperty("subject")
    private SubjectDTO subject;

    @JsonProperty("issuer")
    private IssuerDTO issuer;

    @JsonProperty("serial")
    private String serial;

    @JsonProperty("notBefore")
    private String notBefore;

    @JsonProperty("notAfter")
    private String notAfter;

    @JsonProperty("subjectHash")
    private String subjectHash;

    @JsonProperty("signatureAlgorithm")
    private String signatureAlgorithm;

    @JsonProperty("fingerPrint")
    private String fingerPrint;

    @JsonProperty("publicKey")
    private PublicKeyDTO publicKey;

    @JsonProperty("altNames")
    private List<String> altNames;

    @JsonProperty("extensions")
    private Map<String, String> extensions;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public IssuerDTO getIssuer() {
        return issuer;
    }

    public void setIssuer(IssuerDTO issuer) {
        this.issuer = issuer;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }

    public String getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(String notAfter) {
        this.notAfter = notAfter;
    }

    public String getSubjectHash() {
        return subjectHash;
    }

    public void setSubjectHash(String subjectHash) {
        this.subjectHash = subjectHash;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public PublicKeyDTO getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKeyDTO publicKey) {
        this.publicKey = publicKey;
    }

    public List<String> getAltNames() {
        return altNames;
    }

    public void setAltNames(List<String> altNames) {
        this.altNames = altNames;
    }

    public Map<String, String> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, String> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        return "DetailsDTO{" +
                "version=" + version +
                ", subject=" + subject +
                ", issuer=" + issuer +
                ", serial='" + serial + '\'' +
                ", notBefore='" + notBefore + '\'' +
                ", notAfter='" + notAfter + '\'' +
                ", subjectHash='" + subjectHash + '\'' +
                ", signatureAlgorithm='" + signatureAlgorithm + '\'' +
                ", fingerPrint='" + fingerPrint + '\'' +
                ", publicKey=" + publicKey +
                ", altNames=" + altNames +
                ", extensions=" + extensions +
                '}';
    }
}