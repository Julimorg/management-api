package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Auth.SignUpReq;
import com.example.managementapi.Dto.Request.User.UpdateUseReq;
import com.example.managementapi.Dto.Response.User.*;
import com.example.managementapi.Entity.Role;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Enum.ErrorCode;
import com.example.managementapi.Enum.Status;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Mapper.UserMapper;
import com.example.managementapi.Repository.RoleRepository;
import com.example.managementapi.Repository.UserRepository;
import com.example.managementapi.Specification.UserSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;

// ** =============================== ROLE USER ===============================

    public SignUpUserRes signUp(SignUpReq request){

        if(userRepository.existsByUserName(request.getUserName()))
            throw  new AppException((ErrorCode.USER_EXISTED));

        //? Sử dụng Mapper
        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name("USER")
                            .description("Default user role")
                            .build();
            Role savedRole = roleRepository.save(newRole);
            log.info("Created role: {}", savedRole);
            return savedRole;
                });

        user.setIsActive(String.valueOf(Status.ACTIVE));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        user.setRoles(roles);

        return userMapper.toSignUpUserRes(userRepository.save(user));
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UpdateUserRes updateUser(String userId, UpdateUseReq request){

        if(userRepository.existsByUserName(request.getUserName()))
            throw  new AppException((ErrorCode.USER_EXISTED));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found!"));

        userMapper.updateUser(user, request);

        return userMapper.toResUpdateUser(userRepository.save(user));
    }

    // ** =============================== ROLE ADMIN ===============================

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public List<GetUserRes> getUser(){
        return userRepository.findAll().stream()
                .map(user -> userMapper.toGetUser(user)).toList();

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public Page<UserSearchResByAdmin> searchUserByAdmin(String keyword, String status, Pageable pageable){
        Specification<User> spec = UserSpecification.searchByCriteria(keyword, status);
        Page<User> userPage = userRepository.findAll(spec, pageable);
        return  userPage.map(user -> userMapper.toUserSearchResByAdmin(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }



}
