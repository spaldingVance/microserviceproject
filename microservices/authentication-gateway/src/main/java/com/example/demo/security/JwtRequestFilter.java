package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.AuthenticationUser;
import com.example.demo.service.JwtUtilService;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtilService jwtUtilService;

	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		
		logger.info("request " + request.getHeaderNames());
		final String jwtToken = jwtUtilService.extractJwtFromRequest(request);
		logger.info("request " + request.getHeader("Authorization"));
		logger.info("JWT TOKEN: " + jwtToken);
		
		
		
		logger.info("getRolesFromToken in doFilterInternal: " + jwtUtilService.getRolesFromToken(jwtToken));
		
		if (jwtToken != null && jwtUtilService.validateToken(jwtToken)) {
			
			AuthenticationUser loggedInUser = jwtUtilService.getLoggedInUser(request);
			
//			UserDetails userDetails = new User(jwtUtilService.getUseridFromToken(jwtToken), null, jwtUtilService.getRolesFromToken(jwtToken));

//			logger.info(jwtUtilService.getRolesFromToken(jwtToken));
			
			UserDetails userDetails = new User(loggedInUser.getUserid(), loggedInUser.getPassword(), jwtUtilService.getRolesFromToken(jwtToken));

			
			logger.info("Inside JWT Request Filter");
			
//			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//					userDetails, null, userDetails.getAuthorities());
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
			userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
			
			
			logger.info("username Password Auth Token: " + usernamePasswordAuthenticationToken);
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		} else {
			logger.info(jwtToken);
			logger.info("Error setting security context");
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if (request.getRequestURI().startsWith("/authenticate")) {
			logger.info("context path starts with authenticate, shouldNotFilter returns true");
			logger.info("context path: " + request.getRequestURI());
			return true;
		} else {
			logger.info("context path: " + request.getRequestURI());
			logger.info("context path does not start with authenticate, shouldNotFilter returns false");
			return false;
		}
	}
}