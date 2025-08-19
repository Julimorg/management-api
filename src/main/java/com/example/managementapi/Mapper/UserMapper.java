package com.example.managementapi.Mapper;

import com.example.managementapi.Dto.Request.SignUpReq;
import com.example.managementapi.Dto.Request.UpdateUseReq;
import com.example.managementapi.Dto.Response.GetUserRes;
import com.example.managementapi.Dto.Response.UpdateUserRes;
import com.example.managementapi.Entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(SignUpReq request);

    UpdateUserRes toResUpdateUser(User user);

    GetUserRes toGetUser(User user);

    void updateUser(@MappingTarget User user, UpdateUseReq request);

}
