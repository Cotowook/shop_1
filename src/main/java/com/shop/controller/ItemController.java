package com.shop.controller;

import com.shop.service.ItemService;
import com.shop.dto.ItemFormDto;
import com.shop.config.JwtTokenUtil;
import com.shop.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import com.shop.service.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
import java.util.List;
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberService memberService;

    @PostMapping(value = "/item/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> itemNew(HttpServletRequest request, @Valid ItemFormDto itemFormDto,
                                          BindingResult bindingResult, Model model,
                                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        String token = request.getHeader("Authorization");
        String username = jwtTokenUtil.getUsernameFromToken(token);
        CustomUserDetails userDetails = (CustomUserDetails) memberService.loadUserByUsername(username);

        if (!jwtTokenUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("유효하지 않은 토큰입니다.");
        }
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("관리자 권한이 필요합니다.");
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("첫번째 상품 이미지는 필수 입력 값입니다.");
        }
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
            return ResponseEntity.ok("상품이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 등록 중 에러가 발생하였습니다.");
        }
    }

    /*
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "localhost:3000/itemForm";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "localhost:3000/itemForm";
        }
        return "redirect:localhost:3000";
    }
    */
}