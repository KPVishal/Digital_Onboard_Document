package com.bijlipay.service;

import com.bijlipay.exception.FileStorageException;
import com.bijlipay.exception.MyFileNotFoundException;
import com.bijlipay.model.dto.FileDto;
import com.bijlipay.property.FileStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${osv.path}")
    private String osvPath;

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);
    private final Path fileStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        log.info("OSVVVV---{}",fileStorageProperties.getUploadDir());
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ioException) {
            throw new FileStorageException("Could not create directory where the uploaded files will be stored!!", ioException);
        }
    }

    public String storeFileReturnFileName(MultipartFile file) {

        try {
            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence" + fileNameOriginal);
            }
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();

            String fileExtension = null;

            String mimeType = file.getContentType();
            log.info("mime type --- : {}", mimeType);
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }
            log.info("file extension -- : {}", fileExtension);

            String fileNameNew = randomUUIDString + fileExtension;
            // Normalize file name
            String fileName = StringUtils.cleanPath(fileNameNew);

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Could not store file  Please try again!", e);
        }
    }

    public Map<String, String> storeFile(MultipartFile file, String fileNameReq) {
        try {

            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence" + fileNameOriginal);
            }

            String randomUUIDString = String.valueOf(System.currentTimeMillis());
            if (fileNameReq != null) {
                randomUUIDString = fileNameReq + "_" + randomUUIDString;
            }
            randomUUIDString = randomUUIDString.replaceAll("[^a-zA-Z0-9_]", "");
            String fileExtension = null;
            String mimeType = file.getContentType();
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }

            String fileNameNew = randomUUIDString + fileExtension;
            // Normalize file name
            String fileName = StringUtils.cleanPath(fileNameNew);

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("fileName", fileName);
            map.put("mimeType", mimeType);
            map.put("fileNameOriginal", fileNameOriginal);
            return map;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file  Please try again!", e);
        }
    }

    public Map<String, String> storeOSVFile(MultipartFile file, FileDto fileName) {
        try {

//            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
//            if (fileNameOriginal.contains("..")) {
//                throw new FileStorageException("Filename contains invalid path sequence" + fileNameOriginal);
//            }
//
//            String randomUUIDString = String.valueOf(System.currentTimeMillis());
//            if (fileNameReq != null) {
//                randomUUIDString = fileNameReq + "_" + randomUUIDString;
//            }
//            randomUUIDString = randomUUIDString.replaceAll("[^a-zA-Z0-9_]", "");
//            String fileExtension = null;
//            String mimeType = file.getContentType();
//            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
//                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
//            }
//
//            String fileNameNew = randomUUIDString + fileExtension;
//            // Normalize file name
//            String fileName = StringUtils.cleanPath(fileNameNew);

            Path targetLocation = Paths.get(osvPath).toAbsolutePath().normalize().resolve(fileName.getFileName());
            log.info("OSV PATH-----{}",targetLocation);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            }

            HashMap<String, String> map = new HashMap<>();
            map.put("fileName", fileName.getFileName());
//            map.put("mimeType", mimeType);
//            map.put("fileNameOriginal", fileNameOriginal);
            return map;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file  Please try again!", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            log.info(" file location -- : {}", filePath);
            Resource resource = new UrlResource(filePath.toUri());
            log.info(" data -- : {}", resource);
            log.info(" return -- : {}", resource.exists());
            if (resource.exists()) {
                log.info("data exists -- :{}", 1);
                return resource;
            } else {
                log.info(" not found -- : {}", 2);
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public Map<String, String> storeFileFromOnboardLead(MultipartFile file, String fileNameReq) {
        try {

            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence" + fileNameOriginal);
            }
            String fileExtension = null;
            String mimeType = file.getContentType();
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }

            // Normalize file name
            String fileName = StringUtils.cleanPath(fileNameReq);

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            HashMap<String, String> map = new HashMap<>();
            map.put("fileName", fileName);
            map.put("mimeType", mimeType);
            map.put("fileNameOriginal", fileNameOriginal);
            return map;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file  Please try again!", e);
        }
    }

    public Map<String, String> storeAgreementFile(MultipartFile file, String fileNameReq) {
        try {

            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence" + fileNameOriginal);
            }

            fileNameReq = fileNameReq.replaceAll("[^a-zA-Z0-9_]", "");
            String fileExtension = null;
            String mimeType = file.getContentType();
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }

            String fileNameNew = fileNameReq + fileExtension;
            // Normalize file name
            String fileName = StringUtils.cleanPath(fileNameNew);

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            HashMap<String, String> map = new HashMap<>();
            map.put("fileName", fileName);
            map.put("mimeType", mimeType);
            map.put("fileNameOriginal", fileNameOriginal);
            return map;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file  Please try again!", e);
        }
    }

    // Aml Changes

    public String storeFileForAmlStatus(MultipartFile file, Long leadId) {

        try {
            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence" + fileNameOriginal);
            }
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();

            String fileExtension = null;

            String mimeType = file.getContentType();
            log.info("mime type --- : {}", mimeType);
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }
            log.info("file extension -- : {}", fileExtension);

            String fileNameNew =  leadId+"_"+"AmlStatus"+"_"+randomUUIDString + fileExtension;
            log.info("Aml File name ---->{}",fileNameNew!=null?fileNameNew:"NA");
            // Normalize file name
            String fileName = StringUtils.cleanPath(fileNameNew);

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Could not store file  Please try again!", e);
        }
    }
}
