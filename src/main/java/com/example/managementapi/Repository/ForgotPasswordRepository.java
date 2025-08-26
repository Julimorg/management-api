package com.example.managementapi.Repository;

import com.example.managementapi.Entity.ForgotPassword;
import com.example.managementapi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, String> {

    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(int otp, User user);

    Optional<ForgotPassword> findByUser(User user);
}
