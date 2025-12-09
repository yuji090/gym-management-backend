package com.example.GymApplication.controller;

import com.example.GymApplication.dto.AuthRequestDTO;
import com.example.GymApplication.dto.AuthResponseDTO;
import com.example.GymApplication.model.Owner;
import com.example.GymApplication.repository.OwnerRepository;
import com.example.GymApplication.security.AppOwnerDetails;
import com.example.GymApplication.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OwnerRepository ownerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          OwnerRepository ownerRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequestDTO req) {
        if (ownerRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already used");
        }

        Owner o = Owner.builder()
                .email(req.getEmail())
                .name("Owner") // ya req.getName() agar DTO me name add kare
                .password(passwordEncoder.encode(req.getPassword()))
                .build();

        ownerRepository.save(o);
        return ResponseEntity.status(201).body("Owner registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            AppOwnerDetails ownerDetails = (AppOwnerDetails) auth.getPrincipal();
            String token = jwtUtils.generateToken(ownerDetails);

            return ResponseEntity.ok(new AuthResponseDTO(token, "Bearer"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        return ResponseEntity.ok("Valid token");
    }

    @GetMapping("/list")
    public List<Owner> ak(){
        return ownerRepository.findAll();
    }
}
