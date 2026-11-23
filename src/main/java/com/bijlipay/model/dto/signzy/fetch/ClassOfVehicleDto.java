package com.bijlipay.model.dto.signzy.fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassOfVehicleDto {

    private String classOfVehicle;

    public ClassOfVehicleDto(String classOfVehicle) {
        this.classOfVehicle = classOfVehicle;
    }

    public String getClassOfVehicle() {
        return classOfVehicle;
    }

    public void setClassOfVehicle(String classOfVehicle) {
        this.classOfVehicle = classOfVehicle;
    }
}
