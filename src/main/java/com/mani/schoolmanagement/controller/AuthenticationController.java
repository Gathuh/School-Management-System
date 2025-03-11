package com.mani.schoolmanagement.controller;


import com.mani.schoolmanagement.model.AuthenticationResponse;
import com.mani.schoolmanagement.model.User;
import com.mani.schoolmanagement.service.AuthenticationService;
//import jakarta.servlet.Registration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody User request) { // Change from User to RegistrationRequest
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
