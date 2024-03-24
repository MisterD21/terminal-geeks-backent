package com.geeks.terminal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.geeks.terminal.constants.TGConstants;
import com.geeks.terminal.entities.User;
import com.geeks.terminal.jwt.CustomerUserDetailsService;
import com.geeks.terminal.jwt.JwtRequestFilter;
import com.geeks.terminal.jwt.JwtUtils;
import com.geeks.terminal.repositories.UserRepository;
import com.geeks.terminal.service.SequenceGeneratorService;
import com.geeks.terminal.service.UserService;
import com.geeks.terminal.util.EmailUtils;
import com.geeks.terminal.util.TGUtils;
import com.geeks.terminal.wrapper.UserWrapper;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			if(validateSignUpMap(requestMap)) {
				User user = userRepository.findByEmailId(requestMap.get("email"));
				if(!Objects.isNull(user)) {
					return TGUtils.getResponseEntity(TGConstants.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
				}else {
					userRepository.save(getUserFromRequestMap(requestMap));
					return TGUtils.getResponseEntity(TGConstants.SUCCESS, HttpStatus.OK);
				}
			}else {
				return TGUtils.getResponseEntity(TGConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), 
							requestMap.get("password")));
			if(authentication.isAuthenticated()) {
				if(customerUserDetailsService.getUserDetails().getStatus().equals("TRUE")) {
					User user = userRepository.findByEmailId(requestMap.get("email"));
					String token = jwtUtils.generateToken(customerUserDetailsService.getUserDetails().getEmail(),
							customerUserDetailsService.getUserDetails().getRole());
					if(!Objects.isNull(user))
						return TGUtils.getResponseEntity(token, HttpStatus.OK);
					else
						return TGUtils.getResponseEntity("user is not available", HttpStatus.BAD_REQUEST);
				}else {
					return TGUtils.getResponseEntity("user is not available", HttpStatus.BAD_REQUEST);
				}
			}else {
				return TGUtils.getResponseEntity(TGConstants.WRONG_USERNAME_PASSWORD, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if(requestMap.containsKey("name") &&
				requestMap.containsKey("contactNumber") &&
				requestMap.containsKey("email") &&
				requestMap.containsKey("password")) {
			return true;
		}else {
			return false;
		}
	}
	
	private User getUserFromRequestMap(Map<String, String> requestMap) {
		User user = new User();
		user.setId(sequenceGeneratorService.generateSequence("users_sequence"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setName(requestMap.get("name"));
		user.setPassword(passwordEncoder.encode(requestMap.get("password")));
		user.setRole(requestMap.get("role"));
		user.setStatus(requestMap.get("status"));
		return user;
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUsers() {
		try {
			if(jwtRequestFilter.isAdmin()) {
				List<UserWrapper> allUser = userRepository.getAllUser();
				return new ResponseEntity<List<UserWrapper>>(allUser, HttpStatus.OK);
			}else {
				return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if(jwtRequestFilter.isAdmin()) {
				Optional<User> userById = userRepository.findById(Integer.parseInt(requestMap.get("id")));
				
				if(!userById.isEmpty()) {
					User user = userById.get();
					user.setStatus(requestMap.get("status"));
					List<UserWrapper> allAdmin = userRepository.getAllAdmin();
					List<String> listOfEmails = new ArrayList<String>();
					if(allAdmin.size()>0) {
					listOfEmails = allAdmin.stream()
                            .map(UserWrapper::getEmail)
                            .collect(Collectors.toList());
					}
					
					sendMailToAllAdmin(requestMap.get("status"), user.getEmail(), listOfEmails);
					userRepository.save(user);
					return TGUtils.getResponseEntity(TGConstants.SUCCESS, HttpStatus.OK);
				}else {
					return TGUtils.getResponseEntity("User not available", HttpStatus.OK);
				}

			}else {
				return TGUtils.getResponseEntity(TGConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

		allAdmin.remove(jwtRequestFilter.getCurrentUser());
		if(status!=null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtRequestFilter.getCurrentUser(), "Account Approved", "USER:-"+user+"\n is approved by \nADMIN:-"+jwtRequestFilter.getCurrentUser()+".", allAdmin);
			
		}else {
			emailUtils.sendSimpleMessage(jwtRequestFilter.getCurrentUser(), "Account Disabled", "USER:-"+user+"\n is disabled by \nADMIN:-"+jwtRequestFilter.getCurrentUser()+".", allAdmin);

		}
	}

	@Override
	public ResponseEntity<String> checkToken() {
		return TGUtils.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			User userObj = userRepository.findByEmailId(jwtRequestFilter.getCurrentUser());
			if(!userObj.equals(null)) {
				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(userObj.getEmail(), 
								requestMap.get("oldPassword")));
				if(authentication.isAuthenticated()) {
					userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
					userRepository.save(userObj);
					return TGUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);

				}else {
					return TGUtils.getResponseEntity(TGConstants.INCORRECT_OLD_PASSWORD, HttpStatus.BAD_REQUEST);
				}
			}
			return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {
		try {
			User user = userRepository.findByEmailId(requestMap.get("email"));
			if(!Objects.isNull(user) && !Strings.isEmpty(user.getEmail())) {
				emailUtils.forgetPasswordMail(user.getEmail(), "Credentials By Cafe Management System", user.getPassword());
			}
			return TGUtils.getResponseEntity("check your mail for credentials", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
}
