package com.example.petadoption.auth;

import com.example.petadoption.auth.dto.LoginRequest;
import com.example.petadoption.auth.dto.RegisterRequest;
import com.example.petadoption.auth.dto.AuthResponse;
import com.example.petadoption.user.model.Adopter;
import com.example.petadoption.user.model.Shelter;
import com.example.petadoption.user.model.User;
import com.example.petadoption.user.service.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * SIGN UP — accepts a RegisterRequest that should contain:
     * - userType (adopter | shelter)
     * - fullName (for adopters) OR organizationName (for shelters)
     * - email, password, phone, address, city, state, zipCode
     *
     * This method will set only the fields we agreed: fullName/organizationName, phone, location.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest signupRequest) {
        log.debug("Signup request received for email: {}", signupRequest.getEmail());

        // Prevent duplicate email
        Optional<User> existing = userService.findByEmail(signupRequest.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.status(409).body("Email already registered");
        }

        // Normalize userType
        String userType = signupRequest.getUserType() != null
                ? signupRequest.getUserType().trim().toLowerCase()
                : "adopter";

        // fullName (single field)
        String fullName = signupRequest.getFullName() != null
                ? signupRequest.getFullName().trim()
                : "";

        // phone
        String phone = signupRequest.getPhone() != null
                ? signupRequest.getPhone().trim()
                : "";

        // build location from address/city/state/zip (if provided)
        StringBuilder loc = new StringBuilder();
        if (signupRequest.getAddress() != null && !signupRequest.getAddress().isBlank()) {
            loc.append(signupRequest.getAddress().trim());
        }
        if (signupRequest.getCity() != null && !signupRequest.getCity().isBlank()) {
            if (loc.length() > 0) loc.append(", ");
            loc.append(signupRequest.getCity().trim());
        }
        if (signupRequest.getState() != null && !signupRequest.getState().isBlank()) {
            if (loc.length() > 0) loc.append(", ");
            loc.append(signupRequest.getState().trim());
        }
        if (signupRequest.getZipCode() != null && !signupRequest.getZipCode().isBlank()) {
            if (loc.length() > 0) loc.append(" ");
            loc.append(signupRequest.getZipCode().trim());
        }
        String location = loc.toString();

        User user;
        if ("adopter".equalsIgnoreCase(userType)) {
            Adopter adopter = new Adopter();

            // Set only fields we want for Adopter
            adopter.setFullName(fullName);
            adopter.setPhone(phone);
            adopter.setLocation(location);

            // Common fields in base User
            adopter.setEmail(signupRequest.getEmail());
            adopter.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            adopter.setUserType("ADOPTER");

            user = userService.saveAdopter(adopter);

            log.info("Created adopter id={} email={}", user.getId(), user.getEmail());

        } else {
            // Shelter sign-up
            Shelter shelter = new Shelter();

            // prefer organizationName, fallback to fullName if org missing
            String orgName = signupRequest.getOrganizationName();
            if (orgName != null && !orgName.isBlank()) {
                shelter.setOrganizationName(orgName.trim());
            } else {
                shelter.setOrganizationName(fullName);
            }

            shelter.setPhone(phone);
            shelter.setLocation(location);

            shelter.setEmail(signupRequest.getEmail());
            shelter.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            shelter.setUserType("SHELTER");

            user = userService.saveShelter(shelter);

            log.info("Created shelter id={} email={}", user.getId(), user.getEmail());
        }

        // Generate JWT token and return basic auth response
        String token = tokenProvider.generateToken(user.getEmail());
        AuthResponse resp = new AuthResponse(token, user.getEmail(), user.getUserType());
        return ResponseEntity.ok(resp);
    }

    /**
     * LOGIN — validate credentials and return token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userService.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        String token = tokenProvider.generateToken(user.getEmail());
        AuthResponse resp = new AuthResponse(token, user.getEmail(), user.getUserType());
        return ResponseEntity.ok(resp);
    }
}
