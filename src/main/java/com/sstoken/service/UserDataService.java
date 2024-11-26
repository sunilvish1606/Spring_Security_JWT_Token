package com.sstoken.service;

import com.sstoken.entity.UserData;
import com.sstoken.payload.LoginDto;
import com.sstoken.payload.TokenDto;
import com.sstoken.repository.UserDataRepository;
import com.sstoken.utility.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDataService {

    private UserDataRepository userDataRepository;
    private JwtUtils jwtUtils;

    public UserDataService(UserDataRepository userDataRepository, JwtUtils jwtUtils) {
        this.userDataRepository = userDataRepository;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<String> signUp(UserData userData) {
        // Checking username is existence
        Optional<UserData> opByUsername = userDataRepository.findByUsername(userData.getUsername());
        if (opByUsername.isPresent()) {
            return new ResponseEntity<>("Username already taken", HttpStatus.CONFLICT);
        }
        // Checking email is existence
        Optional<UserData> opByEmail = userDataRepository.findByEmail(userData.getEmail());
        if (opByEmail.isPresent()) {
            return new ResponseEntity<>("Email already taken", HttpStatus.CONFLICT);
        }

        // Encrypt the Password
        userData.setPassword(BCrypt.hashpw(userData.getPassword(), BCrypt.gensalt(5)));

        // If both username and email are unique, then save the user
        userDataRepository.save(userData);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(LoginDto loginDto) {
        // Finding user by username
        Optional<UserData> opByUsername = userDataRepository.findByUsername(loginDto.getUsername());
        if (opByUsername.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        UserData userData = opByUsername.get();

        // Checking password
        if (BCrypt.checkpw(loginDto.getPassword(), userData.getPassword())) {
            String jwtToken = jwtUtils.generateToken(loginDto.getUsername());
            // Returning JWT token as response
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(jwtToken);
            tokenDto.setType("JWT");
            return new ResponseEntity<>(tokenDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.FORBIDDEN);
    }
}