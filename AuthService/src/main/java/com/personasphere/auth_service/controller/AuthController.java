package com.personasphere.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personasphere.auth_service.dto.LoginRequestDTO;
import com.personasphere.auth_service.dto.RegisterRequestDTO;
import com.personasphere.auth_service.dto.TokenResponseDTO;
import com.personasphere.auth_service.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
		return ResponseEntity.ok(authService.login(loginRequestDTO));
	}
	
	@PostMapping("/signUp")
	public ResponseEntity<Void> signUp(@RequestBody RegisterRequestDTO registerRequestDTO){
		authService.signUp(registerRequestDTO);
		return ResponseEntity.ok().build();
	}
}
