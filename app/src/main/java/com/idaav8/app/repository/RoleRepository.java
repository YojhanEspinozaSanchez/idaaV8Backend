package com.idaav8.app.repository;

import org.springframework.stereotype.Repository;

import com.idaav8.app.model.Role;

@Repository 
public interface RoleRepository extends org.springframework.data.jpa.repository.JpaRepository<com.idaav8.app.model.Role, Long> {
    Role findByRoleName(String roleName);

}
