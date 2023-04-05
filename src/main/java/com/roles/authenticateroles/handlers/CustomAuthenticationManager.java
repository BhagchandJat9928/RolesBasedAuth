package com.roles.authenticateroles.handlers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.roles.authenticateroles.user.User;
import com.roles.authenticateroles.user.UserRepository;

//@Service
public class CustomAuthenticationManager {
	
	/*
	 * @Autowired UserRepository repository;
	 * 
	 * @Override public Authentication authenticate(Authentication authentication)
	 * throws AuthenticationException { String username=authentication.getName();
	 * String password=authentication.getCredentials().toString();
	 * System.out.println(username+" "+password); if(username!=null &&
	 * password!=null) {
	 * 
	 * Optional<User> us=repository.findByUsername(username); User user=us.get();
	 * System.out.println(user); BCryptPasswordEncoder passwordEncoder = new
	 * BCryptPasswordEncoder(); if(!us.isEmpty() &&passwordEncoder.matches(password,
	 * user.getPassword()) ) { return new UsernamePasswordAuthenticationToken(
	 * username,password,List.of(new SimpleGrantedAuthority("ROLE_"+user.getRoles())
	 * ));
	 * 
	 * }else { throw new BadCredentialsException("Bad Credential"); } }else { throw
	 * new BadCredentialsException("Username or Password Cannot be null"); }
	 * 
	 * 
	 * 
	 * }
	 */
	

}