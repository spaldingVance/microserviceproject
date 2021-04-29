package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.repository.PayrollRepository;

@Service
public class BcryptGenerator {

	@Autowired
	private PayrollRepository userRepository;

	// TODO: setup spring security
	public String passwordEncoder(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}

	public Boolean passwordDecoder(String currentPassword, String ExistingPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(currentPassword, ExistingPassword);
	}
}
