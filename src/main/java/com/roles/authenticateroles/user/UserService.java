package com.roles.authenticateroles.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.roles.authenticateroles.cart.Cart;

@Service
public class UserService {

	@Autowired
	MongoTemplate mongoTemplate;

	public User updateNameByUsername(String username, String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		Update update = new Update();

		update.set("name", name);
		FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
		return mongoTemplate.findAndModify(query, update, options, User.class, "user");
	};

	public User addProductToCartByUsername(String username, Cart cart) {
		FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		Update update = new Update();

		update.addToSet("cart", cart);

		User user = mongoTemplate.findAndModify(query, update, options, User.class, "user");
		System.out.println(user);
		return user;
	}

	public Cart UpdateProductToCartByUsername(String username, Cart cart) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(cart.getId()));
		Update update = new Update();
		update.set("quantity", cart.getQuantity());
		FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
		Cart carts = mongoTemplate.findAndModify(query, update, options, Cart.class, "cart");
		return carts;
	}

	public User deleteCartItemByUsername(String username, Cart cart) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(cart.getId()));
		Update update = new Update();
		update.pull("cart", cart);
		FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
		User user = mongoTemplate.findAndModify(query, update, options, User.class, "user");
		return user;
	}

	public List<Cart> findCartByItemsername(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("user").is(id));

		List<Cart> carts = mongoTemplate.find(query, Cart.class, "cart");
		return carts;
	}

}
