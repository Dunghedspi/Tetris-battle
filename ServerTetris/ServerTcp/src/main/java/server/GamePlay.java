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
	Thread systemThread;
	public GamePlay(Room room) {
		this.room = room;
	}

	@Override
	public void run() {
		room.getClient2().setP(this);
			systemThread = new Thread(() -> {
			TimerTask myTask = new TimerTask() {
				@Override
				public void run() {
					String list = Utils.randomArrayNumber();
					try {
						sendAll(StatusResponse.LIST, list);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			time = new Timer();
			time.schedule(myTask, 0, 5000);
		});
		systemThread.start();
	}

	public void handleSendRows(Connection client, String numberRows) throws IOException {
		if(room.getClient1().id == client.id){
			room.getClient2().getTransmission().sendObject(new Response(StatusResponse.ROWS, numberRows));
		}else {
			room.getClient1().getTransmission().sendObject(new Response(StatusResponse.ROWS, numberRows));
		}
	}

	public void handleResultGame(Connection client) throws IOException {
		systemThread.interrupt();
		if(room.getClient1().id == client.id){
			room.getClient2().getTransmission().sendObject(new Response(StatusResponse.WIN, ""));
		}else{
			room.getClient1().getTransmission().sendObject(new Response(StatusResponse.WIN, ""));
		}
	}

	public boolean recvMess(Connection client, Request request) throws IOException {
		boolean c = true;
		switch (request.getType()){
			case "rows": {
				handleSendRows(client, request.getData());
				break;
			}
			case "lose": {
				handleResultGame(client);
				c = false;
				break;
			}
		}
		return c;
	}

	public void sendAll(StatusResponse statusResponse,String message) throws IOException {
		room.getClient1().getTransmission().sendObject(new Response(statusResponse, message));
		room.getClient2().getTransmission().sendObject(new Response(statusResponse, message));
	}
}

