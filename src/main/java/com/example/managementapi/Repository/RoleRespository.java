package com.example.managementapi.Repository;

import com.example.managementapi.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRespository extends JpaRepository<Role, String> {
}
