package com.example.GymApplication.repository;

import com.example.GymApplication.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByActiveTrueAndMembershipExpiryBefore(LocalDate date);

    List<Member> findByOwnerId(Long ownerId);

    Optional<Member> findByIdAndOwnerId(Long id, Long ownerId);

    List<Member> findByOwnerIdAndActiveFalse(Long ownerId);
}
