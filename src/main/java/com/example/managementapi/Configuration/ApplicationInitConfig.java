package com.example.managementapi.Configuration;


import com.example.managementapi.Entity.User;
import com.example.managementapi.Enum.Role;
import com.example.managementapi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
        //? Application này sẽ được chạy khi Project này được chạy
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            //? check xem application khi chạy đã có admin chưa
            if(userRepository.findByUserName("admin").isEmpty()){
                var roles = new HashSet<String>();

                roles.add(Role.ADMIN.name());
                User user = User.builder()
                        .userName("admin")
                        .password(passwordEncoder.encode("123456"))
                        .build();

                userRepository.save(user);

                log.warn("ADMIN HAS BEEN CREATED WITH DEFAULT PASSWORD: 123456");
            }

        };
    }

}
