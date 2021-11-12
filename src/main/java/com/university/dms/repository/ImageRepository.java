package com.university.dms.repository;

import java.util.Optional;

import com.university.dms.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


public interface ImageRepository extends JpaRepository<Image, Long> {
	Optional<Image> findByName(String name);

	@Transactional
	void deleteByName(String name);
}
