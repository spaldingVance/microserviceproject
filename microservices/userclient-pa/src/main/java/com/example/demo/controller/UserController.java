package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.NewUserRequest;
import com.example.demo.model.PayrollDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{userid}")
	public ResponseEntity<?> getUser(@PathVariable @Valid String userid) {
		NewUserRequest user = userService.findById(userid);
		
		return new ResponseEntity<NewUserRequest>(user, HttpStatus.OK);

	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody NewUserRequest userRequest)
			throws JsonMappingException, JsonProcessingException {

		NewUserRequest registeredUser = userService.saveNewUser(userRequest);

		if (registeredUser != null) {
			return new ResponseEntity<String>("Successfully registered", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PostMapping("/update")
	public ResponseEntity<String> updateUser(@Valid @RequestBody NewUserRequest userRequest) {
		NewUserRequest registeredUser = userService.updateUser(userRequest);
		if (registeredUser != null) {
			return new ResponseEntity<String>("User Updated", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}