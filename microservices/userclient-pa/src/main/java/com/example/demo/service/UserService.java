package com.example.demo.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.NewUserRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	public User saveNewUser(User newUser) {
		System.out.println("got to here");
		System.out.println(newUser.getUserid());
		User user = userRepository.save(newUser);
		if(user != null) {
			System.out.println("successfully saved user in userclient");
		} else {
			System.out.println("error saving user");
		}
		
		return user;

	}

	public User findById(String userid) {
		User user = userRepository.findByUserid(userid);
		System.out.println("Found User " + userid);
		System.out.println(user.getAge());
		return user;
	}

	public User updateUser(@Valid NewUserRequest user) {
		
		User userInDb = userRepository.findByUserid(user.getUserid());
		System.out.println(user.getName());
		userInDb.setName(user.getName());
		userInDb.setAge(user.getAge());
		userInDb.setPassword(user.getPassword());
		userInDb.setUserid(user.getUserid());
		System.out.println(userInDb.getName());
		System.out.println(userInDb.getAge());
		System.out.println(userInDb.getPassword());
		System.out.println(userInDb.getUserid());
		User updatedUser = userRepository.save(userInDb);
		return updatedUser;
	}
}

