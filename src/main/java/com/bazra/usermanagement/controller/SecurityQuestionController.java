package com.bazra.usermanagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.core.layout.YamlLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.Bank;
import com.bazra.usermanagement.model.Promotion;
import com.bazra.usermanagement.model.SecurityQuestion;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.SecurityQuestionsRepository;
import com.bazra.usermanagement.request.CreateQuestionRequest;
import com.bazra.usermanagement.request.PromotionUpdateRequest;
import com.bazra.usermanagement.request.QuestionUpdateRequest;
import com.bazra.usermanagement.response.BankResponse;
import com.bazra.usermanagement.response.ListOfQuestions;
import com.bazra.usermanagement.response.ListOfResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.SuccessMessageResponse;
import com.bazra.usermanagement.response.UpdateResponse;



@RestController
@CrossOrigin("*")
@RequestMapping("/Api/Question")
@Api(value = "SECURITY QUESTION CONTROLLER AND CRUD", description = "ADMIN SIDE CREATE, UPDATE, DELETE AND GET RANDOM AND AUTHORIZED SECURITY QUESTIONS HERE")
@ApiResponses(value ={
		@ApiResponse(code = 404, message = "web user that a requested page is not available "),
		@ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
		@ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
		@ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
		@ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
public class SecurityQuestionController {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	SecurityQuestionsRepository securityQuestionsRepository;
	
	
	@PostMapping("/CreateQuestion")
	@ApiOperation(value = "ONLY ADMIN CREATES SECURITY QUESTIONS")
	public ResponseEntity<?> createQuestion(@RequestBody CreateQuestionRequest createQuestionRequest, Authentication authentication){
		Account adminAccount= accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		
		if (!adminAccount.getType().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		if(createQuestionRequest.getQuestion()==null) {
			return ResponseEntity.badRequest().body(new ResponseError("Question cannot be empty!"));
		}
		Boolean xBoolean = securityQuestionsRepository.findByquestionName(createQuestionRequest.getQuestion()).isPresent();
		if(xBoolean) {
			return ResponseEntity.badRequest().body(new ResponseError("Question already exists!"));
		}
		SecurityQuestion securityQuestion = new SecurityQuestion(createQuestionRequest.getQuestion(),false,"English"); 
		securityQuestionsRepository.save(securityQuestion);
		return ResponseEntity.ok(new SuccessMessageResponse("Created Question successfully!!"));
	}
	
	@GetMapping("/All")
	@ApiOperation(value = "ONLY ADMIN ACCESS TO ALL SECURITY QUESTIONS")
	public ResponseEntity<?> all(@RequestParam Optional<String> sortBy,Authentication authentication) {
		Account adminAccount= accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		
		if (!adminAccount.getType().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		List<SecurityQuestion> securityQuestions = securityQuestionsRepository.findAll();
		if (securityQuestions.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("No Promotion found"));
		}
		return ResponseEntity.ok(new ListOfQuestions(securityQuestions));
	}
	@GetMapping("/All/{id}")
	@ApiOperation(value = "ONLY ADMIN ACCESS TO ALL SECURITY QUESTIONS BY ID")
	public ResponseEntity<?> all(Authentication authentication,@PathVariable Integer id) {
		
		Optional<SecurityQuestion> secuOptional = securityQuestionsRepository.findById(id);
		if (secuOptional.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("No Question with this ID"));
		}
		return ResponseEntity.ok(new ListOfQuestions(secuOptional));
	}
	

	@GetMapping("/GetRandom")
	@ApiOperation(value = "ALL USERS AND ADMIN ACCESS TO GET RANDOM SECURITY QUESTIONS")
	public ResponseEntity<?> random(@RequestParam Optional<String> sortBy) {
		
		List<SecurityQuestion> promotionsList = securityQuestionsRepository.findAll();
		if (promotionsList.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("No Promotion found"));
		}
		Long count = securityQuestionsRepository.count();
		List<Integer> numbers1 = new ArrayList<>();
		List<Integer> numbers2 = new ArrayList<>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		if(count<8) {
			return ResponseEntity.badRequest().body(new ResponseError("There are no enough questions!"));
		}
        for (int i=1; i<count; i++) list.add(i);
        Collections.shuffle(list);
        System.out.println("list"+list);
        for (int i=0; i<4; i++) numbers1.add(list.get(i));
        for (int i=4;i<8;i++) numbers2.add(list.get(i));

		List<SecurityQuestion> securityQuestions1 = new ArrayList<>();
		List<SecurityQuestion> securityQuestions2 = new ArrayList<>();
		for(int i=0;i<4;i++) {
			SecurityQuestion securityQuestion1 =promotionsList.get(numbers1.get(i));
			SecurityQuestion securityQuestion2 =promotionsList.get(numbers2.get(i));
			securityQuestions1.add(securityQuestion1);
			securityQuestions2.add(securityQuestion2);
		}
		return ResponseEntity.ok(new ListOfQuestions(securityQuestions1,securityQuestions2));
	}
	@DeleteMapping("/{id}")
	@ApiOperation(value = "ONLY ADMIN DELETE SECURITY QUESTION BY ID")
	public ResponseEntity<?> deletePromotion(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		if (securityQuestionsRepository.findById(id).isPresent()) {
			if(securityQuestionsRepository.getById(id).getStatus()==true) {
				return ResponseEntity.badRequest().body(new ResponseError("Question is being used! Cannot be deleted!"));
			}
			securityQuestionsRepository.deleteById(id);
			return ResponseEntity.ok(new UpdateResponse("Question Deleted successfully"));
		}
		else {
			return ResponseEntity.badRequest().body(new ResponseError("No Question found"));
		}
	}
	@PutMapping("/UpdateQuestion/{id}")
	@ApiOperation(value = "ONLY ADMIN ACCESS TO UPDATE SECURITY QUESTIONS")
	public ResponseEntity<?> updateSecurityQuestion(Authentication authentication,@RequestBody QuestionUpdateRequest questionUpdateRequest,@PathVariable int id) throws IOException {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		Optional<SecurityQuestion> securityOptional = securityQuestionsRepository.findById(id);
		if (!securityOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No Question with this ID was found"));
		}
		SecurityQuestion securityQuestion = securityOptional.get();
		if(questionUpdateRequest.getQuestion()!=null) {
			if( securityQuestion.getStatus()!=true) {
				Boolean b= securityQuestionsRepository.findByquestionName(questionUpdateRequest.getQuestion()).isPresent();
				if(b) {
					return ResponseEntity.ok(new UpdateResponse("Question already exists!"));
				}
			securityQuestion.setQuestion(questionUpdateRequest.getQuestion());
			securityQuestionsRepository.save(securityQuestion);
			return ResponseEntity.ok(new UpdateResponse("Question Updated successfully"));
			}
			return ResponseEntity.badRequest().body(new ResponseError("Question is being used! Cannot be updated!"));
		}
		return ResponseEntity.badRequest().body(new ResponseError("No update value provided!"));
		
	}
}
