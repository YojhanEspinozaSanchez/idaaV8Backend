package com.idaav8.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idaav8.app.dto.ProfileResponseDTO;
import com.idaav8.app.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> getProfile(
            @PathVariable String id) throws Exception {

        ProfileResponseDTO profile =
                profileService.getProfile(id);

        return ResponseEntity.ok(profile);
    }
}