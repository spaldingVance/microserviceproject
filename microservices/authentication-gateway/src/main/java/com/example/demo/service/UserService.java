package com.example.demo.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.AuthenticationUser;
import com.example.demo.model.NewUserRequest;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	public AuthenticationUser saveNewUser(AuthenticationUser newUser) {
		AuthenticationUser user = userRepository.save(newUser);
		return user;

	}

	public AuthenticationUser findById(String userid) {
		AuthenticationUser user = userRepository.findByUserid(userid);
		return user;
	}

}

