package com.example.managementapi.Service.UserService;

import com.example.managementapi.Dto.Request.ReqCreateUser;
import com.example.managementapi.Dto.Request.ReqUpdateUser;
import com.example.managementapi.Dto.Response.ResGetUser;
import com.example.managementapi.Dto.Response.ResUpdateUser;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Enum.ErrorCode.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Mapper.UserMapper.UserMapper;
import com.example.managementapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;


    public List<ResGetUser> getUser(){
        return userRepository.findAll().stream()
                .map(userMapper::toGetUser).toList();
    }

    public User createUser(ReqCreateUser request){

        if(userRepository.existsByUserName(request.getUserName()))
            throw  new AppException((ErrorCode.USER_EXISTED));


        //? Sử dụng Mapper
        User user = userMapper.toUser(request);

        //? Sử dụng thuật toán Bcrypt để encode Pass
        //? Nên đặt length là 10 vì đây là ở muc thuật toán có thể encrypt nhanh
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        //? 2. Đây là cách thủ công ko sử dụng đến mapper
//        User user = new User();
//        user.setUserName(request.getUserName());
//        user.setPassword(request.getPassword());
//        user.setEmail(request.getEmail());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setAddress(request.getAddress());
//        user.setPhoneNumber(request.getPhoneNumber());
//        user.setRole(request.getRole());
//        user.setIsActive(request.getIsActive());


        return userRepository.save(user);
    }

    public ResUpdateUser updateUser(String userId, ReqUpdateUser request){

        if(userRepository.existsByUserName(request.getUserName()))
            throw  new AppException((ErrorCode.USER_EXISTED));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found!"));

        userMapper.updateUser(user, request);

        return userMapper.toResUpdateUser(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

}
