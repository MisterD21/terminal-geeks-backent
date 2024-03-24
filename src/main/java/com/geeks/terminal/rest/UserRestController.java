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
import com.geeks.terminal.service.UserService;
import com.geeks.terminal.util.TGUtils;
import com.geeks.terminal.wrapper.UserWrapper;

@RestController
@RequestMapping("/user")
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap){
		try {
			return userService.signUp(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap){
		try {
			return userService.login(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<UserWrapper>>getAllUsers(){
		try {
			return userService.getAllUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/update")
	public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap){
		try {
			return userService.update(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/checktoken")
	public ResponseEntity<String> checkToken(){
		try {
			return userService.checkToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String, String> requestMap){
		try {
			return userService.changePassword(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/forgetpassword")
	public ResponseEntity<String> forgetPassword(@RequestBody(required = true) Map<String, String> requestMap){
		try {
			return userService.forgetPassword(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}