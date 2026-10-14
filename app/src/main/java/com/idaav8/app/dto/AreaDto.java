package com.idaav8.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AreaDto {

    @NotBlank 
    @Size (max = 32)
    private String areaName;

    public AreaDto() {
    }

    public AreaDto(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }
    

}
