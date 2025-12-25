package com.personasphere.page.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.personasphere.page.entity.Visibility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDTO {

		private Long id;
	
	    private String username;

	    private String displayName;

	    private String bio;

	    private List<String> hobbies;

	    private List<String> skills;

	    private List<String> interests;

	    private LocalDate dateOfBirth;

	    private Visibility visibility;

	    private String theme;
	    
	    private LocalDateTime createdAt;
}
