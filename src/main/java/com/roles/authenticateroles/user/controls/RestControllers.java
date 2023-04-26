package com.roles.authenticateroles.user.controls;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject.Type.JsonType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.models.GeoPosition;
import com.azure.maps.search.MapsSearchClient;
import com.azure.maps.search.MapsSearchClientBuilder;
import com.azure.maps.search.models.MapsSearchAddress;
import com.azure.maps.search.models.ReverseSearchAddressOptions;
import com.azure.maps.search.models.ReverseSearchAddressResult;
import com.azure.maps.search.models.ReverseSearchAddressResultItem;
import com.azure.maps.search.models.SearchAddressOptions;
import com.azure.maps.search.models.SearchAddressResult;
import com.azure.maps.search.models.SearchAddressResultItem;
import com.roles.authenticateroles.cart.Cart;
import com.roles.authenticateroles.cart.CartRepository;
import com.roles.authenticateroles.cart.Product;
import com.roles.authenticateroles.cart.ProductRepository;
import com.roles.authenticateroles.subscription.Subscription;
import com.roles.authenticateroles.subscription.SubscriptionRepository;
import com.roles.authenticateroles.user.CustomUserDetails;
import com.roles.authenticateroles.user.User;
import com.roles.authenticateroles.user.UserRepository;

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

	@PostMapping(value = "/addtocart")
	public void addToCart(Product product, Model model, HttpSession session) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Cart item = new Cart(product, 1);
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
			if (cart != null) {
				list = cart;
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
				cartRepository.save(item);
			}

			repository.updateUserCart(user.getUsername(), list);

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

	@GetMapping(value = "/maps/{search}")
	public List<GeoPosition> getMaps(@PathVariable("search") String search) {
		MapsSearchClientBuilder builder = new MapsSearchClientBuilder();
		AzureKeyCredential keyCredential = new AzureKeyCredential("1F0MfCFmUz9OcYnwfyC5A1cXVX1Thk-540niXCHDnvg");
		builder.credential(keyCredential);
		MapsSearchClient client = builder.buildClient();
		SearchAddressResult result = client.searchAddress(new SearchAddressOptions(search));
		List<GeoPosition> list = new ArrayList<>();
		if (result.getResults().size() > 0) {
			for (SearchAddressResultItem item : result.getResults()) {
				list.add(item.getPosition());
				System.out.format("The coordinates is (%.4f, %.4f)", item.getPosition().getLatitude(),
						item.getPosition().getLongitude());
			}
		}
		return list;
	}

	@GetMapping(value = "/maps")
	public List<MapsSearchAddress> getMaps(@RequestBody GeoPosition position) {
		MapsSearchClientBuilder builder = new MapsSearchClientBuilder();
		AzureKeyCredential keyCredential = new AzureKeyCredential("1F0MfCFmUz9OcYnwfyC5A1cXVX1Thk-540niXCHDnvg");
		builder.credential(keyCredential);
		MapsSearchClient client = builder.buildClient();
		ReverseSearchAddressResult result = client.reverseSearchAddress(new ReverseSearchAddressOptions(position));
		List<MapsSearchAddress> list = new ArrayList<>();
		if (result.getAddresses().size() > 0) {
			for (ReverseSearchAddressResultItem item : result.getAddresses()) {

				list.add(item.getAddress());
				System.out.println("address: " + item);
			}
		}
		return list;
	}

	@GetMapping(value = "/distance/{query}")
	public String distance(@PathVariable("query") String query) {

		String url = "https://atlas.microsoft.com/route/directions/json?api-version=1.0&query=" + query
				+ "&report=effectiveSettings&subscription-key=1F0MfCFmUz9OcYnwfyC5A1cXVX1Thk-540niXCHDnvg";
		ResponseEntity<JsonType> result = rest.getForEntity(url, JsonType.class);

		JsonType body = result.getBody();
		if (body != null) {
			return body.value();
		} else {
			return "No Response";
		}
	}

	@GetMapping(value = "/circledistance/{query}")
	public String circleDistance(@PathVariable("query") String query) {

		String url = "https://us.atlas.microsoft.com/spatial/greatCircleDistance/json?api-version=2022-08-01&query=query="
				+ query + "&subscription-key=1F0MfCFmUz9OcYnwfyC5A1cXVX1Thk-540niXCHDnvg";
		ResponseEntity<JsonType> result = rest.getForEntity(url, JsonType.class);

		JsonType body = result.getBody();
		if (body != null) {
			return body.value();
		} else {
			return "No Response";
		}

	}

	@GetMapping(value = "/subscription")
	public List<Subscription> subscriptions() {
		return subscriptionRepository.findAll();
	}

	@Transactional
	@PostMapping(value = "/user/product", produces = "application/json")
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


}
