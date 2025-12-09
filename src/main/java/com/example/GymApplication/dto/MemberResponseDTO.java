package com.example.GymApplication.dto;

import java.time.LocalDate;
import lombok.*;

@Data @Builder // @Getter @Setter @NoArgsConstructor @AllArgsConstructor  " already added through @Data annotation"
public class MemberResponseDTO{
    private Long id;
    private String name;
    private String phone;
    private String email;
    private LocalDate joinDate;
    private boolean active;
    private LocalDate  membershipExpiry;
    private LocalDate lastPayment;
    private Long ownerId;
}
