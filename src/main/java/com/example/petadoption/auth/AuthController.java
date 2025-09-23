
package com.example.petadoption.auth;

//DTOs
import com.example.petadoption.auth.dto.LoginRequest;
import com.example.petadoption.auth.dto.RegisterRequest;
import com.example.petadoption.auth.dto.AuthResponse;
import com.example.petadoption.user.model.Adopter;
import com.example.petadoption.user.model.Shelter;
import com.example.petadoption.user.model.User;
import com.example.petadoption.user.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired	
    private JwtUtil tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest signupRequest) {
        User user;

        if ("adopter".equalsIgnoreCase(signupRequest.getUserType())) {
            Adopter adopter = new Adopter();
            adopter.setFirstName(signupRequest.getFirstName());
            adopter.setLastName(signupRequest.getLastName());
            adopter.setEmail(signupRequest.getEmail());
            adopter.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            adopter.setPhone(signupRequest.getPhone());
            adopter.setAddress(signupRequest.getAddress());
            adopter.setCity(signupRequest.getCity());
            adopter.setState(signupRequest.getState());
            adopter.setZipCode(signupRequest.getZipCode());

            user = userService.saveAdopter(adopter);
        } else {
            Shelter shelter = new Shelter();
            shelter.setOrganizationName(signupRequest.getOrganizationName());
            shelter.setEmail(signupRequest.getEmail());
            shelter.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            shelter.setPhone(signupRequest.getPhone());
            shelter.setAddress(signupRequest.getAddress());
            shelter.setCity(signupRequest.getCity());
            shelter.setState(signupRequest.getState());
            shelter.setZipCode(signupRequest.getZipCode());
            shelter.setVerified(false);

            user = userService.saveShelter(shelter);
        }

        // Optional: auto-login user after signup
        String token = tokenProvider.generateToken(user.getEmail());
        AuthResponse authResponse = new AuthResponse(
                token,
                user.getEmail(),
                user.getUserType()
        );

        return ResponseEntity.ok(authResponse);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userService.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty() ||
            !passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userOpt.get();
        String token = tokenProvider.generateToken(user.getEmail());

        AuthResponse authResponse = new AuthResponse(
                token,
                user.getEmail(),
                user.getUserType()
        );

        return ResponseEntity.ok(authResponse);
    }
}
	