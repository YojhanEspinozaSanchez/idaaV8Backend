package com.idaav8.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idaav8.app.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
   Optional<Employee> findByEmail(String email);

   Optional<Employee> findByUsername(String username);

   Optional<Employee> findById(Long id);

   boolean existsByEmpId(String empId);

   



}
