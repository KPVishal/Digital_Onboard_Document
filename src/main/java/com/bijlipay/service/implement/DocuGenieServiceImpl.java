package com.bijlipay.service.implement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.bijlipay.model.DocuGenieResponse;
import com.bijlipay.model.LeadInformation;
import com.bijlipay.repository.DocuGenieRepository;
import com.bijlipay.service.DocuGenieService;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocuGenieServiceImpl implements DocuGenieService {
    @Autowired
    private DocuGenieRepository docuGenieRepository;
//    public static PublicKey loadPublicKey(String publicKeyPemPath) throws Exception {
//        File file = new File(publicKeyPemPath);
//        if (!file.exists()) {
//            throw new FileNotFoundException("PEM file not found at: " + publicKeyPemPath);
//        }
//        System.out.println("Loading PEM from: " + file.getAbsolutePath());
//        System.out.println("Loading PEM NAME " + file.getName());
//
//        Reader reader = new FileReader(file);
//        PemReader pemReader = new PemReader(reader);
//
//        try {
//            PemObject pemObject = pemReader.readPemObject();
//            if (pemObject == null) {
//                throw new IllegalArgumentException("Invalid PEM file: no content found.");
//            }
//
//            System.out.println("PEM Type: " + pemObject.getType());
//            System.out.println("PEM Content Length: " + pemObject.getContent().length);
//
//            byte[] content = pemObject.getContent();
//            X509EncodedKeySpec spec = new X509EncodedKeySpec(content);
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            PublicKey publicKey = kf.generatePublic(spec);
//
//            System.out.println("Public Key Algorithm: " + publicKey.getAlgorithm());
//            System.out.println("Public Key Format: " + publicKey.getFormat());
//
//            return publicKey;
//        } finally {
//            pemReader.close();
//            reader.close();
//        }
//    }
//
//
//    public static PrivateKey loadPrivateKey(String privateKeyPemPath) throws Exception {
//        Reader reader = new FileReader(privateKeyPemPath);
//        PemReader pemReader = new PemReader(reader);
//        try {
//            PemObject pemObject = pemReader.readPemObject();
//            if (pemObject == null) {
//                throw new IllegalArgumentException("Invalid PEM file: no content");
//            }
//            byte[] content = pemObject.getContent();
//            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            return kf.generatePrivate(spec);
//        } finally {
//            pemReader.close();
//            reader.close();
//        }
//    }

    @Override
    public JSONObject decryptResponse(JSONObject var0, String var1) throws Exception {
        byte[] var2 = Base64.getDecoder().decode(var0.getString("encrypted_key"));
        byte[] var3 = Base64.getDecoder().decode(var0.getString("encrypted_data"));
        byte[] var4 = Base64.getDecoder().decode(var0.getString("nonce"));
        byte[] var5 = Base64.getDecoder().decode(var0.getString("tag"));
        System.out.println("encrypted_key------" + var0.getString("encrypted_key"));
        RSAPrivateCrtKeyParameters var6 = loadRSAPrivateKeyPKCS1(var1);
        OAEPEncoding var7 = new OAEPEncoding(new RSAEngine(), new SHA256Digest());
        var7.init(false, var6);
        byte[] var8 = var7.processBlock(var2, 0, var2.length);
        Cipher var9 = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec var10 = new GCMParameterSpec(128, var4);
        var9.init(2, new SecretKeySpec(var8, "AES"), var10);
        byte[] var11 = var9.doFinal(concat(var3, var5));
        return new JSONObject(new String(var11, StandardCharsets.UTF_8));
    }

    private static byte[] concat(byte[] var0, byte[] var1) {
        byte[] var2 = new byte[var0.length + var1.length];
        System.arraycopy(var0, 0, var2, 0, var0.length);
        System.arraycopy(var1, 0, var2, var0.length, var1.length);
        return var2;
    }

    private static RSAPrivateCrtKeyParameters loadRSAPrivateKeyPKCS1(String var0) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        PEMParser var1 = new PEMParser(new FileReader(var0));

        RSAPrivateCrtKeyParameters var6;
        label42:
        {
            RSAPrivateCrtKeyParameters var10;
            label41:
            {
                try {
                    Object var2 = var1.readObject();
                    JcaPEMKeyConverter var3;
                    RSAPrivateCrtKey var5;
                    if (var2 instanceof PEMKeyPair) {
                        var3 = (new JcaPEMKeyConverter()).setProvider("BC");
                        KeyPair var11 = var3.getKeyPair((PEMKeyPair) var2);
                        var5 = (RSAPrivateCrtKey) var11.getPrivate();
                        var6 = new RSAPrivateCrtKeyParameters(var5.getModulus(), var5.getPublicExponent(), var5.getPrivateExponent(), var5.getPrimeP(), var5.getPrimeQ(), var5.getPrimeExponentP(), var5.getPrimeExponentQ(), var5.getCrtCoefficient());
                        break label42;
                    }

                    if (var2 instanceof RSAPrivateKey) {
                        RSAPrivateKey var9 = (RSAPrivateKey) var2;
                        var10 = new RSAPrivateCrtKeyParameters(var9.getModulus(), var9.getPublicExponent(), var9.getPrivateExponent(), var9.getPrime1(), var9.getPrime2(), var9.getExponent1(), var9.getExponent2(), var9.getCoefficient());
                        break label41;
                    }

                    if (!(var2 instanceof PrivateKeyInfo)) {
                        throw new IllegalArgumentException("Unsupported PEM format for private key.");
                    }

                    var3 = (new JcaPEMKeyConverter()).setProvider("BC");
                    PrivateKey var4 = var3.getPrivateKey((PrivateKeyInfo) var2);
                    var5 = (RSAPrivateCrtKey) var4;
                    var6 = new RSAPrivateCrtKeyParameters(var5.getModulus(), var5.getPublicExponent(), var5.getPrivateExponent(), var5.getPrimeP(), var5.getPrimeQ(), var5.getPrimeExponentP(), var5.getPrimeExponentQ(), var5.getCrtCoefficient());
                } catch (Throwable var8) {
                    try {
                        var1.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }

                    throw var8;
                }

                var1.close();
                return var6;
            }

            var1.close();
            return var10;
        }

        var1.close();
        return var6;
    }

    @Override
    public byte[] encryptFileHybrid(byte[] var9, String var1) throws Exception {
        String var2 = new String(Files.readAllBytes((new File(var1)).toPath()), StandardCharsets.US_ASCII);
        var2 = var2.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
        byte[] var3 = Base64.getDecoder().decode(var2);
        X509EncodedKeySpec var4 = new X509EncodedKeySpec(var3);
        KeyFactory var5 = KeyFactory.getInstance("RSA");
        PublicKey var6 = var5.generatePublic(var4);
        KeyGenerator var7 = KeyGenerator.getInstance("AES");
        var7.init(256);
        SecretKey var8 = var7.generateKey();
//        byte[] var9 = Files.readAllBytes((new File(var0)).toPath());
        byte[] var10 = new byte[12];
        (new SecureRandom()).nextBytes(var10);
        Cipher var11 = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec var12 = new GCMParameterSpec(128, var10);
        var11.init(1, var8, var12);
        byte[] var13 = var11.doFinal(var9);
        byte var14 = 16;
        int var15 = var13.length - var14;
        byte[] var16 = new byte[var15];
        byte[] var17 = new byte[var14];
        System.arraycopy(var13, 0, var16, 0, var15);
        System.arraycopy(var13, var15, var17, 0, var14);
        Cipher var18 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        var18.init(1, var6);
        byte[] var19 = var18.doFinal(var8.getEncoded());
        ByteArrayOutputStream var20 = new ByteArrayOutputStream();
        var20.write(var19);
        var20.write(var10);
        var20.write(var17);
        var20.write(var16);
        return var20.toByteArray();
    }

    @Override
    public DocuGenieResponse getAllDocuGenie(LeadInformation lead, String feildName, String encrytedResponse) {
        System.out.println("Error LOg--"+feildName);
        DocuGenieResponse docuGenieResponse = docuGenieRepository.getByIds(lead);
        if (docuGenieResponse != null) {
            switch (feildName) {
                case "Pan" -> docuGenieResponse.setPanCard(encrytedResponse);
                case "Aadhaar Card Front" -> docuGenieResponse.setAadhaarCardFront(encrytedResponse);
                case "Aadhaar Card Back" -> docuGenieResponse.setAadhaarCardBack(encrytedResponse);
                case "GST Document" -> docuGenieResponse.setGst(encrytedResponse);
                case "All Documents" ->docuGenieResponse.setAllDocumentResponse(encrytedResponse);
            }
           return docuGenieRepository.save(docuGenieResponse);
        }else {
            DocuGenieResponse docuGenieResponse1=new DocuGenieResponse();
            switch (feildName) {
                case "Pan" -> docuGenieResponse1.setPanCard(encrytedResponse);
                case "Aadhaar Card Front" -> docuGenieResponse1.setAadhaarCardFront(encrytedResponse);
                case "Aadhaar Card Back" -> docuGenieResponse1.setAadhaarCardBack(encrytedResponse);
                case "GST Document" -> docuGenieResponse1.setGst(encrytedResponse);
                case "All Documents" ->docuGenieResponse1.setAllDocumentResponse(encrytedResponse);

            }
            docuGenieResponse1.setLeadInformation(lead);
          return   docuGenieRepository.save(docuGenieResponse1);
        }
    }

