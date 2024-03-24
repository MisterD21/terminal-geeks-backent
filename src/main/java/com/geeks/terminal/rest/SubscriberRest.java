package com.geeks.terminal.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geeks.terminal.constants.TGConstants;
import com.geeks.terminal.service.SubscriberService;
import com.geeks.terminal.util.TGUtils;

@RestController
@RequestMapping("/subscriber")
public class SubscriberRest {
	
	@Autowired
	private SubscriberService subscriberService;

	@PostMapping("/subscribe")
	public ResponseEntity<String> subscriber(@RequestBody(required = true) Map<String, String> requestMap){
		try {
			return subscriberService.subscriber(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/client")
	public ResponseEntity<String> client(@RequestBody(required = true) Map<String, String> requestMap){
		try {
			return subscriberService.client(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
