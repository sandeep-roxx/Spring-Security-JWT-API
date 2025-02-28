package com.myhcl.security.entity;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="user_tab")
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String password;
	private Long phone;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "role_tab",joinColumns = @JoinColumn(name="id"))
	@Column(name = "role")
	private Set<String> roles;

}
