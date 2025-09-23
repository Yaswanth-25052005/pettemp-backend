package com.example.petadoption.auth;

import com.example.petadoption.user.model.User;
import com.example.petadoption.user.model.Adopter;
import com.example.petadoption.user.model.Shelter;

import com.example.petadoption.user.repository.UserRepository;
import com.example.petadoption.user.repository.AdopterRepository;
import com.example.petadoption.user.repository.ShelterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Save a new Adopter
    public Adopter saveAdopter(Adopter adopter) {
        adopter.setPassword(passwordEncoder.encode(adopter.getPassword()));
        return adopterRepository.save(adopter);
    }

    // Save a new Shelter
    public Shelter saveShelter(Shelter shelter) {
        shelter.setPassword(passwordEncoder.encode(shelter.getPassword()));
        return shelterRepository.save(shelter);
    }

    // Authenticate user by email and password
    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    // Optional: check if email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
