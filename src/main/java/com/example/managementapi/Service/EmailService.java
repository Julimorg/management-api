package com.example.managementapi.Service;


import com.example.managementapi.Dto.Email.MailBody;
import com.example.managementapi.Dto.Response.Order.GetOrderResponse;
import com.example.managementapi.Entity.OrderItem;
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

    public void sendOrderStatusEmail(String to, String customerName, String orderId, String orderDate,
                                     String orderStatus, List<OrderItem> orderItems, String orderDetails,
                                     String shippingAddress, String estimatedDeliveryDate,
                                     String companyName, String supportEmail, String supportPhone,
                                     String companyWebsite) throws MessagingException {

        Context context = new Context();
        context.setVariable("customer_name", customerName);
        context.setVariable("order_id", orderId);
        context.setVariable("order_date", orderDate);
        context.setVariable("order_status", orderStatus);
        context.setVariable("order_items", orderItems);
        context.setVariable("order_details", orderDetails);
        context.setVariable("shipping_address", shippingAddress);
        context.setVariable("estimated_delivery_date", estimatedDeliveryDate);
        context.setVariable("company_name", companyName);
        context.setVariable("support_email", supportEmail);
        context.setVariable("support_phone", supportPhone);
        context.setVariable("company_website", companyWebsite);

        String emailContent = templateEngine.process("TestForm", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setFrom("kienphongtran2003@gmail.com");
        helper.setSubject("Cập nhật tình trạng đơn hàng #" + orderId);
        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }

}
