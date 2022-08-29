package com.bazra.usermanagement.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.internal.build.AllowSysOut;
import org.hibernate.loader.entity.NaturalIdEntityJoinWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.CommunicationMedium;
import com.bazra.usermanagement.model.ResetPin;
import com.bazra.usermanagement.model.SecurityQuestion;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.model.UserSecurityQuestion;

import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.MediaRepository;
import com.bazra.usermanagement.repository.ResetPinRepository;
import com.bazra.usermanagement.repository.SecurityQuestionsRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.repository.UserSecurityRepository;
import com.bazra.usermanagement.request.ResetPasswordByPIN;
import com.bazra.usermanagement.request.ResetPinQuestionsRequest;
import com.bazra.usermanagement.request.ResetPinRequest;
import com.bazra.usermanagement.request.SignInRequest;
import com.bazra.usermanagement.response.ListOfResetPinQuestions;
import com.bazra.usermanagement.response.ResetPasswordResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.SignInResponse;
import com.bazra.usermanagement.response.SuccessMessageResponse;
import com.bazra.usermanagement.response.UpdateResponse;
import com.bazra.usermanagement.service.UserInfoService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/ResetPin")
@Api(value = "RESET PIN CONTROLLER", description = "RESET PASSWORD WITH GENERATING PIN AND PROMPT SECURITY QUESTIONS")
@ApiResponses(value ={
		@ApiResponse(code = 404, message = "web user that a requested page is not available "),
		@ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
		@ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
		@ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
		@ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
public class ResetPinController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserSecurityRepository userSecurityRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MediaRepository mediaRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ResetPinRepository resetPinRepository;
	@Autowired
	SecurityQuestionsRepository securityQuestionsRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private UserInfoService userInfoService;

	private UserInfo userInfo;
	

	private UserDetails userDetails;
	Timer timer;
	@PostMapping("/GenerateQuestion")
	@ApiOperation(value = "GENERATE QUESTIONS TO RESET PASSWORD")
	public ResponseEntity<?> questionsResetPin( @RequestBody ResetPinQuestionsRequest resetPinQuestionsRequest) {
		Account account = accountRepository.findByAccountNumberEquals(resetPinQuestionsRequest.getPhone()).get();
		
		List<UserSecurityQuestion> userSecurityQuestion = userSecurityRepository.findByUserId(account.getUser_id());
		List<SecurityQuestion> promotionsList = securityQuestionsRepository.findAll();
		Long count = securityQuestionsRepository.count();
	
		
		List<SecurityQuestion> securityQuestion1 = new ArrayList<>();
		List<Integer> index = new ArrayList<Integer>();
		
		for(int i=0;i<userSecurityQuestion.size();i++) {
			SecurityQuestion que1 = securityQuestionsRepository.findById(userSecurityQuestion.get(i).getQuestionId()).get();
			index.add(que1.getId());
			securityQuestion1.add(securityQuestionsRepository.findById(userSecurityQuestion.get(i).getQuestionId()).get());
		}
		List<Integer> numbers1 = new ArrayList<>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=1; i<count; i++) list.add(i);
        Collections.shuffle(list);
        for(int i=0;i<index.size();i++) {
        list.remove(Integer.valueOf(index.get(0)));
        }
        System.out.println("list"+list);
        for (int i=0; i<6; i++) numbers1.add(list.get(i));
		for(int i=0;i<numbers1.size();i++) {
			securityQuestion1.add(promotionsList.get(numbers1.get(i)));
			Collections.shuffle(securityQuestion1);
		}
		
		
		return ResponseEntity.ok(new ListOfResetPinQuestions(securityQuestion1));
	}
	
	@PostMapping("/GeneratePIN")
	@ApiOperation(value = "GENERATE PIN ONCE IT GET SECURITY QUESTION ANSWERED")
	public ResponseEntity<?> generateResetPin(@RequestBody ResetPinRequest resetpinRequest) {
		CommunicationMedium communicationMedium =mediaRepository.findByTitle("SMS").get();
		Integer userid=userRepository.findByUsername(resetpinRequest.getPhone()).get().getId();
		List<UserSecurityQuestion> userSecurityQuestions = userSecurityRepository.findByUserId(userid);
		LocalDateTime now = LocalDateTime.now();
		UserInfo userInfo2 = userRepository.findById(userid).get();
		List<String> questions = new ArrayList<String>();
		List<String> answers = new ArrayList<String>();
		for (int i = 0; i < userSecurityQuestions.size(); i++) {
			Integer questionId = userSecurityQuestions.get(i).getQuestionId();
			answers.add(userSecurityQuestions.get(i).getAnswer());
			SecurityQuestion securityQuestion =securityQuestionsRepository.findById(questionId).get();
			String question = securityQuestion.getQuestion();
			questions.add(question);
		}

		if (questions.contains(resetpinRequest.getQuestion())) {
			for (int i = 0; i < questions.size(); i++) {
				if(questions.get(i).matches(resetpinRequest.getQuestion())) {
					SecurityQuestion securityQuestion= securityQuestionsRepository.findByquestionName(resetpinRequest.getQuestion()).get();
					
					if (answers.get(i).matches(resetpinRequest.getAnswer())) {
						String pin ="";
						for (int j = 0; j < 6; j++) {
							
							pin=pin+randomNumberGenerator(0, 9);
						}
//						LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
						if (resetPinRepository.findByUserId(userid).isPresent()) {
							LocalDateTime expiration = LocalDateTime.now().plusMinutes(2);
//							userInfo2.setPassword(passwordEncoder.encode(pin));
//							userRepository.save(userInfo2);
							ResetPin resetPin = resetPinRepository.findByUserId(userid).get();
							resetPin.setSentTime(now);
							resetPin.setExpirationTime(expiration);
							resetPin.setUsed(false);
							resetPin.setPin(pin);
							resetPin.setSendingmediumId(communicationMedium.getId());
							resetPin.setUserId(userRepository.findByUsername(resetpinRequest.getPhone()).get().getId());
							resetPinRepository.save(resetPin);
							timer = new Timer();
					        timer.schedule(new RemindTask(resetPin,userid), 120*1000);
					        
							return ResponseEntity.ok(new ResetPasswordResponse("Auto generated PIN sent via SMS! " , resetPin.getPin()));
						}
						LocalDateTime expiration = LocalDateTime.now().plusMinutes(2);
//						userInfo2.setPassword(passwordEncoder.encode(pin));
//						userRepository.save(userInfo2);
						ResetPin resetPin = new ResetPin(pin);
						resetPin.setSentTime(now);
						resetPin.setExpirationTime(expiration);
						resetPin.setUsed(false);
						resetPin.setPin(pin);
						resetPin.setSendingmediumId(communicationMedium.getId());
						resetPin.setUserId(userRepository.findByUsername(resetpinRequest.getPhone()).get().getId());
						resetPinRepository.save(resetPin);
						timer = new Timer();
				        timer.schedule(new RemindTask(resetPin,userid), 120*1000);
						return ResponseEntity.ok(new ResetPasswordResponse("Auto generated PIN sent via SMS!" ,resetPin.getPin()));
					}
					return ResponseEntity.ok(new UpdateResponse("Not a valid answer!"));
					
				}
				
			}
			
		}
		
		
		
		
		return ResponseEntity.ok(new UpdateResponse("Not a valid question!"));
	}	
	
	
//	@PostMapping("/SetPassword")
//	public ResponseEntity<?> setPassword(@RequestBody ResetPasswordByPIN resetPasswordByPIN,Authentication authentication) {
//		Account account= accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//		UserInfo userInfo =userRepository.findById(account.getUser_id()).get();
//		System.out.println(account.getUser_id());
//		ResetPin resetOptional = resetPinRepository.findByUserId(account.getUser_id()).get();
//		String pin =resetOptional.getPin();
//		
//		System.out.println(pin);
//		System.out.println(resetPasswordByPIN.getPin());
//		if (pin.matches(resetPasswordByPIN.getPin())&&!resetOptional.isUsed()) {
//			if(resetPasswordByPIN.getPassword().matches(pin)) {
//	        	
//	            return ResponseEntity.badRequest().body(new UpdateResponse("Password cannot be same as pin"));
//	        }
//			if (resetPasswordByPIN.getPassword().matches(resetPasswordByPIN.getConfirmPassword())) {
//				userInfo.setPassword(passwordEncoder.encode(resetPasswordByPIN.getPassword()));
//				resetOptional.setUsed(true);
//				userRepository.save(userInfo);
//				resetPinRepository.save(resetOptional);
//				return ResponseEntity.ok(new SuccessMessageResponse("Successfully updated password!!"));
//			}
//			else {
//				return ResponseEntity.badRequest().body(new ResponseError("Passwords don't match"));
//			}
//		}
//		return ResponseEntity.badRequest().body(new ResponseError("Not a valid pin"));
//		
//	}
	


    class RemindTask extends TimerTask {
    	private UserInfo userInfo2;
    	private boolean resetPin;
    	private ResetPin resetPin2;
    	private LocalDateTime expirationDateTime;
    	private  RemindTask(ResetPin resetPin,int id) {
    		this.resetPin = resetPin.isUsed();
    		this.expirationDateTime=resetPin.getExpirationTime();
    		this.userInfo2 = userRepository.findById(id).get();
    		this.resetPin2 = resetPinRepository.findByUserId(id).get();
		}
        public void run() {
        	if (!resetPin && expirationDateTime.isBefore(LocalDateTime.now())) {
//				userInfo2.setPassword("null");
//				userRepository.save(userInfo2);
//				resetPin2.setIsUsed(true);
				resetPinRepository.delete(resetPin2);
				resetPinRepository.save(resetPin2);
				System.out.println("Not used");
				timer.cancel();
			}
        
        	System.out.println("Not used used");
        	timer.cancel();
        }
    }
	
	public static int randomNumberGenerator(int min, int max)
	{
		double r = Math.random();
		int randomNum = (int)(r * (max - min)) + min;
		return randomNum;
	}
	
	
	
}
