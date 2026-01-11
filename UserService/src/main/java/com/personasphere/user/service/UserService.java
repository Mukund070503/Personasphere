package com.personasphere.user.service;

import java.util.List;

import com.personasphere.user.dto.UserRequestDTO;
import com.personasphere.user.dto.UserResponseDTO;

public interface UserService {

	UserResponseDTO createUser(UserRequestDTO requestDTO);

	UserResponseDTO getUserById(Long id);

	UserResponseDTO getUserByUsername(String username);

	List<UserResponseDTO> getAllUsers();

	void deactivateUser(Long id);
	

}
