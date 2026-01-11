package com.personasphere.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personasphere.user.dto.UserRequestDTO;
import com.personasphere.user.dto.UserResponseDTO;
import com.personasphere.user.service.UserServiceImpl;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserServiceImpl serviceImpl;
	
	@PostMapping("/create")
	public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO){
		UserResponseDTO response = serviceImpl.createUser(userRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PreAuthorize("hasAuthority('user:read')")
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
		UserResponseDTO response = serviceImpl.getUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<UserResponseDTO> findByName(@PathVariable String username){
		UserResponseDTO response = serviceImpl.getUserByUsername(username);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PreAuthorize("hasAuthority('user:read:all')")
	@GetMapping("/allUsers")
	public ResponseEntity<List<UserResponseDTO>> findAllUsers(){
		List<UserResponseDTO> response = serviceImpl.getAllUsers();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PreAuthorize("hasAuthority('user:deactivate')")
	@PatchMapping("/{id}/deactivate")
	public ResponseEntity<Void> deactivateUserById(@PathVariable Long id){
		serviceImpl.deactivateUser(id);
		return ResponseEntity.ok().build();
	}
}
