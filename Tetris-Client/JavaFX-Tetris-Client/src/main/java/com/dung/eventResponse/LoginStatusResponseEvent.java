package com.dung.eventResponse;

public class LoginStatusResponseEvent {
	String status;

	public LoginStatusResponseEvent(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
