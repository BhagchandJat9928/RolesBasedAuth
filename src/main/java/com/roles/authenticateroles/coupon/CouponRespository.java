package com.roles.authenticateroles.coupon;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CouponRespository extends MongoRepository<Coupon, String> {

}
