package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bazra.usermanagement.model.SecurityQuestion;

public class ListOfQuestions {
		private List<?> list = new ArrayList<>();
		private List<SecurityQuestion> questions = new ArrayList<>();
		private List<SecurityQuestion> questions1 = new ArrayList<>();
		private Optional<SecurityQuestion> security;
		
		
		
		public Optional<SecurityQuestion> getSecurity() {
			return security;
		}


		public void setSecurity(Optional<SecurityQuestion> security) {
			this.security = security;
		}


		public List<SecurityQuestion> getQuestions1() {
			return questions1;
		}


		public void setQuestions1(List<SecurityQuestion> questions1) {
			this.questions1 = questions1;
		}


		public ListOfQuestions(List<SecurityQuestion> list) {
			this.questions=list;
		}


		public List<SecurityQuestion> getQuestions() {
			return questions;
		}


		public void setQuestions(List<SecurityQuestion> questions) {
			this.questions = questions;
		}

		public ListOfQuestions(Optional<SecurityQuestion> secOptional) {
			
			
			this.security=secOptional;
		}
		public ListOfQuestions(List<SecurityQuestion> questions,List<SecurityQuestion> questions1) {
			this.questions = questions;
			this.questions1 = questions1;
		}
	
}
