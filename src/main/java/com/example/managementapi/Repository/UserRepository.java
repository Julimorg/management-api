package com.example.managementapi.Repository;

import com.example.managementapi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    //? Function nay check user da ton tai trong DB thong qua username
    boolean existsByUserName(String userName);
}
