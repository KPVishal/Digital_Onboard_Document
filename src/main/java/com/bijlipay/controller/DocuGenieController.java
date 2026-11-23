package com.bijlipay.controller;

import com.bijlipay.model.*;
import com.bijlipay.model.dto.DocuGenieData;
import com.bijlipay.model.dto.FileDto;
import com.bijlipay.repository.UploadedDocumentRepository;
import com.bijlipay.service.DocuGenieService;
import com.bijlipay.service.DocumentService;
import com.bijlipay.service.FileStorageService;
import com.bijlipay.service.UserAuthenticationService;
import com.bijlipay.util.ApiResponse;
import com.bijlipay.util.AttributeEncryptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/document/DocuGenie")
public class DocuGenieController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(DocuGenieController.class);
    @Value("${app.server-public-key-path}")
    private String serverPublicKeyPath;

    @Value("${app.client-private-key-path}")
    private String clientPrivateKeyPath;

    @Value("${app.bearer-token}")
    private String bearerToken;

    @Value("${app.target-url}")
    private String targetUrl;
    @Autowired
    private DocuGenieService docuGenieService;

    @Autowired
    private UserAuthenticationService userAuthenticateService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private AttributeEncryptor attributeEncryptor;
    @Autowired
    private UploadedDocumentRepository uploadedDocumentRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping({"/upload/{leadId}/{subDocumentType}", "/upload/{leadId}/{subDocumentType}/{action}"})
    public ResponseEntity<?> uploadEncryptedFiles(@RequestPart("files") List<MultipartFile> files, HttpServletRequest requests, @PathVariable("leadId") long leadInformation, @PathVariable("subDocumentType") long subDocumentType,
                                                  @RequestParam(value = "typeOfDocument", required = false) String typeOfDocument, @PathVariable("latlng") Optional<String> latlng, @PathVariable("action") Optional<Integer> action) throws Exception {

        logger.info("start uploading to internal server :");
        Gson gson = new Gson();
        LeadInformation lead =null;
        SubDocumentType subDocument=null;
        UploadedDocuments model=null;
        subDocument = documentService.getSubDocumentDetails(subDocumentType);
//        String authtoken = requests.getHeader("Authorization");
//        logger.info("authtoken==={}", authtoken);
//        User curuser = getSessionUser();
//        logger.info("Session User {}", curuser);
//        UserAuthentication userauth = userAuthenticateService.getToken(curuser.getId());
//        if (authtoken.equals(userauth.getToken())) {

        if (action.isPresent()) {
            for (MultipartFile file : files) {

                if (file.isEmpty()) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "file is empty"), HttpStatus.OK);
                }

                //Lead check
                 lead = documentService.checkLead(leadInformation);
                if (lead != null) {
                    //Document type check
                    //DocumentType document = documentService.getDocumentDetails(documentType);
                     subDocument = documentService.getSubDocumentDetails(subDocumentType);
                    if (subDocument != null) {
                        logger.info("file -- : {}", file.getOriginalFilename());
                        FileDto fileDto = new FileDto();
                        logger.info("file -- : {}", file.getOriginalFilename());
                        // file name generate
                        String newFileName = lead.getLeadId() + "_" + subDocument.getName();
                        Map<String, String> response = fileStorageService.storeFile(file, newFileName);
                        fileDto.setFileName(response.get("fileName"));
                        fileDto.setMimeType(response.get("mimeType"));
                        fileDto.setFileNameOriginal(response.get("fileNameOriginal"));
                        if (response != null) {
                            logger.info("files stored successfully ");

                            //Storing lead and uploaded documents table
                             model = documentService.storeLeadDocuments(lead, fileDto, subDocument, typeOfDocument, latlng);
                        }
                    }
                }
            }
        }

        logger.info("Starting file encryption and multipart request preparation.");

