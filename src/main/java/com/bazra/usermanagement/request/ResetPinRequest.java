package com.bazra.usermanagement.request;

import javax.validation.constraints.NotBlank;

public class ResetPinRequest {
	@NotBlank
	private String question;
	@NotBlank
	private String answer;
	@NotBlank
	private String phone;
	
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	

}
