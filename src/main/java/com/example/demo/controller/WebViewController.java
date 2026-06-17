package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import com.example.demo.service.BarcodeGeneratorService;
import com.example.demo.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class WebViewController {

    private final ProfileService profileService;
    private final BarcodeGeneratorService assetService;

    public WebViewController(ProfileService profileService, BarcodeGeneratorService assetService) {
        this.profileService = profileService;
        this.assetService = assetService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/profiles"; 
    }

    @GetMapping("/profiles")
    public String listProfiles(Model model) {
        model.addAttribute("profiles", profileService.getAllProfiles());
        return "profiles-list";
    }

    @PostMapping("/profiles/create")
    public String createProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("department") String department,
            @RequestParam("profileType") ProfileType profileType,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            Model model) {
        try {
            Profile profile = Profile.builder()
                    .fullName(fullName)
                    .email(email)
                    .department(department)
                    .profileType(profileType)
                    .build();

            profileService.saveProfile(profile, photo);
            return "redirect:/profiles";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("profiles", profileService.getAllProfiles());
            return "profiles-list";
        }
    }

    @GetMapping("/profiles/{id}/card")
    public String viewIdCard(@PathVariable Long id, Model model) {
        Profile profile = profileService.getProfileById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID"));

        String qrCode = assetService.generateQRCodeBase64(profile.getRegistrationNumber(), 150, 150);
        String barcode = assetService.generateCode128BarcodeBase64(profile.getRegistrationNumber(), 250, 50);

        model.addAttribute("profile", profile);
        model.addAttribute("qrCode", qrCode);
        model.addAttribute("barcode", barcode);

        return "card-view";
    }
}