package com.dung.eventRequest;

public class RoomTypeEvent {
	String roomType;
	String token;

	public RoomTypeEvent(String roomType, String token) {
		this.roomType = roomType;
		this.token = token;
	}

	public String getTypeRoom() {
		if (this.roomType.equals("private")) {
			return this.roomType + "," + this.token;
		} else {
			return this.roomType;
		}
	}
}
