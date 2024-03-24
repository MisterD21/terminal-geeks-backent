package com.geeks.terminal.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blog")
public class Blog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public static final String SEQUENCE_NAME="blog_sequence";
	
	@Id
	private Integer id;
	private String heading;
	private String text;
	private String image;
	private String authorName;
	private List<String> tags;
	private LocalDate date;
	private LocalTime localTime;
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public LocalTime getLocalTime() {
		return localTime;
	}
	public void setLocalTime(LocalTime localTime) {
		this.localTime = localTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	
}
