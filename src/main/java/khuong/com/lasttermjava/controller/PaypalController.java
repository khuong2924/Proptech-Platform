package khuong.com.lasttermjava.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import khuong.com.lasttermjava.service.PaypalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;

    @PostMapping("/payment/create")
    public RedirectView createPayment(@RequestParam("total") String amount) {
        try {
            // Kiểm tra nếu amount null hoặc rỗng
            if (amount == null || amount.trim().isEmpty()) {
                log.error("Amount is empty or null.");
                return new RedirectView("/payment/error");
            }

            Double total;
            try {
                String sanitizedAmount = amount.replace(".", "").replace(",", ".");
                total = Double.valueOf(sanitizedAmount);

                if (total <= 0) {
                    log.error("Invalid total amount: {}. Amount must be greater than 0.", amount);
                    return new RedirectView("/payment/error");
                }
            } catch (NumberFormatException e) {
                log.error("Invalid total amount provided: {}", amount);
                return new RedirectView("/payment/error");
            }

            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";

            // Gọi service để tạo payment
            Payment payment = paypalService.createPayment(total, "USD", cancelUrl, successUrl);

            // Tìm kiếm và chuyển hướng đến approval URL
            for (Links links : payment.getLinks()) {
                if ("approval_url".equals(links.getRel())) {
                    return new RedirectView(links.getHref());
                }
            }

            log.error("Approval URL not found in payment response.");
        } catch (PayPalRESTException e) {
            log.error("PayPal REST Exception occurred:", e);
        }

        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);

            if ("approved".equalsIgnoreCase(payment.getState())) {
                return "payment-success";
            } else {
                log.warn("Payment not approved. Current state: {}", payment.getState());
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal REST Exception occurred while executing payment:", e);
        }

        return "payment-error";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        log.info("Payment canceled by user.");
        return "payment-error";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        log.info("Redirected to error page.");
        return "payment-error";
    }
}
