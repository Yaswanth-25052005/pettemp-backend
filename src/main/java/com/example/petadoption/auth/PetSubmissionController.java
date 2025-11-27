package com.example.petadoption.auth;

import com.example.petadoption.auth.dto.RehomeRequestDTO;
import com.example.petadoption.auth.dto.RehomeRequestForm;
import com.example.petadoption.user.model.PetSubmission;
import com.example.petadoption.user.service.PetSubmissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "http://localhost:5173") // optional - helps local frontend; remove or change for prod
public class PetSubmissionController {

    private static final Logger log = LoggerFactory.getLogger(PetSubmissionController.class);

    @Autowired
    private PetSubmissionService submissionService;

    /**
     * Preferred: single multipart/form-data request where all fields are form-data (text) and files are file fields.
     * Frontend / Postman: Body -> form-data -> keys = DTO property names (fullName, email, ... petPhotos (File))
     */
    @PostMapping(value = "/rehome",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitPetRehome(@ModelAttribute RehomeRequestForm form,
                                             HttpServletRequest request) {
        try {
            log.debug("Incoming rehome (form-data) request. URI={}, remote={}", request.getRequestURI(),
                    request.getRemoteAddr());

            RehomeRequestDTO dto = form.toDto();

            // debug logging of some fields to verify binding
            log.debug("DTO bound: fullName='{}', email='{}', petName='{}'", dto.getFullName(), dto.getEmail(), dto.getPetName());
            log.debug("Files received: {}", dto.getPetPhotos() == null ? 0 : dto.getPetPhotos().length);

            PetSubmission saved = submissionService.createSubmission(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (MultipartException mex) {
            log.warn("MultipartException handling rehome request: {}", mex.getMessage(), mex);
            return ResponseEntity.badRequest().body("Error uploading file(s): " + mex.getMessage());

        } catch (Exception ex) {
            log.error("Unhandled error in submitPetRehome", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + ex.getMessage());
        }
    }

    /**
     * Alternate: if frontend sends JSON for fields in one part and files in other parts.
     * Example (Postman):
     * - Body type: form-data (multipart)
     * - Key "data"  -> type: text -> JSON string (application/json)
     * - Key "petPhotos" -> type: file (can repeat key for multiple files)
     *
     * Use this if your client serializes DTO to JSON and sends files separately.
     */
    @PostMapping(value = "/rehome/json",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitPetRehomeJson(
            @RequestPart("data") RehomeRequestDTO dto,
            @RequestPart(value = "petPhotos", required = false) MultipartFile[] petPhotos,
            HttpServletRequest request) {

        try {
            log.debug("Incoming rehome (json+files) request. URI={}, remote={}", request.getRequestURI(),
                    request.getRemoteAddr());

            if (petPhotos != null && petPhotos.length > 0) {
                dto.setPetPhotos(petPhotos);
            }

            log.debug("DTO JSON bound: fullName='{}', email='{}', petName='{}'", dto.getFullName(), dto.getEmail(), dto.getPetName());
            log.debug("Files received: {}", dto.getPetPhotos() == null ? 0 : dto.getPetPhotos().length);

            PetSubmission saved = submissionService.createSubmission(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (MultipartException mex) {
            log.warn("MultipartException handling rehome/json request: {}", mex.getMessage(), mex);
            return ResponseEntity.badRequest().body("Error uploading file(s): " + mex.getMessage());

        } catch (Exception ex) {
            log.error("Unhandled error in submitPetRehomeJson", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + ex.getMessage());
        }
    }

    // Admin / list all submissions
    @GetMapping("/submissions")
    public ResponseEntity<?> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }

    @GetMapping("/submissions/{id}")
    public ResponseEntity<?> getSubmission(@PathVariable Long id) {
        PetSubmission sub = submissionService.getSubmissionById(id);
        if (sub == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sub);
    }
}
