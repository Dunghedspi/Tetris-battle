package com.dung.logic;

public class Room {
	private String userNameClient1;
	private String userNameClient2;
	private final String roomID;
	private int status;
	private int master;
	private int isPublic;
	public Room(String roomId, String userNameClient1, String userNameClient2, int status, int master, int isPublic) {
		this.userNameClient1 = userNameClient1;
		this.userNameClient2 = userNameClient2;
		this.roomID = roomId;
		this.status = status;
		this.master = master;
		this.isPublic = isPublic;
	}

	public String getRoomID() {
		return roomID;
	}

	public String getUserNameClient1() {
		return userNameClient1;
	}

	public String getUserNameClient2() {
		return userNameClient2;
	}

	public void setUserNameClient1(String name) {
		this.userNameClient1 = name;
	}

	public void setUserNameClient2(String name) {
		this.userNameClient2 = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMaster() {
		return master;
	}

	public void setMaster(int master) {
		this.master = master;
	}

	public int getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(int isPublic) {
		this.isPublic = isPublic;
	}
}
