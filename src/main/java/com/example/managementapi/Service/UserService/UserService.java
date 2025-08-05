package com.example.managementapi.Service.UserService;

import com.example.managementapi.Entity.User;
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



}
