package com.teamshyt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamshyt.model.Email;
import com.teamshyt.model.VerificationType;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findFirstByEmailAndTypeOrderByExpiryTimeDesc(String email, VerificationType type);
    // this query will give us the newest otp
    // first it will fetch all otp of the email with that type and then order them
    // in descending and then provide the first(latest) one
}
