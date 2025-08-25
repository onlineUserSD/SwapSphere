package com.teamshyt.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.teamshyt.dto.JWTResponse;
import com.teamshyt.dto.LoginRequest;
import com.teamshyt.dto.RegisterRequest;
import com.teamshyt.model.User;
import com.teamshyt.model.UserRole;
import com.teamshyt.repo.UserRepository;
import com.teamshyt.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository urepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    // register user
    public String register(RegisterRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new RuntimeException("Email and Password required.");
        }

        if (urepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        if (urepo.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone Number already registered.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));

        user.setAddressLine1(request.getAddressLine1());
        user.setAddressLine2(request.getAddressLine2()); // optional
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setPincode(request.getPincode());

        user.setEnabled(false); // login disable until email verified with otp -- true for verified account and false for not verified account
        urepo.save(user);

        return "User registered successfully";
    }

    // login user -- > token + role
    public JWTResponse login(LoginRequest request) {
        Optional<User> optional = urepo.findByEmail(request.getEmail());
        if (optional.isEmpty()) {
            throw new RuntimeException("Invalid Credentials");
        }
        User user = optional.get();
        if (!user.isEnabled()) {
            throw new RuntimeException("Please verify your email before logging in.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return JWTResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .build();
    }
}
