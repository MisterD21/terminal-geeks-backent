package com.geeks.terminal.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.geeks.terminal.entities.Blog;

public interface BlogService {

	ResponseEntity<String> postBlog(Map<String, String> requestMap);

	ResponseEntity<List<Blog>> getAllPost();

}
