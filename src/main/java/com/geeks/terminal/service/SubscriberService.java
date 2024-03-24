package com.geeks.terminal.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface SubscriberService {

	ResponseEntity<String> subscriber(Map<String, String> requestMap);

	ResponseEntity<String> client(Map<String, String> requestMap);

}
