package com.myhcl.security.service;

import java.util.Optional;

import com.myhcl.security.entity.User;

public interface IUserService {
	
	public Long saveUser(User user);
	public Optional<User> findByEmail(String email);

}
