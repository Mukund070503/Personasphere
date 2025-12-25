package com.personasphere.page.service;

import java.util.List;

import com.personasphere.page.dto.PageRequestDTO;
import com.personasphere.page.dto.PageResponseDTO;
import com.personasphere.page.entity.Visibility;

public interface PageService {

	    // Create a new personal page
	    PageResponseDTO createPage(PageRequestDTO requestDTO);

	    // Get a page by username (public view)
	    PageResponseDTO getPageByUsername(String username);

	    // Get page by ID (owner/admin use later)
	    PageResponseDTO getPageById(Long id);

	    // Update page details
	    PageResponseDTO updatePage(Long id, PageRequestDTO requestDTO);

	    // Change visibility (PUBLIC / PRIVATE)
	    void updateVisibility(Long id, Visibility visibility);

	    // Check if today is user's birthday (UI trigger)
	    boolean isBirthdayToday(Long id);

	    // Get all public pages (explore feature)
	    List<PageResponseDTO> getAllPublicPages();

}
