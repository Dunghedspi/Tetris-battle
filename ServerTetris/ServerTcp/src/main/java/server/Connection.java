package server;

import com.dung.network.CodeRequest;
import com.dung.network.Request;
import com.dung.network.Response;
import com.dung.network.StatusResponse;
import com.dung.utils.DefaultObjectTransmission;
import com.dung.utils.SerializableObjectAdapter;
import com.dung.utils.SocketByteTransmission;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Connection extends Thread {
    int id;
    Socket connectionSocket;
    private String username, host, inMessage, outMessage;
    Room room;
    private GamePlay p;
    private DefaultObjectTransmission transmission;
    private boolean closing;
    Timer time;
    public Connection(Socket c, int i) {
        System.out.println("socket id: "+i);
        connectionSocket = c;
        id = i;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInMessage() {
        return inMessage;
    }

    public void setInMessage(String inMessage) {
        this.inMessage = inMessage;
    }

    public String getOutMessage() {
        return outMessage;
    }

    public void setOutMessage(String outMessage) {
        this.outMessage = outMessage;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public GamePlay getP() {
        return p;
    }

    public void setP(GamePlay p) {
        this.p = p;
    }

    public DefaultObjectTransmission getTransmission() {
        return transmission;
    }

    public void setTransmission(DefaultObjectTransmission transmission) {
        this.transmission = transmission;
    }

    public boolean isClosing() {
        return closing;
    }

    public void setClosing(boolean closing) {
        this.closing = closing;
    }


    public boolean joinRoom(String token) {
        for (int i = 0; i < ServerTCP.rooms.size(); i++) {
            Room room = ServerTCP.rooms.get(i);
            if (room.getToken().equals(token)) {
                if (room.getClient2() == null) {
                    room.setClient2(this);
                    this.room = room;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean joinRoom() {
        boolean result = false;
        for (int i = 0; i < ServerTCP.rooms.size(); i++) {
            Room room = ServerTCP.rooms.get(i);
            if (null == room.getClient2() && room.isPublic()) {
                room.setClient2(this);
                this.room = room;
                result = true;
                break;
            }
        }
        return result;
    }

    public void createRoom() {
        room = new Room(this);
        room.setToken("1234");
        ServerTCP.rooms.add(room);
    }

    public void handleJoinRoom(String inMessage) throws IOException {
        if (inMessage.equals("create")) {
            createRoom();
            transmission.sendObject(new Response(StatusResponse.ROOM, room.getToken() + "," + room.getClient1().getUsername() + ",," + 0 + "," + 1 + "," + (room.isPublic() ? 1 : 0)));
        } else {
            if (inMessage.equals("public")) {
                boolean isCheck = joinRoom();
                if (!isCheck) {
                    transmission.sendObject(new Response(StatusResponse.NOT_FOUND, "room no found"));
                } else {
                    transmission.sendObject(new Response(StatusResponse.ROOM, room.getToken() + "," + room.getClient1().getUsername() + "," + room.getClient2().getUsername() + "," + 0 + "," + 0 + "," + 1));
                    room.getClient1().transmission.sendObject(new Response(StatusResponse.JOIN_ROOM, this.getUsername()));
                }
            } else {
                String[] work = inMessage.split(",");
                boolean isCheck = joinRoom(work[1]);
                if (!isCheck) {
                    transmission.sendObject(new Response(StatusResponse.NOT_FOUND, "room no found"));
                } else {
                    transmission.sendObject(new Response(StatusResponse.ROOM, room.getToken() + "," + room.getClient1().getUsername() + "," + room.getClient2().getUsername() + "," + 0 + "," + 0 + "," + (room.isPublic() ? 1 : 0)));
                    room.getClient1().transmission.sendObject(new Response(StatusResponse.JOIN_ROOM, this.getUsername()));
                }
            }
        }
    }

    public void handleMessage(String inMessage) throws IOException {
        if (room.getClient1().id == id) {
            room.getClient2().transmission.sendObject(new Response(StatusResponse.MESS, inMessage));
        } else {
            room.getClient1().transmission.sendObject(new Response(StatusResponse.MESS, inMessage));
        }
    }

    public void handleStatus(String inMessage) throws IOException {
        if (inMessage.equals("Start")) {
            p = new GamePlay(this.room);
            p.start();
            p.sendAll(StatusResponse.START, "");
        } else {
            if (room.getClient1().id == id) {
                if (inMessage.equals("Yes")) {
                    room.getClient2().transmission.sendObject(new Response(StatusResponse.READY, ""));
                }else{
					room.getClient2().transmission.sendObject(new Response(StatusResponse.REFUSE,""));
				}
            } else {
				if (inMessage.equals("Yes")) {
					room.getClient1().transmission.sendObject(new Response(StatusResponse.READY, ""));
				}else{
					room.getClient1().transmission.sendObject(new Response(StatusResponse.REFUSE,""));
				}
            }
        }
    }

	public void handleExitRoom() throws IOException {
		if (this.id == room.getClient1().id) {
			if (room.getClient2() != null) {
                room.getClient2().transmission.sendObject(new Response(StatusResponse.UPDATE_ROOM, room.getToken() + "," + room.getClient2().getUsername() + ",," + 0 + "," + 1 + "," + (room.isPublic() ? 1 : 0)));
                room.setClient1(room.getClient2());
                room.setClient2(null);
            } else {
				ServerTCP.rooms.remove(room);
			}
		} else {
            room.getClient1().transmission.sendObject(new Response(StatusResponse.UPDATE_ROOM, room.getToken() + "," + room.getClient1().getUsername() + ",," + 0 + "," + 1 + "," + (room.isPublic() ? 1 : 0)));
            room.setClient2(null);
            room = null;
        }
	}

    public void run() {
        closing = false;
        try {
            transmission = new DefaultObjectTransmission(new SerializableObjectAdapter(), new SocketByteTransmission(connectionSocket));
            startCheckConnected();
            waitForIncommingMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitForIncommingMessages() {
        while (!closing) {
            try {
                Object receivedMessage = transmission.receiveObject();
                if (receivedMessage instanceof Request) {
                    handleRequest((Request) receivedMessage);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void handleDisConnect() throws IOException {
        time.cancel();
        System.out.println("Client disconnect....");

        if (null != room) {
            if (room.getClient1().id == id) {
                if (null != p) {
                    p.recvMess(this, new Request(CodeRequest.LOST, ""));
                    p.setExit(true);
                    p = null;
                }
            } else {
                if (null != p) {
                    p.recvMess(this, new Request(CodeRequest.LOST, ""));
                    p.setExit(true);
                    p = null;
                }
            }
            handleExitRoom();
        }
        closing = true;
    }

    public void startCheckConnected() {
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    transmission.sendObject(new Response(StatusResponse.CONNECTED, ""));
                } catch (IOException e) {
                    try {
                        handleDisConnect();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        };
        time = new Timer();
        time.schedule(myTask, 0, 2000);
    }

    public void handleChangeStatusRoom(String data) throws IOException {
        if (data.equals("Public")) {
            room.setPublic(true);
        } else {
            room.setPublic(false);
        }
        if (room.getClient2() != null) {
            room.getClient2().getTransmission().sendObject(new Response(StatusResponse.STATUS_ROOM, data));
        }
    }

    public void handleRequest(Request receivedMessage) throws IOException {
        System.out.println("Request from socket" + id);
        System.out.println("Type: " + receivedMessage.getType());
        System.out.println("Data: " + receivedMessage.getData());
        switch (receivedMessage.getType()) {
            case USER: {
                this.setUsername(receivedMessage.getData());
                transmission.sendObject(new Response(StatusResponse.OK, ""));
                break;
            }
            case TYPE: {
                handleJoinRoom(receivedMessage.getData());
                break;
            }
            case MESS: {
                handleMessage(receivedMessage.getData());
                break;
            }
            case STATUS: {
                handleStatus(receivedMessage.getData());
                break;
            }
            case EXIT: {
                handleExitRoom();
                break;
            }
            case QUIT: {
                handleDisConnect();
                break;
            }
            case STATUS_ROOM: {
                handleChangeStatusRoom(receivedMessage.getData());
                break;
            }
            default: {
                boolean c = p.recvMess(this, receivedMessage);
                if (!c) p.interrupt();
                break;
            }
        }
    }
}
