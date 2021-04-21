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
import com.example.demo.model.AuthenticationUser;
import com.example.demo.model.CurrentUser;
import com.example.demo.model.NewUserRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.BcryptGenerator;
import com.example.demo.security.JwtRequestFilter;
import com.example.demo.service.JwtUtilService;
import com.example.demo.service.UserDetailsServiceImpl;
import com.example.demo.service.UserService;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class.getName());

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

	@PostMapping("/authenticate")
	public ResponseEntity<Map<String, String>> createAuthenticationToken( @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		logger.info("Made it to /authenticate ~~~~~~~");
		logger.info(authenticationRequest.getUserid());
		logger.info(authenticationRequest.getPassword());
		
		String passEncoded = bcryptgenerator.passwordEncoder(authenticationRequest.getPassword());
		

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUserid(), authenticationRequest.getPassword()));
		} catch (DisabledException e) {
			
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		
		

		final CurrentUser userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUserid());

		final String token = jwtTokenUtil.generateToken(userDetails);
		String authority = "USER";
		

		Map<String, String> response = new HashMap<>();
		response.put("token", token);
//		response.put("name", name);
		response.put("credentials", authority);

		logger.info("response: " + response);
		logger.info("token: " + token);

		return ResponseEntity.ok(response);
	}


	@GetMapping(value = "/authenticate/hello")
	public String getHello() {
		return "Hello";
	}

}