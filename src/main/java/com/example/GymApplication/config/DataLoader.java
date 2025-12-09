package com.example.GymApplication.config;

import com.example.GymApplication.model.Owner;
import com.example.GymApplication.repository.OwnerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!ownerRepository.existsByEmail("owner1@gym.com")) {
            Owner o = Owner.builder()
                    .name("Owner One")
                    .email("owner1@gym.com")
                    .password(passwordEncoder.encode("owner1pass"))
                    .build();
            ownerRepository.save(o);
        }
        if (!ownerRepository.existsByEmail("owner2@gym.com")) {
            Owner o2 = Owner.builder()
                    .name("Owner Two")
                    .email("owner2@gym.com")
                    .password(passwordEncoder.encode("owner2pass"))
                    .build();
            ownerRepository.save(o2);
        }
        if (!ownerRepository.existsByEmail("a@a")) {
            Owner o2 = Owner.builder()
                    .name("Owner three")
                    .email("a@a")
                    .password(passwordEncoder.encode("a"))
                    .build();
            ownerRepository.save(o2);
        }
    }
}
