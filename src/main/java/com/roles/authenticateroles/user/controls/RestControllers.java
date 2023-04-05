package com.roles.authenticateroles.user.controls;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roles.authenticateroles.user.CustomUserDetails;
import com.roles.authenticateroles.user.User;
import com.roles.authenticateroles.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(value="/api")
public class RestControllers {
	
	@Autowired UserRepository repository;


	@PostMapping(value="/signup")
	public ResponseEntity<?> signup(@RequestBody User user,HttpServletRequest request) {
		ResponseEntity<?> response;
		if(repository.findByUsername(user.getUsername()).isEmpty()) {
			 BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));
			HttpSession session=request.getSession();
			String token=UUID.randomUUID().toString();
			session.setAttribute("refreshToken",token);
			user.setRefreshToken(token);
			repository.save(user);
			
			response=ResponseEntity.ok(user.toString());
		}else {
			response=ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("User Already Exist");
		}
		
		return response;
		
	}
	
	@GetMapping(value="/user")
	public CustomUserDetails user() {
		SecurityContext context=SecurityContextHolder.getContext();
		Authentication auth=context.getAuthentication();
		CustomUserDetails userDetails=(CustomUserDetails) auth.getPrincipal();
		System.out.println(auth.isAuthenticated());

		return userDetails;
	}
	
}
