package com.bazra.usermanagement.request;

import javax.validation.constraints.NotBlank;

public class ValidatePassRequest {
@NotBlank
private String password;

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

}
