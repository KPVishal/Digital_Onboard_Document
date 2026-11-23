package com.bijlipay.service;

import com.bijlipay.model.DocuGenieResponse;
import com.bijlipay.model.LeadInformation;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface DocuGenieService {


    JSONObject decryptResponse(JSONObject responseJson, String clientPrivateKeyPath) throws Exception;

    byte[] encryptFileHybrid(byte[] fileBytes, String serverPublicKeyPath) throws Exception;


    DocuGenieResponse getAllDocuGenie(LeadInformation lead, String feildName, String encrytedResponse);
}
