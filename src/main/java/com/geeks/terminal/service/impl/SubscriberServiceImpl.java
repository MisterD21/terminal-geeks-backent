package com.geeks.terminal.service.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.geeks.terminal.constants.TGConstants;
import com.geeks.terminal.entities.Clients;
import com.geeks.terminal.entities.Subscriber;
import com.geeks.terminal.repositories.ClientRepository;
import com.geeks.terminal.repositories.SubscriberRepository;
import com.geeks.terminal.service.SequenceGeneratorService;
import com.geeks.terminal.service.SubscriberService;
import com.geeks.terminal.util.EmailUtils;
import com.geeks.terminal.util.TGUtils;

@Service
public class SubscriberServiceImpl implements SubscriberService {

	@Autowired
	private SubscriberRepository subscriberRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@Override
	public ResponseEntity<String> subscriber(Map<String, String> requestMap) {
		try {
			Subscriber subscriber = subscriberRepository.findByEmailId(requestMap.get("email"));

			if (!Objects.isNull(subscriber)) {
				return TGUtils.getResponseEntity("You\'ve allready been subscribed our service",
						HttpStatus.BAD_REQUEST);
			} else {
				subscriberRepository.save(getSubscriberFromRequestMap(requestMap));

				emailUtils.sendSimpleMessage(requestMap.get("email"), "Subscribed!", "You\\'ve subscriber the updated",
						new ArrayList<String>());
				return TGUtils.getResponseEntity("Subscribed", HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Subscriber getSubscriberFromRequestMap(Map<String, String> requestMap) {
		Subscriber subscriber = new Subscriber();
		subscriber.setId(sequenceGeneratorService.generateSequence("users_sequence"));
		subscriber.setEmail(requestMap.get("email"));
		subscriber.setName(requestMap.get("name"));
		return subscriber;
	}

	@Override
	public ResponseEntity<String> client(Map<String, String> requestMap) {
		try {
			Clients client = clientRepository.findByEmailId(requestMap.get("email"));

			if (Objects.isNull(client)) {
				clientRepository.save(getClientsFromRequestMap(requestMap));
			}
			emailUtils.sendSimpleMessage(requestMap.get("email"), "Client-Request!", requestMap.get("message"),
					new ArrayList<String>());
			return TGUtils.getResponseEntity("we\'ll contact you soon", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return TGUtils.getResponseEntity(TGConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Clients getClientsFromRequestMap(Map<String, String> requestMap) {
		Clients clients = new Clients();
		clients.setId(sequenceGeneratorService.generateSequence("users_sequence"));
		clients.setEmail(requestMap.get("email"));
		clients.setName(requestMap.get("name"));
		clients.setMessage(requestMap.get("message"));
		return clients;
	}

}
