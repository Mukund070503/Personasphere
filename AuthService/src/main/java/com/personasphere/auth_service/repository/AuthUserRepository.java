package com.personasphere.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.personasphere.auth_service.entity.AuthUser;
import com.personasphere.auth_service.entity.ProviderType;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long>{

	public Optional<UserDetails> findByUsername(String username);

	public Optional<AuthUser> getByEmail(String email);
	public Optional<AuthUser> getByUsername(String username);

	public Optional<AuthUser> findByProviderIdAndProviderType(String providerId, ProviderType providerType);

	public Optional<AuthUser> findByEmail(String email);
}
