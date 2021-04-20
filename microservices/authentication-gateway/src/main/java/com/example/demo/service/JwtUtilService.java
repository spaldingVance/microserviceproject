package com.example.demo.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.model.AuthenticationUser;
import com.example.demo.model.CurrentUser;
import com.example.demo.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtUtilService {

	@Autowired
	UserRepository userRepository;

	private String secret;
	private int jwtExpirationInMs;

	private Logger logger = LoggerFactory.getLogger(JwtUtilService.class);

	@Value("secret")
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Value("10000000")
	public void setJwtExpirationInMs(int jwtExpirationInMs) {
		this.jwtExpirationInMs = jwtExpirationInMs;
	}

	public String generateToken(CurrentUser userDetails) {
		Map<String, Object> claims = new HashMap<>();
		logger.info("In Generate Token");
		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
		
//		Collection<? extends GrantedAuthority> credentials
		
		logger.info("userDetails in generateToken: " + userDetails);
		logger.info("Roles in generateToken: " + roles);
		logger.info("Roles in generateToken: (from other) " + userDetails);
		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			logger.info("claims - Admin");
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			logger.info("claims- user");
			claims.put("isUser", true);
		} else {
			logger.info("Role does not have ROLE_USER");
		}
		logger.info("UserDetails getUsername in generateToken: " + userDetails.getUsername());
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public boolean validateToken(String authToken) {
			logger.info("authToken in JwtUtilService: " + authToken);
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
			logger.info("in validateToken");
			return true;
		} catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		}
	}

	public String getUseridFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

		logger.info(claims.getSubject());
		return claims.getSubject();
	}

	public String extractJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		logger.info("bearer token: " + bearerToken);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public AuthenticationUser getLoggedInUser(HttpServletRequest request) {
		String token = extractJwtFromRequest(request);
		String userid = getUseridFromToken(token);
		
		logger.info("in getLoggedInUser() userid: " + userid);

		AuthenticationUser user = userRepository.findByUserid(userid);
		return user;
	}

	public List<SimpleGrantedAuthority> getRolesFromToken(String authToken) {
		List<SimpleGrantedAuthority> roles = null;
		logger.info(secret);
		logger.info(authToken);
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
		logger.info("Claims: " + String.valueOf(claims));
	

		Boolean isAdmin = claims.get("isAdmin", Boolean.class);
		Boolean isUser = claims.get("isUser", Boolean.class);
		
		
		logger.info("is User? : " + isUser);
		
		if (isAdmin != null && isAdmin == true) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		if (isUser != null && isUser == true) {
		roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		}
		logger.info(roles.toString());
		return roles;
	}

}
