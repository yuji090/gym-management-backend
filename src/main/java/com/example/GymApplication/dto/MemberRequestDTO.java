package com.example.GymApplication.dto;

import lombok.*;

import java.time.LocalDate;

@Data
public class MemberRequestDTO{
    private String name;
    private String email;
    private String phone;
    private LocalDate joinDate;

}