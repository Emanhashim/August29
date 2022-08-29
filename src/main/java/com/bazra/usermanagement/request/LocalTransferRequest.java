package com.bazra.usermanagement.request;

import javax.validation.constraints.NotBlank;

public class LocalTransferRequest {
	@NotBlank
	private String receiverName;
	@NotBlank
	private String receiverPhone;
	@NotBlank
	private String amount;
	@NotBlank 
	private String remark;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
