package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

//	@LoadBalanced
//	@Bean
//	RestTemplate template() {
//		return new RestTemplate();
//	}
//
//	private final RestTemplate restTemplate;
//
//	public AuthenticationController(RestTemplate restTemplate) {
//		this.restTemplate = restTemplate;
//	}

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
	@Autowired
	private JwtUtilService jwtUtilService;

	// route for User login into Auth gateway
//	@PostMapping("/authenticate")
//	public ResponseEntity<Map<String, String>> createAuthenticationToken(
//			@RequestBody String authenticationRequest) throws Exception {
//		ObjectMapper mapper = new ObjectMapper();
//		AuthenticationRequest authRequest = mapper.readValue(authenticationRequest, AuthenticationRequest.class);
//		logger.info("inside authenticate");
//
//		try {
//			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//					authRequest.getUserid(), authRequest.getPassword()));
//		} catch (DisabledException e) {
//			throw new Exception("USER_DISABLED", e);
//		} catch (BadCredentialsException e) {
//			throw new Exception("INVALID_CREDENTIALS", e);
//		}
//
//		final CurrentUser userDetails = myUserDetailsService.loadUserByUsername(authRequest.getUserid());
//		final String token = jwtTokenUtil.generateToken(userDetails);
//
//		// hard coding USER authority
//		String authority = "USER";
//
//		Map<String, String> response = new HashMap<>();
//		response.put("token", token);
//		response.put("credentials", authority);
//		
//		logger.info("RESPONSE: ");
//		logger.info(response.toString());
//		logger.info(response.getClass().getName());
//
//		return ResponseEntity.ok(response);
//	}
	
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

	// route for getting a user's information
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getUser(ProxyExchange<byte[]> proxy, @PathVariable String userId) throws Exception {
		logger.info("Inside get user loadbalancer route");
		return proxy.uri("http://localhost:8080/user/" + userId)
				.get(response -> ResponseEntity.status(response.getStatusCode()) //
				.headers(response.getHeaders()) //
				.body(response.getBody()) //
		);
	}

	// route for registering a new user into the authentication database
	@PostMapping("/authenticate/new")
	public ResponseEntity<?> createNewUser(ProxyExchange<byte[]> proxy, @RequestBody NewUserRequest newUserRequest) {
		logger.info("Authenticate New");

		String passEncoded = bcryptgenerator.passwordEncoder(newUserRequest.getPassword());
		AuthenticationUser newUser = new AuthenticationUser(newUserRequest.getUserid(), passEncoded, "ROLE_USER");
		AuthenticationUser savedUser = userService.saveNewUser(newUser);

		if (savedUser != null) {
			logger.info("successfull saved user");
			logger.info("Redirecting");
			return proxy.uri("http://localhost:8080/user/register")
					.post(response -> ResponseEntity.status(response.getStatusCode()) //
							.headers(response.getHeaders()) //
							.body(response.getBody()) //
					);
		} else {
			logger.info("Error saving user to db");
			return new ResponseEntity<String>("Error saving auth user to db", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// route for updating a user's information
	@PostMapping("/user/update")
	public ResponseEntity<?> updateUser(ProxyExchange<byte[]> proxy) throws Exception {
//		return proxy.uri("http://localhost:8083/user/update").post();
		return proxy.uri("http://localhost:8080/user/update").post();
		
	}

	// route for displaying a message on the /user/welcome page indicate a
	// successful login
	@RequestMapping("/user/welcome")
	public ResponseEntity<?> welcome(ProxyExchange<byte[]> proxy) throws Exception {
		return proxy.uri("http://localhost:8080/user/welcome").get();
	}

	// route to validate JWT from front end
//	@GetMapping(value = "/verify")
	
//	@PostMapping(value = "/verify")
//	public ResponseEntity<String> getVerified(@RequestBody HashMap<String, HashMap<String, String>> request) {
//		logger.info("Inside Verify");
//		AuthenticationUser loggedInUser = jwtUtilService.getLoggedInUserMap(request);
//
//		return ResponseEntity.ok(loggedInUser.getRole());
//	}
	
	@GetMapping(value = "/verify")
	public ResponseEntity<String> getVerified(HttpServletRequest request) {
		logger.info("Inside Verify");
		AuthenticationUser loggedInUser = jwtUtilService.getLoggedInUser(request);
		
		return ResponseEntity.ok(loggedInUser.getRole());
	}

}