package com.example.GymApplication.service;

import com.example.GymApplication.dto.*;
import com.example.GymApplication.model.*;
import com.example.GymApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// Single Responsibility: this service only handles Member-related operations.
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final CurrentOwnerService currentOwnerService;

    public MemberService(MemberRepository memberRepository,
                         PaymentRepository paymentRepository,
                         CurrentOwnerService currentOwnerService) {
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
        this.currentOwnerService = currentOwnerService;
    }

    private MemberResponseDTO toResponse(Member m) {
        return MemberResponseDTO.builder()
                .id(m.getId())
                .name(m.getName())
                .email(m.getEmail())
                .phone(m.getPhone())
                .joinDate(m.getJoinDate())
                .lastPayment(m.getLastPayment())
                .active(m.isActive())
                .membershipExpiry(m.getMembershipExpiry())
                .ownerId(m.getOwner().getId())
                .build();
    }

    @Transactional
    public MemberResponseDTO createMember(MemberRequestDTO dto) {
        Owner owner = currentOwnerService.getCurrentOwner();

        Member m = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .joinDate(dto.getJoinDate() == null ? LocalDate.now() : dto.getJoinDate())
                .active(true)
                .owner(owner)        // 👈 IMPORTANT
                .build();

        Member saved = memberRepository.save(m);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO getMember(Long id) {
        Long ownerId = currentOwnerService.getCurrentOwnerId();
        Member m = memberRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return toResponse(m);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> listAll() {
        Long ownerId = currentOwnerService.getCurrentOwnerId();
        return memberRepository.findByOwnerId(ownerId)
                .stream().map(this::toResponse).toList();
    }

    // members who did NOT pay for a given month (only current owner's members)
    @Transactional(readOnly = true)
    public List<MemberResponseDTO> findMembersNotPaidForMonth(String month) {
        Long ownerId = currentOwnerService.getCurrentOwnerId();

        List<Member> all = memberRepository.findByOwnerId(ownerId);
        List<Long> ids = all.stream().map(Member::getId).toList();

        List<Payment> paid = ids.isEmpty()
                ? List.of()
                : paymentRepository.findByMemberIdInAndPaidForMonth(ids, month);

        Set<Long> paidMemberIds = paid.stream()
                .map(p -> p.getMember().getId())
                .collect(Collectors.toSet());

        return all.stream()
                .filter(m -> !paidMemberIds.contains(m.getId()) && m.isActive())
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deactivate(long id){
        Long ownerId = currentOwnerService.getCurrentOwnerId();
        Member m = memberRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        m.setActive(false);
    }

    @Transactional
    public void activate(long id){
        Long ownerId = currentOwnerService.getCurrentOwnerId();
        Member m = memberRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        m.setActive(true);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> nonActive(){
        Long ownerId = currentOwnerService.getCurrentOwnerId();
        return memberRepository.findByOwnerIdAndActiveFalse(ownerId)
                .stream().map(this::toResponse).toList();
    }
}
