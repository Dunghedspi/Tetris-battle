package com.dung.eventResponse;

public class MessageResponseEvent {
	String mess;

	public MessageResponseEvent(String mess) {
		this.mess = mess;
	}

	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}
}
