package com.dung.eventRequest;

public class GameStatusEvent {
	String status;

	public GameStatusEvent(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
