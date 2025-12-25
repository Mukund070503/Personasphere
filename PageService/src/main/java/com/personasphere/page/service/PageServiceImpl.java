package com.personasphere.page.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.personasphere.page.dto.PageRequestDTO;
import com.personasphere.page.dto.PageResponseDTO;
import com.personasphere.page.entity.PersonalPage;
import com.personasphere.page.entity.Visibility;
import com.personasphere.page.exception.DuplicateResourceException;
import com.personasphere.page.exception.PageNotFoundException;
import com.personasphere.page.repository.PersonalPageRepository;

@Service
public class PageServiceImpl implements PageService{

	private final PersonalPageRepository repository;
	private final ModelMapper modelMapper;
	public PageServiceImpl(PersonalPageRepository repository, ModelMapper modelMapper) {
		// TODO Auto-generated constructor stub
		this.repository = repository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public PageResponseDTO createPage(PageRequestDTO requestDTO) {

        if (repository.existsByUsername(requestDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        PersonalPage page = modelMapper.map(requestDTO, PersonalPage.class);
        page.setCreatedAt(LocalDateTime.now());

        if(requestDTO.getVisibility()==null) {
        	page.setVisibility(Visibility.PUBLIC);
        }else {
            page.setVisibility(requestDTO.getVisibility());
        }
        
        PersonalPage savedPage = repository.save(page);
        return modelMapper.map(savedPage, PageResponseDTO.class);
    }

	@Override
    public PageResponseDTO getPageByUsername(String username) {

        PersonalPage page = repository.findByUsername(username).orElseThrow(() -> new PageNotFoundException("Page not found"));

        return modelMapper.map(page, PageResponseDTO.class);
    }

	@Override
	public PageResponseDTO getPageById(Long id) {
		// TODO Auto-generated method stub
		PersonalPage page = repository.findById(id)
				.orElseThrow(() -> new PageNotFoundException("Page not found"));
		return modelMapper.map(page, PageResponseDTO.class);
	}

	@Override
	public PageResponseDTO updatePage(Long id, PageRequestDTO requestDTO) {
		// TODO Auto-generated method stub
		 PersonalPage page = repository.findById(id)
	                .orElseThrow(() -> new PageNotFoundException("Page not found"));

	        page.setDisplayName(requestDTO.getDisplayName());
	        page.setBio(requestDTO.getBio());
	        page.setHobbies(requestDTO.getHobbies());
	        page.setSkills(requestDTO.getSkills());
	        page.setInterests(requestDTO.getInterests());
	        page.setTheme(requestDTO.getTheme());
	        page.setDateOfBirth(requestDTO.getDateOfBirth());

	        PersonalPage updatedPage = repository.save(page);
	        return modelMapper.map(updatedPage, PageResponseDTO.class);
	}

	@Override
	public void updateVisibility(Long id, Visibility visibility) {
		// TODO Auto-generated method stub
		PersonalPage page = repository.findById(id)
                .orElseThrow(() -> new PageNotFoundException("Page not found"));

        page.setVisibility(visibility);

        repository.save(page);
		
	}

	@Override
	public boolean isBirthdayToday(Long id) {
		// TODO Auto-generated method stub
		PersonalPage page = repository.findById(id).orElseThrow(() -> new PageNotFoundException("Page not found"));
		if(page!=null && page.getDateOfBirth().equals(LocalDate.now())) {
			return true;
		}
		return false;
	}

	@Override
	public List<PageResponseDTO> getAllPublicPages() {
		// TODO Auto-generated method stub
		return repository.findByVisibility(Visibility.PUBLIC)
                .stream()
                .map(page -> modelMapper.map(page, PageResponseDTO.class))
                .toList();
	}

}
