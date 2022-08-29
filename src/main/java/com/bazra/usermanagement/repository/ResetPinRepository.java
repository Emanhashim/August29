package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazra.usermanagement.model.Promotion;
import com.bazra.usermanagement.model.ResetPin;

public interface ResetPinRepository extends JpaRepository<ResetPin, Integer>{
	Optional<ResetPin> findByUserId(Integer id);
}
