package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="authusers")
public class AuthenticationUser {
	@Id
	@Column(nullable = false, unique = true)
	private String userid;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String role;

}
