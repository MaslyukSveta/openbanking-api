package com.example.openbanking.controller;

import com.example.openbanking.model.Payment;
import com.example.openbanking.service.PaymentService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling payment operations.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Constructor for PaymentController.
     * @param paymentService Service for handling payment operations.
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Initiates a new payment.
     * @param payment The payment details.
     * @return A message indicating the result of the payment initiation.
     */
    @PostMapping("/initiate")
    public String initiatePayment(@RequestBody Payment payment) {
        return paymentService.initiatePayment(payment);
    }
}
