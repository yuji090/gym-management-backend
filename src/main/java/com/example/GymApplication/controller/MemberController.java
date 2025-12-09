package com.example.GymApplication.controller;

import com.example.GymApplication.dto.*;
import com.example.GymApplication.model.Member;
import com.example.GymApplication.repository.MemberRepository;
import com.example.GymApplication.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    public MemberController(MemberService memberService) { this.memberService = memberService; }

    @PostMapping
    public MemberResponseDTO create(@RequestBody MemberRequestDTO dto) {
        return memberService.createMember(dto);
    }

    @GetMapping("/{id}")
    public MemberResponseDTO get(@PathVariable Long id) {
        return memberService.getMember(id);
    }

    @GetMapping
    public List<MemberResponseDTO> listAll() {
        return memberService.listAll();
    }

    // list members who did NOT pay for given month
    @GetMapping("/unpaid")
    public List<MemberResponseDTO> unpaidForMonth(@RequestParam String month /* "YYYY-MM" */) {
        return memberService.findMembersNotPaidForMonth(month);
    }


    @PutMapping("/deactivate/{id}")
    public void deactivate(@PathVariable Long id) {
      memberService.deactivate(id);
    }


    @PutMapping("/activate/{id}")
    public void activate(@PathVariable Long id){
        memberService.activate(id);
    }

    // list of non active members and due date
    @GetMapping("not-active")
    public List<MemberResponseDTO> nonActive(){
        return  memberService.nonActive();
    }
}
