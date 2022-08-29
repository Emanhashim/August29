package com.bazra.usermanagement.request;

import javax.validation.constraints.NotBlank;

public class CreateBankRequest {
	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
