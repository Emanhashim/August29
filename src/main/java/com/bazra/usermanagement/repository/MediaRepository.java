package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bazra.usermanagement.model.CommunicationMedium;
import com.bazra.usermanagement.model.Promotion;
@Repository
@Transactional(readOnly = true)
public interface MediaRepository extends JpaRepository<CommunicationMedium, String> {
	
	Optional<CommunicationMedium> findByTitle(String title);

}
