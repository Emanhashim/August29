package com.bazra.usermanagement.request;

import javax.validation.constraints.NotBlank;

public class CreateMediaRequest {
	@NotBlank(message = "Enter Media Type")
	
    private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	
}
