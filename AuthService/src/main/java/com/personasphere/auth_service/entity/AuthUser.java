package com.personasphere.auth_service.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.annotation.Generated;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Auth_table",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class AuthUser implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String username;
	private String email;
	private String password;
	private String providerId;
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<RoleType> roles = new HashSet<RoleType>();
//	@Override ((This is used when only roles are used for granting authority not on granular level))
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return roles.stream()
//				.map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
//				.collect(Collectors.toSet());
//	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

	    Set<SimpleGrantedAuthority> authorities = new HashSet<>();

	    // ROLE based
	    roles.forEach(role ->
	            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()))
	    );

	    // Permission based (derived)
	    roles.forEach(role ->
	            role.getPermissions()
	                .forEach(p ->
	                        authorities.add(
	                            new SimpleGrantedAuthority(p.getPermission())
	                        )
	                )
	    );

	    return authorities;
	}

	
	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return true;
	}

	
}
