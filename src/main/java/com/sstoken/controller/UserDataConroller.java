package com.sstoken.controller;

import com.sstoken.entity.UserData;
import com.sstoken.payload.LoginDto;
import com.sstoken.service.UserDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserDataConroller {

    private UserDataService userDataService;

    public UserDataConroller(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    // http://localhost:8080/api/v1/user/signup/user
    @PostMapping("/signup/user")
    public ResponseEntity<String> signUpUser(
            @RequestBody UserData userData
    ) {
        userData.setRole("ROLE_USER");
        return userDataService.signUp(userData);
    }

    // http://localhost:8080/api/v1/user/signup/admin
    @PostMapping("/signup/admin")
    public ResponseEntity<String> signUpAdmin(
            @RequestBody UserData userData
    ) {
        userData.setRole("ROLE_ADMIN");
        return userDataService.signUp(userData);
    }

    // http://localhost:8080/api/v1/user/signup/owner
    @PostMapping("/signup/owner")
    public ResponseEntity<String> signUpOwner(
            @RequestBody UserData userData
    ) {
        userData.setRole("ROLE_OWNER");
        return userDataService.signUp(userData);
    }

    // http://localhost:8080/api/v1/user/login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto loginDto
    ) {
        return userDataService.login(loginDto);
    }
}
