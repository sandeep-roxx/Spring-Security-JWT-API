package com.myhcl.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myhcl.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findByEmail(String email);

}
