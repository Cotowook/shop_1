package com.shop.config;

import com.shop.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    MemberService memberService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.cors().and().csrf()
                .ignoringAntMatchers("/api/members/new", "/api/members/login",
                        "/api/items", "/api/items/{id}",
                        "/api/image", "/api/image/{itemId}/{imageId}",
                        "/api/search", "api/search/{itemName}",
                        "/api/community/posts", "/api/community/posts/{id}"
                        );

        http.authorizeRequests()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/api/members/new", "/api/members/login").permitAll()
                .antMatchers("/api/items", "/api/items/{id}").permitAll()
                .antMatchers("/api/image", "/api/image/{itemId}/{imageId}").permitAll()
                .antMatchers("/api/search", "/api/search/{itemName}").permitAll()
                .antMatchers("/api/community/posts", "/api/community/posts/{id}").permitAll()

                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .mvcMatchers("/","/members/**","/item/**","/items/**","/image/**","/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

