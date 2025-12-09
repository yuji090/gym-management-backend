package com.example.GymApplication.dto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long memberId;
    private LocalDate paymentDate; // optional - default to LocalDate.now() in service
    private double amount;
    private String paidForMonth; // "YYYY-MM" e.g. "2025-11" - optional
    private LocalDate manualExpiry; // optional - if provided, use this as expiry instead of default +1 month
    private Integer monthsPaid;
}
