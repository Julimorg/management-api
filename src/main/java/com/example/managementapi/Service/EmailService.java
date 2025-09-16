package com.example.managementapi.Service;


import com.example.managementapi.Dto.Email.MailBody;
import com.example.managementapi.Dto.Response.Order.GetOrderResponse;
import com.example.managementapi.Entity.OrderItem;
import com.example.managementapi.Enum.OrderStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    @NonFinal
    @Value("${spring.mail.username}")
    private String email_name;

    private final SpringTemplateEngine templateEngine;

    private final JavaMailSender javaMailSender; // --> Class JavaMailSender chịu trách nhiệm cho việc send mail
    //? Load template từ resources
    //? Code này của GPT =)
    public String loadTemplate(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource(fileName);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    //? Function dùng để send mail otp
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

    public void sendOrderNotificationToAdmin(String adminEmail,
                                             GetOrderResponse order,
                                             String storeName,
                                             String orderManagementUrl,
                                             String adminName,
                                             String processingDeadline) {
        try {

            Context context = new Context();
            context.setVariable("adminName", adminName);
            context.setVariable("order", order);
            context.setVariable("storeName", storeName);
            context.setVariable("orderManagementUrl", orderManagementUrl);
            context.setVariable("processingDeadline", processingDeadline);


            String htmlContent = templateEngine.process("SendEmailToAdminToHandleCartForUser", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email_name);
            helper.setSubject("Thông Báo Đơn Hàng Mới Cần Xử Lý - " + order.getOrderCode());
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage());
        }
    }

    private void populateContext(Context context, GetOrderResponse orderResponse) {
        context.setVariable("orderCode", orderResponse.getOrderCode());
        context.setVariable("createAt", orderResponse.getCreateAt().toString());
        context.setVariable("status", orderResponse.getStatus().toString());
        context.setVariable("amount", orderResponse.getAmount());
        context.setVariable("paymentMethod", orderResponse.getPaymentMethod());
        context.setVariable("paymentStatus", orderResponse.getPaymentStatus());
        context.setVariable("email", orderResponse.getEmail());
        context.setVariable("phone", orderResponse.getPhone());
        context.setVariable("userAddress", orderResponse.getUserAddress());
        context.setVariable("shipAddress", orderResponse.getShipAddress());
        context.setVariable("orderItems", orderResponse.getOrderItems());
        context.setVariable("completeAt", orderResponse.getCompleteAt() != null ? orderResponse.getCompleteAt().toString() : null);
        context.setVariable("company_name", "Công Ty ABC");
        context.setVariable("support_email", "support@abc.com");
        context.setVariable("support_phone", "0123 456 789");
        context.setVariable("company_website", "www.abc.com");
    }

    public void sendOrderApprovedEmail(GetOrderResponse orderResponse) throws MessagingException {
        Context context = new Context();
        populateContext(context, orderResponse);

        String emailContent = templateEngine.process("SendEmailOrderSuccessfullyForm", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(orderResponse.getEmail());
        helper.setFrom("your-email@gmail.com");
        helper.setSubject("Thông báo đơn hàng được phê duyệt #" + orderResponse.getOrderCode());
        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }

    public void sendOrderCanceledEmail(GetOrderResponse orderResponse) throws MessagingException {
        Context context = new Context();
        populateContext(context, orderResponse);

        String emailContent = templateEngine.process("SendEmailOrderCanceled", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(orderResponse.getEmail());
        helper.setFrom("your-email@gmail.com");
        helper.setSubject("Thông báo hủy đơn hàng #" + orderResponse.getOrderCode());
        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }
}
