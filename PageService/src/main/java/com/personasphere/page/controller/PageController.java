package com.personasphere.page.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personasphere.page.dto.PageRequestDTO;
import com.personasphere.page.dto.PageResponseDTO;
import com.personasphere.page.dto.VisibilityDTO;
import com.personasphere.page.entity.Visibility;
import com.personasphere.page.service.PageServiceImpl;

@RestController
@RequestMapping("/api/pages")
public class PageController {
	
	@Autowired
	private PageServiceImpl serviceImpl;
	
	@PreAuthorize("hasAuthority('page:create')")
	@PostMapping
	public  ResponseEntity<PageResponseDTO> createPage(@RequestBody PageRequestDTO pageRequestDTO){
		PageResponseDTO page = serviceImpl.createPage(pageRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(page);
	}
	
	@PreAuthorize("permitAll()")
	@GetMapping("/username/{username}")
	public ResponseEntity<PageResponseDTO> findByUsername(@PathVariable String username){
		PageResponseDTO page = serviceImpl.getPageByUsername(username);
		return ResponseEntity.status(HttpStatus.FOUND).body(page);
	}
	
	@PreAuthorize("permitAll()")
	@GetMapping("{id}")
	public ResponseEntity<PageResponseDTO> findById(@PathVariable Long id){
		PageResponseDTO page = serviceImpl.getPageById(id);
		return ResponseEntity.status(HttpStatus.FOUND).body(page);
	}
	
	@PreAuthorize(
			  "hasAuthority('page:update:any') or hasAuthority('page:update:own')"
			)
	@PutMapping("/{id}/update")
	public ResponseEntity<PageResponseDTO> pageUpdate(@PathVariable Long id,@RequestBody PageRequestDTO pageRequestDTO){
		PageResponseDTO page = serviceImpl.updatePage(id, pageRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(page);
	}

	@PreAuthorize("hasAuthority('page:change:visibility')")
	@PatchMapping("/{id}/visibility")
	public ResponseEntity<Void> pageVisibilityUpdate(
			@PathVariable Long id,@RequestBody VisibilityDTO visibilityDto  ){
		serviceImpl.updateVisibility(id, visibilityDto.getVisibility());
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}/birthday")
	public ResponseEntity<Boolean> birthdayCheck(@PathVariable Long id){
		Boolean value = serviceImpl.isBirthdayToday(id);
		return ResponseEntity.status(HttpStatus.OK).body(value);
	}
	
	@GetMapping("/allPages")
	public ResponseEntity<List<PageResponseDTO>> allPages(){
		List<PageResponseDTO> visiblePages = serviceImpl.getAllPublicPages();
		return ResponseEntity.status(HttpStatus.FOUND).body(visiblePages);
	}
}
