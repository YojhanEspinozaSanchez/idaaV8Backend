package com.idaav8.app.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idaav8.app.model.Accelerator;
import com.idaav8.app.model.Employee;

@Repository
public interface AcceleratorRepository extends JpaRepository<Accelerator, Long> {
    
    Optional<Accelerator> findByAccelName(String accelName);
    Optional<Accelerator> findByIpAddress(String ipAddress);
    Optional<Accelerator> findByAccelNameIgnoreCase(String name);
    boolean existsByOwner(Employee owner);

}
