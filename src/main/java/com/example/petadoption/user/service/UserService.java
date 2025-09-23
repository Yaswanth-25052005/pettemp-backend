package com.example.petadoption.user.service;

import com.example.petadoption.user.model.Adopter;
import com.example.petadoption.user.model.Shelter;
import com.example.petadoption.user.model.User;
import com.example.petadoption.user.repository.AdopterRepository;
import com.example.petadoption.user.repository.ShelterRepository;
import com.example.petadoption.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    // Save a new Adopter
    public Adopter saveAdopter(Adopter adopter) {
        adopter.setUserType("adopter");
        return adopterRepository.save(adopter);
    }

    // Save a new Shelter
    public Shelter saveShelter(Shelter shelter) {
        shelter.setUserType("shelter");
        return shelterRepository.save(shelter);
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Check if email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
