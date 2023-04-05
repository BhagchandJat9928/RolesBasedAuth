package com.roles.authenticateroles.user.controls;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.roles.authenticateroles.user.User;
import com.roles.authenticateroles.user.UserRepository;

@Controller()
public class Controllers {
	
	@Autowired UserRepository repository;

	
	@GetMapping(value="/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@GetMapping(value="/login")
	public String login(Model model) {
		return "login";
	}
	
	
	
	@GetMapping(value="/user")
	public String user(Model model) {
		return "user";
	}
	
	@GetMapping(value="/admin")
	public String admin(Model model) {
		return "admin";
	}
	
	@PostMapping(value="/signup",consumes="application/x-www-form-urlencoded")
	public ModelAndView signup( User user,ModelAndView model) {
		System.out.println(user.getUsername());
		Optional<User> us=repository.findByUsername(user.getUsername());
		
		if(us.isEmpty()) {
			 BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));
			repository.save(user);
			model.addObject("success", "Successfully Created");
			model.setStatus(HttpStatus.OK);
			model.setViewName("login");
			
		}else {
			System.out.println(us);
			model.addObject("error", "User Already Exist");
			model.setStatus(HttpStatus.ALREADY_REPORTED);
			model.setViewName("signup");
		}
          
		
		return model;
		
	}

}
