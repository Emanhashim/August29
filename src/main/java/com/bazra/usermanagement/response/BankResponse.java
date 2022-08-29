package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bazra.usermanagement.model.Bank;
import com.bazra.usermanagement.model.MerchantInfo;

public class BankResponse {
	private Optional<Bank> bank;
	private List<Bank> banks = new ArrayList<>();

	
	public Optional<Bank> getBank() {
		return bank;
	}

	public void setBank(Optional<Bank> bank) {
		this.bank = bank;
	}

	public List<Bank> getBanks() {
		return banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}
	
	public BankResponse(List<Bank> banks) {
		
	
		this.banks=banks;
	}
	public BankResponse(Optional<Bank> bank) {
		
		
		this.bank=bank;
	}
}
