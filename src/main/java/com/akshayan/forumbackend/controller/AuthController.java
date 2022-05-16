package com.akshayan.forumbackend.controller;

import com.akshayan.forumbackend.dto.AuthenticationResponse;
import com.akshayan.forumbackend.dto.LoginRequest;
import com.akshayan.forumbackend.dto.RefreshTokenRequest;
import com.akshayan.forumbackend.dto.RegisterRequestDto;
import com.akshayan.forumbackend.service.AuthService;
import com.akshayan.forumbackend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService  refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto registerRequestDto){
        authService.register(registerRequestDto);
        return new ResponseEntity<>("Registration Successful", OK);
    }
    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Verified Successfully", OK);
    }
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }


}
