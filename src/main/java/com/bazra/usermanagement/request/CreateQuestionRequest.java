package com.bazra.usermanagement.request;

import javax.validation.constraints.NotBlank;

public class CreateQuestionRequest {
	@NotBlank(message = "Enter Security Question")
	private String question;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	


}
