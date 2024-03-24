package com.geeks.terminal.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subscriber")
public class Subscriber implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	public static final String SEQUENCE_NAME="subscriber_sequence";
	
	@Id
	private Integer id;
	private String name;
	private String email;
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
