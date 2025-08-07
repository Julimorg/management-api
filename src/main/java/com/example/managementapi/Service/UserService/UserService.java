package com.example.managementapi.Service.UserService;

import com.example.managementapi.Dto.Request.ReqCreateUser;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Enum.ErrorCode.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public List<User> getUser(){
        return userRepository.findAll();
    }

    public User createUser(ReqCreateUser request){

        if(userRepository.existsByUserName(request.getUserName()))
            throw  new AppException((ErrorCode.USER_EXISTED));


        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setIsActive(request.getIsActive());


        return userRepository.save(user);
    }



}
