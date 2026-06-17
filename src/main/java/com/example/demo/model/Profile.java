package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String registrationNumber; 

    private String fullName;
    private String email;
    private String department;
    
    @Enumerated(EnumType.STRING)
    private ProfileType profileType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo; 

    private LocalDateTime createdAt = LocalDateTime.now();
}