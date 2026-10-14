package com.idaav8.app.dto;

public record EmployeeDto(

        Long id,

        String empId,

        String name,

        String lastName,

        String username,

        String email,

        String roleName,

        String areaName

) {}
