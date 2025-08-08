package com.example.managementapi.Mapper.UserMapper;

import com.example.managementapi.Dto.Request.ReqCreateUser;
import com.example.managementapi.Dto.Request.ReqUpdateUser;
import com.example.managementapi.Dto.Response.ResGetUser;
import com.example.managementapi.Dto.Response.ResUpdateUser;
import com.example.managementapi.Entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(ReqCreateUser request);

    ResUpdateUser toResUpdateUser(User user);

    ResGetUser toGetUser(User user);

    void updateUser(@MappingTarget User user, ReqUpdateUser request);

}
