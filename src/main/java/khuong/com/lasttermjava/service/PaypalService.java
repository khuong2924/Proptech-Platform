package khuong.com.lasttermjava.service;


import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;

    public Payment createPayment(Double total, String currency, String cancelUrl, String successUrl) throws PayPalRESTException {
        // Kiểm tra nếu total không hợp lệ
        if (total == null || total <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than 0");
        }

        // Đảm bảo rằng apiContext được cấu hình đúng
        if (apiContext == null) {
            throw new IllegalStateException("APIContext is not configured properly.");
        }

        // Tạo Amount cho giao dịch
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total)); // Đảm bảo format 2 chữ số thập phân

        // Tạo Transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Payment using PayPal");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Tạo Payer với phương thức thanh toán là PayPal
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Tạo Payment
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Thiết lập RedirectUrls cho các đường dẫn success và cancel
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // Tạo payment từ PayPal
        try {
            return payment.create(apiContext);
        } catch (PayPalRESTException e) {
            throw new PayPalRESTException("Error creating PayPal payment: " + e.getMessage(), e);
        }
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        // Kiểm tra xem paymentId và payerId có hợp lệ không
        if (paymentId == null || payerId == null) {
            throw new IllegalArgumentException("Payment ID and Payer ID must not be null");
        }

        // Tạo Payment từ paymentId
        Payment payment = new Payment();
        payment.setId(paymentId);

        // Tạo PaymentExecution để thực hiện thanh toán
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            return payment.execute(apiContext, paymentExecution);
        } catch (PayPalRESTException e) {
            throw new PayPalRESTException("Error executing PayPal payment: " + e.getMessage(), e);
        }
    }
}
