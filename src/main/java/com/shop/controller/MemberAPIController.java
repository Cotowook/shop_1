package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MemberService;
import com.shop.config.JwtTokenUtil;
import com.shop.service.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberAPIController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/new")
    public ResponseEntity<?> createMember(@RequestBody MemberFormDto memberFormDto) {
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입에 실패하였습니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberFormDto memberFormDto) {
        try {
            Member member = memberRepository.findByEmail(memberFormDto.getEmail());

            if (member == null || !passwordEncoder.matches(memberFormDto.getPassword(), member.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }

            CustomUserDetails userDetails = CustomUserDetails.build(member);
            String token = jwtTokenUtil.generateToken(userDetails);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            return ResponseEntity.ok().headers(headers).body("로그인이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인에 실패하였습니다.");
        }
    }
}