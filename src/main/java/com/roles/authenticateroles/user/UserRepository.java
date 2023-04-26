package com.roles.authenticateroles.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.roles.authenticateroles.cart.Cart;

public interface UserRepository extends MongoRepository<User, String> {
	
	
	public Optional<User> findByUsername(String username);
	
	@Query("{'username':?0}")
	@Update("$set{'roles':?1}")
	public void updateByUsername(String username,String role);

	@Query("{'username':?0}")
	@Update("$set{'cart':?1}")
	public void updateUserCart(String username, List<Cart> cart);

	@Query("{'username':?0}")
	@Update("$addField:{'cart':?1}")
	public void addUserCart(String username, List<Cart> cart);

	@Query("{'username':?0}")
	@Update("$set{'name':?1}")
	public void updateNameByUsername(String username, String name);



}
