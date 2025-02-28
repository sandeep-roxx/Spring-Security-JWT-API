package com.myhcl.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.myhcl.security.filter.SecurityFilter;

import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private SecurityFilter securityFilter;
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(encoder);
		authProvider.setUserDetailsService(userDetailsService);
		return authProvider;
		
	}
	
	@Bean
	@SneakyThrows
	public AuthenticationManager authManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}
	
	@Bean
	@SneakyThrows
	public SecurityFilterChain filterChain(HttpSecurity http) {
		
		http.csrf(csrf->csrf.disable())
		        .authorizeHttpRequests(req->req
		        		.requestMatchers("/user/register","/user/login").permitAll()
		        		.anyRequest() .authenticated()
		        )
		        .exceptionHandling(ex -> ex //Register the Entry Point
		                .authenticationEntryPoint(authenticationEntryPoint) // Handle unauthorized access
		        )
		        .sessionManagement(session -> session
		                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No session for JWT
		        )
		        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);// Register custom filter
		return http.build();
	}

}
