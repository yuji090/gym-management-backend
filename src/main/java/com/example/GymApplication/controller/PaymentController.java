package com.example.GymApplication.controller;

import com.example.GymApplication.dto.PaymentRequestDTO;
import com.example.GymApplication.dto.PaymentResponseDTO;
import com.example.GymApplication.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ------------------------------------------------------------
    // 1️⃣ RECORD PAYMENT
    // ------------------------------------------------------------
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> recordPayment(
            @Valid @RequestBody PaymentRequestDTO dto
    ) {
        PaymentResponseDTO response = paymentService.recordPayment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ------------------------------------------------------------
    // 2️⃣ GET PAYMENT HISTORY FOR MEMBER
    // ------------------------------------------------------------
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<PaymentResponseDTO>> getHistory(
            @PathVariable Long memberId
    ) {
        List<PaymentResponseDTO> payments = paymentService.getMemberPayments(memberId);
        return ResponseEntity.ok(payments);  // 200 OK
    }

    // ------------------------------------------------------------
    // 3️⃣ TOTAL COLLECTION FOR A GIVEN MONTH
    // ------------------------------------------------------------
    @GetMapping("/month/{month}/total")
    public ResponseEntity<Double> getMonthlyTotal(
            @PathVariable String month
    ) {
        Double total = paymentService.totalPaymentsForMonth(month);
        return ResponseEntity.ok(total);  // always returns 0 if none
    }

    // ------------------------------------------------------------
    // 4️⃣ GET ALL PAYMENTS FOR A GIVEN MONTH
    // ------------------------------------------------------------
    @GetMapping("/month/{month}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsForMonth(
            @PathVariable String month
    ) {
        List<PaymentResponseDTO> payments = paymentService.paymentsForMonth(month);
        return ResponseEntity.ok(payments);
    }
}
