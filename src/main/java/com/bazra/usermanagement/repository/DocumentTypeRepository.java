package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bazra.usermanagement.model.Document_Type;
@Repository
@Transactional(readOnly = true)
public interface DocumentTypeRepository extends JpaRepository<Document_Type, Integer> {
	Optional<Document_Type> findByName(String name);
	Optional<Document_Type> findById(Integer id);
}
