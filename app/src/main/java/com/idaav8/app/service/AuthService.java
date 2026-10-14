package com.idaav8.app.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.idaav8.app.dto.LoginRequestDto;
import com.idaav8.app.dto.LoginResponseDto;
import com.idaav8.app.model.Employee;
import com.idaav8.app.repository.EmployeeRepository;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDto login(LoginRequestDto request){
        Employee employee =
                employeeRepository
                    .findByUsername(request.getUsername())
                    .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                    "User not found"));
        if (!passwordEncoder.matches(
                request.getPassword(),
                employee.getPassword())){
                    throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid credentials");
                    } 
        String token =
            jwtService.generateToken(
                employee.getUsername());                      
        return new LoginResponseDto(token, employee.getUsername(), employee.getRole().getRoleName());
                            
    }

}
