package com.personasphere.auth_service.service;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.personasphere.auth_service.dto.LoginRequestDTO;
import com.personasphere.auth_service.dto.RegisterRequestDTO;
import com.personasphere.auth_service.dto.TokenResponseDTO;
import com.personasphere.auth_service.entity.AuthUser;
import com.personasphere.auth_service.entity.ProviderType;
import com.personasphere.auth_service.entity.RoleType;
import com.personasphere.auth_service.exceptions.InvalidCredentialsException;
import com.personasphere.auth_service.exceptions.OAuthConflictException;
import com.personasphere.auth_service.exceptions.UserAlreadyExistsException;
import com.personasphere.auth_service.repository.AuthUserRepository;
import com.personasphere.auth_service.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final AuthUserRepository repository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public TokenResponseDTO login(LoginRequestDTO loginRequestDTO) {
		// TODO Auto-generated method stub
		try {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
				);
		
		AuthUser user = (AuthUser) authentication.getPrincipal();
		
		String token = jwtUtil.generateAccessToken(user);
		
		return new TokenResponseDTO(user.getId(), token);} 
		catch (Exception ex) {
	        throw new InvalidCredentialsException("Invalid username or password");
	    }
	}
	
	@Override
	public void signUp(RegisterRequestDTO registerRequestDTO) {
		AuthUser user = repository.getByUsername(registerRequestDTO.getUsername()).orElse(null);
		AuthUser user1 = repository.getByEmail(registerRequestDTO.getEmail()).orElse(null);
		if (user != null) {
		    throw new UserAlreadyExistsException("Username already exists");
		}

		if (user1 != null) {
		    throw new UserAlreadyExistsException("Email already exists");
		}
		registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
		user = modelMapper.map(registerRequestDTO, AuthUser.class);
		user.setRoles(Set.of(RoleType.USER));
		repository.save(user);
	}

	public ResponseEntity<TokenResponseDTO> handleOauth2Login(OAuth2User oauthUser, String registrationId) {

	    ProviderType providerType = jwtUtil.getOAuth2ProviderType(registrationId);
	    String providerId = jwtUtil.determineProviderIdFromOAuth2User(oauthUser, registrationId);
	    String resolvedEmail = jwtUtil.resolveEmailOrFallback(oauthUser, providerId);
	    AuthUser providerUser = repository
	            .findByProviderIdAndProviderType(providerId, providerType)
	            .orElse(null);

	    AuthUser emailUser = repository.findByEmail(resolvedEmail).orElse(null);
	    
	    if (providerUser == null && emailUser == null) {

	        AuthUser newUser = new AuthUser();
	        newUser.setUsername(resolvedEmail);
	        newUser.setEmail(resolvedEmail);
	        newUser.setProviderId(providerId);
	        newUser.setProviderType(providerType);
	        newUser.setRoles(Set.of(RoleType.USER));
	        repository.save(newUser);
	        String token = jwtUtil.generateAccessToken(newUser);
	        return ResponseEntity.ok(new TokenResponseDTO(newUser.getId(), token));
	    }
	    if (providerUser == null && emailUser != null) {
	    	throw new OAuthConflictException(
	    		    "Email already registered with password login"
	    		);

	    }
	    if (providerUser != null) {

	        if (jwtUtil.isFallbackEmail(providerUser)
	                && !resolvedEmail.equals(providerUser.getEmail())) {
	            providerUser.setEmail(resolvedEmail);
	            providerUser.setUsername(resolvedEmail);
	            repository.save(providerUser);
	        }

	        String token = jwtUtil.generateAccessToken(providerUser);
	        return ResponseEntity.ok(new TokenResponseDTO(providerUser.getId(), token));
	    }

	    throw new IllegalStateException("Unexpected OAuth login state");
	}


}
