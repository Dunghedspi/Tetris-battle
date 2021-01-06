package com.dung.eventResponse;

public class RoomResponseEvent {
	private String username1;
	private String username2;
	private String token;
	private int status;
	private int master;
	private int isPublic;
	public RoomResponseEvent(String username1, String username2, String token, int status, int master, int isPublic) {
		this.username1 = username1;
		this.username2 = username2;
		this.token = token;
		this.status = status;
		this.master = master;
		this.isPublic = isPublic;
	}

	public String getUsername1() {
		return username1;
	}

	public String getUsername2() {
		return username2;
	}

	public String getToken() {
		return token;
	}

	public int getStatus() {
		return status;
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
}
