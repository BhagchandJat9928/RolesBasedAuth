package com.roles.authenticateroles.user.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roles.authenticateroles.cart.Cart;
import com.roles.authenticateroles.cart.Item;
import com.roles.authenticateroles.cart.Product;
import com.roles.authenticateroles.cart.ProductRepository;
import com.roles.authenticateroles.user.CustomUserDetails;
import com.roles.authenticateroles.user.User;
import com.roles.authenticateroles.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(value="/api")
public class RestControllers {
	
	@Autowired UserRepository repository;
	@Autowired ProductRepository productRepository;


	@Transactional
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
	
	@Transactional
	@PostMapping(value="product")
	public Product addProduct(@RequestBody Product product) {
		return productRepository.save(product);
	}
	
	@PostMapping(value="/addtocart")
	public void addToCart(Product product,Model model,HttpSession session) {
		Item item=new Item(product,1);
		List<Item> list=new ArrayList<>();
		if(session.getAttribute("cart")==null) {
			list.add(item);
			Cart cart=new Cart(list);
			session.setAttribute("cart", cart);
			System.out.println("new Cart Session");
		}else {
			Cart cart=(Cart)session.getAttribute("cart");
			System.out.println("cart: "+cart);
			for(Item it : cart.getItems()) {
				if(it.getProduct().getId().equals(item.getProduct().getId())) {
				      int index=  cart.getItems().indexOf(it);
				      Item ite=new Item(it.getProduct(),it.getQuantity()+1);
					cart.getItems().set(index, ite);
				}
			}
			session.setAttribute("cart", cart);
			
			
		}
		model.addAttribute("addToCart","Added Successfully to Cart");
		
	}
	
	@GetMapping(value="product")
	public List<Product> products() {
		return productRepository.findAll();
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
