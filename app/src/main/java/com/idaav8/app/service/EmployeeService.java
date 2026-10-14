package com.idaav8.app.service;

import com.idaav8.app.repository.AcceleratorRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.idaav8.app.dto.ChangePasswordDto;
import com.idaav8.app.dto.EmployeeCreateDto;
import com.idaav8.app.dto.EmployeeDto;
import com.idaav8.app.dto.EmployeeUpdateDto;
import com.idaav8.app.dto.ProfileResponseDTO;
import com.idaav8.app.model.Area;
import com.idaav8.app.model.Employee;
import com.idaav8.app.model.Role;
import com.idaav8.app.repository.AreaRepository;
import com.idaav8.app.repository.EmployeeRepository;
import com.idaav8.app.repository.RoleRepository;

@Service
public class EmployeeService {

    private final AcceleratorRepository acceleratorRepository;

    private final ProfileService profileService;

    //@Autowired
    private final EmployeeRepository employeeRepository; 
    private final AreaRepository areaRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    

    public EmployeeService(ProfileService profileService, EmployeeRepository employeeRepository, AreaRepository areaRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService, AcceleratorRepository acceleratorRepository) {
        this.profileService = profileService;
        this.employeeRepository = employeeRepository;
        this.areaRepository = areaRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.acceleratorRepository = acceleratorRepository;
    }

    public Employee addEmployee(EmployeeCreateDto dto) throws Exception {

        ProfileResponseDTO profile =
                profileService.getProfile(dto.empId());

        Employee employee = new Employee();
        String hash = passwordEncoder.encode("DONT!FORG3T");

    
        
        

        
        // Datos ingresados manualmente
        employee.setEmpId(dto.empId());
        
       

        // Datos obtenidos desde Unified Profile
        employee.setName(profile.getName());
        employee.setLastName(profile.getLastName());
        employee.setEmail(profile.getEmail());
        employee.setUsername(profile.getUserName());
        employee.setPassword(hash);
        employee.setArea(setAreaToEmployee(profile.getArea()));
        employee.setRole(setRoleToEmployee(dto.roleName()));

        employeeRepository.save(employee);


        return employee;
    }

    public Employee editEmployee(EmployeeUpdateDto dto) throws Exception {


        Employee employee =employeeRepository.findById(dto.id()).orElseThrow(() ->new RuntimeException("Employee not found"));
        employee.setRole(setRoleToEmployee(dto.roleName()));

        employeeRepository.save(employee);


        return employee;
    }

    public void deleteEmployee(Long id) {

        Employee employee =
                employeeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"));

        if(acceleratorRepository.existsByOwner(employee)){
            throw new RuntimeException(
                "User has assigned accelerators and cannot be deleted."
            );

        }
        employeeRepository.delete(employee);
    }

    public List<EmployeeDto> getUsers() {

        return employeeRepository
                .findAll()
                .stream()
                .map(emp ->

                        new EmployeeDto(

                                emp.getId(),

                                emp.getEmpId(),

                                emp.getName(),

                                emp.getLastName(),

                                emp.getUsername(),

                                emp.getEmail(),

                                emp.getRole()
                                        .getRoleName(),

                                emp.getArea()
                                        .getAreaName()
                        )

                )
                .toList();
    }


    private Area setAreaToEmployee(String areaName) {
        Area area = areaRepository.findByAreaName(areaName);
        if (area != null) {
            return area;
        }
        return null;
    }

    private Role setRoleToEmployee(String roleName) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role != null) {
            return role;
        }
        return null;
    }

    public boolean exist(String id){
        if(employeeRepository.existsByEmpId(id)){
            return true;
        }

        return false;
    }

    public Employee recoverPassword(String email) throws Exception {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(email);
        Employee employee = employeeOptional.orElse(null);
        if(employee == null){
            throw new Exception("Employee not found");
        }

        String temporaryPassword = "DONT!FORG3T";
        employee.setPassword( passwordEncoder.encode(temporaryPassword));
        employeeRepository.save(employee);

        emailService.sendPasswordEmail(employee.getEmail(), employee.getName(), temporaryPassword);

        return employee;
    }

    public void changePassword (ChangePasswordDto dto) throws Exception {

        Employee employee =
                employeeRepository
                        .findByUsername(
                                dto.username())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        boolean valid =
                passwordEncoder.matches(
                        dto.currentPassword(),
                        employee.getPassword());

        if (!valid) {

            throw new Exception(
                    "Current password is incorrect");
        }

        employee.setPassword(
                passwordEncoder.encode(
                        dto.newPassword()));

        employeeRepository.save(
                employee);
    }









}