package com.bazra.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.CommunicationMedium;
import com.bazra.usermanagement.model.Settings;

@Repository
   
public interface SendingMediumRepository extends JpaRepository<CommunicationMedium, Integer>  {

	Optional<CommunicationMedium> findByTitle(String title);
	
}
