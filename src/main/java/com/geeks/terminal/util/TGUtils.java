package com.geeks.terminal.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TGUtils {

	private TGUtils() {
		
	}
	
	public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus){
		return new ResponseEntity<String>("{\"message\":\""+message+"\"}",httpStatus);
	}
}
