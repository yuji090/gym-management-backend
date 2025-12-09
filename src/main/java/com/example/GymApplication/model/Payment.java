package com.example.GymApplication.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // when payment was received
    private LocalDate paymentDate;

    // amount paid
    private double amount;

    // which month the payment is for (e.g., 2025-11)
    // store as string "YYYY-MM" for simplicity
    private String paidForMonth;

    // due date if you want invoice-style (optional)
    private LocalDate dueDate;
}
