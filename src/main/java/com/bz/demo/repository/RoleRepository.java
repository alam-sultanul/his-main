package com.bz.demo.repository;


import com.bz.demo.model.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByRole(String role);
}
