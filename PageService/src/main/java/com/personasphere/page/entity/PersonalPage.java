package com.personasphere.page.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "personal_pages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String displayName;

    @Column(length = 500)
    private String bio;

    @ElementCollection
    @CollectionTable(
        name = "page_hobbies",
        joinColumns = @JoinColumn(name = "page_id")
    )
    private List<String> hobbies;

    @ElementCollection
    @CollectionTable(
        name = "page_skills",
        joinColumns = @JoinColumn(name = "page_id")
    )
    private List<String> skills;

    @ElementCollection
    @CollectionTable(
        name = "page_interests",
        joinColumns = @JoinColumn(name = "page_id")
    )
    private List<String> interests;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private String theme;

    private LocalDateTime createdAt;
}
