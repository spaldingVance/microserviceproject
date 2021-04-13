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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{userid}")
	public ResponseEntity<?> getUser(@PathVariable @Valid String userid) {
		User user = userService.findById(userid);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return (ResponseEntity<?>) ResponseEntity.notFound();
		}
		
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid User user) {
		System.out.println("at /user/register");
		User registeredUser = userService.saveNewUser(user);
		
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
	public ResponseEntity<String> updateUser(@Valid @RequestBody NewUserRequest userRequest) {
		User user = userService.findById(userRequest.getUserid());
		System.out.println("IN /user/update route");
		System.out.println("Userid: " + userRequest.getUserid());
		User registeredUser = userService.updateUser(userRequest);
		if(registeredUser != null) {
			return new ResponseEntity<String>("User Updated", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	

}