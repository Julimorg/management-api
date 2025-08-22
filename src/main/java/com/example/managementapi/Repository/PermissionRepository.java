package com.example.managementapi.Repository;

import com.example.managementapi.Entity.Permission;
import com.example.managementapi.Service.PermissionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

}
