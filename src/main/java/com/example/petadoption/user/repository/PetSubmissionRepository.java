package com.example.petadoption.user.repository;

import com.example.petadoption.user.model.PetSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetSubmissionRepository extends JpaRepository<PetSubmission, Long> {

}
