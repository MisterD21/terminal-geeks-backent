package com.geeks.terminal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.geeks.terminal.entities.Clients;

@Repository
public interface ClientRepository extends MongoRepository<Clients, Integer> {

	@Query("{email:?0}")
	public Clients findByEmailId(String email);

}
