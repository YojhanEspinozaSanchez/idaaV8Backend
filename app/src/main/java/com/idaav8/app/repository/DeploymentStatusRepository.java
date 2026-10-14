package com.idaav8.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idaav8.app.model.DeploymentStatus;

@Repository
public interface DeploymentStatusRepository extends JpaRepository<DeploymentStatus, Long>{
    Optional<DeploymentStatus> findByStatusName(String statusName);
}
