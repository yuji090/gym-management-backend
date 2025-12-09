package com.example.GymApplication.service;

import com.example.GymApplication.dto.PaymentRequestDTO;
import com.example.GymApplication.dto.PaymentResponseDTO;
import com.example.GymApplication.model.Member;
import com.example.GymApplication.model.Payment;
import com.example.GymApplication.repository.MemberRepository;
import com.example.GymApplication.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final CurrentOwnerService currentOwnerService;

    public PaymentService(PaymentRepository paymentRepository,
                          MemberRepository memberRepository,
                          CurrentOwnerService currentOwnerService) {
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
        this.currentOwnerService = currentOwnerService;
    }


    private PaymentResponseDTO toDto(Payment p) {
        return PaymentResponseDTO.builder()
                .id(p.getId())
                .memberId(p.getMember().getId())
                .amount(p.getAmount())
                .paymentDate(p.getPaymentDate())
                .paidForMonth(p.getPaidForMonth())
                .build();
    }



    private LocalDate computeExpiry(LocalDate paymentDate, String paidForMonth, LocalDate manualExpiry, Integer monthsPaid) {
        if (manualExpiry != null) return manualExpiry;



        if (monthsPaid != null && monthsPaid > 0) {
            return paymentDate.plusMonths(monthsPaid).minusDays(1);
        }



        return paymentDate.plusMonths(1).minusDays(1);
    }

    @Transactional
    public PaymentResponseDTO recordPayment(PaymentRequestDTO dto) {
        Long ownerId = currentOwnerService.getCurrentOwnerId();

        // ✅ member must belong to current owner
        Member member = memberRepository.findByIdAndOwnerId(dto.getMemberId(), ownerId)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));

        LocalDate paymentDate = LocalDate.now();

        Payment p = Payment.builder()
                .member(member)
                .amount(dto.getAmount())
                .paymentDate(paymentDate)
                .paidForMonth(dto.getPaidForMonth())
                .build();

        Payment saved = paymentRepository.save(p);

        LocalDate expiry = computeExpiry(paymentDate, dto.getPaidForMonth(), dto.getManualExpiry(), dto.getMonthsPaid());
        member.setActive(true);
        member.setLastPayment(paymentDate);
        member.setMembershipExpiry(expiry);
        memberRepository.save(member);

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getMemberPayments(Long memberId) {
        Long ownerId = currentOwnerService.getCurrentOwnerId();

        // security check
        memberRepository.findByIdAndOwnerId(memberId, ownerId)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));

        return paymentRepository.findByMemberIdOrderByPaymentDateDesc(memberId)
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Double totalPaymentsForMonth(String month) {
        Long ownerId = currentOwnerService.getCurrentOwnerId();
        Double total = paymentRepository.totalAmountForMonthAndOwner(month, ownerId);
        return total == null ? 0.0 : total;
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> paymentsForMonth(String month) {
        Long ownerId = currentOwnerService.getCurrentOwnerId();
        return paymentRepository.findByPaidForMonthAndMemberOwnerId(month, ownerId)
                .stream().map(this::toDto).toList();
    }
}




/*

  private PaymentResponseDTO toDto(Payment p) {
        return PaymentResponseDTO.builder()
                .id(p.getId())
                .memberId(p.getMember().getId())
                .amount(p.getAmount())
                .paymentDate(p.getPaymentDate())
                .paidForMonth(p.getPaidForMonth())
                .build();
    }



private LocalDate computeExpiry(LocalDate paymentDate, String paidForMonth, LocalDate manualExpiry, Integer monthsPaid) {
    if (manualExpiry != null) return manualExpiry;



    if (monthsPaid != null && monthsPaid > 0) {
        return paymentDate.plusMonths(monthsPaid).minusDays(1);
    }



    return paymentDate.plusMonths(1).minusDays(1);
}




 */