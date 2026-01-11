package com.personasphere.auth_service.security;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personasphere.auth_service.dto.TokenResponseDTO;
import com.personasphere.auth_service.service.AuthServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler{

	private final AuthServiceImpl serviceImpl;
	private final ObjectMapper objectMapper;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
		OAuth2User user = oAuth2AuthenticationToken.getPrincipal();
		String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
		ResponseEntity<TokenResponseDTO> tokenResponseDTO = serviceImpl.handleOauth2Login(user, registrationId);
		response.setStatus(tokenResponseDTO.getStatusCode().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(tokenResponseDTO.getBody()));
	}

}
