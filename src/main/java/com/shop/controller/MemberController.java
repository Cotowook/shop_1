package com.shop.controller;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MemberService;
import com.shop.service.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import javax.validation.Valid;
import org.springframework.ui.Model;
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

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

            if (member == null || !member.getPassword().equals(memberFormDto.getPassword())) {
                throw new Exception("Invalid credentials");
            }

            UserDetails userDetails = memberService.loadUserByUsername(member.getEmail());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("로그인이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인에 실패하였습니다.");
        }
    }
    /*
    @GetMapping("/login")
    public String getLogin() { return ""; }
    @GetMapping("/login/error")
    public String getLoginError() { return "Login error"; }
    */
}