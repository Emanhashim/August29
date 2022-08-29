package com.bazra.usermanagement.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "ResetPin")
public class ResetPin {
	
		@Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	    private int id;
		
		private String pin;
		
		private LocalDateTime sentTime;
		private LocalDateTime expirationTime;
		private boolean isUsed;
		private int userId;
		private int sendingmediumId;
		
		
		public boolean isUsed() {
			return isUsed;
		}

		public ResetPin() {
			super();
			// TODO Auto-generated constructor stub
		}

		public void setUsed(boolean isUsed) {
			this.isUsed = isUsed;
		}

		public int getId() {
			return id;
		}
		
		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int getSendingmediumId() {
			return sendingmediumId;
		}

		public void setSendingmediumId(int sendingmediumId) {
			this.sendingmediumId = sendingmediumId;
		}

		public String getPin() {
			return pin;
		}
		public void setPin(String pin) {
			this.pin = pin;
		}
		public LocalDateTime getSentTime() {
			return sentTime;
		}
		public void setSentTime(LocalDateTime sentTime) {
			this.sentTime = sentTime;
		}
		public LocalDateTime getExpirationTime() {
			return expirationTime;
		}
		public void setExpirationTime(LocalDateTime expirationTime) {
			this.expirationTime = expirationTime;
		}
		public ResetPin(String pin, LocalDateTime sentTime, LocalDateTime expirationTime, Boolean isUsed, int userId,
				int sendingmediumId) {
			super();
			this.pin = pin;
			this.sentTime = sentTime;
			this.expirationTime = expirationTime;
			this.isUsed = isUsed;
			this.userId = userId;
			this.sendingmediumId = sendingmediumId;
		}

		public ResetPin(String pin) {
			this.pin=pin;
		}
		
		
		
		
		
		
}
