package com.bijlipay.model.dto.signzy.fetch;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DlValidityDto {

    private NonTransportDto nonTransport;
    private String hazardousValidTill;
    private TransportDto transport;
    private String hillValidTill;

    public NonTransportDto getNonTransport() {
        return nonTransport;
    }

    public void setNonTransport(NonTransportDto nonTransport) {
        this.nonTransport = nonTransport;
    }

    public String getHazardousValidTill() {
        return hazardousValidTill;
    }

    public void setHazardousValidTill(String hazardousValidTill) {
        this.hazardousValidTill = hazardousValidTill;
    }

    public TransportDto getTransport() {
        return transport;
    }

    public void setTransport(TransportDto transport) {
        this.transport = transport;
    }

    public String getHillValidTill() {
        return hillValidTill;
    }

    public void setHillValidTill(String hillValidTill) {
        this.hillValidTill = hillValidTill;
    }
}
