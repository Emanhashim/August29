package com.bazra.usermanagement.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.LocalTransfer;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.BankRepository;
import com.bazra.usermanagement.repository.BankTransferRepository;
import com.bazra.usermanagement.repository.LocalTransferRepository;
import com.bazra.usermanagement.request.BankTransferRequest;
import com.bazra.usermanagement.request.FinalizeLocalTransferRequest;
import com.bazra.usermanagement.request.LocalTransferRequest;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.UpdateResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api")
public class TransferController {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	BankTransferRepository bankTransferRepository;
	@Autowired
	BankRepository bankRepository;
	@Autowired
	LocalTransferRepository localTransferRepository;


	@PostMapping("/BankTransfer")
	public ResponseEntity<?> createBankTransfer(@RequestBody BankTransferRequest bankTransferRequest, Authentication authentication){
		Account fromAccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if(bankTransferRequest.getName()==null) {
			return ResponseEntity.badRequest().body(new ResponseError("Bank name cannot be empty!"));
		}
		if(bankTransferRequest.getAccountNumber()==null) {
			return ResponseEntity.badRequest().body(new ResponseError("You haven't inserted an account number"));
		}
		if(bankTransferRequest.getAmount()==null) {
			return ResponseEntity.badRequest().body(new ResponseError("Amount cannot be empty!"));
		}
		if (!bankRepository.findByName(bankTransferRequest.getName()).isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a registered bank"));
		}
		if (fromAccount.getBalance().compareTo(new BigDecimal(bankTransferRequest.getAmount())) == 1) {
			fromAccount.setBalance(fromAccount.getBalance().subtract(new BigDecimal(bankTransferRequest.getAmount())));
			accountRepository.save(fromAccount);
			return ResponseEntity.ok(new UpdateResponse("Transfer successful"));
		}
		return ResponseEntity.badRequest().body(new ResponseError("You don't have enough credit for this request!"));
		
	}
	@PostMapping("/LocalTransfer")
	public ResponseEntity<?> createLocalTransfer(@RequestBody LocalTransferRequest localTransferRequest, Authentication authentication){
		Account fromAccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if(localTransferRequest.getReceiverName()==null) {
			return ResponseEntity.badRequest().body(new ResponseError("Receiver name cannot be empty!"));
		}
		if(localTransferRequest.getReceiverPhone()==null) {
			return ResponseEntity.badRequest().body(new ResponseError("Receiver phone cannot be empty!"));
		}
		if(localTransferRequest.getAmount()==null) {
			return ResponseEntity.badRequest().body(new ResponseError("Amount cannot be empty!"));
		}
		System.out.println(new BigDecimal(localTransferRequest.getAmount()));
		System.out.println(fromAccount.getBalance());
		if (fromAccount.getBalance().compareTo(new BigDecimal(localTransferRequest.getAmount())) == 1) {
			
			String pin ="";
			for (int j = 0; j < 6; j++) {
				
				pin=pin+ResetPinController.randomNumberGenerator(0, 9);
			}
			System.out.println(pin);
			LocalTransfer localTransfer = new LocalTransfer(localTransferRequest.getReceiverName(),localTransferRequest.getReceiverPhone(),localTransferRequest.getAmount(),pin,fromAccount.getAccountNumber());
			localTransferRepository.save(localTransfer);
			return ResponseEntity.ok(new UpdateResponse("Transfer is being processed!"));
		}
		return ResponseEntity.badRequest().body(new ResponseError("You don't have enough credit for this request!"));
		
	}
//	@PostMapping("/FinalizeLocalTransfer")
//	public ResponseEntity<?> finalizeLocalTransfer(@RequestBody FinalizeLocalTransferRequest finalizeLocalTransferRequest, Authentication authentication){
//		Account agentaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//		if (!agentaccount.getType().matches("AGENT")) {
//			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
//		}
//		Optional<LocalTransfer> localOptional = localTransferRepository.findByPin(finalizeLocalTransferRequest.getPin());
//		if(!localOptional.isPresent()) {
//			return ResponseEntity.badRequest().body(new ResponseError("Not a valid PIN"));
//		}
//		LocalTransfer localTransfer = localOptional.get();
//		
//	}
	
	
}
