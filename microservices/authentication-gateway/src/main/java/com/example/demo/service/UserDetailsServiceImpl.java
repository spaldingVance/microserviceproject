package com.example.demo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.AuthenticationUser;
import com.example.demo.model.CurrentUser;
import com.example.demo.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public CurrentUser loadUserByUsername(String userid) throws UsernameNotFoundException {
		System.out.println("In UserDetailsService loadByUsername");
		AuthenticationUser foundUser = userRepository.findByUserid(userid);
		
		if (foundUser == null) throw new UsernameNotFoundException("User does not exist");
		System.out.println("foundUser: " + foundUser.getUserid());
		System.out.println("foundPass: " + foundUser.getPassword());
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return CurrentUser.createCurrentUser(foundUser, authorities);
	}
	

}