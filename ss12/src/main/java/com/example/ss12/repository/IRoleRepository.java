package com.example.ss12.repository;

import com.example.ss12.model.entity.Role;
import com.example.ss12.model.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    // Define methods for role-specific queries if needed
    // For example, you might want to find roles by name or permissions
    Optional<Role> findByRoleName(RoleName roleName);
}
