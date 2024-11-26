package com.sstoken.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private long expirationTime;

    private Algorithm algorithm;

    @PostConstruct
    public void postConstruct() throws UnsupportedEncodingException {
        // Initialize JWT algorithm and issuer
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(String username) {
        // Generate JWT token with username, issuer, and expiration time
        return JWT.create()
                .withClaim("name", username)
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    public String getUsername(String jwtToken) {
        // Verify and extract username from the provided JWT token
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(jwtToken);
        return decodedJWT.getClaim("name").asString();
    }
}