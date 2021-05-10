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
		if(userRepository.existsById(newUser.getUserid())) {
			return null;
		}
		AuthenticationUser user = userRepository.save(newUser);
		return user;

	}
	
	public AuthenticationUser saveUser(AuthenticationUser user) {
		AuthenticationUser dbUser = userRepository.findByUserid(user.getUserid());
		dbUser.setPassword(user.getPassword());
		dbUser.setRole(user.getRole());
		return userRepository.save(dbUser);
	}

	public AuthenticationUser findById(String userid) {
		AuthenticationUser user = userRepository.findByUserid(userid);
		return user;
	}

}

