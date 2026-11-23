package com.bijlipay.controller;

import com.bijlipay.model.*;
import com.bijlipay.model.dto.NotificationDto;
import com.bijlipay.model.dto.PushLeadDto;
import com.bijlipay.model.dto.PushNotificationResponseDto;
import com.bijlipay.repository.LeadDocumentHistoryRepository;
import com.bijlipay.repository.NotificationRepository;
import com.bijlipay.repository.UserRepository;
import com.bijlipay.service.implement.AndroidPushNotificationsService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BaseController {

    private static Logger logger= LoggerFactory.getLogger(BaseController.class);
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @Autowired
    private LeadDocumentHistoryRepository leadDocumentHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${file.url}")
    private String fileUrl;

    /**
     * To get uploaded file URL for Download
     */

    public String getFileUrl(String fileName) {
        String url = null;
        url = fileUrl + fileName;
        return url;
    }

    public void storeLeadDocumentActivityHistory(UploadedDocuments uploadedDocuments , String remarks){
        LeadDocumentHistory documentHistory = new LeadDocumentHistory();

        documentHistory.setLeadDocuments(uploadedDocuments.getLeadDocuments());
        documentHistory.setRemarks(remarks);
        leadDocumentHistoryRepository.save(documentHistory);
    }

    public User getSessionUser() {
        String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // log.info("The session user ===========user id  {}, user name {}",user.getId(),user.getName());
        return userRepository.findSessionUser(user);
    }

    @Async
    public void sendPushNotification(String title,String reason, LeadInformation leadInformation, String redirectTo) {
        sendPushNotificationGenerate(title, reason,leadInformation, redirectTo, leadInformation.getCreatedBy());
    }

    @Async
    public void sendPushNotificationGenerate(String title,String reason, LeadInformation leadInformation, String redirectTo, User userToNotify) {
        Gson gson = new Gson();
        PushNotificationResponseDto<PushLeadDto> pushNotificationResponseDto = new PushNotificationResponseDto();
        pushNotificationResponseDto.setNotification(new NotificationDto(title,reason));
        PushLeadDto pushLeadDto = new PushLeadDto();
        pushLeadDto.setLeadId(leadInformation.getLeadId());
        pushLeadDto.setLeadStatus(leadInformation.getLeadStatus().getLeadStatusId());
        pushLeadDto.setLeadNumber(leadInformation.getLeadNumber());
        pushLeadDto.setRedirectTo(redirectTo);
        pushNotificationResponseDto.setToken(userToNotify != null ? userToNotify.getDeviceToken() : null);
        Notification notification = new Notification();
        notification.setNotificationTitle(title);
        notification.setNotificationReason(reason);
        notification.setNotificationRedirectTo(redirectTo);
        notification.setNotificationRegion(leadInformation.getRegion());
        notification.setUser(userToNotify);
        notification.setData(new Gson().toJson(pushLeadDto));
        Notification afterNotificationSave = notificationRepository.save(notification);
        pushLeadDto.setNotificationId(afterNotificationSave.getId());
        String pushData=gson.toJson(pushLeadDto);
        Map<String, String> dataElement = new HashMap<>();
        dataElement.put("keysandvalues",pushData);
        pushNotificationResponseDto.setData(dataElement);

        try {
            Map<String, PushNotificationResponseDto> result = new HashMap<>();
            result.put("message",pushNotificationResponseDto);
            String payload = gson.toJson(result);
            String firebaseResponse = this.sendPushNotification(payload);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public String sendPushNotification(String bodyRequest) throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(bodyRequest);
        String firebaseResponse = pushNotification.get();
        logger.info("Firebase Response --{}",firebaseResponse);
        return firebaseResponse;
    }

}
