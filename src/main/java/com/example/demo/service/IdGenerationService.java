package com.example.demo.service;

import com.example.demo.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import java.time.Year;

@Service
public class IdGenerationService {

    private final ProfileRepository profileRepository;

    public IdGenerationService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public synchronized String generateRegistrationNumber(String department) {
        int currentYear = Year.now().getValue();
        String deptCode = department.trim().replaceAll("\\s+", "").toUpperCase();
        deptCode = deptCode.substring(0, Math.min(deptCode.length(), 4));

        int sequence = 1;
        String formattedId;
        boolean exists;

        do {
            formattedId = String.format("%d-%s-%03d", currentYear, deptCode, sequence);
            exists = profileRepository.existsByRegistrationNumber(formattedId);
            sequence++;
        } while (exists);

        return formattedId;
    }
}