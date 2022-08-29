package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bazra.usermanagement.model.Bank;

@Repository
@Transactional(readOnly = true)
public interface BankRepository extends JpaRepository<Bank, Integer> {
	Optional<Bank> findByName(String name);
	Optional<Bank> findById(Integer id);

}
