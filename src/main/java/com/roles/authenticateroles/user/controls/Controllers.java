package com.roles.authenticateroles.user.controls;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.roles.authenticateroles.cart.Cart;
import com.roles.authenticateroles.cart.CartRepository;
import com.roles.authenticateroles.cart.Product;
import com.roles.authenticateroles.cart.ProductRepository;
import com.roles.authenticateroles.user.CustomUserDetails;
import com.roles.authenticateroles.user.User;
import com.roles.authenticateroles.user.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller()
public class Controllers {

	@Autowired
	UserRepository repository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CartRepository cartRepository;

	@GetMapping(value = "/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@GetMapping(value = "/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping(value = "/profile")
	public String profile(Model model) {
		return "profile";
	}

	@GetMapping(value = "/")
	public String homepage(Model model) {
		return "homepage";
	}

	@GetMapping(value = "/products")
	public String products(Model model) {
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);
		return "products";
	}

	@GetMapping(value = "/cart")
	public String cart(Model model) {

		return "cart";
	}

	@GetMapping(value = "/user/map")
	public String azureMap(Model model) {
		return "azuremaps";
	}

	@GetMapping(value = "/user")
	public String user(Model model) {
		return "user";
	}

	@GetMapping(value = "/admin")
	public String admin(Model model) {
		return "admin";
	}

	@Transactional
	@PostMapping(value = "/addtocart", consumes = "application/x-www-form-urlencoded")
	public String addToCart(String id, Model model, HttpSession session) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();

		Optional<Product> product = productRepository.findById(id);
		Cart item = new Cart(product.get(), 1);
		List<Cart> list = new ArrayList<>();
		session.setMaxInactiveInterval(60 * 5);
		if (auth == null || !auth.isAuthenticated()) {
		if (session.getAttribute("cart") == null) {
			System.out.println("new Cart Session");
			list.add(item);

			session.setAttribute("cart", list);
		} else {
			List<Cart> cart = (List<Cart>) session.getAttribute("cart");
			list = cart;
			boolean exist = false;
			for (Cart it : list) {
				if (it.getProduct().getId().equals(item.getProduct().getId())) {
					int index = list.indexOf(it);
					item = new Cart(it.getProduct(), it.getQuantity() + 1);
					list.set(index, item);

					exist = true;
				}

			}

			if (!exist) {
				list.add(item);

			}

			session.setAttribute("cart", list);

		}
	} else {
		CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
		System.out.println(details.toString());
		User user = repository.findByUsername(details.getUsername()).get();
		System.out.println(user.toString());

		List<Cart> cart = user.getCart();
		if (cart != null) {			list = cart;
		}

		boolean exist = false;
		for (Cart it : list) {
			if (it.getProduct().getId().equals(item.getProduct().getId())) {
				int index = list.indexOf(it);
				item = new Cart(it.getProduct(), it.getQuantity() + 1);
				list.set(index, item);
				cartRepository.updateById(item.getId(), item.getQuantity());
				exist = true;
			}

		}

		if (!exist) {
			list.add(item);
			item.setUser(user);
			cartRepository.save(item);
		}

		user.setCart(list);

		// repository.addUserCart(user.getUsername(), list);
		repository.updateNameByUsername(user.getUsername(), "Bhagchand jat");


	}
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);
		model.addAttribute("addToCart", "Added Successfully to Cart");
		return "products";
	}

	@Transactional
	@PostMapping(value = "/signup", consumes = "application/x-www-form-urlencoded")
	public ModelAndView register(User user, ModelAndView model) {

		Optional<User> us = repository.findByUsername(user.getUsername());
        
		if (us.isEmpty()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));
			user.setRoles("USER");
			repository.save(user);
			model.addObject("success", "Successfully Created");
			model.setStatus(HttpStatus.OK);
			model.setViewName("login");

		} else {

			model.addObject("error", "User Already Exist");
			model.setStatus(HttpStatus.ALREADY_REPORTED);
			model.setViewName("signup");
		}

		return model;

	}

	@GetMapping(value = "product")
	public String product(Model model) {
		model.addAttribute("product", new Product());
		return "product";
	}

	@Transactional
	@PostMapping(value = "/product", consumes = "application/x-www-form-urlencoded")
	public String addProduct(Product product) {

		product.setDate(LocalDateTime.now().toString());
		productRepository.save(product);
		return "login";
	}

	@Transactional
	@PostMapping(value = "/user/product", consumes = "application/x-www-form-urlencoded")
	public String addProductToUser(Product product) {

		product.setDate(LocalDateTime.now().toString());
		productRepository.save(product);
		return "login";
	}


}
