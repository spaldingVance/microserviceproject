package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.JwtUtilService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class VerificationController {
	
	@Autowired
	private JwtUtilService jwtUtilService;
	
	@GetMapping(value = "/verify")
	public ResponseEntity<String> getVerified(HttpServletRequest request) {
		User loggedInUser = jwtUtilService.getLoggedInUser(request);
		
		return ResponseEntity.ok(loggedInUser.getRole());
	}

}
