package com.roles.authenticateroles.cart;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface CartRepository extends MongoRepository<Cart, String> {

	@Query("{'id':?0}")
	@Update("$set{'quantity':?1}")
	void updateById(String id, int quantity);


}
