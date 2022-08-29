package com.bazra.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "security_questions")
public class SecurityQuestion {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "Question_id")
	private int id;
	@Column(unique=true)
	private String questionName;
	private Boolean status;
	private int count;
	private String language;
	
	
//	public SecurityQuestion() {
//		super();
//		// TODO Auto-generated constructor stub
//	}

	public SecurityQuestion(String questionName, Boolean status, int count, String language) {
		super();
		this.questionName = questionName;
		this.status = status;
		this.count = count;
		this.language = language;
	}
	
	public SecurityQuestion(String questionName, Boolean status, String language) {
		super();
		this.questionName = questionName;
		this.status = status;
		this.language = language;
	}

	public int getId() {
		return id;
	}
	
	public String getQuestion() {
		return questionName;
	}
	public void setQuestion(String question) {
		questionName = question;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	

}
