package com.priceline.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.priceline.role.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByUid(String uid);

}