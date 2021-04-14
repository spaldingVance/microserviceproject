package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.NewUserRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.BcryptGenerator;
import com.example.demo.security.JwtRequestFilter;
import com.example.demo.service.JwtUtilService;
import com.example.demo.service.UserDetailsServiceImpl;
import com.example.demo.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AuthenticationController {

	private final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl myUserDetailsService;

	@Autowired
	private JwtUtilService jwtTokenUtil;

	@Autowired
	private UserService userService;
	
	@Autowired
	private BcryptGenerator bcryptgenerator;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<Map<String, String>> createAuthenticationToken( @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		logger.info("Made it to /authenticate ~~~~~~~");
		logger.info(authenticationRequest.getUserid());

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUserid(), authenticationRequest.getPassword()));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}

		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUserid());

		final String token = jwtTokenUtil.generateToken(userDetails);

		User loggedUser = userRepository.findByUserid(authenticationRequest.getUserid());

		String name = loggedUser.getName();

		String authority = "USER";

		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		response.put("name", name);
		response.put("credentials", authority);

		System.out.println(response);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/authenticate/new")
	public User createNewUser(@RequestBody NewUserRequest newUserRequest) {
		User newUser = new User();
		newUser.setUserid(newUserRequest.getUserid());
		newUser.setName(newUserRequest.getName());
		newUser.setPassword(bcryptgenerator.passwordEncoder(newUserRequest.getPassword()));
		newUser.setAge(newUserRequest.getAge());
		newUser.setRole(newUserRequest.getRole());

		return userService.saveNewUser(newUser);

	}

	@GetMapping(value = "/authenticate/hello")
	public String getHello() {
		return "Hello";
	}

}