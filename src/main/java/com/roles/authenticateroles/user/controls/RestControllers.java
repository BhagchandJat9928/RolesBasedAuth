package com.roles.authenticateroles.user.controls;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.roles.authenticateroles.cart.Cart;
import com.roles.authenticateroles.cart.CartRepository;
import com.roles.authenticateroles.cart.Product;
import com.roles.authenticateroles.cart.ProductRepository;
import com.roles.authenticateroles.subscription.Subscription;
import com.roles.authenticateroles.subscription.SubscriptionRepository;
import com.roles.authenticateroles.user.CustomUserDetails;
import com.roles.authenticateroles.user.User;
import com.roles.authenticateroles.user.UserRepository;
import com.roles.authenticateroles.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api")
public class RestControllers {

	@Autowired
	UserRepository repository;
	@Autowired
	SubscriptionRepository subscriptionRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	ProductRepository productRepository;

	RestTemplate rest = new RestTemplate();


	@Autowired
	UserService service;

	@Transactional
	@PostMapping(value = "/signup")
	public ResponseEntity<?> signup(@RequestBody User user, HttpServletRequest request) {
		ResponseEntity<?> response;
		System.out.println("helo");
		if (repository.findByUsername(user.getUsername()).isEmpty()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));
			HttpSession session = request.getSession();
			String token = UUID.randomUUID().toString();
			session.setAttribute("refreshToken", token);
			user.setRefreshToken(token);
			repository.save(user);

			response = ResponseEntity.ok(user.toString());
		} else {
			response = ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("User Already Exist");
		}

		return response;

	}

	@SuppressWarnings("unchecked")
	@PutMapping(value = "/addtocart")
	public void addToCart(@RequestBody Product product, Model model, HttpSession session) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Cart item = new Cart(product, 1);
		List<Cart> list = new ArrayList<>();
		// session.setMaxInactiveInterval(60 * 5);
		if (auth == null || !auth.isAuthenticated()) {
		if (session.getAttribute("cart") == null) {
				System.out.println("new Cart Session");
			list.add(item);
				session.setAttribute("cart", list);
		} else {
			list = (List<Cart>) session.getAttribute("cart");
				boolean exist = false;
				if (list.size() > 0) {
					int index=0;
			while(index<list.size()) {
				Cart it = list.get(index);
				if (it.getProduct().getId().equals(item.getProduct().getId())) {
						item = new Cart(it.getProduct(), it.getQuantity() + 1);
						list.set(index, item);
						exist = true;
						break;
				}
				index++;
				}
			}

				if (!exist) {
					list.add(item);
				}
				session.setAttribute("cart", list);

			}
		} else {

			User user = repository.findByUsername(auth.getName()).get();
			List<Cart> cart = user.getCart();
			boolean exist = false;
			if (cart != null) {
				list = cart;
				for (Cart it : list) {
					if (it.getProduct().getId().equals(item.getProduct().getId())) {
						int index = list.indexOf(it);
						item = new Cart(it.getProduct(), it.getQuantity() + 1);
						list.set(index, item);
						cartRepository.updateById(item.getId(), item.getQuantity());
						exist = true;
					}

				}

			}

			if (!exist) {
				Cart afterUpdate = cartRepository.save(item);
				service.addProductToCartByUsername(auth.getName(), afterUpdate);
			}


	}

		model.addAttribute("addToCart", "Added Successfully to Cart");

	}

	@GetMapping(value = "product")
	public List<Product> products() {
		return productRepository.findAll();
	}

	@GetMapping(value = "/user")
	public CustomUserDetails user() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		System.out.println(auth.isAuthenticated());

		return userDetails;
	}




	@GetMapping(value = "/subscription")
	public List<Subscription> subscriptions() {
		return subscriptionRepository.findAll();
	}

	@Transactional
	@PostMapping(value = "/product", produces = "application/json")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {

		product.setDate(LocalDateTime.now().toString());
		productRepository.save(product);
		return ResponseEntity.ok(product);
	}

	@Transactional
	@PostMapping(value = "/subscription")
	public ResponseEntity<?> addSubscriion(@RequestBody Subscription subscription) {
		subscriptionRepository.save(subscription);
		return ResponseEntity.ok(subscription);
	}
	
	@PutMapping(value = "/update/{username}/{name}")
	public User updateName(@PathVariable("username") String username, @PathVariable("name") String name) {

		return service.updateNameByUsername(username, name);
	}

	@DeleteMapping(value = "/delete/{username}")
	public void deleteUser(@PathVariable("username") String username) {
		repository.deleteByUsername(username);
	}

	@DeleteMapping(value = "/deleteById/{id}")
	public void deleteUserById(@PathVariable("id") String id) {
		repository.deleteById(id);
	}

	@GetMapping(value = "/findCart/{username}")
	public List<Cart> cart(@PathVariable("username") String username) {
		List<Cart> list=new ArrayList<>();


		Optional<User> user = repository.findByUsername(username);
		if (!user.isEmpty() && user.get().getCart().size() > 0) {
			list = service.findCartByItemsername(user.get().getId());
		/*
		 * Optional<Cart> cart=
		 * cartRepository.findById(user.get(0).getCart().get(0).getId()); if
		 * (!cart.isEmpty()) { list.add(cart.get()); }
		 */

	}
		return list;
	}

	@GetMapping(value = "/find")
	public Optional<User> getUser() {
		return repository.findByUsername("bhag");
	}

	@PutMapping(value = "/find")
	public int getName() {
		return repository.updateNameByUsername("bhag", "Ravan ");
	}







}
