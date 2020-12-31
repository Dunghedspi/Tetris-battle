package com.dung.eventResponse;

public class StatusResponseEvent {
	String status;

	public StatusResponseEvent(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
