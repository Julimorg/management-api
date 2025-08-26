package com.example.managementapi.Service;


import com.example.managementapi.Dto.Email.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class EmailService {

    @NonFinal
    @Value("${spring.mail.username}")
    private String email_name;

    private final JavaMailSender javaMailSender; // --> Class JavaMailSender chịu trách nhiệm cho việc send mail

    //? Load template từ resources
    //? Code này của GPT =)
    public String loadTemplate(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource(fileName);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    //? Function dùng để send mail
    public void sendOtpEmail(MailBody mailBody, int otp) throws Exception {
        String htmlContent = loadTemplate("sendEmailForm.html");
        htmlContent = htmlContent.replace("{{otp}}", String.valueOf(otp));
        htmlContent = htmlContent.replace("{{email}}", mailBody.to());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        //? collect các config của MailBody và Application Properties
        helper.setTo(mailBody.to());
        helper.setFrom(email_name);
        helper.setSubject(mailBody.subject());
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }
}
