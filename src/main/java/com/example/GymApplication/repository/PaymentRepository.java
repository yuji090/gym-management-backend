package com.example.GymApplication.repository;

import com.example.GymApplication.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // <-- add this
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByMemberIdOrderByPaymentDateDesc(Long memberId);

    List<Payment> findByPaidForMonth(String paidForMonth);

    @Query("SELECT COALESCE(SUM(p.amount),0) " +
            "FROM Payment p " +
            "WHERE p.paidForMonth = :month AND p.member.owner.id = :ownerId")
    Double totalAmountForMonthAndOwner(@Param("month") String month, @Param("ownerId") Long ownerId);

    List<Payment> findByPaidForMonthAndMemberOwnerId(String paidForMonth, Long ownerId);

    List<Payment> findByMemberIdInAndPaidForMonth(List<Long> memberIds, String paidForMonth);
}
