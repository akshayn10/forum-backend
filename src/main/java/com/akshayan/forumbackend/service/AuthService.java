package com.akshayan.forumbackend.service;

import com.akshayan.forumbackend.Exception.ForumException;
import com.akshayan.forumbackend.dto.AuthenticationResponse;
import com.akshayan.forumbackend.dto.LoginRequest;
import com.akshayan.forumbackend.dto.RegisterRequestDto;
import com.akshayan.forumbackend.model.ForumUser;
import com.akshayan.forumbackend.model.NotificationEmail;
import com.akshayan.forumbackend.model.VerificationToken;
import com.akshayan.forumbackend.repository.ForumUserRepository;
import com.akshayan.forumbackend.repository.VerificationTokenRepository;
import com.akshayan.forumbackend.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final ForumUserRepository forumUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void register(RegisterRequestDto registerRequestDto) {
        ForumUser forumUser = new ForumUser();
        forumUser.setUsername(registerRequestDto.getUsername());
        forumUser.setEmail(registerRequestDto.getEmail());
        forumUser.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        forumUser.setCreated(Instant.now());
        forumUser.setEnabled(false);
        forumUserRepository.save(forumUser);

        String token = generateVerificationToken(forumUser);
        mailService.sendMail(new NotificationEmail("Please Activate your Forum Account",forumUser.getEmail(),"Thank you for Signing up with us. Please click on the link below to activate your account.\n" +
                "http://localhost:8080/api/auth/accountVerification/" + token));

    }

    private String generateVerificationToken(ForumUser forumUser) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setForumUser(forumUser);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken= verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow((() -> new ForumException("Invalid Token")));
        fetchUserAndEnable(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getForumUser().getUsername();
        Optional<ForumUser> forumUser = forumUserRepository.findByUsername(username);
        forumUser.orElseThrow((() -> new ForumException("User not found with name: " + username)));
        forumUser.get().setEnabled(true);
        forumUserRepository.save(forumUser.get());
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate=  authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication((Authentication) authenticate);
        String token= jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token,loginRequest.getUsername());
    }

}
