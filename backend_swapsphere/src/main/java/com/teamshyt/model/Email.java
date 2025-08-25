package com.teamshyt.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
// import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
// @Builder
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otp; // 4 digits random - use String to preserve leading zeroes if any presemt like
                        // 0045 since it can be treated as 45 when fetched from DB

    @Column(nullable = false)
    private LocalDateTime expiryTime; // time for which otp will be valid

    @Column(nullable = false)
    private VerificationType type; // Verify or Reset -- since verify for email verification and reset for pass reset

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private boolean consumed = false;// otp will be only used for once - when it is used it will mark true for consumed
}
