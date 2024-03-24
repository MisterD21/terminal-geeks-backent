package com.geeks.terminal.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geeks.terminal.constants.TGConstants;
import com.geeks.terminal.entities.Blog;
import com.geeks.terminal.service.BlogService;
import com.geeks.terminal.util.TGUtils;

@RestController
@RequestMapping("/blog")
public class BlogRest {

	@Autowired
	private BlogService blogService;

	@PostMapping("/post")
	public ResponseEntity<String> post(@RequestBody(required = true) Map<String, String> requestMap){
		try {
			return blogService.postBlog(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/post/get")
	public ResponseEntity<List<Blog>> getAllPost(){
		try {
			return blogService.getAllPost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Blog>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
