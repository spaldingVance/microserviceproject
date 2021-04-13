package com.example.demo.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrentUser implements UserDetails {
	
	private final User user;

	private final Set<GrantedAuthority> authorities = new HashSet<>();

    public void User() {
        authorities.add(new SimpleGrantedAuthority("USER"));
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }


	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPassword() { return user.getPassword(); }

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUserid();
	};
}
