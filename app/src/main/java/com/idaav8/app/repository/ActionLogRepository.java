package com.idaav8.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idaav8.app.model.ActionLog;

@Repository 
public interface ActionLogRepository extends JpaRepository<ActionLog, Long>{
    

}
