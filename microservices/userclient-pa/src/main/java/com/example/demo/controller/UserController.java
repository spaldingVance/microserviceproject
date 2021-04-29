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

import com.example.demo.model.NewUserRequest;
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
		System.out.println("in get user");
		System.out.println("Userid is : " + userid);
		User user = userService.findById(userid);
		System.out.println(user.getName());
//		System.out.println(user.toString());
	
		return new ResponseEntity<User>(user, HttpStatus.OK);
		
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody String userRequest) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		User userReq = mapper.readValue(userRequest, User.class);
		
		System.out.println("at /user/register");
//		System.out.println(user.getUserid());
		
		User registeredUser = userService.saveNewUser(userReq);
		
		if(registeredUser != null) {
			return new ResponseEntity<String>("Successfully registered", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@Valid User user) {
		System.out.println("Logging in~~~~~");
		User UseridExists = userService.findById(user.getUserid());

		String existingPassword = UseridExists.getPassword();
		String currentPassword = user.getPassword();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		return passwordEncoder.matches(currentPassword, ExistingPassword);

		if (passwordEncoder.matches(currentPassword, existingPassword)) {
			return new ResponseEntity<String>("Logged In Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Incorrect Username or Password", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("/update")
//	public ResponseEntity<String> updateUser(@Valid @RequestBody NewUserRequest userRequest) {
	public ResponseEntity<String> updateUser(@Valid @RequestBody String userRequest) throws JsonMappingException, JsonProcessingException {
		System.out.println("IN /user/update route");
		ObjectMapper mapper = new ObjectMapper();
//		User user = mapper.readValue(authenticationRequest, AuthenticationRequest.class);
		NewUserRequest userReq = mapper.readValue(userRequest, NewUserRequest.class);
		User user = userService.findById(userReq.getUserid());

		System.out.println("Userid: " + userReq.getUserid());
		User registeredUser = userService.updateUser(userReq);
		if(registeredUser != null) {
			return new ResponseEntity<String>("User Updated", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}