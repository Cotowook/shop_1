package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.dto.ResponseDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@CrossOrigin
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/new")
    public ResponseDto createMember(@Valid @RequestBody MemberFormDto memberFormDto) {
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
            return new ResponseDto("success", "회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return new ResponseDto("error", "회원가입에 실패하였습니다.");
        }
    }


}
