package com.teamshyt.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamshyt.model.Email;
import com.teamshyt.model.VerificationType;
import com.teamshyt.repo.EmailRepository;

@Service
public class EmailService {
    private final EmailOTPSendService emailOTPSendService;
    private final EmailRepository emailRepository;
    private final Random random = new Random();

    public EmailService(EmailOTPSendService emailOTPSendService, EmailRepository emailRepository) {
        this.emailOTPSendService = emailOTPSendService;
        this.emailRepository = emailRepository;
    }

    @Value("${OTP_LENGTH}")
    private int otpLength;

    @Value("${OTP_EXPIRY}")
    private int otpExpiryMinutes;

    public String generateOtp() {
        int lowValue = (int) Math.pow(10, Math.max(1, otpLength - 1));
        int maxValue = (int) Math.pow(10, otpLength) - 1;
        int n = lowValue + random.nextInt(maxValue - lowValue + 1);
        return String.valueOf(n);
    }

    @Transactional
    public void createAndSendOtp(String email, VerificationType type) {
        String otp = generateOtp();
        Email emailObject = new Email();
        emailObject.setEmail(email);
        emailObject.setOtp(otp);
        emailObject.setType(type);
        emailObject.setExpiryTime(LocalDateTime.now().plusMinutes(otpExpiryMinutes));
        emailObject.setConsumed(false);

        emailRepository.save(emailObject);

        String subject = (type == VerificationType.VERIFY) ? "Verify Your Email" : "Password reset code";
        emailOTPSendService.sendOtp(email, subject, otp);
    }

    @Transactional
    public boolean validateOtp(String email, String otp, VerificationType type) {
        Optional<Email> optional = emailRepository.findFirstByEmailAndTypeOrderByExpiryTimeDesc(email, type);

        if (optional.isEmpty())
            return false;

        Email emailObject = optional.get();
        if (emailObject.isConsumed())
            return false;
        if (emailObject.getExpiryTime().isBefore(LocalDateTime.now()))
            return false;
        if (!emailObject.getOtp().equals(otp))
            return false;
        emailObject.setConsumed(true); // mark consumed - otp used
        emailRepository.save(emailObject);
        return true;
    }

    @Transactional
    public void resendOtp(String email, VerificationType type) {
        createAndSendOtp(email, type);
    }
}
