package com.example.GymApplication.security;

import com.example.GymApplication.model.Owner;
import com.example.GymApplication.repository.OwnerRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class OwnerDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;
    public OwnerDetailsService(OwnerRepository ownerRepository) { this.ownerRepository = ownerRepository; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Owner o = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Owner not found: " + email));
        return new AppOwnerDetails(o);
    }
}
