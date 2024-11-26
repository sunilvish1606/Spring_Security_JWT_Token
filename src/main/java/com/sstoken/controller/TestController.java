package com.sstoken.controller;

import com.sstoken.entity.UserData;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    // http://localhost:8080/api/v1/test/user/test
    @PostMapping("/user/test")
    public String testUser(@AuthenticationPrincipal UserData userData) {
        return userData.getUsername() + " User Test Successful";
    }

    // http://localhost:8080/api/v1/test/admin/test
    @PostMapping("/admin/test")
    public String testAdmin(@AuthenticationPrincipal UserData userData) {
        return userData.getUsername() + " Admin Test Successful";
    }

    // http://localhost:8080/api/v1/test/owner/test
    @PostMapping("/owner/test")
    public String testOwner(@AuthenticationPrincipal UserData userData) {
        return userData.getUsername() + " Owner Test Successful";
    }

    // http://localhost:8080/api/v1/test/home/test
    @PostMapping("/home/test")
    public String testHome() {
        return "Home Test Successful";
    }
}