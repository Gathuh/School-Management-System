package com.mani.schoolmanagement.service;


import com.mani.schoolmanagement.model.AuthenticationResponse;
import com.mani.schoolmanagement.model.User;
import com.mani.schoolmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder; // Fixed missing type
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(User request) { // Moved inside class, fixed typo
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // Ensure user has a role

        repository.save(user);
        String token =jwtService.generateToken(user);
        return "Succesfull Registration"; // Placeholder, implement as needed
    }
    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user =repository.findByUserName(request.getUsername()).orElseThrow();
        String token =jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

}