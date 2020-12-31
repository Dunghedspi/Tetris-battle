package com.dung.logic;

public class Message {
	private String mess;
	private Boolean isYou;
	public Message(String mess, Boolean isyou){
		this.mess = mess;
		this.isYou = isyou;
	}
	
	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

	public Boolean getYou() {
		return isYou;
	}

	public void setYou(Boolean you) {
		isYou = you;
	}
}
