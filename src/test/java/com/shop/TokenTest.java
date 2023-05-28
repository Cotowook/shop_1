package com.shop;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class TokenTest {
    public static void main(String[] args) {
        // 테스트용 사용자 정보 생성
        String email = "test@example.com";
        String password = "password";
        // 토큰 생성
        CustomUserDetails userDetails = new CustomUserDetails(email, password, null);

        // 토큰 생성
        String token = generateToken(userDetails);

        // 생성된 토큰 출력
        System.out.println("Token: " + token);
    }
    private static String generateToken(CustomUserDetails userDetails) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 안전한 키 생성

        long expiration = 86400000; // 토큰 만료 시간 (24시간)

        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }
    private static class CustomUserDetails {
        private String email;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;

        public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities) {
            this.email = email;
            this.password = password;
            this.authorities = authorities;
        }
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }
        public String getPassword() {
            return password;
        }
        public String getUsername() {
            return email;
        }
        public boolean isAccountNonExpired() {
            return true;
        }
        public boolean isAccountNonLocked() {
            return true;
        }
        public boolean isCredentialsNonExpired() {
            return true;
        }
        public boolean isEnabled() {
            return true;
        }
    }
}
