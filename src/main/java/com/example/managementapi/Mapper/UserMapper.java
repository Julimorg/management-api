package com.example.managementapi.Mapper;

import com.example.managementapi.Dto.Request.Auth.SignUpReq;
import com.example.managementapi.Dto.Request.User.CreateStaffReq;
import com.example.managementapi.Dto.Request.User.UpdateUseReq;
import com.example.managementapi.Dto.Response.User.*;
import com.example.managementapi.Entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(SignUpReq request);

    @Mapping(target = "userImg", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "forgotPassword", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "cart", ignore = true)
    User toCreateStaff(CreateStaffReq request);

    CreateStaffRes toCreateStaffRes(User user);

    UpdateUserRes toResUpdateUser(User user);

    GetUserRes toGetUser(User user);

    SignUpUserRes toSignUpUserRes(User user);

    SearchByAdminRes toUserSearchResByAdmin(User user);

    SearchByUserRes toUserSearchResByUser(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UpdateUseReq request);

}
