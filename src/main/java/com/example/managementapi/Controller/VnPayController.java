package com.example.managementapi.Controller;


import ch.qos.logback.core.model.Model;
import com.example.managementapi.Service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("api/v1/vn-pay")
@RequiredArgsConstructor
public class VnPayController {
    private final VnPayService vnPayService;

//    @GetMapping("/submitOrder")
//    public ResponseEntity<?> submidOrder() throws UnsupportedEncodingException {
////        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        return vnPayService.createOrder();
//
//    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> paymentReturn(HttpServletRequest request) {
        int result = vnPayService.orderReturn(request);
        switch (result) {
            case 1:
                return ResponseEntity.ok("Thanh toán thành công!");
            case 0:
                return ResponseEntity.ok("Thanh toán thất bại!");
            default:
                return ResponseEntity.badRequest().body("Sai chữ ký, nghi ngờ giả mạo!");
        }
    }

//    @GetMapping("/vnpay-payment")
//    public String GetMapping(HttpServletRequest request, Model model){
//        int paymentStatus =vnPayService.orderReturn(request);
//
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//
//        model.addAttribute("orderId", orderInfo);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("paymentTime", paymentTime);
//        model.addAttribute("transactionId", transactionId);
//
//        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
//        }
}
