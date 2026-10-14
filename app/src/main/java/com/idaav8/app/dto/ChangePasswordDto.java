package com.idaav8.app.dto;

public record ChangePasswordDto(
        String username,
        String currentPassword,
        String newPassword
) {}