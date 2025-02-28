package com.myhcl.security.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.myhcl.security.entity.User;
import com.myhcl.security.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService,UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public Long saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepo.save(user).getId();
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user=findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not exist"));
		return new org.springframework.security.core.userdetails.User(email,
				                                                                                                                   user.getPassword(),
				                                                                                                                    user.getRoles()
				                                                                                                                            .stream()
				                                                                                                                            .map(role->new SimpleGrantedAuthority(role))
				                                                                                                                            .collect(Collectors.toList())
				                                                                                                                    );
	}

}
