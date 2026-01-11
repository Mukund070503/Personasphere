package com.personasphere.auth_service.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final Oauth2SuccessHandler oauth2SuccessHandler;
	private final HandlerExceptionResolver handlerExceptionResolver;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
			.csrf(CsrfConfigurer -> CsrfConfigurer.disable())
			.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/auth/**").permitAll()
					.anyRequest().authenticated())
			//.formLogin(Customizer.withDefaults())
			.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
			.oauth2Login(oauth2 -> oauth2.loginPage("")
					.failureHandler(
							new AuthenticationFailureHandler() {
								@Override
								public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) throws IOException, ServletException {
									// TODO Auto-generated method stub
									log.error("Oauth2 login error");
									handlerExceptionResolver.resolveException(request, response, null, exception);
								}
							})
					.successHandler(oauth2SuccessHandler))
			.exceptionHandling(exceptionHandlingConfigurer -> 
			exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> 
			handlerExceptionResolver.resolveException(request, response, null, accessDeniedException)));
		
		return httpSecurity.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
