package com.geeks.terminal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.geeks.terminal.entities.Blog;

@Repository
public interface BlogRepository extends MongoRepository<Blog, Integer>{

}
