package com.personasphere.page.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personasphere.page.entity.PersonalPage;
import com.personasphere.page.entity.Visibility;

@Repository
public interface PersonalPageRepository extends JpaRepository<PersonalPage, Integer>{

	Optional<PersonalPage> findByUsername(String username);
	Optional<PersonalPage> findById(Long id);
	List<PersonalPage> findByVisibility(Visibility visibility);

	boolean existsByUsername(String username);

}
