package com.bazra.usermanagement.controller;

import java.util.Optional;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.CommunicationMedium;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.MediaRepository;
import com.bazra.usermanagement.request.CreateMediaRequest;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.SuccessMessageResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/Medium")
@Api(value = "COMMUNICATION MEDIUM CONTROLLER", description = "CREATES MEDIUM OF SMS TEXT")
@ApiResponses(value ={
		@ApiResponse(code = 404, message = "web user that a requested page is not available "),
		@ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
		@ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
		@ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
		@ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
public class CommunicationMediumController {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	MediaRepository mediaRepository;

	@PostMapping("/CreateMedium")
	@ApiOperation(value = "CreateMedium")
	public ResponseEntity<?> createMedium(@RequestBody CreateMediaRequest createMediaRequest,
			Authentication authentication) {
		System.out.println(createMediaRequest.getTitle());
		Optional<CommunicationMedium> comOptional = mediaRepository.findByTitle(createMediaRequest.getTitle());
		Account adminAccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		
		if (!adminAccount.getType().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		if (comOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Media already exists"));
		}
		if (createMediaRequest.getTitle() == null) {
			System.out.println(createMediaRequest.getTitle());
			return ResponseEntity.badRequest().body(new ResponseError("Title cannot be empty!"));
		}
		CommunicationMedium communicationMedium = new CommunicationMedium();
		communicationMedium.setTitle(createMediaRequest.getTitle());
		mediaRepository.save(communicationMedium);
		return ResponseEntity.ok(new SuccessMessageResponse("Media created successfully!!"));

	}

}
