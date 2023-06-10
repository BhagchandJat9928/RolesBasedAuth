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
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.roles.authenticateroles.cart.Cart;
import com.roles.authenticateroles.cart.CartRepository;
import com.roles.authenticateroles.cart.Product;
import com.roles.authenticateroles.cart.ProductRepository;
import com.roles.authenticateroles.user.User;
import com.roles.authenticateroles.user.UserRepository;
import com.roles.authenticateroles.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller()
public class Controllers {

	@Autowired
	UserRepository repository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	UserService service;

	// SecurityContext context = SecurityContextHolder.getContext();

	@GetMapping(value = "/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@GetMapping(value = "/login")
	public String login(HttpServletRequest request, HttpSession session) {
		if (session != null) {
			SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
			System.out.println("savedRequest" + savedRequest.getRedirectUrl());
		}

		return "login";
	}

	@GetMapping(value = "/user/profile")
	public String profile(Model model) {
		return "profile";
	}

	@GetMapping(value = "/")
	public String homepage(Model model) {
		return "homepage";
	}

	@GetMapping(value = "/user/products")
	public String products(Model model) {
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);
		return "products";
	}

	@GetMapping(value = "/cart")
	public String cart(Model model) {

		return "cart";
	}

	@GetMapping(value = "/user/userCart")
	public String userCart(Model model) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		User user = repository.findByUsername(auth.getName()).get();

		model.addAttribute("cartList", user.getCart());
		return "userCart";
	}



	@GetMapping(value = "/user")
	public String user(Model model) {
		return "user";
	}

	@GetMapping(value = "/admin")
	public String admin(Model model) {
		return "admin";
	}

	@GetMapping(value = "product")
	public String product(Model model) {
		model.addAttribute("product", new Product());
		return "product";
	}

	@PostMapping(value = "/user/cart/")
	public String updateCart(Cart cart, Model model) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		User user = service.deleteCartItemByUsername(auth.getName(), cart);
		model.addAttribute("cartList", user.getCart());
		return "userCart";
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@PostMapping(value = "/addtocart", consumes = "application/x-www-form-urlencoded")
	public String addToCart(String id, Model model, HttpSession session) {

		Optional<Product> product = productRepository.findById(id);
		Cart item = new Cart(product.get(), 1);
		List<Cart> list = new ArrayList<>();
		// session.setMaxInactiveInterval(60 * 5);
		if (session.getAttribute("cart") == null) {
			System.out.println("new Cart Session");
			list.add(item);

			session.setAttribute("cart", list);
		} else {
			list = (List<Cart>) session.getAttribute("cart");

			boolean exist = false;

			for (Cart it : list) {
				if (it.getProduct().getId().equals(item.getProduct().getId())) {
					int index = list.indexOf(it);
					item = new Cart(it.getProduct(), it.getQuantity() + 1);
					list.set(index, item);
					exist = true;
					break;
				}

			}

			if (!exist) {
				list.add(item);

			}

			session.setAttribute("cart", list);

		}

		// List<Product> products = productRepository.findAll();
		// model.addAttribute("products", products);
		model.addAttribute("addToCart", "Added Successfully to Cart");

		return "products";
	}

	@Transactional
	@PostMapping(value = "/user/addtocart", consumes = "application/x-www-form-urlencoded")
	public RedirectView addToUserCart(String id, RedirectAttributes model) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Optional<Product> product = productRepository.findById(id);
		Cart item = new Cart(product.get(), 1);
		User user = repository.findByUsername(auth.getName()).get();
		item.setUser(user);
		System.out.println(user);
		List<Cart> list = user.getCart();
		boolean exist = false;
		if (list.size() > 0) {
			for (Cart it : list) {
				if (it.getProduct().getId().equals(item.getProduct().getId())) {
					it.setQuantity(it.getQuantity() + 1);
					// cartRepository.updateById(item.getId(), item.getQuantity());
					service.UpdateProductToCartByUsername(auth.getName(), it);
					exist = true;
					break;
				}

			}
		}
		if (!exist) {
			Cart afterUpdate = cartRepository.save(item);
			// System.out.print(afterUpdate);
			service.addProductToCartByUsername(auth.getName(), afterUpdate);
			// System.out.println("user: " + result);
		}
		// List<Product> products = productRepository.findAll();
		// model.addAttribute("products", products);
		model.addAttribute("addToCart", "Added Successfully to Cart");
		RedirectView view = new RedirectView("/user/products");
		view.addStaticAttribute("addToCart", "Added Successfully to Cart");
		return view;
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
