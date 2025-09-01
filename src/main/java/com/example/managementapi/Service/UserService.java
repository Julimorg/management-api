package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Auth.SignUpReq;
import com.example.managementapi.Dto.Request.User.UpdateUseReq;
import com.example.managementapi.Dto.Response.User.GetUserRes;
import com.example.managementapi.Dto.Response.User.SearchUserRes;
import com.example.managementapi.Dto.Response.User.UpdateUserRes;
import com.example.managementapi.Entity.Role;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Enum.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Mapper.UserMapper;
import com.example.managementapi.Repository.RoleRepository;
import com.example.managementapi.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Url/search/name
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<GetUserRes> getUser(){
        return userRepository.findAll().stream()
                .map(userMapper::toGetUser).toList();
    }

    public User signUp(SignUpReq request){

        if(userRepository.existsByUserName(request.getUserName()))
            throw  new AppException((ErrorCode.USER_EXISTED));

        //? Sử dụng Mapper
        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name("STAFF")
                            .description("Staff role office")
                            .build();
            Role savedRole = roleRepository.save(newRole);
            log.info("Created role: {}", savedRole);
            return savedRole;
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        user.setRoles(roles);


        return userRepository.save(user);
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UpdateUserRes updateUser(String userId, UpdateUseReq request){

        if(userRepository.existsByUserName(request.getUserName()))
            throw  new AppException((ErrorCode.USER_EXISTED));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found!"));

        userMapper.updateUser(user, request);

        return userMapper.toResUpdateUser(userRepository.save(user));
    }

    public Page<SearchUserRes> searchUser(String keyword, Pageable pageable){
        return userRepository.searchUser(keyword, pageable);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

// ** =============================== ROLE ADMIN ===============================


}
