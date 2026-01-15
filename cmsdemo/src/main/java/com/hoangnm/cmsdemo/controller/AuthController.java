package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.UserRepository;
import com.hoangnm.cmsdemo.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class AuthController {

    private static final long OTP_VALID_DURATION_MINUTES = 5;
    private static final long OTP_COOLDOWN_SECONDS = 60;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping("/complete-registration")
    public String showCompleteRegistrationForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "complete_registration";
    }

    @PostMapping("/complete-registration")
    public String processCompleteRegistration(@RequestParam String email,
                                              @RequestParam String username,
                                              @RequestParam String password,
                                              @RequestParam String confirmPassword,
                                              HttpServletRequest request,
                                              RedirectAttributes redirectAttributes) {
        
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
            return "redirect:/complete-registration?email=" + email;
        }

        if (userRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Username already exists!");
            return "redirect:/complete-registration?email=" + email;
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || !userOptional.get().isOAuth2User()) {
            return "redirect:/login?error=Invalid+session";
        }

        User user = userOptional.get();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setOAuth2User(false);
        user.setTempOAuth2Email(null);
        userRepository.save(user);

        // --- TỰ ĐỘNG ĐĂNG NHẬP ---
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);

        return "redirect:/default"; // Chuyển đến trang default để phân luồng
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        return resendOtp(email, redirectAttributes);
    }

    @GetMapping("/resend-otp")
    public String resendOtp(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email not found!");
            return "redirect:/forgot-password";
        }

        User user = userOptional.get();

        if (user.getOtpRequestedTime() != null) {
            long secondsSinceLastRequest = Duration.between(user.getOtpRequestedTime(), LocalDateTime.now()).getSeconds();
            if (secondsSinceLastRequest < OTP_COOLDOWN_SECONDS) {
                long secondsToWait = OTP_COOLDOWN_SECONDS - secondsSinceLastRequest;
                redirectAttributes.addFlashAttribute("error", "Please wait " + secondsToWait + " seconds before requesting a new OTP.");
                return "redirect:/verify-otp?email=" + email;
            }
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        userRepository.updateOtp(otp, LocalDateTime.now(), email);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error sending email. Check your credentials.");
            e.printStackTrace();
            return "redirect:/verify-otp?email=" + email;
        }

        redirectAttributes.addFlashAttribute("message", "A new OTP has been sent to your email.");
        return "redirect:/verify-otp?email=" + email;
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify_otp";
    }

    @PostMapping("/verify-otp")
    public String processVerifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty() || userOptional.get().getOtp() == null || !userOptional.get().getOtp().equals(otp)) {
            model.addAttribute("error", "Invalid OTP!");
            model.addAttribute("email", email);
            return "verify_otp";
        }

        User user = userOptional.get();
        if (user.getOtpRequestedTime().plusMinutes(OTP_VALID_DURATION_MINUTES).isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "OTP has expired! Please request a new one.");
            model.addAttribute("email", email);
            return "verify_otp";
        }

        return "redirect:/reset-password?otp=" + otp;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("otp") String otp, Model model) {
        Optional<User> userOptional = userRepository.findByOtp(otp);
        if (userOptional.isEmpty()) {
            return "redirect:/forgot-password?error=Invalid+session";
        }
        model.addAttribute("otp", otp);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("otp") String otp, @RequestParam("password") String password) {
        Optional<User> userOptional = userRepository.findByOtp(otp);
        if (userOptional.isEmpty()) {
            return "redirect:/forgot-password?error=Invalid+session";
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(password));
        user.setOtp(null);
        user.setOtpRequestedTime(null);
        userRepository.save(user);

        return "redirect:/login?message=Password+changed+successfully";
    }
}
