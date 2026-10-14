package com.idaav8.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idaav8.app.dto.LoginRequestDto;
import com.idaav8.app.dto.LoginResponseDto;
import com.idaav8.app.service.AuthService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController 
@RequestMapping("/api/auth")
public class AuthController {
    

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {

        return authService.login(request);

    }
    
    

}
