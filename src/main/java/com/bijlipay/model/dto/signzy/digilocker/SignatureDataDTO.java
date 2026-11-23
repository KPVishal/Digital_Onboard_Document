package com.bijlipay.model.dto.signzy.digilocker;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignatureDataDTO {

        @JsonProperty("xmlns")
        private String xmlns;

        @JsonProperty("SignedInfo")
        private SignedInfoDTO signedInfo;

        @JsonProperty("SignatureValue")
        private String signatureValue;

//        @JsonProperty("KeyInfo")
//        private KeyInfoDTO keyInfo;

        public String getXmlns() {
                return xmlns;
        }

        public void setXmlns(String xmlns) {
                this.xmlns = xmlns;
        }

        public SignedInfoDTO getSignedInfo() {
                return signedInfo;
        }

        public void setSignedInfo(SignedInfoDTO signedInfo) {
                this.signedInfo = signedInfo;
        }

        public String getSignatureValue() {
                return signatureValue;
        }

        public void setSignatureValue(String signatureValue) {
                this.signatureValue = signatureValue;
        }

//        public KeyInfoDTO getKeyInfo() {
//                return keyInfo;
//        }
//
//        public void setKeyInfo(KeyInfoDTO keyInfo) {
//                this.keyInfo = keyInfo;
//        }

        @Override
        public String toString() {
                return "SignatureDataDTO{" +
                        "xmlns='" + xmlns + '\'' +
                        ", signedInfo=" + signedInfo +
                        ", signatureValue='" + signatureValue + '\'' +
//                        ", keyInfo=" + keyInfo +
                        '}';
        }
}
