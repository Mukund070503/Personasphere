package com.personasphere.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

	private Long id;
	private String username;
	private String email;
	private Boolean active;
	private LocalDateTime createdAt;
}
