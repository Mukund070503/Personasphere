package com.personasphere.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personasphere.user.dto.UserRequestDTO;
import com.personasphere.user.dto.UserResponseDTO;
import com.personasphere.user.entity.User;
import com.personasphere.user.exceptions.DuplicateUserException;
import com.personasphere.user.exceptions.UserNotFoundException;
import com.personasphere.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        repository.findByUsername(requestDTO.getUsername())
                .ifPresent(u -> {
                    throw new DuplicateUserException("Username already exists");
                });

        User user = modelMapper.map(requestDTO, User.class);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = repository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {

        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return repository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .toList();
    }

    @Override
    public void deactivateUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (Boolean.TRUE.equals(user.getActive())) {
            user.setActive(false);
            repository.save(user);
        }
    }
}