//    @Override
//    public byte[] encryptFileHybrid(byte[] data, String serverPublicKeyPath) throws Exception {
//        PublicKey publicKey = loadPublicKeys(serverPublicKeyPath);
//        System.out.println("publicKey---:"+publicKey);
//
//        // 2. Generate AES key (256 bits)
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(256);
//        SecretKey aesKey = keyGen.generateKey();
//
//        // 3. Read file bytes
////        byte[] fileData = Files.readAllBytes(Paths.get(filePath));
//
//        // 4. Encrypt file data with AES GCM (authenticated encryption)
//        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
//        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
//        byte[] iv = aesCipher.getIV(); // GCM nonce
//        byte[] encryptedData = aesCipher.doFinal(data);
//
//        // 5. Encrypt AES key with RSA
//        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
//        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());
//
//        // 6. Combine encrypted AES key + IV + encrypted file data
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        outputStream.write(encryptedAesKey);
//        outputStream.write(iv);
//        outputStream.write(encryptedData);
//
//        return outputStream.toByteArray();
//    }

//    public PublicKey loadPublicKeys(String pemFilePath) throws Exception {
//        // Step 1: Read the PEM file content
//        String pem = new String(Files.readAllBytes(Paths.get(pemFilePath)), StandardCharsets.UTF_8);
//
//        // Step 2: Clean the PEM header/footer and whitespace
//        pem = pem.replace("-----BEGIN PUBLIC KEY-----", "")
//                .replace("-----END PUBLIC KEY-----", "")
//                .replaceAll("\\s+", "");
//
//        // Step 3: Decode and convert to PublicKey
//        byte[] decoded = Base64.getDecoder().decode(pem);
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//        return keyFactory.generatePublic(keySpec);
//    }


    // Decrypt response JSON (map containing base64 strings)
//    public Map decryptResponse(Map<String, Object> responseJson, String clientPrivateKeyPath) throws Exception {
//        PrivateKey privateKey = loadPrivateKey(clientPrivateKeyPath);
//
//        String encryptedKeyBase64 = (String) responseJson.get("encrypted_key");
//        String encryptedDataBase64 = (String) responseJson.get("encrypted_data");
//
//        byte[] encryptedKey = Base64.getDecoder().decode(encryptedKeyBase64);
//        byte[] encryptedData = Base64.getDecoder().decode(encryptedDataBase64);
//
//        // Decrypt AES key
//        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
//        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedKey);
//        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
//
//        // Decrypt data with AES ECB + PKCS5Padding (as in your Python)
//        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
//        byte[] decryptedData = aesCipher.doFinal(encryptedData);
//
//        String json = new String(decryptedData, StandardCharsets.UTF_8);
//
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(json, Map.class);
//    }
}

