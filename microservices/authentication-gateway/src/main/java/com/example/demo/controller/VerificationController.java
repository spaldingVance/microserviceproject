//package com.example.demo.controller;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.model.AuthenticationUser;
//import com.example.demo.security.JwtRequestFilter;
//import com.example.demo.service.JwtUtilService;
//
//@CrossOrigin(origins = "http://localhost:3000")
//@RestController
//public class VerificationController {
//	
//	@Autowired
//	private JwtUtilService jwtUtilService;
//	
//	private final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
//	
//	@GetMapping(value = "/verify")
//	public ResponseEntity<String> getVerified(HttpServletRequest request) {
//		logger.info("Inside Verify");
//		AuthenticationUser loggedInUser = jwtUtilService.getLoggedInUser(request);
//		
//		return ResponseEntity.ok(loggedInUser.getRole());
//	}
//
//}