//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Random random = new Random();
        String boundary = "----Boundary" + random.nextInt(999999);
        String lineBreak = "\r\n";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        JSONObject json = new JSONObject();
        json.put("model_id", "mdl200");
        json.put("source", "reference");
        json.put("query", "");

        logger.info("JSON data prepared: {}", json.toString());
        for (MultipartFile file : files) {

            logger.info("Encrypting file: {}", file.getOriginalFilename());

            byte[] encryptedBytes;

            try (InputStream inputStream = file.getInputStream()) {
                encryptedBytes = docuGenieService.encryptFileHybrid(inputStream.readAllBytes(), serverPublicKeyPath);
            }

            byteArrayOutputStream.write(("--" + boundary + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"files\"; filename=\"" + file.getOriginalFilename() + "\"" + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(("Content-Type: application/octet-stream" + lineBreak + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(encryptedBytes);
            byteArrayOutputStream.write(lineBreak.getBytes(StandardCharsets.UTF_8));

            logger.info("File {} encrypted and added to request body, size: {} bytes", file.getOriginalFilename(), encryptedBytes.length);
        }

        for (String key : json.keySet()) {
            String value = json.getString(key);
            logger.info("Adding form field: {} = {}", key, value);

            byteArrayOutputStream.write(("--" + boundary + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"" + lineBreak + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(value.getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(lineBreak.getBytes(StandardCharsets.UTF_8));
        }


        byteArrayOutputStream.write(("--" + boundary + "--" + lineBreak).getBytes(StandardCharsets.UTF_8));


        byte[] requestBytes = byteArrayOutputStream.toByteArray();

        System.out.println("==== REQUEST BODY START ====");
        System.out.println(new String(requestBytes, StandardCharsets.UTF_8));
        System.out.println("==== REQUEST BODY END ====");

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI(targetUrl))
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofByteArray(requestBytes))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response status: {}", response.statusCode());
        logger.info("Response body: {}", response.body());

        JSONObject jsonResponse = new JSONObject(response.body());

//        logger.info("Received response with status code: {}", response);
//            FileWriter fileWriter = new FileWriter("encrypted_response.json");

//        try {
//            fileWriter.write(jsonResponse.toString(4));
//        } catch (Throwable var15) {
//            try {
//                fileWriter.close();
//            } catch (Throwable e) {
//                var15.addSuppressed(e);
//            }
//
//            throw var15;
//        }

//            fileWriter.close();
        JSONObject var17 = docuGenieService.decryptResponse(jsonResponse, clientPrivateKeyPath);
//        logger.info("JSON OBJECT--{}", var17);
        Map<String, Object> data = var17.toMap();
        String encrytedResponse = attributeEncryptor.encrypt(gson.toJson(data));
        String feildName=subDocument!=null?subDocument.getName():null;
        docuGenieService.getAllDocuGenie(lead,feildName,encrytedResponse);
        DocuGenieData docuGenieData=new DocuGenieData();
        docuGenieData.setData(data);
        docuGenieData.setUploadedDocumentId(model!=null?model.getUploadedDocumentId():0);
        docuGenieData.setLeadDocumentId(model!=null?model.getLeadDocuments().getLeadDocumentId():0);
        return new ResponseEntity<>(new ApiResponse(OK, "", docuGenieData), OK);
//        }
//        return new ResponseEntity<>(new ApiResponse(UNAUTHORIZED, "unAuthorized"), UNAUTHORIZED);
    }

    @PostMapping("upload-all-documents/{leadId}")
        private ResponseEntity<?> uploadAllFiles(@RequestParam("uploadId") List<Long> uploadId ,@PathVariable("leadId") long leadId) throws Exception {
        Random random = new Random();
        String boundary = "----Boundary" + random.nextInt(999999);
        String lineBreak = "\r\n";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        JSONObject json = new JSONObject();
        json.put("model_id", "mdl200");
        json.put("source", "reference");
        json.put("query", "");

        logger.info("JSON data prepared: {}", json.toString());
        for (long id:uploadId){
            UploadedDocuments uploadedDocuments=uploadedDocumentRepository.findByUpdloadedDocumentId(id);
            Resource resource = fileStorageService.loadFileAsResource(uploadedDocuments.getFileName());
            byte[] encryptedBytes;

            try (InputStream inputStream = resource.getInputStream()) {
                encryptedBytes = docuGenieService.encryptFileHybrid(inputStream.readAllBytes(), serverPublicKeyPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            byteArrayOutputStream.write(("--" + boundary + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"files\"; filename=\"" + uploadedDocuments.getFileOriginalName() + "\"" + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(("Content-Type: application/octet-stream" + lineBreak + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(encryptedBytes);
            byteArrayOutputStream.write(lineBreak.getBytes(StandardCharsets.UTF_8));

            logger.info("File {} encrypted and added to request body, size: {} bytes", uploadedDocuments.getFileOriginalName(), encryptedBytes.length);
        }
        for (String key : json.keySet()) {
            String value = json.getString(key);
            logger.info("Adding form field: {} = {}", key, value);

            byteArrayOutputStream.write(("--" + boundary + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"" + lineBreak + lineBreak).getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(value.getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(lineBreak.getBytes(StandardCharsets.UTF_8));
        }


        byteArrayOutputStream.write(("--" + boundary + "--" + lineBreak).getBytes(StandardCharsets.UTF_8));


        byte[] requestBytes = byteArrayOutputStream.toByteArray();

        System.out.println("==== REQUEST BODY START ====");
        System.out.println(new String(requestBytes, StandardCharsets.UTF_8));
        System.out.println("==== REQUEST BODY END ====");

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI(targetUrl))
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofByteArray(requestBytes))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response status: {}", response.statusCode());
//        logger.info("Response body: {}", response.body());

        JSONObject jsonResponse = new JSONObject(response.body());

//        logger.info("Received response with status code: {}", response);
        JSONObject var17 = docuGenieService.decryptResponse(jsonResponse, clientPrivateKeyPath);
//        logger.info("JSON OBJECT--{}", var17);
        Map<String, Object> data = var17.toMap();
        Gson gson = new Gson();
        LeadInformation lead = documentService.checkLead(leadId);
        SubDocumentType subDocument=null;
        String encrytedResponse = attributeEncryptor.encrypt(gson.toJson(data));
        String feildName="All Documents";
        DocuGenieResponse docuGenieResponse=docuGenieService.getAllDocuGenie(lead,feildName,encrytedResponse);
        return new ResponseEntity<>(new ApiResponse(OK, "", data), OK);

    }

}