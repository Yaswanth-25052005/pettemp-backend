package com.example.petadoption.user.service;

import com.example.petadoption.auth.dto.RehomeRequestDTO;
import com.example.petadoption.user.model.PetSubmission;
import com.example.petadoption.user.repository.PetSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PetSubmissionService {

    @Autowired
    private PetSubmissionRepository repository;

    @Autowired
    private FileStorageUtil fileStorage;

    /**
     * Creates and saves a PetSubmission entity from a RehomeRequestDTO.
     * Handles photo upload + mapping all user/pet fields.
     */
    public PetSubmission createSubmission(RehomeRequestDTO dto) throws IOException {

        List<String> savedPhotoUrls = fileStorage.saveFiles(dto.getPetPhotos());

        PetSubmission submission = new PetSubmission();

        // Owner Info
        submission.setOwnerFullName(dto.getFullName());
        submission.setOwnerEmail(dto.getEmail());
        submission.setOwnerPhone(dto.getPhone());
        submission.setOwnerStreetAddress(dto.getStreetAddress());
        submission.setOwnerCity(dto.getCity());
        submission.setOwnerState(dto.getState());
        submission.setOwnerZipCode(dto.getZipCode());

        // Pet Info
        submission.setPetName(dto.getPetName());
        submission.setSpecies(dto.getSpecies());
        submission.setBreed(dto.getBreed());
        submission.setAge(dto.getAge());
        submission.setSize(dto.getSize());
        submission.setGender(dto.getGender());
        submission.setDescription(dto.getDescription());
        submission.setReasonForRehoming(dto.getReasonForRehoming());
        submission.setSpayed(dto.getSpayed());
        submission.setVaccinated(dto.getVaccinated());
        submission.setAdoptionFee(dto.getAdoptionFee());

        // Photos
        submission.setPhotoUrls(savedPhotoUrls);

        // Agreements
        submission.setAgreeTerms(dto.getAgreeTerms());
        submission.setAgreeScreening(dto.getAgreeScreening());

        // default status
        submission.setStatus("PENDING");

        return repository.save(submission);
    }

    public List<PetSubmission> getAllSubmissions() {
        return repository.findAll();
    }

    public PetSubmission getSubmissionById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
