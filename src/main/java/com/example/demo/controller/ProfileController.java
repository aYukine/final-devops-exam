package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import com.example.demo.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("department") String department,
            @RequestParam("profileType") ProfileType profileType,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            Profile profile = Profile.builder()
                    .fullName(fullName)
                    .email(email)
                    .department(department)
                    .profileType(profileType)
                    .build();

            Profile savedProfile = profileService.saveProfile(profile, photo);
            return ResponseEntity.ok(savedProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error parsing file stream data.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Long id) {
        return profileService.getProfileById(id)
                .map(profile -> {
                    if (profile.getPhoto() != null) {
                        return ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_PNG) // or IMAGE_JPEG depending on upload
                                .body(profile.getPhoto());
                    }
                    return ResponseEntity.notFound().<byte[]>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        try {
            profileService.deleteProfile(id);
            return ResponseEntity.ok("Profile with ID " + id + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}