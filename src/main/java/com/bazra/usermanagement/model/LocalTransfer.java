package com.bazra.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "localtransfer")
public class LocalTransfer {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
	private String receiverName;
	private String receiverPhone;
	private String amount;
	private String pin;
	private String fromAccount;
	
	
	public LocalTransfer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LocalTransfer(String receiverName, String receiverPhone, String amount, String pin,String from) {
		super();
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.amount = amount;
		this.pin = pin;
		this.fromAccount = from;
	}

	
	
	

	public int getId() {
		return id;
	}
	
	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	

}
