package com.joo.digimon.user.controller;

import com.joo.digimon.user.dto.LoginResponseDto;
import com.joo.digimon.user.dto.UsernameLoginRequestDto;
import com.joo.digimon.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/admin-api/account")
@RequiredArgsConstructor
@Validated
public class AdminAccountController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody UsernameLoginRequestDto usernameLoginRequestDto, HttpServletResponse response) throws IOException {
        LoginResponseDto loginResponseDto = userService.usernameLogin(usernameLoginRequestDto);
        setAdminTokenCookie(response, loginResponseDto);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(HttpServletResponse response) {
        invalidateAdminTokenCookie(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static void setAdminTokenCookie(HttpServletResponse response, LoginResponseDto loginResponseDto) {
        Cookie jwtCookie = new Cookie("ADMIN_JWT_TOKEN", loginResponseDto.getAccessToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(60 * 60 * 24);
        jwtCookie.setPath("/admin-api");
        response.addCookie(jwtCookie);
    }

    private static void invalidateAdminTokenCookie(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("ADMIN_JWT_TOKEN", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/admin-api");
        response.addCookie(jwtCookie);
    }
}
