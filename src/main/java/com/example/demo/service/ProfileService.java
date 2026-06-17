package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final IdGenerationService idGenerationService;

    public ProfileService(ProfileRepository profileRepository, IdGenerationService idGenerationService) {
        this.profileRepository = profileRepository;
        this.idGenerationService = idGenerationService;
    }

    // CREATE / UPDATE
    public Profile saveProfile(Profile profile, MultipartFile photoFile) throws IOException {
        if (photoFile != null && !photoFile.isEmpty()) {
            String contentType = photoFile.getContentType();
            
            if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
                throw new IllegalArgumentException("Invalid file type. Only JPEG and PNG images are supported.");
            }
            
            // Store file as byte array inside the DB LONGBLOB column mapping
            profile.setPhoto(photoFile.getBytes());
        }

        // 2. Registration Number handling logic
        if (profile.getRegistrationNumber() == null || profile.getRegistrationNumber().trim().isEmpty()) {
            String generatedId = idGenerationService.generateRegistrationNumber(profile.getDepartment());
            profile.setRegistrationNumber(generatedId);
        }

        return profileRepository.save(profile);
    }

    // READ ALL
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    // READ ONE
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    // DELETE
    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new IllegalArgumentException("Profile with ID " + id + " does not exist.");
        }
        profileRepository.deleteById(id);
    }
}