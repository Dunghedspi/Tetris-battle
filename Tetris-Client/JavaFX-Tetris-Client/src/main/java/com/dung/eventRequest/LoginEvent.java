package com.dung.eventRequest;

public class LoginEvent {
	String username;

	public LoginEvent(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
