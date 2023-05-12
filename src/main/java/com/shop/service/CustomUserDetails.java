package com.shop.service;

import com.shop.entity.Member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static CustomUserDetails build(Member member) {
        // 사용자의 권한 정보를 생성
        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().toString());
        // 권한 정보를 리스트로 포장하여 생성
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        return new CustomUserDetails(member.getEmail(), member.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
    // 아래의 메서드는 추가적인 정보를 제공하고자 할 때 사용할 수 있습니다.
    // 해당 예시에서는 사용하지 않는 정보이므로 간단히 반환하도록 구현합니다.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

