package com.bazra.usermanagement.response;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.bazra.usermanagement.model.Promotion;



public class SinglePromotionResponse {
	
	
    private String title;
	
	private String description;

	private MultipartFile picture;
	private String pictureName;
	private String picturePath;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expirationDate;
	
	private boolean status;
	
	public String getPictureName() {
		return pictureName;
	}


	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public MultipartFile getPicture() {
		return picture;
	}


	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}


	public LocalDate getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public String getPicturePath() {
		return picturePath;
	}


	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}


	public SinglePromotionResponse(Promotion promotion,String photopath) {
		this.description=promotion.getDescription();
		this.title=promotion.getTitle();
		this.expirationDate=promotion.getExpirationDate();
		this.pictureName=promotion.getPicture();
		
		this.status=promotion.isStatus();

		
	}
	public SinglePromotionResponse(Promotion promotion) {
		this.picturePath=promotion.getPhotopath();
		this.description=promotion.getDescription();
		this.title=promotion.getTitle();
		this.expirationDate=promotion.getExpirationDate();
		this.pictureName=promotion.getPicture();
		this.status=promotion.isStatus();
	}
		
	
	
    
}
