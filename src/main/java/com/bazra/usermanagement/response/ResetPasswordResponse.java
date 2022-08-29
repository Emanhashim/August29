package com.bazra.usermanagement.response;

public class ResetPasswordResponse {

	private String message;
	private String pin;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public ResetPasswordResponse(String message, String pin) {
		super();
		this.message = message;
		this.pin = pin;
	}
	
}
