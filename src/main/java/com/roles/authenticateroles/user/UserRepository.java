package com.roles.authenticateroles.user;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
	
	public Optional<User> findByUsername(String username);
	
	@Query("{'username':?0}")
	@Update("$set{'roles':?1}")
	public void updateByUsername(String username,String role);

}
