package com.personasphere.user.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponseDTO {

	private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
