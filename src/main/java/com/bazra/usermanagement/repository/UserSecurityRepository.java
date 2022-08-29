package com.bazra.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.UserSecurityQuestion;
@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurityQuestion, Integer> {
	Optional<UserSecurityQuestion> findById(Integer id);
//	Optional<UserSecurityQuestion> findByUserId(Integer id);
	List<UserSecurityQuestion> findByUserId(Integer id);
	Optional<UserSecurityQuestion> findByQuestionId(Integer id);
	
}
