package com.idaav8.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idaav8.app.dto.ChangePasswordDto;
import com.idaav8.app.dto.EmployeeCreateDto;
import com.idaav8.app.dto.EmployeeDto;
import com.idaav8.app.dto.EmployeeUpdateDto;
import com.idaav8.app.model.Employee;
import com.idaav8.app.service.EmployeeService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    public Employee addEmployee(@RequestBody EmployeeCreateDto dto)throws Exception {
        

        return employeeService
                .addEmployee(dto);
    }

    @PutMapping("/edit")
    public Employee editEmployee(@RequestBody @Valid EmployeeUpdateDto dto)throws Exception {
        System.out.println(dto);
        return employeeService
                .editEmployee(dto);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);
    }

    @GetMapping("/list")
    public List<EmployeeDto> getUsers() {

        return employeeService.getUsers();
    }

    @PostMapping("/recover-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email)
            throws Exception {

        employeeService.recoverPassword(email);

        return ResponseEntity.ok(
                "Password sent successfully");
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordDto dto) {

        try {

            employeeService.changePassword(
                    dto);

            return ResponseEntity.ok(
                    "Password changed successfully");

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        e.getMessage());
        }
    }
}

