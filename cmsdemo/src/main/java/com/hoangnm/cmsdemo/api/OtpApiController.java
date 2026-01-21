package com.hoangnm.cmsdemo.api;

import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.UserRepository;
import com.hoangnm.cmsdemo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/otp")
public class OtpApiController {

    private static final long OTP_COOLDOWN_SECONDS = 60;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/resend")
    public ResponseEntity<?> resendOtp(@RequestParam("email") String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email not found."));
        }

        User user = userOptional.get();

        // --- LOGIC COOLDOWN ---
        if (user.getOtpRequestedTime() != null) {
            long secondsSinceLastRequest = Duration.between(user.getOtpRequestedTime(), LocalDateTime.now()).getSeconds();
            if (secondsSinceLastRequest < OTP_COOLDOWN_SECONDS) {
                long secondsToWait = OTP_COOLDOWN_SECONDS - secondsSinceLastRequest;
                return ResponseEntity.badRequest().body(Map.of("message", "Please wait " + secondsToWait + " seconds."));
            }
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        
        // Lấy token hiện tại để giữ nguyên
        String currentToken = user.getResetToken();
        
        // Cập nhật OTP và giữ nguyên token cũ
        userRepository.updateOtp(otp, LocalDateTime.now(), currentToken, email);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Error sending email."));
        }

        return ResponseEntity.ok(Map.of("message", "A new OTP has been sent."));
    }
}
