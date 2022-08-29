package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazra.usermanagement.model.LocalTransfer;

public interface LocalTransferRepository extends JpaRepository<LocalTransfer, Integer> {
	Optional<LocalTransfer> findByPin(String title);

}
