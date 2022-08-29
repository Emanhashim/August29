package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bazra.usermanagement.repository.PromotionRepository;

public class ListOfResponse {
	@Autowired
	PromotionRepository promotionRepository;
	private List<?> list = new ArrayList<>();
	public ListOfResponse(List<?> promotions) {
		this.list=promotions;
	}
	public List<?> getPromotions() {
		return list;
	}
	public void setPromotions(List<?> promotions) {
		this.list = promotions;
	}
}
