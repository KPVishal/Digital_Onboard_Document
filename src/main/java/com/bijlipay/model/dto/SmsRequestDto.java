package com.bijlipay.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmsRequestDto {
    private String to;
    private String sender;
    private String service;
    private String country_code;
    private String template_id;
    private String message;
    @JsonProperty
    private String shorten_url;

    // Getters and Setters


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShorten_url() {
        return shorten_url;
    }

    public void setShorten_url(String shorten_url) {
        this.shorten_url = shorten_url;
    }

    @Override
    public String toString() {
        return "SmsRequestDto{" +
                "to='" + to + '\'' +
                ", sender='" + sender + '\'' +
                ", service='" + service + '\'' +
                ", country_code='" + country_code + '\'' +
                ", template_id='" + template_id + '\'' +
                ", message='" + message + '\'' +
                ",shorten_url='"+shorten_url+'\''+
                '}';
    }
}

