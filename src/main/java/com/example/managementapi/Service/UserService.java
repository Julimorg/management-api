package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Auth.SignUpReq;
import com.example.managementapi.Dto.Request.User.UpdateUseReq;
import com.example.managementapi.Dto.Response.User.GetUserRes;
import com.example.managementapi.Dto.Response.User.UpdateUserRes;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Enum.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Mapper.UserMapper;
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


    public List<GetUserRes> getUser(){
        return userRepository.findAll().stream()
                .map(userMapper::toGetUser).toList();
    }

    public User signUp(SignUpReq request){

        if(userRepository.existsByUserName(request.getUserName()))
            throw  new AppException((ErrorCode.USER_EXISTED));


        //? Sử dụng Mapper
        User user = userMapper.toUser(request);

        //? Sử dụng thuật toán Bcrypt để encode Pass
        //? Nên đặt length là 10 vì đây là ở muc thuật toán có thể encrypt nhanh
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        //? 2. Đây là cách thủ công ko sử dụng đến mapper


        return userRepository.save(user);
    }

    public UpdateUserRes updateUser(String userId, UpdateUseReq request){

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
