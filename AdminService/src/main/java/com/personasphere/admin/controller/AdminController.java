package com.personasphere.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.personasphere.admin.DTO.VisibilityDTO;
import com.personasphere.admin.DTO.PageResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final RestTemplate restTemplate;

    @PreAuthorize("hasAuthority('admin:override')")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Admin service is running");
    }

    @PreAuthorize("hasAuthority('admin:override')")
    @PatchMapping("/users/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        String authHeader = request.getHeader("Authorization");

        // 2. Forward JWT to User Service
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 3. Call User Service (via Gateway OR direct service call)
        restTemplate.exchange(
                "http://USER-SERVICE/api/users/" + id + "/deactivate",
                HttpMethod.PATCH,
                entity,
                Void.class
        );

        return ResponseEntity.noContent().build();
    }
    
    @PreAuthorize("hasAuthority('admin:override')")
    @PatchMapping("/pages/{id}/visibility")
    public ResponseEntity<Void> overridePageVisibility(
            @PathVariable Long id,
            @RequestBody VisibilityDTO visibilityDTO,
            HttpServletRequest request
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VisibilityDTO> entity =
                new HttpEntity<>(visibilityDTO, headers);

        restTemplate.exchange(
                "http://PAGE-SERVICE/api/pages/" + id + "/visibility",
                HttpMethod.PATCH,
                entity,
                Void.class
        );

        return ResponseEntity.noContent().build();
    }
    
    @PreAuthorize("hasAuthority('admin:override')")
    @GetMapping("/pages")
    public ResponseEntity<List<PageResponseDTO>> getAllPages(HttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<PageResponseDTO>> response =
                restTemplate.exchange(
                        "http://PAGE-SERVICE/api/pages/admin/allPages",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<PageResponseDTO>>() {}
                );

        return ResponseEntity.ok(response.getBody());
    }

}
