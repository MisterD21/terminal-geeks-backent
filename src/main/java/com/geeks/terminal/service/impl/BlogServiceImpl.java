package com.geeks.terminal.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.geeks.terminal.constants.TGConstants;
import com.geeks.terminal.entities.Blog;
import com.geeks.terminal.repositories.BlogRepository;
import com.geeks.terminal.service.BlogService;
import com.geeks.terminal.service.SequenceGeneratorService;
import com.geeks.terminal.util.TGUtils;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	@Override
	public ResponseEntity<String> postBlog(Map<String, String> requestMap) {
		try {

			blogRepository.save(getBlogFromRequestMap(requestMap));

			return TGUtils.getResponseEntity("Post saved successfully", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Blog getBlogFromRequestMap(Map<String, String> requestMap) {
		Blog blog = new Blog();
		blog.setId(sequenceGeneratorService.generateSequence("users_sequence"));
		blog.setAuthorName(requestMap.get("authorName"));
		blog.setHeading(requestMap.get("heading"));
		
        Path source = Paths.get("/Users/nandandubey/Downloads/test.png");
        Path destinationDir = Paths.get("/Users/nandandubey/react-projects/startup-nextjs-main/public/images/blog");
        try {
        	String fileName = source.getFileName().toString();
            Files.copy(source, destinationDir.resolve(fileName));
            blog.setImage(requestMap.get("image"));
        } catch (IOException e) {
        	blog.setImage(requestMap.get(null));
        }
		
		
		String tagsString = requestMap.get("tags");
        String[] tagsArray = tagsString.split("\\s*,\\s*");
        List<String> tagsList = Arrays.asList(tagsArray);
		blog.setTags(tagsList);
		blog.setText(requestMap.get("text"));
		blog.setDate(LocalDate.now());
		blog.setLocalTime(LocalTime.now());
		return blog;
	}

	@Override
	public ResponseEntity<List<Blog>> getAllPost() {
		try {

			List<Blog> allPost = blogRepository.findAll();

			return new ResponseEntity<List<Blog>>(allPost, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Blog>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
