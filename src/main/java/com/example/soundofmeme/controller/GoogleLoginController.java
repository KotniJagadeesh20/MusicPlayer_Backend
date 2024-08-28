package com.example.soundofmeme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.soundofmeme.response.AuthenticationResponse;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class GoogleLoginController {

	@GetMapping("/loginSuccess")
    public ResponseEntity<Map<String, Object>> getLoginInfo(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal != null) {
            Map<String, Object> attributes = principal.getAttributes();
            response.put("message", "Login successful");
            response.put("success", true);
            response.put("user", attributes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("message", "Login failed. Please try again.");
        response.put("success", false);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/loginFailure")
    public ResponseEntity<Map<String, Object>> loginFailure() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login failed. Please try again.");
        response.put("success", false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
}


