package server;

import com.dung.network.Request;
import com.dung.network.Response;
import com.dung.network.StatusResponse;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GamePlay extends Thread {
	private Room room;
	private Timer time;

	public GamePlay(Room room) {
		this.room = room;
	}

	public boolean isExit = false;

	@Override
	public void run() {
		room.getClient2().setP(this);
		TimerTask myTask = new TimerTask() {
			@Override
			public void run() {
				String list = Utils.randomArrayNumber();
				try {
					if (!isExit) {
						sendAll(StatusResponse.LIST, list);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		time = new Timer();
		time.schedule(myTask, 0, 5000);
	}

	public void handleSendRows(Connection client, String numberRows) throws IOException {
		if(room.getClient1().id == client.id){
			room.getClient2().getTransmission().sendObject(new Response(StatusResponse.ROWS, numberRows));
		}else {
			room.getClient1().getTransmission().sendObject(new Response(StatusResponse.ROWS, numberRows));
		}
	}

	public void handleResultGame(Connection client) {
		time.cancel();
		try{
			if (room.getClient1().id == client.id) {
				room.getClient2().getTransmission().sendObject(new Response(StatusResponse.WIN, ""));
			} else {
				room.getClient1().getTransmission().sendObject(new Response(StatusResponse.WIN, ""));
			}
		} catch (Exception ignored){

		}
	}

	public boolean recvMess(Connection client, Request request) throws IOException {
		boolean c = true;
		switch (request.getType()){
			case ROWS: {
				handleSendRows(client, request.getData());
				break;
			}
			case LOST: {
				handleResultGame(client);
				c = false;
				break;
			}
		}
		return c;
	}

	public void sendAll(StatusResponse statusResponse, String message) throws IOException {
		room.getClient1().getTransmission().sendObject(new Response(statusResponse, message));
		room.getClient2().getTransmission().sendObject(new Response(statusResponse, message));
	}

	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean exit) {
		isExit = exit;
	}
}

