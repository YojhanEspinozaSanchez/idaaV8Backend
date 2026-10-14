package com.idaav8.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idaav8.app.model.State;


@Repository 
public interface  StateRepository extends JpaRepository<State, Long>{
    Optional<State> findById(Long id);

}
