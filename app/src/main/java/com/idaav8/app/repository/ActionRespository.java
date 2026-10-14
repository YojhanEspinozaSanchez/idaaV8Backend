package com.idaav8.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idaav8.app.model.Action;

@Repository 
public interface ActionRespository extends JpaRepository<Action, Long>{

}
