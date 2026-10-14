package com.idaav8.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idaav8.app.model.Area;

@Repository 
public interface AreaRepository extends JpaRepository<Area, Long> {
    boolean existsByAreaName(String areaName);
    Area findByAreaName(String areaName);
}
