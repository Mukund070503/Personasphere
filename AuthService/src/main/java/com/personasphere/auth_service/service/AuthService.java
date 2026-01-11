package com.personasphere.auth_service.service;

import org.springframework.http.ResponseEntity;

import com.personasphere.auth_service.dto.LoginRequestDTO;
import com.personasphere.auth_service.dto.RegisterRequestDTO;
import com.personasphere.auth_service.dto.TokenResponseDTO;

public interface AuthService {

	TokenResponseDTO login(LoginRequestDTO loginRequestDTO);
	public void signUp(RegisterRequestDTO registerRequestDTO);
	
}
