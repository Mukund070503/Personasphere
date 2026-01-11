package com.personasphere.auth_service.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.personasphere.auth_service.entity.AuthUser;
import com.personasphere.auth_service.repository.AuthUserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

	private final AuthUserRepository repository;
	private final JwtUtil jwtUtil;
	private final HandlerExceptionResolver exceptionResolver;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
		// TODO Auto-generated method stub
		log.info("Once per request filter hit on url: {}",request.getRequestURI());
		
		final String requestTokenHeader = request.getHeader("Authorization");
		
		if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = requestTokenHeader.split("Bearer ")[1];
		String name = jwtUtil.getUsername(token);
		
		if(name!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			AuthUser user = repository.getByUsername(name).orElseThrow();
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
					new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		
		filterChain.doFilter(request, response);
		}catch(Exception ex) {
			exceptionResolver.resolveException(request, response, null, ex);
		}
		
		
	}

}
