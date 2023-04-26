package com.roles.authenticateroles.orders;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Orders, String> {

}
