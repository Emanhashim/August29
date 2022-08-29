package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;

import com.bazra.usermanagement.model.SecurityQuestion;

public class ListOfResetPinQuestions {

	private List<SecurityQuestion> resetquestions = new ArrayList<>();

	public List<SecurityQuestion> getResetquestions() {
		return resetquestions;
	}

	public void setResetquestions(List<SecurityQuestion> resetquestions) {
		this.resetquestions = resetquestions;
	}

	public ListOfResetPinQuestions(List<SecurityQuestion> resetquestions) {
		super();
		this.resetquestions=resetquestions;
	}
	
	
}
