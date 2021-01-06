package com.dung.eventResponse;

public class RoomUpdateResponseEvent extends RoomResponseEvent{

	public RoomUpdateResponseEvent(String username1, String username2, String token, int status, int master, int isPublic) {
		super(username1, username2, token, status, master, isPublic);
	}
}
