package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Role.CreateRoleReq;
import com.example.managementapi.Dto.Response.Role.CreateRoleRes;
import com.example.managementapi.Dto.Response.Role.RoleRes;
import com.example.managementapi.Mapper.RoleMapper;
import com.example.managementapi.Repository.PermissionRepository;
import com.example.managementapi.Repository.RoleRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    @Autowired
    private final RoleMapper roleMapper;

    @Autowired
    private final PermissionRepository permissionRepository;

    @Autowired
    private final RoleRespository roleRespository;

    public CreateRoleRes createRole(CreateRoleReq request){
        var role = roleMapper.toCreateRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permission));


        role =  roleRespository.save(role);

       return roleMapper.toCreateRoleRes(role);
    }

    public List<RoleRes> getRole(){
        var roles = roleRespository.findAll();

        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    void deleteRole(String roleId){
        roleRespository.deleteById(roleId);
    }
}
