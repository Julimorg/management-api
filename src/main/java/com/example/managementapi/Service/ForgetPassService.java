package com.example.managementapi.Service;

import com.example.managementapi.Dto.Email.MailBody;
import com.example.managementapi.Entity.ForgotPassword;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Repository.ForgotPasswordRepository;
import com.example.managementapi.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForgetPassService {
    private final  UserService userService;

    private final AuthenticateService authenticationService;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final ForgotPasswordRepository forgotPasswordRepository;

    private final PasswordEncoder passwordEncoder;

    private int otpGenerate(){
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    public void sendOtpViaEmail(String email) throws Exception {

        int otp = otpGenerate();
        //? Check Email với User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email Not Found!"));

        //? Đoạn này rất quan trọng
        //? Mình phải check xem trong User đã có ForgotPass chưa?
        //?     Tại sao phải check?
        //?         --> Vì ta có relational OneToOne giũa User và ForgotPassword ( OTP, ExpiryDate )
        //?              không thể có 1 user mà nhiều mã OTP được, dẫn đều việc nếu user resend otp
        //?              sẽ bị conflict giữa DB
        //?  Nên là khi check User đã có ForgetPass rồi thì chỉ cần việc generate ra OTP, ExpiryDate mới
        //?   và update lại OTP, ExpiryDate và  trong forget password là xong
        ForgotPassword forgotPassword = forgotPasswordRepository.findByUser(user)
                .orElse(null);
        if(forgotPassword != null){
            forgotPassword.setOtp(otp);
            forgotPassword.setExpirationTime(new Date(System.currentTimeMillis() + 70 * 1000));
        }else {
            forgotPassword = ForgotPassword
                    .builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                    .user(user)
                    .build();
        }

        forgotPasswordRepository.save(forgotPassword);

        //? Build Email Form từ  template SendEmailHtml
        MailBody mailBody = MailBody.builder()
                .to(user.getEmail())
                .subject("OTP for forgot password request")
                .text("Hello!")
                .build();

        emailService.sendOtpEmail(mailBody,otp);

    }

}
