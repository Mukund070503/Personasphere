package com.personasphere.auth_service.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.personasphere.auth_service.entity.AuthUser;
import com.personasphere.auth_service.entity.ProviderType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
	
	@Value("${jwtSecretKey}")
	private String jwtSecretKey;
	
	public SecretKey getKey() {
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	}
	
	public String generateAccessToken(AuthUser user) {
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("userId",user.getId())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*10))
				.signWith(getKey())
				.compact();
				
	}

	public String getUsername(String token) {
		// TODO Auto-generated method stub
		Claims claims = Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.getSubject();
	}
	
	public ProviderType getOAuth2ProviderType(String providerId) {
		return switch(providerId.toLowerCase()) {
		case "google" -> ProviderType.GOOGLE;
		case "github" -> ProviderType.GITHUB;
		case "facebook" -> ProviderType.FACEBOOK;
		default -> throw new IllegalArgumentException("Unexpected value: " + providerId.toLowerCase());
		};
	}
	
	public String determineProviderIdFromOAuth2User(
	        OAuth2User oAuth2User,
	        String registrationId
	) {

	    String providerId = switch (registrationId.toLowerCase()) {

	        case "google" -> oAuth2User.getAttribute("sub");

	        case "github" -> {
	            Object id = oAuth2User.getAttribute("id");
	            yield id != null ? id.toString() : null;
	        }

	        default -> {
	            log.error("Unsupported OAuth2 provider: {}", registrationId);
	            throw new IllegalArgumentException(
	                    "Unsupported OAuth2 provider: " + registrationId
	            );
	        }
	    };

	    if (providerId == null || providerId.isBlank()) {
	        log.error("Unable to determine providerId for provider: {}", registrationId);
	        throw new IllegalArgumentException(
	                "Unable to determine providerId for OAuth2 login"
	        );
	    }

	    return providerId;
	}
	
	public String resolveEmailOrFallback(OAuth2User oauthUser, String providerId) {

        String email = oauthUser.getAttribute("email");

        if (email == null || email.isBlank()) {
            
            return providerId;
        }

        return email;
    }

	public boolean isFallbackEmail(AuthUser user) {
	    return user.getEmail() != null
	            && user.getProviderId() != null
	            && user.getEmail().equals(user.getProviderId());
	}


}
