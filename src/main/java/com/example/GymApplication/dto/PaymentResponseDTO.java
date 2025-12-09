package com.example.GymApplication.dto;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentResponseDTO {
    private Long id;
    private Long memberId;
    private double amount;
    private LocalDate paymentDate;
    private String paidForMonth;
}
