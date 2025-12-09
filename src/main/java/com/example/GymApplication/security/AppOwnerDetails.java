package com.example.GymApplication.security;

import com.example.GymApplication.model.Owner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Set;

public class AppOwnerDetails implements UserDetails {
    private final Owner owner;
    public AppOwnerDetails(Owner owner) { this.owner = owner; }

    // simple: owner has a single authority so Spring sees them authenticated
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_OWNER"));
    }
    @Override public String getPassword() { return owner.getPassword(); }
    @Override public String getUsername() { return owner.getEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // convenience
    public Long getId() { return owner.getId(); }
    public String getName() { return owner.getName(); }
    public Owner getOwner() { return owner; }
}
