package com.example.GymApplication.dto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class MemberUpdateDTO {
    private Boolean active;          // optional manual override
    private LocalDate membershipExpiry; // optional manual expiry set
}
