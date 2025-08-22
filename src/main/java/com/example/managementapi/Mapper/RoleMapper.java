package com.example.managementapi.Mapper;

import com.example.managementapi.Enum.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

//    @Mapping(target = "permissions", ignore = true)
//    Role toCreateRole(CreateRoleRequest request);
//
//    RoleResponse toRoleResponse(Role role);
}
