package com.example.managementapi.Repository;

import com.example.managementapi.Dto.Response.User.GetUserRes;
import com.example.managementapi.Dto.Response.User.SearchUserRes;
import com.example.managementapi.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    //? Function nay check user da ton tai trong DB thong qua username
    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);


    @Query(" SELECT u.userName AS userName, u.email AS email FROM User u WHERE " +
            "LOWER(u.userName) LIKE LOWER (CONCAT('%', :keyword ,'%')) OR " +
            "LOWER(u.email) LIKE LOWER (CONCAT('%', :keyword , '%'))"
    )
    Page<SearchUserRes> searchUser(String keyword, Pageable pageable);

}
