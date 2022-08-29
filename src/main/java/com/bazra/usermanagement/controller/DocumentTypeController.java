package com.bazra.usermanagement.controller;

import java.time.LocalDateTime;
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
import com.bazra.usermanagement.model.Document_Type;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.DocumentTypeRepository;
import com.bazra.usermanagement.request.CreateDocumentTypeRequest;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.SuccessMessageResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/DocumentType")
public class DocumentTypeController {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	DocumentTypeRepository documentTypeRepository;
	@PostMapping("/Create")
	public ResponseEntity<?> createDocumentType(@RequestBody CreateDocumentTypeRequest createDocumentTypeRequest, Authentication authentication){
		Optional<Document_Type> documenttype = documentTypeRepository.findByName(createDocumentTypeRequest.getName());
		Account adminAccount= accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		
		if (!adminAccount.getType().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		if (documenttype.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Document already exists!"));
		}
		if (createDocumentTypeRequest.getDescription().isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("Description cannot be empty!"));
		}
		if (createDocumentTypeRequest.getName().isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("Name of document cannot be empty!"));
		}
		Document_Type document_Type = new Document_Type(createDocumentTypeRequest.getName(), createDocumentTypeRequest.getDescription(), adminAccount.getId());
		document_Type.setCreated_date(LocalDateTime.now());
		documentTypeRepository.save(document_Type);
		return ResponseEntity.ok(new SuccessMessageResponse("Created Document Type successfully!!"));
	}
	

}
