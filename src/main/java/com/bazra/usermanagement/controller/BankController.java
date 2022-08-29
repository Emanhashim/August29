package com.bazra.usermanagement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.Bank;
import com.bazra.usermanagement.model.SecurityQuestion;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.BankRepository;
import com.bazra.usermanagement.request.CreateBankRequest;
import com.bazra.usermanagement.request.CreateQuestionRequest;
import com.bazra.usermanagement.response.BankResponse;
import com.bazra.usermanagement.response.ListOfQuestions;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.SuccessMessageResponse;
import com.bazra.usermanagement.response.UpdateResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/Bank")

@Api(value = "BANK CONTROLLER", description = "ADMIN ACTIVITIES ON BANK SYSTEM")
@ApiResponses(value ={
		@ApiResponse(code = 404, message = "web user that a requested page is not available "),
		@ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
		@ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
		@ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
		@ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
public class BankController {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	BankRepository bankRepository;
	
	@PostMapping("/CreateBank")

	@ApiOperation(value = "ADMIN CREATES BANKS OR REGISTER BANKS")
	public ResponseEntity<?> createBank(@RequestBody CreateBankRequest createBankRequest, Authentication authentication){
		Account adminAccount= accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		
		if (!adminAccount.getType().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		if(createBankRequest.getName()==null) {
			return ResponseEntity.badRequest().body(new ResponseError("Question cannot be empty!"));
		}
		Boolean xBoolean = bankRepository.findByName(createBankRequest.getName()).isPresent();
		if(xBoolean) {
			return ResponseEntity.badRequest().body(new ResponseError("Bank already added!"));
		}
		
		Bank bank = new Bank(createBankRequest.getName());
		bank.setCreated_date(LocalDate.now());
		bank.setCreator_id(adminAccount.getUser_id());
		bankRepository.save(bank);
		return ResponseEntity.ok(new SuccessMessageResponse("Bank added successfully!!"));
	}
	@GetMapping("/All")
	@ApiOperation(value = "ADMIN GETS ALL BANKS THAT REGISTERED")
	public ResponseEntity<?> allBanks(@RequestParam Optional<String> sortBy) {
		List<Bank> banks = bankRepository.findAll();
		if (banks.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("No Registered Banks"));
		}
		return ResponseEntity.ok(new BankResponse(banks));
	}
	@GetMapping("/All/{id}")
	@ApiOperation(value = "ADMIN CREATES BANKS OR REGISTER BANKS BY ID")
	public ResponseEntity<?> getBank(@PathVariable Integer id) {
		Optional<Bank> banks = bankRepository.findById(id);
		if (banks.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("No Registered Banks"));
		}
		return ResponseEntity.ok(new BankResponse(banks));
	}
	@DeleteMapping("/All/{id}")
	@ApiOperation(value = "ADMIN DELETES BANKS OR REGISTER BANKS BY ID")
	public ResponseEntity<?> deleteBank(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		if (bankRepository.findById(id).isPresent()) {
			bankRepository.deleteById(id);
			return ResponseEntity.ok(new UpdateResponse("Bank removed successfully"));
		}
		else {
			return ResponseEntity.badRequest().body(new ResponseError("No Bank found"));
		}
	}
}
