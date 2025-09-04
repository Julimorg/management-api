package com.example.managementapi.Mapper;

import com.example.managementapi.Dto.Request.Auth.SignUpReq;
import com.example.managementapi.Dto.Request.User.UpdateUseReq;
import com.example.managementapi.Dto.Response.User.GetUserRes;
import com.example.managementapi.Dto.Response.User.SignUpUserRes;
import com.example.managementapi.Dto.Response.User.UpdateUserRes;
import com.example.managementapi.Dto.Response.User.UserSearchResByAdmin;
import com.example.managementapi.Entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(SignUpReq request);


    UpdateUserRes toResUpdateUser(User user);

    GetUserRes toGetUser(User user);

    SignUpUserRes toSignUpUserRes(User user);

    UserSearchResByAdmin toUserSearchResByAdmin(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UpdateUseReq request);

}
