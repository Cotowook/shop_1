package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
@Getter @Setter
public class MemberFormDto {
    @NotBlank
    private String name;
    @NotEmpty
    @Email
    public String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String address;
}
