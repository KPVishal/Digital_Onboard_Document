package com.bijlipay.controller;

import com.bijlipay.model.AmlStatus;
import com.bijlipay.model.LeadInformation;
import com.bijlipay.repository.AmlStatusRepository;
import com.bijlipay.repository.LeadInformationRepository;
import com.bijlipay.service.FileStorageService;
import com.bijlipay.util.ApiResponse;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/document/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileStorageService fileStorageService;

    //Aml Status
    @Autowired
    private AmlStatusRepository amlStatusRepository;

    @Autowired
    private LeadInformationRepository leadInformationRepository;

    /**
     * Uploading file
     */

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        String fileName = fileStorageService.storeFileReturnFileName(file);
        String fileUri = fileName;
        HashMap<String, String> out = new HashMap<>();
        out.put("fileUri", fileUri);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", out), HttpStatus.OK);
    }

    /*** Downloading the file */


    @GetMapping("/downloadFile/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request) throws IOException, DocumentException {
        //Load File as a Resource

        Resource resource = fileStorageService.loadFileAsResource(filename);
        logger.info("file ============{}", resource);
        // Try to determine file's content type
        String contenType = null;
        String mimeType = null;
        try {
            logger.info("file test {}", resource.getFile().getAbsolutePath());
            InputStream is = new BufferedInputStream(new FileInputStream(resource.getFile()));
            mimeType = URLConnection.guessContentTypeFromStream(is);
            contenType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }
        catch (IOException e) {
            System.out.println("Could not determine file type.");
            logger.info("Could not determine file type.");
            throw new RuntimeException(e);
        }
        contenType = (contenType != null ? contenType : (mimeType != null ? mimeType : "application/pdf"));//application/octet-stream
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contenType)).body(resource);
    }

    @PostMapping("/upload-image-file")
    public ResponseEntity<?> uploadImageFile(@RequestParam("file") MultipartFile file, @RequestParam("fileNameOriginal") String fileNameOriginal) {
        logger.info("upload file name  >>>>>>>>>>>" + fileNameOriginal);
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        Map<String, String> response = fileStorageService.storeFile(file, fileNameOriginal);
        String fileUri = response.get("fileName");
        response.put("fileUri", fileUri);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", response), HttpStatus.OK);
    }


    /**
     * Aml Changes file upload
     * Lead Id
     */

    @PostMapping("/uploadFile/aml/{leadId}")
    public ResponseEntity<?> uploadAmlFile(@RequestParam("file") MultipartFile file, @PathVariable Long leadId) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
            }
            LeadInformation lead = leadInformationRepository.findByLeadId(leadId);
            if (lead == null) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Lead not found"), HttpStatus.NOT_FOUND);
            }

            String fileName = fileStorageService.storeFileForAmlStatus(file, leadId);

            AmlStatus aml = amlStatusRepository.findByLead(lead).orElseGet(AmlStatus::new);
            aml.setLead(lead);
            aml.setDocument(fileName);
            amlStatusRepository.save(aml);

            HashMap<String, String> out = new HashMap<>();
            out.put("fileUri", fileName);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", out), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error!", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
