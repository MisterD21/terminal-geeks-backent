package com.geeks.terminal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.geeks.terminal.entities.Subscriber;

public interface SubscriberRepository extends MongoRepository<Subscriber, Integer> {
	
	@Query("{email:?0}")
	public Subscriber findByEmailId(String email);

}
